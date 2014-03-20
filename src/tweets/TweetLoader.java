package tweets;
import javax.xml.bind.JAXBContext;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import xml.es.daedalus.tass.tweets.Tweet;
import xml.es.daedalus.tass.tweets.Tweets;

import java.io.File;
import java.util.List;

public class TweetLoader {
	  
//	private final String SCHEMA_FILE = "/xml/es/daedalus/tass/tweets/tweets.xsd";
//	private String schemaPath;
	

	public TweetLoader(){
/*		
		String inEclipseStr = System.getProperty("runInEclipse");
		if ("true".equalsIgnoreCase(inEclipseStr))	
			schemaPath = "/bin"+SCHEMA_FILE;
		else
			schemaPath = SCHEMA_FILE;
*/		
	}
	
	
	public static Tweets LoadFromXML(String filePath, boolean getContent)
	{
		try{		
		File xmlFile = new File(filePath);
		JAXBContext jaxbContext = JAXBContext.newInstance(Tweets.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Tweets tweets = (Tweets) jaxbUnmarshaller.unmarshal(xmlFile);		
		if (getContent)
		{
			List<Tweet> tweetList = tweets.getTweet();
			int tope = tweetList.size();			
			for(int indTweet=0; indTweet<tope; indTweet++)
			{
				Tweet tweet = tweetList.get(indTweet);
				if ((tweet.getContent()==null)||(tweet.getContent().length()==0))
				{
					TweetUtils.getTweetContent(tweet);
				}
			}
		}
		return tweets;
		}catch(Exception ex){			
			ex.printStackTrace();
			return new Tweets();
		}
	}
	
	
	
	public static void SaveToXML(String filePath, Tweets tweets)
	{
		try{
		File xmlFile = new File(filePath);
	    JAXBContext context = JAXBContext.newInstance(Tweets.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    m.marshal(tweets, xmlFile);	    
		}catch(Exception ex){
			ex.printStackTrace();
		}		
	}
	
	

}
