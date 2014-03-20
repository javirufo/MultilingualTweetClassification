package tweets;

import java.util.ArrayList;
import java.util.List;

import json.tweets.JSONTweet;

//import json.tweets.JSONTweet;

import xml.es.daedalus.tass.tweets.Sentiments;
import xml.es.daedalus.tass.tweets.Topics;
import xml.es.daedalus.tass.tweets.Tweet;
import xml.es.daedalus.tass.tweets.Tweets;
import xml.es.daedalus.tass.usuarios.User;
import xml.es.daedalus.tass.usuarios.Users;

public class TweetUtils {
//Creo una cache de usuarios para evitar recorrer toda la lista siempre	
	public static ArrayList<String> screenNamesCache = new ArrayList<String>();
	public static ArrayList<String> userTypeCache = new ArrayList<String>();

	public static void printTweets(Tweets tweets){
		List<Tweet> lista = tweets.getTweet();
		for(int index=0; index<lista.size(); index++)
		{
			Tweet tweet = lista.get(index);
			System.out.println("Tweet ID: "+tweet.getTweetid());
			System.out.println("Usuario: "+tweet.getUser());
			System.out.println("Fecha: "+tweet.getDate());
			System.out.println("Idioma: "+tweet.getLang());
			System.out.println("Contenido: "+tweet.getContent());
			Sentiments sent = tweet.getSentiments();
			System.out.println("Sentimientos");
			for (int indPol=0; indPol<sent.getPolarity().size(); indPol++)
			{
				System.out.println("	"+sent.getPolarity().get(indPol).getType());
				System.out.println("	"+sent.getPolarity().get(indPol).getValue());
			}
			System.out.println("Topics");
			Topics topics = tweet.getTopics();
			for (int indTopic=0; indTopic<topics.getTopic().size(); indTopic++)
			{
				System.out.println("	"+topics.getTopic().get(indTopic).toString());	
			}						
			System.out.println("*****************");
		}
		
	}
	
	
	public static void printProcessedTweets(List<ProcessedTweet> pTweets)
	{
		
		for(int index=0; index<pTweets.size(); index++)
		{
			ProcessedTweet ptweet = pTweets.get(index);					
			System.out.println(index+") Tweet ID: "+ptweet.getTweetid());
			System.out.println("Idioma: "+ptweet.getLang());
//			System.out.println("Contenido: "+ptweet.getOriginalContent());
			System.out.println("Topics");
			Topics topics = ptweet.getTopics();
			for (int indTopic=0; indTopic<topics.getTopic().size(); indTopic++)
			{
				System.out.println("	"+topics.getTopic().get(indTopic).toString());	
			}						
			System.out.println("**** Processed content ****");			
			System.out.println("Content words");
			List<String> words = ptweet.getContent();
			for (int indWords=0; indWords<words.size(); indWords++)
			{
				System.out.println("	"+words.get(indWords));	
			}	
			System.out.println("Hashtags");
			List<String> hastags = ptweet.getHastags();
			for (int indHashtags=0; indHashtags<hastags.size(); indHashtags++)
			{
				System.out.println("	"+hastags.get(indHashtags));	
			}				
			System.out.println("Users refs");
			List<String> users = ptweet.getUsersRef();
			for (int indUsers=0; indUsers<users.size(); indUsers++)
			{
				System.out.println("	"+users.get(indUsers));	
			}							
			System.out.println("**** User info ****");
			System.out.println(ptweet.getTipo());
			System.out.println("**** Calculated data *****");
			System.out.println("****Words frequency****");
			ptweet.getTfContent().print();
			System.out.println("***************************");
			
		}		
	}
	
	public static void getTweetContent(Tweet tweet)
	{
			tweet.setContent(JSONTweet.getTweetContent(tweet.getTweetid().toString()));
	}

	
}
