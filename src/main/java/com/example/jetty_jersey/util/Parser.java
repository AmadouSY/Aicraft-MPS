package com.example.jetty_jersey.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.example.jetty_jersey.dao.Flight;
import com.example.jetty_jersey.dao.Plane;
import com.example.jetty_jersey.dao.Task;

public class Parser
{
	public static List<Task> parseTask()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		List<Task> liste = new ArrayList<Task>();
		BufferedReader buffer = null;
		FileReader fileReader = null;
		String line;
		String[] splitedLine;
		try
		{
			fileReader = new FileReader("/home/binta/workspace/Test/src/task.txt");
			buffer = new BufferedReader(fileReader);
			while ((line = buffer.readLine()) != null)
			{
				splitedLine = line.split(",");
				if (splitedLine.length != 10)
				{
					System.out.println("Le fichier n'est pas dans le bon format!");
					return null;
				}
				int id = Integer.parseInt(splitedLine[0]);
				Date starTime = dateFormat.parse(splitedLine[1]);
				Date endTime = dateFormat.parse(splitedLine[2]);
				String description = splitedLine[3];
				String periodicity = splitedLine[4];
				String ataCategory = splitedLine[5];
				boolean needHangar = false;
				if (splitedLine[6].equals("true"))
					needHangar = true;
				int planeId = Integer.parseInt(splitedLine[7]);
				int statut = Integer.parseInt(splitedLine[8]);
				int morId = Integer.parseInt(splitedLine[9]);
				// System.out.println(line);
				// Task t = new Task(id, starTime, endTime, description, periodicity, ataCategory, needHangar, planeId, statut, morId);
				// liste.add(t);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (buffer != null)
					buffer.close();
				if (fileReader != null)
					fileReader.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return liste;

	}

	public static List<Flight> parseFlight()
	{
		List<Flight> liste = new ArrayList<Flight>();
		BufferedReader buffer = null;
		FileReader fileReader = null;
		String line;
		String[] splitedLine;
		try
		{
			fileReader = new FileReader("/home/binta/workspace/Test/src/flight.txt");
			buffer = new BufferedReader(fileReader);
			while ((line = buffer.readLine()) != null)
			{
				splitedLine = line.split(",");
				if (splitedLine.length != 2)
				{
					System.out.println("Le fichier n'est pas dans le bon format!");
					return null;
				}
				int id = Integer.parseInt(splitedLine[0]);
				int planeId = Integer.parseInt(splitedLine[1]);
				Flight f = new Flight(id, planeId);
				liste.add(f);

			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (buffer != null)
					buffer.close();
				if (fileReader != null)
					fileReader.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return liste;
	}

	public static List<Plane> parsePlane()
	{
		List<Plane> liste = new ArrayList<Plane>();
		BufferedReader buffer = null;
		FileReader fileReader = null;
		String line;
		String[] splitedLine;
		try
		{
			fileReader = new FileReader("/home/binta/workspace/Test/src/plane.txt");
			buffer = new BufferedReader(fileReader);
			while ((line = buffer.readLine()) != null)
			{
				splitedLine = line.split(",");
				if (splitedLine.length != 2)
				{
					System.out.println("Le fichier n'est pas dans le bon format!");
					return null;
				}
				int id = Integer.parseInt(splitedLine[0]);
				String type = splitedLine[1];
				Plane p = new Plane(id, type);
				liste.add(p);

			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (buffer != null)
					buffer.close();
				if (fileReader != null)
					fileReader.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return liste;
	}

	public static void main(String[] args)
	{
		parseTask();

	}
}
