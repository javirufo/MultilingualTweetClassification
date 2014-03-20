package textprocess;

import java.util.ArrayList;
import java.util.List;

public class NGrams {
	
	public static List<String> obtainNGrams(List<String> content, int sizegrams)
	{
		List<String> ngrams = new ArrayList<String>();
		int tope=content.size()-1;
		for(int index=0; index<tope; index++)
		{
			ngrams.add(content.get(index)+" "+content.get(index+1));
		}
		return ngrams;
	}
	

}
