package ejecutables.translation;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import textprocess.Tokenizer;
import tweets.TweetLoader;
import utils.URLUtils;
import xml.es.daedalus.tass.tweets.Tweet;
import xml.es.daedalus.tass.tweets.Tweets;

public class InsertaURLIdioma {

	/**
	 * @param args
	 * arg0 Fichero url
	 * arg1 Fichero url original
	 * arg2 Fichero corpus
	 * arg3 idioma
	 */
	
	public static void main(String[] args) {
		if (args.length != 4)
		{
			System.out.println("Formato de invocación erróneo. El formato es:");
			System.out.println("java -jar InsertaURLIdioma.jar Fichero_urls Fichero_url_original Fichero_corpus Idioma");
			return;
		}				
		List<String> urls, contents;		
		Tweets corpus=TweetLoader.LoadFromXML(args[2], false);		
		URLUtils.loadFromFile(args[0]);
		urls = URLUtils.getUrls();
		contents = URLUtils.getContents();
		
		
		URLUtils.loadFromFile(args[1]);
		List<Tweet> tweets = corpus.getTweet();		
		for(int ind=0; ind<tweets.size(); ind++)
		{
			Tweet tweet = tweets.get(ind);
			if (tweet.getLang().compareTo(args[3])==0)
			{
				List<String> urlsT = Tokenizer.getURLs(tweet.getContent(), "http://", Tokenizer.REG_URLS);
				int topURL = urlsT.size();
				for (int indURL=0; indURL<topURL; indURL++)
				{
					urls.add(urlsT.get(indURL));
					contents.add(URLUtils.getURLContent(urlsT.get(indURL)));					
				}				
				
			}
		}
		URLUtils.setUrls(urls);
		URLUtils.setContents(contents);
		URLUtils.saveToFile(args[0]);
		
	}

}
