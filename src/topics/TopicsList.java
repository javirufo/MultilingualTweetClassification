package topics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.dcoref.Mention;

import tweets.ProcessedTweet;
import tweets.TFTable;

public class TopicsList implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private List<TopicDesc> topicsList;
	private List<String> topicsNames;
//Esta constante indica los términos a tomar de cada topic. Quizás sea conveniente indicarlo en un fichero de configuración y mostrarlo en %	
	private int PERCENT_TERMS_PER_TOPIC = 70; 
	private int PERCENT_HASHTAGS_PER_TOPIC = 10;
	private int PERCENT_MENTIONS_PER_TOPIC = 20;
	private int selectionFeature;
	private int maxFeatures=1500;
	

	public TopicsList(){
		topicsList = new ArrayList<TopicDesc>();
		topicsNames = new ArrayList<String>();	
		selectionFeature = TopicDesc.TERM_TF;
	}
	
	public TopicsList(int maxFeatures, int termspercent, int hashtagspercent, int mentionspercent, int selectionFeature){
		this.maxFeatures = maxFeatures;
		topicsList = new ArrayList<TopicDesc>();
		topicsNames = new ArrayList<String>();
		PERCENT_TERMS_PER_TOPIC = termspercent;
		PERCENT_HASHTAGS_PER_TOPIC = hashtagspercent;
		PERCENT_MENTIONS_PER_TOPIC = mentionspercent;
		this.selectionFeature = selectionFeature;
	}	
	
	
	
	public void addDocument(String topic)
	{
		int index = topicsNames.indexOf(topic);
		if (index>=0)
			topicsList.get(index).addDocument();
	}
	
	public void addWord(String word, int frequency, String topic)
	{
		int index = topicsNames.indexOf(topic);
		boolean insertar=true;
//Primero compruebo si el topic no existe		
/*		
		int tope = topicsList.size();
		for (int i=0; i<tope; i++)
		{
			if ((i!=index) && (topicsList.get(i).containsWord(word)))
			{
				topicsList.get(i).removeWord(word);
				insertar = false;
			}
		}
		if (!insertar)
			return;	
*/				
		if (index>=0)
		{
			topicsList.get(index).addWord(word, frequency);
		}
		else
		{
			TopicDesc newTopic = new TopicDesc(topic, topicsList.size(), maxFeatures, PERCENT_TERMS_PER_TOPIC, PERCENT_HASHTAGS_PER_TOPIC, PERCENT_MENTIONS_PER_TOPIC, selectionFeature);
			newTopic.addWord(word, frequency);
			topicsList.add(newTopic);
			topicsNames.add(topic);
		}
	}
	
	
	public void addHashtag(String hashtag, String topic)
	{
		int index = topicsNames.indexOf(topic);
		boolean insertar=true;
//Primero compruebo si el topic no existe
/*
		int tope = topicsList.size();
		for (int i=0; i<tope; i++)
		{
			if ((i!=index) && (topicsList.get(i).containsHashtag(hashtag)))
			{
				topicsList.get(i).removeHashtag(hashtag);
				insertar = false;
			}
		}
		if (!insertar)
			return;
*/			
			
		if (index>=0)
		{
			topicsList.get(index).addHashtag(hashtag);
		}
		else
		{
			TopicDesc newTopic = new TopicDesc(topic, topicsList.size(), maxFeatures, PERCENT_TERMS_PER_TOPIC, PERCENT_HASHTAGS_PER_TOPIC, PERCENT_MENTIONS_PER_TOPIC, selectionFeature);
			newTopic.addHashtag(hashtag);
			topicsList.add(newTopic);
			topicsNames.add(topic);
		}		
	}
	
	public void addMention(String mention, String topic)
	{
		int index = topicsNames.indexOf(topic);
		boolean insertar=true;
//Primero compruebo si el topic no existe
/*		
		int tope = topicsList.size();
		for (int i=0; i<tope; i++)
		{
			if ((i!=index) && (topicsList.get(i).containsMention(mention)))
			{
				topicsList.get(i).removeMention(mention);	
				insertar=false;
			}
		}
		if (!insertar)
			return;
*/			
		if (index>=0)
		{
			topicsList.get(index).addMention(mention);
		}
		else
		{
			TopicDesc newTopic = new TopicDesc(topic, topicsList.size(), maxFeatures, PERCENT_TERMS_PER_TOPIC, PERCENT_HASHTAGS_PER_TOPIC, PERCENT_MENTIONS_PER_TOPIC, selectionFeature);
			newTopic.addMention(mention);
			topicsList.add(newTopic);
			topicsNames.add(topic);
		}		
	}	
	
//Este método prepara los topics para quedarse con X términos de cada a generar el fichero arff.	
	public void prepareTopics()
	{
		int tope = topicsNames.size();
		int featuresTopic = maxFeatures/tope;
		for(int index=0; index<tope; index++)
		{
			topicsList.get(index).setMaxFeatures(featuresTopic);
			topicsList.get(index).prepareTopic();
		}
	}
	
	public void printTopics()
	{
		int tope = topicsList.size();
		for (int index=0; index<tope; index++)
		{
			topicsList.get(index).print();
			System.out.println("*********************");
		}
	}
	
	
	public void insertTweet(ProcessedTweet pTweet)
	{
		int sizeTopics = pTweet.getTopics().getTopic().size();
		for (int indTopic=0; indTopic<sizeTopics; indTopic++)
		{
			String topic = pTweet.getTopics().getTopic().get(indTopic);
			TFTable wordsTF = pTweet.getTfContent();
			int sizeContent = wordsTF.getWords().size();
//			System.out.println("Rellenando palabras");
			for (int indContent=0; indContent<sizeContent; indContent++)
			{					
				this.addWord(wordsTF.getWords().get(indContent), wordsTF.getFrequencies().get(indContent).intValue(), topic);
			}
			int sizeHash = pTweet.getHastags().size();
//			System.out.println("Rellenando hashtags");
			for (int indHash=0; indHash<sizeHash; indHash++)
			{
				this.addHashtag(pTweet.getHastags().get(indHash), topic);
			}				
			int sizeUsers = pTweet.getUsersRef().size();
//			System.out.println("Rellenando usuarios");
			for (int indUsers=0; indUsers<sizeUsers; indUsers++)
			{
				this.addMention(pTweet.getUsersRef().get(indUsers), topic);
			}
			addDocument(topic);
		}
/*		
		int sizeTopics = pTweet.getTopics().getTopic().size();
		for (int indTopic=0; indTopic<sizeTopics; indTopic++)
		{
			String topic = pTweet.getTopics().getTopic().get(indTopic);
			TFTable wordsTF = pTweet.getTfContent();
			int sizeContent = wordsTF.getWords().size();
//			System.out.println("Rellenando palabras");
			for (int indContent=0; indContent<sizeContent; indContent++)
			{					
				this.addWord(wordsTF.getWords().get(indContent), wordsTF.getFrequencies().get(indContent).intValue(), topic);
			}
			int sizeHash = pTweet.getHastags().size();
//			System.out.println("Rellenando hashtags");
			for (int indHash=0; indHash<sizeHash; indHash++)
			{
				this.addHashtag(pTweet.getHastags().get(indHash), topic);
			}				
			int sizeUsers = pTweet.getUsersRef().size();
//			System.out.println("Rellenando usuarios");
			for (int indUsers=0; indUsers<sizeUsers; indUsers++)
			{
				this.addMention(pTweet.getUsersRef().get(indUsers), topic);
			}
			addDocument(topic);
		}
*/		
	}
	
	public void fillTopics(List<ProcessedTweet> pTweets)
	{
		int tope = pTweets.size();
		for (int index=0; index<tope; index++)
		{
			ProcessedTweet pTweet = pTweets.get(index);
			insertTweet(pTweet);

		}
		
	}
	
	
	
	public BufferedWriter generateArffHeader(String file)
	{
		BufferedWriter bw;
		try 
		{				
	//Esta parte es para escribir en fichero		
			bw = new BufferedWriter(new FileWriter(file));			
			bw.write("@RELATION TopicDetection\n\n");
			bw.write("@ATTRIBUTE TweetId NUMERIC\n");
//Indico aquí los terminos que conforman el vector
			int tope = this.topicsList.size();
			int attnumber=0;
			for (int i=0; i<tope; i++)
			{
				TopicDesc topic = topicsList.get(i);
				bw.write("% TOPIC: "+topic.getName()+"\n");
/*				
				List<String> words = topic.getWords();				
				for(int indWords=0; indWords<words.size(); indWords++)
				{
					bw.write("@ATTRIBUTE "+words.get(indWords)+" NUMERIC\n");					
					indAtt++;
				}
				List<String> hashtags = topic.getHashtags();
				for(int indWords=0; indWords<hashtags.size(); indWords++)
				{
					bw.write("@ATTRIBUTE "+hashtags.get(indWords)+" NUMERIC\n");
					indAtt++;
				}	
				List<String> mentions = topic.getMentions();
				for(int indWords=0; indWords<mentions.size(); indWords++)
				{					
					bw.write("@ATTRIBUTE "+mentions.get(indWords)+" NUMERIC\n");
				}
*/
				int totalAtt = topic.getWords().size()+topic.getHashtags().size()+topic.getMentions().size();
				for(int indAtt=0; indAtt<totalAtt; indAtt++)
				{
					bw.write("@ATTRIBUTE ATT"+attnumber+" NUMERIC\n");
					attnumber++;
				}
			}
//Ahora defino el atributo Topic							
			bw.write("@ATTRIBUTE Topic {");
			tope = topicsNames.size();
			for(int i=0; i<tope; i++)
			{
				bw.write("'"+topicsNames.get(i)+"'");
				if (i<tope-1)
					bw.write(",");
			}
			bw.write("}\n@DATA\n");
			return bw;
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void generateArffVector(BufferedWriter bw, ProcessedTweet ptweet)
	{
		try{			
			String vector=new String();
			vector+=ptweet.getTweetid().toString()+",";
			int tope = topicsList.size();
			for (int index=0; index<tope; index++)
			{
				vector += topicsList.get(index).generateArffVector(ptweet);
			}
			//Por último, indico la clase (topic)
			if ((ptweet.getTopics()!=null) && (ptweet.getTopics().getTopic().size()>0))
				vector += "'"+ptweet.getTopics().getTopic().get(0)+"'";
			else
				vector += "?";
			bw.write(vector);
			bw.write("\n");					
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	public void generateArffSchema(String file, List<ProcessedTweet> pTweet)
	{
		BufferedWriter bw;
		try 
		{				
	//Esta parte es para escribir en fichero		
			bw = new BufferedWriter(new FileWriter(file));			
			bw.write("@RELATION TopicDetection\n\n");
			bw.write("@ATTRIBUTE TweetId NUMERIC\n");
//Indico aquí los terminos que conforman el vector
			int tope = this.topicsList.size();
			for (int i=0; i<tope; i++)
			{
				TopicDesc topic = topicsList.get(i);
				bw.write("% TOPIC: "+topic.getName()+"\n");
				List<String> words = topic.getWords();
				for(int indWords=0; indWords<words.size(); indWords++)
				{
					bw.write("@ATTRIBUTE "+words.get(indWords)+" NUMERIC\n");
				}
				List<String> hashtags = topic.getHashtags();
				for(int indWords=0; indWords<hashtags.size(); indWords++)
				{
					bw.write("@ATTRIBUTE "+hashtags.get(indWords)+" NUMERIC\n");
				}	
				List<String> mentions = topic.getMentions();
				for(int indWords=0; indWords<mentions.size(); indWords++)
				{
					bw.write("@ATTRIBUTE "+mentions.get(indWords)+" NUMERIC\n");
				}				
			}
//Ahora defino el atributo Topic							
			bw.write("@ATTRIBUTE Topic {");
			tope = topicsNames.size();
			for(int i=0; i<tope; i++)
			{
				bw.write("'"+topicsNames.get(i)+"'");
				if (i<tope-1)
					bw.write(",");
			}
			bw.write("}\n@DATA\n");
/*
 * Ahora tengo que recorrer los tweets procesados y generar los vectores			
 */
			tope = topicsList.size();
			int topeTweets = pTweet.size();
			
			for (int indTweet=0; indTweet<topeTweets; indTweet++)
			{
				ProcessedTweet ptweet = pTweet.get(indTweet);
				String vector=new String();
				vector+=ptweet.getTweetid().toString()+",";
				for (int index=0; index<tope; index++)
				{
					vector += topicsList.get(index).generateArffVector(ptweet);
				}
				//Por último, indico la clase (topic)
				if ((ptweet.getTopics()!=null) && (ptweet.getTopics().getTopic().size()>0))
					vector += "'"+ptweet.getTopics().getTopic().get(0)+"'";
				else
					vector += "?";
				bw.write(vector);
				bw.write("\n");
			}
			bw.flush();
			bw.close();				
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	


	public List<String> getTopicsNames() {
		return topicsNames;
	}

	public List<TopicDesc> getTopicsList() {
		return topicsList;
	}

	public int getSelectionFeature() {
		return selectionFeature;
	}

	public void setSelectionFeature(int selectionFeature) {
		this.selectionFeature = selectionFeature;
	}

	public int getMaxFeatures() {
		return maxFeatures;
	}

	public void setMaxFeatures(int maxFeatures) {
		this.maxFeatures = maxFeatures;
	}
	
}
