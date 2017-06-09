package com.example.jetty_jersey.db;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.*;

import com.example.jetty_jersey.dao.Status;
import com.example.jetty_jersey.dao.Status.Execution;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class DatabaseConnecter
{
	private static Logger log = LogManager.getLogger(DatabaseConnecter.class.getName());
	private static boolean alreadyInit = false;
	private static HashMap<String, Integer> maxIdMap = new HashMap<String, Integer>();
	private final RestClient restClient = RestClient
			.builder(new HttpHost(DatabaseSettings.DB_HOST, DatabaseSettings.DB_PORT_DEFAULT, "http"), new HttpHost(DatabaseSettings.DB_HOST, DatabaseSettings.DB_PORT_SECOND, "http")).build();

	// About TransportClient : https://www.devdiscoveries.com/elasticsearch/java-elasticsearch-become-up-and-running/
	private static Settings settings = Settings.builder().put("client.transport.sniff", true).build();

	@SuppressWarnings("unchecked")
	private static TransportClient client = new PreBuiltTransportClient(settings);

	/**
	 * Create one instance for one session and don't forget to call close() at the end of query.
	 */
	public DatabaseConnecter()
	{
		synchronized (client)
		{
			if (!alreadyInit)
			{
				initClient();
				alreadyInit = true;
			}
		}
	}

	static void initClient()
	{
		try
		{
			client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(DatabaseSettings.DB_HOST), DatabaseSettings.DB_PORT_TRANSPORT));
		} catch (UnknownHostException e)
		{
			log.error("Error while initializing database connection. \n"
					+ "Please check that cluster.name in /usr/local/etc/elasticsearch/elasticsearch.yml is properly configured as 'cluster.name: elasticsearch'.", e);
		}
	}

	public Response execute(DatabaseQuery q)
	{
		Response r = null;
		try
		{
			r = restClient.performRequest(q.getMethod(), q.getURL(), Collections.<String, String> emptyMap(), q.getEntity());

		} catch (IOException e)
		{
			log.error("Error while performing REST database request", e);
		} catch (Exception e)
		{
			log.error("Exception while performing REST database request", e);
		}
		return r;
	}

	public Status deleteAllFromTableNameWhereFieldEqValue(String tableName, String fieldName, String fieldValue)
	{
		Status s = null;
		List<String> idList = getIdInTableNameWhereFieldEqValue(tableName, fieldName, fieldValue);
		if (idList.size() == 0)
		{
			s = new Status(Execution.FAILED);
			String msg = "No record found for deletion: " + fieldName + "=" + fieldValue + " in table:" + tableName;
			s.setMessage(msg);
			log.error("Data inconsistency!! " + msg);
		} else
		{
			int sucessCount = 0;
			for (String id : idList)
			{
				DeleteResponse res = client.prepareDelete(DatabaseSettings.DB_NAME, tableName, id).get();
				log.info(res);
				if (res.getResult().equals(Result.DELETED))
					sucessCount++;
			}

			s = new Status((sucessCount > 0 ? Execution.SUCCESSFUL : Execution.FAILED));
			s.setResultyCount(sucessCount);
		}

		return s;
	}

	public Status updateDataInTableNameWhereFieldEqValue(String tableName, String fieldName, String fieldValue, Map<String, String> data)
	{
		List<String> ids = new ArrayList<String>();
		if (fieldName.equals("_id"))
			ids.add(fieldValue);
		else
		{
			List<String> idList = getIdInTableNameWhereFieldEqValue(tableName, fieldName, fieldValue);
			ids.addAll(idList);
		}
		XContentBuilder jsonDoc = prepareScriptFromDataToUpdate(tableName, data);
		int successCount = 0;
		for (String id : ids)
		{
			UpdateRequest updateRequest = new UpdateRequest();
			updateRequest.index(DatabaseSettings.DB_NAME);
			updateRequest.type(tableName);
			updateRequest.id(id);
			updateRequest.doc(jsonDoc);
			try
			{
				UpdateResponse res = client.update(updateRequest).get();
				log.info(res);
				if (res.getResult().equals(Result.UPDATED))
					successCount++;
			} catch (InterruptedException e)
			{
				log.error("Unable to execute database update", e);
			} catch (ExecutionException e)
			{
				log.error("Unable to execute database update", e);
			}
		}

		Status s = new Status((successCount > 0) ? Execution.SUCCESSFUL : Execution.FAILED);
		s.setResultyCount(successCount);
		if (successCount == 0)
			s.setMessage("Data update into " + tableName + " failed");
		return s;

	}

	private XContentBuilder prepareScriptFromDataToUpdate(String tableName, Map<String, String> data)
	{
		XContentBuilder jsonb = null;
		try
		{
			jsonb = jsonBuilder().startObject();
			for (Entry<String, String> e : data.entrySet())
			{
				if (!e.getKey().equals("_id") && !e.getKey().equals("_db"))
					jsonb.field(e.getKey(), e.getValue());
			}
			jsonb.endObject();
		} catch (Exception e1)
		{
			log.error("Error while generating json document for update");
		}
		return jsonb;
	}

	String generateNewId(String tableName)
	{
		Integer maxId = new Integer(0);
		synchronized (maxIdMap) // Only once at a time to update the max _id of each table in the hash map
		{
			maxId = maxIdMap.get(tableName);
			if (maxId == null)
			{
				List<Map<String, String>> l = selectAllFromTableName(tableName);
				String strMax = findMaxIndexId(l);
				maxId = new Integer(Integer.parseInt(strMax) + 1);
				log.debug("Get max _id=" + strMax + " from database table " + tableName);
			} else
			{
				maxId = new Integer(maxId.intValue() + 1);
			}
			maxIdMap.put(tableName, maxId);
		}
		log.debug("New _id: " + maxId + " genertated from table: " + tableName);
		return maxId.toString();
	}

	Integer getMaxIdFromCache(String tableName)
	{
		return maxIdMap.get(tableName);
	}

	private String findMaxIndexId(List<Map<String, String>> l)
	{
		int nMax = 0;
		for (Map<String, String> m : l)
		{
			String id = m.get("_id");
			int nId = Integer.parseInt(id);
			if (nMax < nId)
				nMax = nId;
		}
		return Integer.toString(nMax);
	}

	/**
	 * @param tableName
	 * @param dataList
	 * @return
	 */
	public Status insertToTableName(String tableName, Map<String, String> data)
	{
		int successCount = 0;
		String givenId = data.get("_id");
		String newId = givenId.equals("-1") ? generateNewId(tableName) : givenId;
		XContentBuilder jsonDoc = prepareScriptFromDataToUpdate(tableName, data);
		IndexRequest indexRequest = new IndexRequest(DatabaseSettings.DB_NAME, tableName, newId).source(jsonDoc);
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index(DatabaseSettings.DB_NAME);
		updateRequest.type(tableName);
		updateRequest.id(newId);
		updateRequest.doc(jsonDoc).upsert(indexRequest);
		try
		{
			UpdateResponse res = client.update(updateRequest).get();
			log.info(res);
			if (res.getResult().equals(Result.CREATED))
				successCount++;
		} catch (InterruptedException e)
		{
			log.error("Unable to execute database update", e);
		} catch (ExecutionException e)
		{
			log.error("Unable to execute database update", e);
		}

		Status s = new Status((successCount > 0) ? Execution.SUCCESSFUL : Execution.FAILED);
		s.setResultyCount(successCount);
		if (successCount == 0)
			s.setMessage("Data insert into " + tableName + " failed");
		return s;
	}

	/**
	 * 
	 * @param tableName
	 * @return
	 */
	public List<Map<String, String>> selectAllFromTableName(String tableName)
	{
		SearchResponse scrollResp = client.prepareSearch(DatabaseSettings.DB_NAME).setTypes(tableName).addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
				.setScroll(DatabaseSettings.MAX_DATA_KEEP_TIME).setSize(DatabaseSettings.MAX_BOLK_RESULTS).get();
		// Scroll until no hits are returned
		List<Map<String, String>> lRet = new ArrayList<Map<String, String>>();
		do
		{
			lRet.addAll(wrapResults(scrollResp));
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(DatabaseSettings.MAX_DATA_KEEP_TIME).execute().actionGet();
		} while (scrollResp.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop.
		return lRet;
	}

	public List<Map<String, String>> selectAllFromTableWhereFieldEqValue(String tableName, String fieldName, String fieldValue)
	{
		// QueryBuilder qb = QueryBuilders.commonTermsQuery(fieldName, fieldValue);
		QueryBuilder qb = QueryBuilders.queryStringQuery(fieldName + ":" + fieldValue);
		// log.debug("Build query " + qb);
		SearchResponse scrollResp = client.prepareSearch(DatabaseSettings.DB_NAME).setTypes(tableName).addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
				.setScroll(DatabaseSettings.MAX_DATA_KEEP_TIME).setQuery(qb).setSize(DatabaseSettings.MAX_BOLK_RESULTS).get();
		// SearchResponse scrollResp = client.prepareSearch(DatabaseSettings.DB_NAME).setTypes(tableName).addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
		// .setScroll(DatabaseSettings.MAX_DATA_KEEP_TIME).setSize(DatabaseSettings.MAX_BOLK_RESULTS).get();
		// Scroll until no hits are returned
		List<Map<String, String>> lRet = new ArrayList<Map<String, String>>();

		do
		{
			lRet.addAll(wrapResults(scrollResp));
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(DatabaseSettings.MAX_DATA_KEEP_TIME).execute().actionGet();
		} while (scrollResp.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop.
		return lRet;
	}

	private List<Map<String, String>> wrapResults(SearchResponse res, String fieldName, String fieldValue)
	{
		List<Map<String, String>> lRet = new ArrayList<Map<String, String>>();
		for (SearchHit hit : res.getHits().getHits())
		{
			CustomHashMap<String, String> hm = new CustomHashMap<String, String>();
			hm.put("_db", hit.getIndex());
			hm.put("_id", hit.getId());
			boolean discard = false;
			for (Entry<String, Object> s : hit.getSource().entrySet())
			{
				hm.putIfAbsent(s.getKey(), (String) s.getValue());
				if (s.getKey().equals(fieldName) && !((String) s.getValue()).equals(fieldValue))
					discard = true;
			}

			if (!discard)
				lRet.add(hm);
		}
		return lRet;
	}

	public List<Map<String, String>> selectAllFromTableWhereFieldEqValueSortAscendingByField(String tableName, String fieldName, String fieldValue, String sortField)
	{
		List<Map<String, String>> l = selectAllFromTableWhereFieldEqValue(tableName, fieldName, fieldValue);
		if (l.size() > 0)
		{
			Collections.sort(l, new MapValueAscendingComparator(sortField));
		}
		return l;
	}

	public List<Map<String, String>> selectAllFromTableWhereFieldEqValueSortDescendingByField(String tableName, String fieldName, String fieldValue, String sortField)
	{
		List<Map<String, String>> l = selectAllFromTableWhereFieldEqValue(tableName, fieldName, fieldValue);
		if (l.size() > 0)
		{
			Collections.sort(l, new MapValueDescendingComparator(sortField));
		}
		return l;
	}

	private List<Map<String, String>> wrapResults(SearchResponse res)
	{
		List<Map<String, String>> lRet = new ArrayList<Map<String, String>>();
		for (SearchHit hit : res.getHits().getHits())
		{
			CustomHashMap<String, String> hm = new CustomHashMap<String, String>();
			lRet.add(hm);
			hm.put("_db", hit.getIndex());
			hm.put("_id", hit.getId());
			for (Entry<String, Object> s : hit.getSource().entrySet())
			{
				hm.putIfAbsent(s.getKey(), (String) s.getValue());
			}
		}
		return lRet;
	}

	public List<String> getIdInTableNameWhereFieldEqValue(String tableName, String fieldName, String fieldValue)
	{
		QueryBuilder qb = QueryBuilders.termQuery(fieldName, fieldValue);
		SearchResponse scrollResp = client.prepareSearch(DatabaseSettings.DB_NAME).setTypes(tableName).addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
				.setScroll(DatabaseSettings.MAX_DATA_KEEP_TIME).setQuery(qb).setSize(DatabaseSettings.MAX_BOLK_RESULTS).get();
		// Scroll until no hits are returned
		List<String> lRet = new ArrayList<String>();
		do
		{
			for (SearchHit hit : scrollResp.getHits().getHits())
			{
				lRet.add(hit.getId());
			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(DatabaseSettings.MAX_DATA_KEEP_TIME).execute().actionGet();
		} while (scrollResp.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while loop.

		return lRet;
	}

	public List<Map<String, String>> selectInRangeFromTableName(String tableName, int nStart, int nEnd)
	{
		SearchResponse response = client.prepareSearch(DatabaseSettings.DB_NAME).setTypes(tableName).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(nStart).setSize(nEnd).setExplain(true)
				.get();
		return wrapResults(response);
	}

	public void close()
	{
		if (restClient != null)
			try
			{
				restClient.close();
			} catch (IOException e)
			{
				log.error("Unable to close REST database connection", e);
			}
		// Never close client as client is an static
		// if (client != null)
		// client.close();
	}

}
