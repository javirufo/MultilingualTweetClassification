package ejecutables.translation;
import com.google.api.GoogleAPI;
import com.google.api.GoogleAPIException;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class GooglTranslate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		    // Set the HTTP referrer to your website address.
		    GoogleAPI.setHttpReferrer("http://www.javirufo.es");
		    // Set the Google Translate API key
		    // See: http://code.google.com/apis/language/translate/v2/getting_started.html
		    GoogleAPI.setKey("AIzaSyAJ68W0qIDfNIbn5WHEelKy-kmFG25O36o");
		    String translatedText;
			try {
				translatedText = Translate.DEFAULT.execute("Bonjour le monde", Language.FRENCH, Language.ENGLISH);
				System.out.println(translatedText);
			} catch (GoogleAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		  }		
}
