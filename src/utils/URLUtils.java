package utils;



import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.util.Elements;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import tweets.ProcessedTweet;


public class URLUtils implements Serializable{

	static List<String> urls = new ArrayList<String>();
	static List<String> contents = new ArrayList<String>();
	
	
	public static void loadFromFile(String file)
	{
		try
		{
			if (!new File(file).exists())
			{
				urls = new ArrayList<String>();
				contents = new ArrayList<String>(); 				
			}
			else
			{
				FileInputStream fileIn = new FileInputStream(file);
				ObjectInputStream in = new ObjectInputStream(fileIn);			
				urls = (List<String>) in.readObject();
				contents = (List<String>) in.readObject();
		        in.close();
				fileIn.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			urls = new ArrayList<String>();
			contents = new ArrayList<String>(); 
		}			
	}
	
	
	
	public static void saveToFile(String file)
	{
		try
	      {
			 FileOutputStream fileOut = new FileOutputStream(file);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(urls);
	         out.writeObject(contents);
	         out.close();
	         fileOut.close();
	      }catch(Exception e)
	      {
	          e.printStackTrace();
	      }		
	}
	
	public static String getURLContent(String url)
	{		
		int index=urls.indexOf(url);
		if (index>=0)
		{
			return contents.get(index);			
		}	
		else
		{
			return "";		
/*
 * FJRM 22/08/2013
 * Se supone que he descargado las URL de todos los tweets. La que no se encuentra es porque no está disponible o tiene acceso restringido.
 * Para el TFM se queda así.		
 */
/*		
		String content="";		
		Document doc;
		try {			
			doc = Jsoup.connect(url).get();
			content += doc.title();
			content += doc.select("h1").text();
			content += doc.select("h2").text();
			if (content.length()==0)
				content = "";
			urls.add(url);
			contents.add(content);
		} catch (Exception e) {
			urls.add(url);
			contents.add("");
			return content;
		}		
		return content;
*/		
		}
						
	}
	
	
	public static String getRawURL(String urlWeb)
	{		
		String content="";
		try{
		    HttpClient httpClient = new DefaultHttpClient();
		    httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		    HttpGet httpGet = new HttpGet(urlWeb);
		    HttpResponse response = httpClient.execute(httpGet);
		    InputStream in = response.getEntity().getContent();
		    StringBuilder sb = new StringBuilder();
		    byte[] buffer = new byte[4096];
		    int length; 
		    while((length = in.read(buffer)) > 0) {
		    	sb.append(new String(buffer));
		    }
		    content = sb.toString();
		}catch(Exception e)
		{
			return content;
		}
		return content;
	}



	public static void addURL(String url, String content)
	{
		urls.add(url);
		contents.add(content);
	}
	
	public static List<String> getUrls() {
		return urls;
	}



	public static void setUrls(List<String> urls) {
		URLUtils.urls = urls;
	}



	public static List<String> getContents() {
		return contents;
	}



	public static void setContents(List<String> contents) {
		URLUtils.contents = contents;
	}	
}
