package ejecutables;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import textprocess.Tokenizer;
import tweets.TweetLoader;
import utils.URLUtils;
import xml.es.daedalus.tass.tweets.Tweet;
import xml.es.daedalus.tass.tweets.Tweets;

public class URLTweetDownloader {

	/**
	 * @param args
	 * Este ejecutable crea un fichero que almacenará el contenido de las url (title, h1 y h2) que hay en los tweets
	 * El primer parámetro es el fichero que almacena o almacenará las url
	 * Recibe por parámetros tantos ficheros en formato xml (formato tass) que contiene los tweets a procesar
	 */
	public static void main(String[] args) {
		if (args.length < 2)
		{
			System.out.println("Formato de invocación erróneo. El formato es:");
			System.out.println("java -jar URLTweetDownloader.jar Fichero_urls Resto_ficheros_tweets");
			return;
		}				
		int topFile = args.length;
		URLUtils.loadFromFile(args[0]);
		for(int indFile=1; indFile<topFile; indFile++)
		{
			Tweets tweets = TweetLoader.LoadFromXML(args[indFile], false);		
			int topTweets = tweets.getTweet().size();
			System.out.println("***"+topTweets);
			for(int indTweet=0; indTweet<topTweets; indTweet++)
			{
				if (indTweet%1000==0)
					System.out.println(indTweet);
				Tweet tweet = tweets.getTweet().get(indTweet);
				List<String> urls = Tokenizer.getURLs(tweet.getContent(), "http://", Tokenizer.REG_URLS);
				int topURL = urls.size();				
				for (int indURL=0; indURL<topURL; indURL++)
				{
					URLUtils.getURLContent(urls.get(indURL));
				}				
			}
		
		}
		URLUtils.saveToFile(args[0]);
	}

}
