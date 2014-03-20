package textprocess;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Lemmatizer {
	
	public static StanfordCoreNLP pipeline=null;        
	public static Properties props=null;
	
	public static List<String> lemmatize(String cadena)
	{
	try{
		   
	        // Create StanfordCoreNLP object properties, with POS tagging
	        // (required for lemmatization), and lemmatization
		if (props==null)
		{
			props = new Properties();		
			props.put("annotators", "tokenize, ssplit, pos, lemma");
			StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		}
		List<String> lemmas = new ArrayList<String>();
//create an empty Annotation just with the given text	        
		Annotation document = new Annotation(cadena);		
//run all Annotators on this text
		pipeline.annotate(document);
//Iterate over all of the sentences found
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for(CoreMap sentence: sentences) 
		{
//Iterate over all tokens in a sentence
			for (CoreLabel token: sentence.get(TokensAnnotation.class)) 
			{
//Retrieve and add the lemma for each word into the
//list of lemmas			            	
				lemmas.add(token.get(LemmaAnnotation.class));
			}
		}
		return lemmas;
		}catch(Exception e)
		{
			return new ArrayList<String>();
		}
	}
}
