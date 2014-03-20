package ejecutables;

import java.util.List;

import tweets.TweetLoader;
import xml.es.daedalus.tass.tweets.Tweet;
import xml.es.daedalus.tass.tweets.Tweets;

public class JoinCorpus {

	/**
	 * args0 -> output file with joined corpus
	 * rest corpus to join
	 */
	public static void main(String[] args) {
		if (args.length < 2)
		{
			System.out.println("Formato de invocación erróneo. El formato es:");
			System.out.println("java -jar JoinCorpus.jar Corpus_unido Ficheros_a_unir");
			return;
		}				
		Tweets corpusT = new Tweets();
		List<Tweet> tweetJoin= corpusT.getTweet();
		for(int i=1; i<args.length; i++)
		{
			tweetJoin.addAll(TweetLoader.LoadFromXML(args[i], false).getTweet());
		}	
		TweetLoader.SaveToXML(args[0], corpusT);		


	}

}
