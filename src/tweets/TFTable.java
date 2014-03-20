package tweets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TFTable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> words;
	private List<Integer> frequencies;
	
	public TFTable()
	{
		words = new ArrayList<String>();
		frequencies = new ArrayList<Integer>();
	}
	
	public void addWord(String word)
	{
		int index = words.indexOf(word);
		if (index>=0)
		{
			frequencies.set(index, Integer.valueOf(frequencies.get(index).intValue()+1));
		}
		else
		{
			words.add(word);
			frequencies.add(Integer.valueOf(1));
		}		
	}
	
	public void addWordFrecuency(String word, Integer frequency)
	{
		int index = words.indexOf(word);
		if (index>=0)
		{
			frequencies.set(index, Integer.valueOf(frequencies.get(index).intValue()+frequency));
		}
		else
		{
			words.add(word);
			frequencies.add(Integer.valueOf(frequency));
		}
	}

	public void print(){
		int tope = words.size();
		for (int index=0; index<tope; index++)
		{
			System.out.println(words.get(index)+":"+frequencies.get(index));
		}
	}
	
	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

	public List<Integer> getFrequencies() {
		return frequencies;
	}

	public void setFrequencies(List<Integer> frequencies) {
		this.frequencies = frequencies;
	}
}
