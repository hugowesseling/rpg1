package com.aquarius.common2dgraphics.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class InternetUtil
{
	public static String url_get_text_contents(String urlString)
	{
		String result="";
		URL url;
		try {
			url = new URL(urlString);
			BufferedReader in = new BufferedReader(
						new InputStreamReader(
						url.openStream()));
	
			String inputLine;
	
			while ((inputLine = in.readLine()) != null)
				result+=inputLine+"\n";
	
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
