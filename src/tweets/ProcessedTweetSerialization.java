package tweets;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

//Esta clase me permite almacenar los tweets procesados, ya que conlleva mucho tiempo el descargar contenidos urls y demás.
public class ProcessedTweetSerialization{
	
	public static void toFile(String file, List<ProcessedTweet> pTweets)
	{
		 try
	      {			 
			 FileOutputStream fileOut = new FileOutputStream(file);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);

	         int tope = pTweets.size();
	         for(int index=0; index<tope; index++)
	         {
	        	 out.writeObject(pTweets.get(index));	 
	         }
	         out.close();
	         fileOut.close();
	      }catch(Exception e)
	      {
	          e.printStackTrace();
	      }		
	}
	
	public static List<ProcessedTweet> fromFile(String file)
	{
		List<ProcessedTweet> pTweets = new ArrayList<ProcessedTweet>();
		try
		{
			int leidos=0;
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			boolean leyendo=true;
			while(leyendo)
			{			
				try{
					ProcessedTweet pTweet = (ProcessedTweet) in.readObject();
					if (pTweet!=null)
						pTweets.add(pTweet);
					leidos++;
				}catch(EOFException e)
				{
					leyendo = false;
				}		        
			}
	        in.close();
			fileIn.close();			
		}
		catch(ClassNotFoundException c)
		{
			System.out.println("Employee class not found");
			c.printStackTrace();
			return pTweets;
		}		
		catch(IOException e)
		{
			e.printStackTrace();
			return pTweets;
		}	
		return pTweets;
	}
	
	
}
