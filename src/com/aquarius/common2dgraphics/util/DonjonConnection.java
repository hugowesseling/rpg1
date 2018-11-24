package com.aquarius.common2dgraphics.util;
/*
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
*/
//import com.sun.org.apache.bcel.internal.generic.NEW;

public class DonjonConnection
{
	/*final static boolean isOnline=false;
	public static String[] location_names=loadlines("/donjonlocations_names.txt");
	public static String[] location_landtypes=loadlines("/donjonlocations_landtypes.txt");
	private static String[] loadlines(String resourceName)
	{
		String[] result={};
		try 
		{
			String fileName = new URI(DonjonConnection.class.getResource(resourceName).toString()).getPath();

			BufferedReader br=new BufferedReader(new FileReader(fileName));
			int countlines=0;
			while(br.readLine()!=null)countlines++;
			
			br.close();
			br=new BufferedReader(new FileReader(fileName));
			result=new String[countlines];
			String line;
			countlines=0;
			while((line=br.readLine())!=null)
			{
				result[countlines++]=line.trim();
			}
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static String getRandomLocationName()
	{
		String name=location_names[new Random().nextInt(location_names.length)];
		String landtype=location_landtypes[new Random().nextInt(location_landtypes.length)];
		if(new Random().nextBoolean())
		{
			return landtype+" of "+name;
		}else
			return name+" "+landtype;
		
	}
	private static HashMap <String,Integer> typeCurrentPositions=new HashMap<String, Integer>();
	private static HashMap<String,Vector<String>> typeLists=new HashMap<String,Vector<String>>(); 
	
	public static String getName(String type)
	{
		if(isOnline)
		{
			Vector<String> typeList=typeLists.get(type);
			if(typeList==null)
			{
				typeList=new Vector<String>();
				typeLists.put(type,typeList);
			}
			Integer typeCurrentPosition=typeCurrentPositions.get(type);
			if(typeCurrentPosition==null)
			{
				typeCurrentPosition=new Integer(0);
				typeCurrentPositions.put(type,typeCurrentPosition);
			}
			if(typeCurrentPosition>=typeList.size())
			{
				typeList.addAll(Arrays.asList(getNames(type,50)));
			}
			String result=typeList.get(typeCurrentPosition);
			typeCurrentPosition++;
			return result;
		}else
		{
			return location_names[new Random().nextInt(location_names.length)];
		}
	}
	
	private static String[] getNames(String type,int count)
	{
		String typeUrl=type;
		try {
			typeUrl = URLEncoder.encode(type,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String result=InternetUtil.url_get_text_contents("http://donjon.bin.sh/name/rpc.cgi?type="+typeUrl+"&n="+count);
		return result.split("\n");
	}*/
}
