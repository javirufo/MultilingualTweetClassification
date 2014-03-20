package json.tweets;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;



import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

//import com.cybozu.labs.langdetect.DetectorFactory;


import twitter4j.RateLimitStatus;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import xml.es.daedalus.tass.tweets.Tweet;
import xml.es.daedalus.tass.tweets.Tweets;

public class JSONTweet {
	
	public static ConfigurationBuilder confbuilder  = new ConfigurationBuilder().setOAuthAccessToken("107456296-egM8cDPKEfOph0sBugmyZ7jvVUuE2yMzw5NRKvhJ") 
			.setOAuthAccessTokenSecret("p1X2uySDgrceRGrQFDqnHQ22x2fNGzWBwXVObMmtI") 
			.setOAuthConsumerKey("x4fchtIAIuJc5plbdc3A") 
			.setOAuthConsumerSecret("IPUpYROPUUMvgvT9eNbS2QMVAqFZ7lW8piKotwWeyA"); 
	public static TwitterFactory tf = new TwitterFactory(confbuilder.build());
	public static Twitter twitter = tf.getInstance();	
	public static Map<java.lang.String,RateLimitStatus> rate=null; 
	public static RateLimitStatus limit=null;	
	public static String OAuthAccessToken ="107456296-egM8cDPKEfOph0sBugmyZ7jvVUuE2yMzw5NRKvhJ"; 
	public static String OAuthAccessTokenSecret = "p1X2uySDgrceRGrQFDqnHQ22x2fNGzWBwXVObMmtI"; 
	public static String OAuthConsumerKey = "x4fchtIAIuJc5plbdc3A"; 
	public static String OAuthConsumerSecret = "IPUpYROPUUMvgvT9eNbS2QMVAqFZ7lW8piKotwWeyA";  
	
	
	public static Tweet getTweetFromTwitter(String id)
	{
		Tweet tweet = new Tweet();

		try {	    	
		    	if (rate==null)
		    	{
		    		rate = twitter.getRateLimitStatus("statuses"); 
					limit = rate.get("/statuses/show/:id");	    		
		    	}
	//Si no puedo descargar m�s, entonces duermo el hilo hasta que pase el tiempo suficiente. Lo duermo un segundo m�s para asegurarme			
				if (limit.getRemaining()==0)
				{
					System.out.println("Debo dormir el proceso durante "+limit.getSecondsUntilReset());;
					Thread.sleep((1+limit.getSecondsUntilReset())*1000);
					System.out.println("Despierto el proceso");
					rate = twitter.getRateLimitStatus("statuses"); 
					limit = rate.get("/statuses/show/:id");				
				}
				System.out.println("Aún puedo coger "+limit.getRemaining());			
				twitter4j.Status stweet = twitter.showStatus(Long.parseLong(id)); 
				tweet.setContent(stweet.getText());
				tweet.setDate(stweet.getCreatedAt().toString());
				tweet.setTweetid(new BigInteger(id));
				tweet.setUser(stweet.getUser().getName());

	/*			
				String urlstr = "https://api.twitter.com/1.1/statuses/show.json?id="+id+"&include_entities=true";  
				URL url = new URL(urlstr);  
				BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));  
				int c;  
				StringBuffer buff = new StringBuffer();  
				while((c=br.read())!=-1)  
				{  
				    buff.append((char)c);  
				}  
				br.close();  
	
				JSONObject tweet = new JSONObject(buff.toString());  
				return tweet.getString("text");
	*/			
			} catch (Exception e) 
			{
				System.out.println(e.getMessage());
			}
		return tweet;
	}
	
	
	public static String getTweetContent(String id)
	{
		String texto=null;

		try {	    	
		    	if (rate==null)
		    	{
		    		rate = twitter.getRateLimitStatus("statuses"); 
					limit = rate.get("/statuses/show/:id");	    		
		    	}
	//Si no puedo descargar m�s, entonces duermo el hilo hasta que pase el tiempo suficiente. Lo duermo un segundo m�s para asegurarme			
				if (limit.getRemaining()==0)
				{
					System.out.println("Debo dormir el proceso durante "+limit.getSecondsUntilReset());;
					Thread.sleep((1+limit.getSecondsUntilReset())*1000);
					System.out.println("Despierto el proceso");
					rate = twitter.getRateLimitStatus("statuses"); 
					limit = rate.get("/statuses/show/:id");				
				}
				System.out.println("A�n puedo coger "+limit.getRemaining());			
				texto = twitter.showStatus(Long.parseLong(id)).getText(); 
	/*			
				String urlstr = "https://api.twitter.com/1.1/statuses/show.json?id="+id+"&include_entities=true";  
				URL url = new URL(urlstr);  
				BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));  
				int c;  
				StringBuffer buff = new StringBuffer();  
				while((c=br.read())!=-1)  
				{  
				    buff.append((char)c);  
				}  
				br.close();  
	
				JSONObject tweet = new JSONObject(buff.toString());  
				return tweet.getString("text");
	*/			
			} catch (Exception e) 
			{
				texto=null;
			}
		return texto;
	}
	
	
}
