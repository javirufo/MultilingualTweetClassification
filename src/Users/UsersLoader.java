package Users;

import java.io.File;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import xml.es.daedalus.tass.usuarios.*;

public class UsersLoader {

	/**
	 * @param args
	 */
		public static Users LoadFromXML(String filePath)
		{
			try{
			System.out.println("Loading from XML:");
			File xmlFile = new File(filePath);
			JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Users users = (Users) jaxbUnmarshaller.unmarshal(xmlFile);

			System.out.println("XML Loaded");
			return users;
			}catch(Exception ex){
				ex.printStackTrace();
				return new Users();
			}
		}	

}
