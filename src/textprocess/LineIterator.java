package textprocess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LineIterator {
	/**
	 * @param fileName String represents name of input file
	 * @return ArrayList<String> with lines from <code>-fileName</code>
	 */
	public static ArrayList<String> getLines(String fileName) {
		ArrayList<String> lines = new ArrayList<String>();
	
		try {			
			FileReader fr = new FileReader(fileName);

			BufferedReader br = new BufferedReader(fr);
			String s;
			while ((s = br.readLine()) != null) {
				// System.out.println(s);
				lines.add(s);
			}

			fr.close();
		} catch (IOException e) {
			System.err.println("Caught in LineIterator class while getLines()"
					+ e.getMessage());
			e.printStackTrace();
		}
		return lines;
	}
}
