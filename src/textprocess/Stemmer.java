package textprocess;


import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.frenchStemmer;
import org.tartarus.snowball.ext.spanishStemmer;


import java.util.ArrayList;
import java.util.List;

public class Stemmer {
	public static final String SPANISH = "es";
	public static final String ENGLISH = "en";
	public static final String FRENCH = "fr";
	public static SnowballStemmer stemmer;
	
	public static List<String> stemming(List<String> originalWords, String language)
	{
		List<String> stemmedWords = new ArrayList<String>();		
		try{
			switch(language)
			{
			case "SPANISH":
				stemmer = spanishStemmer.class.newInstance();
				break;
			case "ENGLISH":
				stemmer = englishStemmer.class.newInstance();
				break;
			case "FRENCH":
				stemmer = frenchStemmer.class.newInstance();
				break;
			default:
				stemmer = englishStemmer.class.newInstance();
			}
			stemmer = spanishStemmer.class.newInstance();
			int tope = originalWords.size();
			for (int index=0; index<tope; index++)
			{
				stemmedWords.add(stemWord(originalWords.get(index)));
			}
		}catch(Exception ex)
		{			
		}		
		return stemmedWords;
	}
	
	
	public static String stemWord(String word)
	{		
		stemmer.setCurrent(word);
		stemmer.stem();
		return stemmer.getCurrent();
	}

}
