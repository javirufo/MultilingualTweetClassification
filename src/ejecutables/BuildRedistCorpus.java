package ejecutables;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import textprocess.Tokenizer;
import tweets.TweetLoader;
import utils.URLUtils;
import xml.es.daedalus.tass.tweets.Tweet;
import xml.es.daedalus.tass.tweets.Tweets;

public class BuildRedistCorpus {

	/**
	 * argi -> Fichero corpus a redistribuir
	 * 
	 * Genera dos ficheros
	 * argi+_redist.xml
	 * argi+_redist.txt
	 */
	public static void main(String[] args) {
		try {
			if (args.length != 1)
			{
				System.out.println("Formato de invocación erróneo. El formato es:");
				System.out.println("java -jar BuildRedisCorpus.jar Fichero_corpus");
				return;
			}					
			int topFiles = args.length;
			for(int indFile=0; indFile<topFiles; indFile++)
			{
				Tweets tweets = TweetLoader.LoadFromXML(args[indFile], false);
				int posExt = args[indFile].lastIndexOf(".");
				String fileXML = args[indFile].substring(0, posExt)+"_redist.xml";
				String fileTXT = args[indFile].substring(0, posExt)+"_redist.txt";
				FileWriter outFile=new FileWriter(fileTXT);
				PrintWriter out=new PrintWriter(outFile);
				int topTweets = tweets.getTweet().size();			
				for(int indTweet=0; indTweet<topTweets; indTweet++)
				{
					if (indTweet%1000==0)
						System.out.println(indTweet);
					Tweet tweet = tweets.getTweet().get(indTweet);					
					tweet.setContent(null);
					out.println(tweet.getTweetid().toString()+"\t"+tweet.getUser()+"\t"+tweet.getTopics().getTopic().get(0));
				}
				TweetLoader.SaveToXML(fileXML, tweets);
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
