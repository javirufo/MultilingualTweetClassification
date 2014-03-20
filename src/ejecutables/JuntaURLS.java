package ejecutables;

import java.util.ArrayList;
import java.util.List;

import textprocess.Tokenizer;
import tweets.TweetLoader;
import utils.URLUtils;
import xml.es.daedalus.tass.tweets.Tweet;
import xml.es.daedalus.tass.tweets.Tweets;

public class JuntaURLS {

	/**
	 * @param args
	 * args[0] fichero 1
	 * args[2] fichero 2
	 * args[3] fichero destino
	 */
	public static void main(String[] args) {
		if (args.length != 3)
		{
			System.out.println("Formato de invocación erróneo. El formato es:");
			System.out.println("java -jar JuntaURLs.jar Fichero1_urls Fichero2_urls Fichero_unido");
			return;
		}				
		List<String> urls, contents;		
		
		urls = new ArrayList<String>();
		contents = new ArrayList<String>();
		for(int i=1; i<args.length; i++)
		{
			URLUtils.loadFromFile(args[i]);
			urls.addAll(URLUtils.getUrls());
			contents.addAll(URLUtils.getContents());
			
		}
		URLUtils.setUrls(urls);
		URLUtils.setContents(contents);
		URLUtils.saveToFile(args[0]);
	}

}
