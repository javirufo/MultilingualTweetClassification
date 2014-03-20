package topics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tweets.ProcessedTweet;
import tweets.TFTable;

public class TopicDesc implements Serializable{
	
	public static final int TERM_TF = 0;
	public static final int TERM_TF_IDF = 1;
	public static final int MIN_FREQ_TERMS = 3;
	public static final int MIN_FREQ_HASHTAGS = 15;
	public static final int MIN_FREQ_MENTIONS = 10;
	
	private static final long serialVersionUID = 1L;
	private String name;
	private int id;
	private List<String> words;
	private List<Integer> frequencies;
	private List<Integer> documents;
	private List<Double> tfidfwords;
	private List<String> hashtags;
	private List<Integer> hashtagsFrequencies;
	private List<Integer> hashtagsDocuments;
	private List<Double> tfidfhashtags;
	private List<String> mentions;
	private List<Integer> mentionsFrequencies;
	private List<Integer> mentionsDocuments;
	private List<Double> tfidfmentions;
	private int percent_terms;
	private int percent_hashtags;
	private int percent_mentions;
	private int totalDocuments;
	private int selectionFeature;
	private int maxFeatures;
	private HashMapFrequency wordsIDF;
	private HashMapFrequency hashtagsIDF;
	private HashMapFrequency mentionsIDF;
	
	
	public TopicDesc (String name, int id, int maxFeatures, int percentTerms, int percentHashtags, int percentMentions, int selectionFeature)
	{
		this.name = name;
		this.id = id;
		words = new ArrayList<String>();
		hashtags = new  ArrayList<String>();
		frequencies = new ArrayList<Integer>();
		mentions = new ArrayList<String>();
		hashtagsFrequencies = new ArrayList<Integer>();
		mentionsFrequencies = new ArrayList<Integer>();
		documents = new ArrayList<Integer>();
		hashtagsDocuments =  new ArrayList<Integer>();
		mentionsDocuments = new ArrayList<Integer>();
		percent_terms = percentTerms;
		percent_hashtags = percentHashtags;
		percent_mentions = percentMentions;
		totalDocuments = 0;
		this.selectionFeature = selectionFeature;
		tfidfwords = new ArrayList<Double>();
		tfidfhashtags = new ArrayList<Double>();
		tfidfmentions = new ArrayList<Double>();
		this.maxFeatures = maxFeatures;
		wordsIDF =  new HashMapFrequency();
		hashtagsIDF= new HashMapFrequency();
		mentionsIDF= new HashMapFrequency();
	}		
	
	public void addDocument(){
		totalDocuments++;
	}
	
	public int getDocuments(){
		return totalDocuments;
	}
	
	public void addWord(String word, int frequency)
	{
/*		
		int index = words.indexOf(word);
		if (index>=0)
		{
			frequencies.set(index, Integer.valueOf(frequencies.get(index).intValue()+frequency));
			documents.set(index, Integer.valueOf(documents.get(index).intValue()+1));
		}
		else
		{
			words.add(word);
			frequencies.add(Integer.valueOf(frequency));
			documents.add(Integer.valueOf(1));
		}
*/
			words.add(word);
			wordsIDF.put(word, new Integer(frequency));
	}
	
	
	public void addHashtag(String hashtag)
	{
		/*
		int index=hashtags.indexOf(hashtag);		
		if (index>=0)
		{
			hashtagsFrequencies.set(index, Integer.valueOf(hashtagsFrequencies.get(index).intValue()+1));
			hashtagsDocuments.set(index, Integer.valueOf(hashtagsDocuments.get(index).intValue()+1));
		}
		else
		{
			hashtags.add(hashtag.replaceAll("\"", ""));
			hashtagsFrequencies.add(new Integer(1));
			hashtagsDocuments.add(new Integer(1));
		}
		*/
		hashtags.add(hashtag.replaceAll("\"", ""));
		hashtagsIDF.put(hashtag, 1);		
	}
	
	public void addMention(String mention)
	{
		/*
		int index=mentions.indexOf(mention);		
		if (index>=0)
		{
			mentionsFrequencies.set(index, Integer.valueOf(mentionsFrequencies.get(index).intValue()+1));
			mentionsDocuments.set(index, Integer.valueOf(mentionsDocuments.get(index).intValue()+1));
		}
		else
		{
			mentions.add(mention);
			mentionsFrequencies.add(new Integer(1));
			mentionsDocuments.add(new Integer(1));
		}
		*/
		mentions.add(mention);
		mentionsIDF.put(mention, 1);
	}
	

	
	public void prepareTopic()
	{
/*
		System.out.println("TOPIC "+id);
		System.out.println("terminos: "+words.size());
		System.out.println("hashtags: "+hashtags.size());
		System.out.println("mentions: "+mentions.size());
*/		
		
		Map<String, Integer> freqWords = new HashMap<>();
		for (String word : words) {
		    Integer count = freqWords.get(word);
		    if(count == null) {
		        count = 0;
		    }
		    freqWords.put(word, (count.intValue()+1));
		}
		
		Map<String, Integer> freqHashtags = new HashMap<>();
		for (String word : hashtags) {
		    Integer count = freqHashtags.get(word);
		    if(count == null) {
		        count = 0;
		    }
		    freqHashtags.put(word, (count.intValue()+1));
		}		

		
		Map<String, Integer> freqMentions = new HashMap<>();
		for (String word : mentions) {
		    Integer count = freqMentions.get(word);
		    if(count == null) {
		        count = 0;
		    }
		    freqMentions.put(word, (count.intValue()+1));
		}		
		
		freqWords = sortByComparator(freqWords);
		freqHashtags = sortByComparator(freqHashtags);
		freqMentions = sortByComparator(freqMentions);
		
	
		words = new ArrayList<String>(freqWords.keySet());
		frequencies = new ArrayList<Integer>(freqWords.values());
		hashtags = new ArrayList<String>(freqHashtags.keySet());
		hashtagsFrequencies = new ArrayList<Integer>(freqHashtags.values());
		mentions = new ArrayList<String>(freqMentions.keySet());
		mentionsFrequencies = new ArrayList<Integer>(freqMentions.values());
		freqWords.clear();
		freqHashtags.clear();
		freqMentions.clear();

		for(int ind=0; ind<words.size(); ind++)
		{
			int freq = frequencies.get(ind).intValue();
			if (freq<MIN_FREQ_TERMS)
			{
				words.remove(ind);
				frequencies.remove(ind);
				ind--;
			}
		}
		for(int ind=0; ind<hashtags.size(); ind++)
		{
			int freq = hashtagsFrequencies.get(ind).intValue();
			if (freq<MIN_FREQ_HASHTAGS)
			{
				hashtags.remove(ind);
				hashtagsFrequencies.remove(ind);
				ind--;
			}
		}
		for(int ind=0; ind<mentions.size(); ind++)
		{
			int freq = mentionsFrequencies.get(ind).intValue();
			if (freq<MIN_FREQ_MENTIONS)
			{
				mentions.remove(ind);
				mentionsFrequencies.remove(ind);
				ind--;
			}
		}

/*		
		System.out.println("-terminos: "+words.size());
		System.out.println("-hashtags: "+hashtags.size());
		System.out.println("-mentions: "+mentions.size());
*/

		if (selectionFeature==TERM_TF)
		{
			selectByFrequency(words, frequencies, percent_terms);
			selectByFrequency(hashtags, hashtagsFrequencies, percent_hashtags);
			selectByFrequency(mentions, mentionsFrequencies, percent_mentions);
		}
		else
		{
			documents = new ArrayList<Integer>();
			for (int ind=0; ind<words.size(); ind++)
			{
				List<Integer> freqs = wordsIDF.get(words.get(ind));
				documents.add(new Integer(freqs.size()));
			}
			hashtagsDocuments = new ArrayList<Integer>();
			for (int ind=0; ind<hashtags.size(); ind++)
			{
				List<Integer> freqs = hashtagsIDF.get(hashtags.get(ind));
				hashtagsDocuments.add(new Integer(freqs.size()));
			}
			mentionsDocuments = new ArrayList<Integer>();
			for (int ind=0; ind<mentions.size(); ind++)
			{
				List<Integer> freqs = mentionsIDF.get(mentions.get(ind));
				mentionsDocuments.add(new Integer(freqs.size()));
			}			
			selectByTFIDF(words, frequencies, documents, tfidfwords, percent_terms);
			selectByTFIDF(hashtags, hashtagsFrequencies, hashtagsDocuments, tfidfhashtags, percent_hashtags);
			selectByTFIDF(mentions, mentionsFrequencies, mentionsDocuments, tfidfmentions, percent_mentions);
		}

/*		
		System.out.println("TOPIC PREPARADO");
		System.out.println("terminos: "+words.size());
		System.out.println("hashtags: "+hashtags.size());
		System.out.println("mentions: "+mentions.size());
*/				
	}
	
	
	private static Map sortByComparator(Map unsortMap) {
		 
		List list = new LinkedList(unsortMap.entrySet());
 
		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
                                       .compareTo(((Map.Entry) (o1)).getValue());
			}
		});
 
		// put sorted list into map again
                //LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}	
	
	private void selectByFrequency(List<String> wordList, List<Integer> frequenciesList, int percent)
	{
		String[][] data = new String[wordList.size()][2];//frequenciesList.size()];
		int tope = wordList.size();
		for(int i=0; i<tope;i++)
		{
			data[i] = new String[] {wordList.get(i).toString(), frequenciesList.get(i).toString()};
		}
		
		
		Arrays.sort(data, new Comparator<String[]>() {
            @Override
            public int compare(final String[] entry1, final String[] entry2) {
                
                final Integer freq1 = Integer.valueOf(entry1[1]);
                final Integer freq2 = Integer.valueOf(entry2[1]);

                	return freq1.compareTo(freq2);
            }
        });	
		
		wordList.clear();		
		frequenciesList.clear();
		//wordList = new ArrayList<String>();
		//frequenciesList = new ArrayList<Integer>();
		int num_terms = Math.min((tope*percent)/100, (maxFeatures*percent)/100);
		for (int i=tope-1; i>tope-num_terms-1; i--)
		//for (int i=tope/2; i<(tope/2)+num_terms; i++)
		{
			wordList.add(new String(data[i][0].toString()));
			frequenciesList.add(new Integer(data[i][1].toString()));
			data[i][0] = null;
			data[i][1] = null;
		}
		data=null;//Para que el recolector lo elimine
		//System.gc();
	}
	
	
	private void selectByTFIDF(List<String> wordList, List<Integer> frequenciesList, List<Integer> documents, List<Double> tfidfList, int percent)
	{
		String[][] data = new String[wordList.size()][2];
		int tope = wordList.size();
		for(int i=0; i<tope;i++)
		{			
			double tfidf = (double)(
					frequenciesList.get(i).intValue() * Math.log(
							(double)((totalDocuments / documents.get(i).intValue()))
							)
							);
			data[i][0] = wordList.get(i).toString();
			data[i][1] = Double.toString(tfidf);
		}
		
		
		Arrays.sort(data, new Comparator<String[]>() {
            @Override
            public int compare(final String[] entry1, final String[] entry2) {
                
                final Double freq1 = Double.valueOf(entry1[1]);
                final Double freq2 = Double.valueOf(entry2[1]);

                	return freq1.compareTo(freq2);
            }
        });	
		wordList.clear();
		tfidfList.clear();
		//wordList = new ArrayList<String>();
		//tfidfList = new ArrayList<Double>();
		int num_terms = Math.min((tope*percent)/100, (maxFeatures*percent)/100);
		for (int i=tope-1; i>tope-num_terms-1; i--)
		{
			wordList.add(new String(data[i][0].toString()));
			tfidfList.add(new Double(data[i][1].toString()));
		}
		data=null;//Para que el recolector lo elimine		
	}
	
	
	
	
	public boolean containsWord(String word)
	{
		return words.contains(word);
	}
	
	public void removeWord(String word)
	{
		int index = words.indexOf(word);
		if (index>=0)
		{
			words.remove(index);
			frequencies.remove(index);
			documents.remove(index);
		}
	}
	public boolean containsHashtag(String hashtag)
	{
		return hashtags.contains(hashtag);
	}
	
	public void removeHashtag(String hashtag)
	{
		int index = hashtags.indexOf(hashtag);
		if (index>=0)
		{
			hashtags.remove(index);
			hashtagsFrequencies.remove(index);
			hashtagsDocuments.remove(index);			
		}
	}
	public boolean containsMention(String mention)
	{
		return mentions.contains(mention);
	}
	
	public void removeMention(String mention)
	{
		int index = mentions.indexOf(mention);
		if (index>=0)
		{
			mentions.remove(index);
			mentionsFrequencies.remove(index);
			mentionsDocuments.remove(index);
		}
	}	
	
	
	public void print()
	{
		System.out.println("#############"+name+":"+id+"###############");
		System.out.println("Terms list");
		int tope = words.size();
		for (int index=0; index<tope; index++)
		{
			System.out.println(words.get(index));
		}
		System.out.println("Mentions");
		tope = mentions.size();
		for (int index=0; index<tope; index++)
		{
			System.out.println(mentions.get(index));
		}
		System.out.println("Hashtags");
		tope = hashtags.size();
		for (int index=0; index<tope; index++)
		{
			System.out.println(hashtags.get(index));
		}
		
	}

/*
 * Modificado el 20-06-2013 para que al crear el vector se tengan en cuenta 
 * los contenidos de las URL.
 * Sé que se ha marcado la opción de tratar contenido URL si el vector tiene elementos.
 */
	public String generateArffVector(ProcessedTweet ptweet)
	{
		String arffVector=new String();
//Primero relleno los valores relativos a los términos		
		int tope=words.size();
/*		
		TFTable wordsList = ptweet.getTfContent();
		for (int index=0; index<tope; index++)
		{
			int freq = 0;
			int indWord = wordsList.getWords().indexOf(words.get(index));
			if (indWord>=0)
			{
				freq += wordsList.getFrequencies().get(indWord).intValue();
//				arffVector += wordsList.getFrequencies().get(indWord).toString()+",";
			}		
*/
		for(int index=0; index<tope; index++)
		{
			if (ptweet.getContent().contains(words.get(index)))
				arffVector += "1,";
			else
				arffVector += "0,";
		}
/*
			else
				arffVector += "0,";
*/
//			arffVector += Integer.toString(freq)+",";
			
//		}
//Ahora los relativos a los hashtags
		tope = hashtags.size();
		for (int index=0; index<tope; index++)
		{
			int indUser = ptweet.getHastags().indexOf(hashtags.get(index));
			if (indUser>=0)
				arffVector += "1,";
			else
				arffVector += "0,";
			
		}		
//Ahora los valores relativos a las menciones
		tope = mentions.size();
		for (int index=0; index<tope; index++)
		{
			int indMention = ptweet.getUsersRef().indexOf(mentions.get(index));
			if (indMention>=0)
				arffVector += "1,";
			else
				arffVector += "0,";
		}
		return arffVector;
	}
	
	
	public String getName() {
		return name;
	}


	public int getId() {
		return id;
	}


	public List<String> getWords() {
		return words;
	}


	public List<String> getHashtags() {
		return hashtags;
	}


	public List<String> getMentions() {
		return mentions;
	}
	
	public List<String> generateTwitterQueries()
	{
		List<String> queries = new ArrayList<String>();
//words
		String query=null;
		for (int index=0; index<words.size(); index++)
		{			
			if ((index%10==0) && (index>0))
			{				
				queries.add(query);
				query = new String("q="+words.get(index));
			}
			else
			{
				if (index==0)
					query = new String("q="+words.get(index));
				else
					query+="%20OR%20"+words.get(index);
			}
			
		}
//mentions
		for (int index=0; index<mentions.size(); index++)
		{			
			if ((index%10==0) && (index>0))
			{				
				queries.add(query);
				query = new String("q="+mentions.get(index));
			}
			else
			{
				if (index==0)
					query = new String("q="+mentions.get(index));
				else
					query+="%20OR%20"+mentions.get(index);
			}
			
		}
//hashtags			
		for (int index=0; index<hashtags.size(); index++)
		{			
			if ((index%10==0) && (index>0))
			{				
				queries.add(query);
				query = new String("q="+hashtags.get(index));
			}
			else
			{
				if (index==0)
					query = new String("q="+hashtags.get(index));
				else
					query+="%20OR%20"+hashtags.get(index);
			}
			
		}		return queries;
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
}//Fin clase
