package topics;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class TopicsSerialization {
	public static void toFile(String file, TopicsList topics)
	{
		 try
	      {
			 FileOutputStream fileOut = new FileOutputStream(file);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
        	 out.writeObject(topics);	 
	         out.close();
	         fileOut.close();
	      }catch(Exception e)
	      {
	          e.printStackTrace();
	      }		
	}
	
	public static TopicsList fromFile(String path)
	{
		try
		{
			FileInputStream fileIn = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fileIn);			
			TopicsList topics = (TopicsList) in.readObject();
	        in.close();
			fileIn.close();
			return topics;
		}
		catch(ClassNotFoundException c)
		{
			System.out.println("Employee class not found");
			c.printStackTrace();
			return new TopicsList();
		}		
		catch(IOException e)
		{
			e.printStackTrace();
			return new TopicsList();
		}			
	}
}
