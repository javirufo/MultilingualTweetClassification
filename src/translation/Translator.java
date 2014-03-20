package translation;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;



import net.htmlparser.jericho.*;


import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;


import utils.URLUtils;



/*
 * 
 * http://translate.google.es/#es/en/hola%2C%20buenos%20d%C3%ADas.%20Como%20estas%3F
 * Esto es la url para traducir.
 * 
 * 
 * Esta es la parte del resultado que contiene la traducción
 * <span id="result_box" class="short_text" lang="en">
<span class="hps">Ramon</span>
<span>'s dog</span>
<span class="hps">has no tail</span>
</span>
 */

public class Translator {
	//http://translate.google.es/translate_a/t?client=t&sl=es&tl=fr&hl=es&sc=2&ie=UTF-8&oe=UTF-8&ssel=0&tsel=0&q=%22hola%22%20que%20tal%22
	//http://translate.google.es/translate_a/t?client=t&sl=es&tl=fr&hl=es&sc=2&ie=UTF-8&oe=UTF-8&ssel=0&tsel=0&q=%22hola%22%20que%20tal%22
	public static final String TRANSLATE_URL = "http://translate.google.es/translate_a/t?client=t&sl=";//"http://translate.google.es/translate_a/t?client=t&sl=";
	public static final String TRANSLATE_URL_1 = "&tl=";
	public static final String TRANSLATE_URL_2 = "&ie=UTF-8&oe=UTF-8&q=";//"&hl=es&sc=2&ie=UTF-8&oe=UTF-8&ssel=0&tsel=0&q=";//hola%20qu%C3%A9%20tal%3F
	public static final String PREFIX_TRANSLATION = "[[[\"";
	public static String translate(String text, String lang, String destLang)
	{
		String translated=URLUtils.getRawURL(TRANSLATE_URL+lang+TRANSLATE_URL_1+destLang+TRANSLATE_URL_2+URLEncoder.encode(text));
		//[[["Comment allez-vous ?","Cómo estás?","",""]],,"
		int pos = translated.indexOf("\",\"");
		try{
			translated = translated.substring(PREFIX_TRANSLATION.length(), pos);			
		}catch(Exception e)
		{
			System.out.println("EXCEPTION with: "+translated);
		}
		return translated;
	}
	
	
	  public static String translate(File file, String sourceLang, String destLang) throws Exception {
//		  http://translate.google.es/
		    //net.htmlparser.jericho.Source doc = new net.htmlparser.jericho.Source("http://translate.google.es/");
	  		//Element form = doc.getElementById("text_form");
		    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		    nvps.add(new BasicNameValuePair("old_sl", sourceLang));
		    nvps.add(new BasicNameValuePair("old_tl", destLang));
		    nvps.add(new BasicNameValuePair("old_submit", "Tłumacz"));
		    nvps.add(new BasicNameValuePair("sl", sourceLang));
		    nvps.add(new BasicNameValuePair("tl", destLang));
		    //addMissingFields(form, nvps);
		    
		    String actionUri = "http://translate.googleusercontent.com/translate_f?";//sl="+sourceLang+"&tl="+destLang;
		    HttpPost post = new HttpPost(actionUri);
		    MultipartEntity multipart = new MultipartEntity();
		    for (NameValuePair nvp : nvps) {
		      multipart.addPart(nvp.getName(), new StringBody(nvp.getValue(), Charset.forName("UTF-8")));
		    }
		    multipart.addPart("file", new FileBody(file));
		    
		    post.setEntity(multipart);
		    HttpClient httpClient = new DefaultHttpClient();
		    HttpResponse response = httpClient.execute(post);		    		    
		    HttpEntity entity = response.getEntity();		    
		    
		    net.htmlparser.jericho.Source page = new net.htmlparser.jericho.Source(entity.getContent());
		    // convert charsets to UTF-8
		    OutputDocument output = new OutputDocument(page);
		    for (Element meta : page.getAllElements("meta")) {
		      String httpEquiv = meta.getAttributeValue("http-equiv");
		      String content = meta.getAttributeValue("content");
		      if (StringUtils.equalsIgnoreCase("content-type", httpEquiv) && content!=null) {
		        String newAttr = meta.toString().replaceAll("charset=[A-Z0-9\\-]*\"", "charset=UTF-8");
		        output.replace(meta, newAttr);
		      }
		    }
		    // now remove all spans with class "google-src-text"
		    for (Element googleSrc : page.getAllElementsByClass("google-src-text")) {
		      output.replace(googleSrc, "");
		    }
		    // clean up scripts
		    for (Element script : page.getAllElements("script")) {
		      output.replace(script, "");
		    }
		    // remove google's iframe
		    for (Element iframe : page.getAllElements("iframe")) {
		      if (StringUtils.containsIgnoreCase(iframe.getAttributeValue("src"), ".google."))
		        output.replace(iframe, "");
		    }
		    page = new Source(output.toString());
		    for (StartTag main : page.getAllStartTagsByClass("main")) {
		      if ("div".equals(main.getName())) {
		        int end = main.getEnd();
		        return page.subSequence(end, page.length()).toString();
		      }
		    }
		    return page.toString();
		  }

		

	  public static  void addMissingFields(Element elem, List<NameValuePair> nvps) {
		  f1: for (Element input : elem.getAllElements(HTMLElementName.INPUT)) 
		  {
			  String name = input.getAttributeValue("name");//   getAttributeValue(“name”);
		      String value = input.getAttributeValue("value");
		      if (name == null || value == null)
		        continue;
		      // check if exist
		      for (int i=0;i<nvps.size();i++) 
		      {
		        if (nvps.get(i).getName().equals(name))
		          continue f1;
		      }
		      nvps.add(new BasicNameValuePair(name, value));
		    }
	  }	

}
