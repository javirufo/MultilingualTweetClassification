package tweets;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import ejecutables.URLTweetDownloader;

import textprocess.Lemmatizer;
import textprocess.NGrams;
import textprocess.Stemmer;
import textprocess.StopWords;
import textprocess.Tokenizer;
import utils.URLUtils;
import xml.es.daedalus.tass.tweets.Tweet;
import xml.es.daedalus.tass.tweets.Tweets;
import xml.es.daedalus.tass.usuarios.*;

public class ProcessTweets {


	private boolean stemming;
	private boolean lematization;
	private boolean extract_urls;
	private boolean extract_hashtags;
	private boolean extract_mentions;	
	private boolean unigrams;
	private boolean bigrams;
	
	private StopWords stopwords;
	private boolean training;

	
	
	
	public ProcessTweets(boolean stemming, boolean lematization,
			boolean extract_urls, boolean extract_hashtags,
			boolean extract_mentions, boolean unigrams, boolean bigrams) {	
		stopwords = new StopWords();
		this.stemming = stemming;
		this.lematization = lematization;
		this.extract_urls = extract_urls;
		this.extract_hashtags = extract_hashtags;
		this.extract_mentions = extract_mentions;
		this.unigrams = unigrams;
		this.bigrams = bigrams;
	}



	
	
	public ProcessTweets() {
		stopwords = new StopWords();
	}





	public List<ProcessedTweet> processTweets(List<Tweet> tweets){		
		List<ProcessedTweet> pTweets = new ArrayList<ProcessedTweet>();
		int tope = tweets.size();
		for (int indTweet=0; indTweet<tope; indTweet++)
		{
			Tweet tweet = tweets.get(indTweet);
			ProcessedTweet pTweet = procesaTweet(tweet);
			pTweets.add(pTweet);
		}
		return pTweets;
	}
	
	
	
	
	
	public String cleanContent(String content)
	{
		content = content.replaceAll("\\*",""); //Para evitar errores
		content = content.replaceAll("\\[", "");
		content = content.replaceAll("\\]", "");
		content = content.replaceAll("\\]?", "");
		content = content.replaceAll("\\+", "");
		content = content.replaceAll("\\}", "");
		content = content.replaceAll("\\{", "");
		content = content.replaceAll("\\\\", "");
		return content;
		
	}
	
	public ProcessedTweet procesaTweet(Tweet tweet)
	{		
		ProcessedTweet pTweet = new ProcessedTweet(tweet);				
		String tweetContent = tweet.getContent();
//		pTweet.setOriginalContent(tweet.getContent());
		try{
		if (tweetContent!=null)
		{		
			tweetContent = cleanContent(tweetContent);
//Extraigo los hashtags y decido si añadirlos o no al tweet procesado. De todas formas, se elimina del
//contenido del tweet.
			List<String> hashtags = Tokenizer.getElement(tweetContent, "#", Tokenizer.REG_HASH_USERS);
			for(int index=0; index<hashtags.size(); index++)
			{
				tweetContent = tweetContent.replaceAll(hashtags.get(index), "");
			}
			if (extract_hashtags)
				pTweet.setHastags(hashtags);
/* Ahora extraigo las url que contiene el tweet. Al igual que sucede con los hashtags, los elimino del 
 * contenido del tweet y luego comprueno si debo añadirlos al tweetprocesado.
 */
			List<String> urls = Tokenizer.getURLs(tweetContent, "http://", Tokenizer.REG_URLS); 		
			List<String> urlsContent = new ArrayList<String>();
			urls.addAll(Tokenizer.getURLs(tweetContent, "https://", Tokenizer.REG_URLS));
			int tope = urls.size();
			for(int index=0; index<urls.size(); index++)
			{
				tweetContent = tweetContent.replaceAll(urls.get(index), "");
			}
					
/* 
 * Sólo extraigo el contenido de las URLs si lo tengo indicado, en caso contrario, no me moleso
 */
			if (extract_urls)
			{											
				for(int indURL=0; indURL<tope; indURL++)					
				{		
					String url = urls.get(indURL);
					if (training)
					{
						switch(tweet.getLang())
						{
						case "es": 
							url = "es_"+url;
							break;
						case "fr":
							url = "FR_"+url;
							break;
						case "en":
							url = "EN_"+url;
							break;
							
						}
					}
					tweetContent = tweetContent+" "+cleanContent(URLUtils.getURLContent(url));
				}				
			}
			urls.clear();	
/*			
			if (extract_urls)
			{	
				pTweet.setOriginalURLContents(urlsContent);
//Hago el stemming del contenido de las url		
				if (stemming)
					pTweet.setUrlsContent(Stemmer.stemming(urlsContent));	
				else
					pTweet.setUrlsContent(urlsContent);	
			}
*/			
/* Con las menciones hago lo mismo que he hecho con urls y hashtags, las elimino del contenido del
 * tweet			
 */
			List<String> mentions = Tokenizer.getElement(tweetContent, "@", Tokenizer.REG_HASH_USERS);
			for(int index=0; index<mentions.size(); index++)
			{
				tweetContent = tweetContent.replaceAll(mentions.get(index), "");
			}						
			if (extract_mentions)
				pTweet.setUsersRef(mentions);	
			
//Llevo a cabo el tokenizado			
			List<String> termsTweet = (ArrayList<String>)Tokenizer.tokenize(tweetContent);			
//Elimino stopwords
			termsTweet = stopwords.deleteStopWords(termsTweet, tweet.getLang());
//			Ahora se lleva a cabo el proceso de stemming o lematizacion
			if (stemming)
				termsTweet = Stemmer.stemming(termsTweet, tweet.getLang());
//			else
//				pTweet.setContentStem(Stemmer.stemming(termsTweet, tweet.getLang()));
			if (lematization)
			{
				String lemmatizar= "";
				for (String s : termsTweet)
				{
					lemmatizar += " "+s;
				}
/*Este caso lo voy a quitar porque no se nos da*/
				if(stemming)
					termsTweet.addAll(Lemmatizer.lemmatize(lemmatizar));
				else
					termsTweet = Lemmatizer.lemmatize(lemmatizar);
/*
				if (lematization)
					termsTweet = Lemmatizer.lemmatize(lemmatizar);
				else
					pTweet.setContentLemma(Lemmatizer.lemmatize(lemmatizar));
*/					
			}				
			pTweet.setContent(termsTweet);
			if (unigrams)
			{
//El contenido viene tokenizado, por lo que ya está con unigramas :)				
			}
			if (bigrams)
			{
//Tengo que tener en cuenta si selecciono unigramas y bigramas				
				List<String> bigrams = NGrams.obtainNGrams(pTweet.getContent(), 2);
				if (unigrams)
				{
					bigrams.addAll(pTweet.getContent());
					pTweet.setContent(bigrams);
				}
				else
					pTweet.setContent(bigrams);
			}
			pTweet.calculaTF();
		}
		}catch(Exception e)
		{
			System.out.println(tweetContent);
			e.printStackTrace();
			System.exit(-1);
		}
		return pTweet;
	}
	
	
	
	public void doStemming(List<ProcessedTweet> corpus)
	{
		int tope=corpus.size();
		for(int ind=0; ind<tope; ind++)
			corpus.get(ind).getStemming();
	}
	
	public void doLematization(List<ProcessedTweet> corpus)
	{
		int tope=corpus.size();
		for(int ind=0; ind<tope; ind++)
			corpus.get(ind).getLemma();
	}





	public boolean isTraining() {
		return training;
	}





	public void setTraining(boolean training) {
		this.training = training;
	}

	

}
