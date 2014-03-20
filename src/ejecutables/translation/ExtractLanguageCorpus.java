package ejecutables.translation;

import java.util.List;

import tweets.TweetLoader;
import xml.es.daedalus.tass.tweets.Tweet;
import xml.es.daedalus.tass.tweets.Tweets;

public class ExtractLanguageCorpus {

	/**
	 * @param args
	 * args[0] - Corpus original
	 * args[1] - Desired language 
	 * args[2] - output corpus
	 * @throws InterruptedException 
	 */
	public static void main(String[] args){
		if (args.length != 3)
		{
			System.out.println("Formato de invocación erróneo. El formato es:");
			System.out.println("java -jar ExtractLanguageCorpus.jar Corpus_origen Idioma Corpus_extraido");
			return;
		}				
		// TODO Auto-generated method stub	
		List<Tweet> corpus = TweetLoader.LoadFromXML(args[0], false).getTweet();
		Tweets corpusT = new Tweets();
		for(int ind=0; ind<corpus.size(); ind++)
		{
			if (corpus.get(ind).getLang().compareTo(args[1])==0)
			{
				corpusT.getTweet().add(corpus.get(ind));
			}
		}
		TweetLoader.SaveToXML(args[2], corpusT);		
	}
}
