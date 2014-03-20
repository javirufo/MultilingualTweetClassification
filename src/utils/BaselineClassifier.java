package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import xml.es.daedalus.tass.tweets.Tweet;

public class BaselineClassifier {
	private List<String> topics;
	private List<Integer> topicTwets;
	private List<Integer> topicCorrect;
	private List<Integer> topicIndicated;
	private int totalTweets;
	private float calcPrec;
	private float calcRecall;	
	public BaselineClassifier(List<Tweet> test, int totalTopics)
	{
		topics = new ArrayList<String>();;
		topicTwets = new ArrayList<Integer>();
		topicCorrect= new ArrayList<Integer>();
		topicIndicated= new ArrayList<Integer>();
		int tweetClass;
		totalTweets = test.size();
		Random rn = new Random();
		//int i = rn.nextInt() % n;
		for(int ind=0; ind<totalTweets; ind++)
		{
			Tweet tweet = test.get(ind);
			String topic = tweet.getTopics().getTopic().get(0);
			
			tweetClass=ind%totalTopics;
			//tweetClass = 0;// Math.abs(rn.nextInt()%totalTopics);
			String topicClass = Integer.toString(tweetClass);
			if (topics.indexOf(topicClass)<0)
			{
				topics.add(topicClass);
				topicTwets.add(new Integer(0));
				topicCorrect.add( new Integer(0));
				topicIndicated.add(new Integer(1));
			}
			else
			{
				topicIndicated.set(topics.indexOf(topicClass), new Integer(topicIndicated.get(topics.indexOf(topicClass)).intValue()+1));
			}
			int index=topics.indexOf(topic);			
			if (index>=0)
			{
				topicTwets.set(index, new Integer(topicTwets.get(index).intValue()+1));
				if (Integer.valueOf(topic)==tweetClass)
				{
					topicCorrect.set(index, new Integer(topicCorrect.get(index).intValue()+1));
				}
//				topicIndicated.set(index, new Integer(topicIndicated.get(index).intValue()+1));
			}
			else
			{
				topics.add(topic);
				topicTwets.add(new Integer(1));
				if (Integer.valueOf(topic)==tweetClass)
					topicCorrect.add( new Integer(1));
				else
					topicCorrect.add( new Integer(0));
				topicIndicated.add(new Integer(0));
			}
		}
	}
	
	public float getRecall(){
		float totalRecall = 0;
		for(int i=0; i<topics.size(); i++)
		{
			totalRecall += 	(float)((float)topicCorrect.get(i).intValue()/(float)topicTwets.get(i).intValue());		
		}
		totalRecall /= topics.size();
		calcRecall = totalRecall;
		return totalRecall;
	}
	
	public float getPrecision(){
		float totalPrec = 0;
		for(int i=0; i<topics.size(); i++)
		{
			totalPrec += (float)((float)topicCorrect.get(i).intValue()/(float)topicIndicated.get(i).intValue());		
		}
		totalPrec /= topics.size();
		calcPrec = totalPrec;
		return totalPrec;		
	}
	
	public float getFmeasure(){
		return 2*((calcPrec*calcRecall)/(calcRecall+calcPrec));
	}
}
