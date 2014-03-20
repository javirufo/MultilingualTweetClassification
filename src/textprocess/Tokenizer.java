package textprocess;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
	
	public final static String REG_HASH_USERS = " ,”“().:;\n\r?\"'";
	public final static String REG_URLS = " ,”“();\n\r\"'<…";
	
	
	static final String PUNTUACION="[^a-zA-Zá-úÁ-Ú]";
	static final int TWITTER_URL_LENGTH = 13;
	static final int MIN_LENGTH=2;
	static final int REPEATED_CHAR_WINDOW = 2;
	static public boolean REMOVE_REPEATED_CHARS = true;
	static public boolean REMOVE_EMOTICONS = true;

	
	
	public static String removeEmoticos(String word)
	{
		return word;
	}
	
	public static String removeRepeatedChars(String word)
	{
		StringBuilder returnWord = new StringBuilder();
		char []charWord = word.toCharArray();
		int index=0;
		for (index=0; index<REPEATED_CHAR_WINDOW; index++)
		{
			returnWord.append(charWord[index]);
		}
		for(index=REPEATED_CHAR_WINDOW; index<charWord.length; index++)
		{
			boolean repeated=true;
			for (int indVentana=REPEATED_CHAR_WINDOW; indVentana>0; indVentana--)
			{
				if (charWord[index]!=charWord[index-indVentana])
				{
					repeated=false;
					break;
				}
			}
			if (!repeated)
				returnWord.append(charWord[index]);
		}
		return returnWord.toString();
	}
	
	
	public static List<String> tokenize (String text){
		ArrayList<String> result = new ArrayList<String>();
		String[] palabras = text.split(PUNTUACION);	    		 
		for (int i = 0; i < palabras.length; i++) 
		{
			String palabra = palabras[i].toLowerCase();	    				
			palabra.replaceAll("/[^A-Za-z0-9]/", "");
			if (palabra.length()>=MIN_LENGTH)
			{
				if (REMOVE_EMOTICONS)
					palabra = removeEmoticos(palabra);
				if (REMOVE_REPEATED_CHARS)
					result.add(removeRepeatedChars(palabra));
				else
					result.add(palabra);
				
			}
	    }
		return result;
	}
	
	
	/*
	 * Este método me permite extraer aquellos elementos que desee. Mediante el start elijo cómo empieza
	 * 'http', '#', '@', y luego le digo como termina, con un espacio, punto, coma, etc.
	 */
	public static List<String> getElement(String text, String startElement, String endElement)
	{
		boolean extrayendo = true;
		int index=0;
		List<String> elements = new ArrayList<String>();
		while(extrayendo)
		{
			index = text.indexOf(startElement, index);			
			if (index>=0)
			{
				int finalIndex = Integer.MAX_VALUE;
				for(int ind=0; ind<endElement.length(); ind++)
				{
					int auxfinalIndex = text.indexOf(endElement.charAt(ind), index);
					if ((auxfinalIndex<finalIndex) && (auxfinalIndex>0))
						finalIndex = auxfinalIndex;
				}
				if (finalIndex==Integer.MAX_VALUE)
					finalIndex = text.length();				
				String elemento = text.substring(index, finalIndex);
				elements.add(elemento);
				index++;
			}
			else
				extrayendo=false;
			
		}
		return elements;
	}
	
	public static List<String> getURLs(String text, String startElement, String endElement)
	{
		boolean extrayendo = true;
		int index=0;
		List<String> elements = new ArrayList<String>();
		while(extrayendo)
		{
			index = text.indexOf(startElement, index);			
			if (index>=0)
			{			
				int finalIndex = Math.min(text.length(), index+startElement.length()+TWITTER_URL_LENGTH);text.length();
				//Lo de arriba hay que cambiarlo por =text.length()				
				for(int ind=0; ind<endElement.length(); ind++)
				{
					int auxfinalIndex = text.indexOf(endElement.charAt(ind), index);
					if ((auxfinalIndex<finalIndex) && (auxfinalIndex>0))
						finalIndex = auxfinalIndex;
				}								
				String elemento = text.substring(index, finalIndex);
				elements.add(elemento);
				index++;
			}
			else
				extrayendo=false;
			
		}
		return elements;
	}
	
}
