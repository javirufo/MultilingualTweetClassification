package tweets;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import textprocess.Lemmatizer;
import textprocess.Stemmer;
import xml.es.daedalus.tass.tweets.Sentiments;
import xml.es.daedalus.tass.tweets.Topics;
import xml.es.daedalus.tass.tweets.Tweet;

public class ProcessedTweet implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Datos propios del tweet	
	private BigInteger tweetid;
//	private String user;
//	private String date;
	private String lang;
//	private String originalContent;
//	private List<String> originalURLContents;
//	private Sentiments sentiments;
	private Topics topics;
	private List<String>  content;
	private List<String> contentStem;
	private List<String> contentLemma;
	private TFTable tfContent;
	private List<String> hastags;
	private List<String> usersRef;
//Datos propios del usuario
//Campos que almacenan todos los datos para evitar un multiple procesamiento
 
	
	
	private String tipo;
	
	public ProcessedTweet(Tweet tweet){
		tweetid = tweet.getTweetid();
//		user = tweet.getUser();
//		date = tweet.getDate();
		lang = tweet.getLang();
//		originalContent = tweet.getContent();
//		sentiments = tweet.getSentiments();
		topics = tweet.getTopics();
		content = new ArrayList<String>();
		hastags = new ArrayList<String>();
		usersRef = new ArrayList<String>();
//		urlsContent = new ArrayList<String>();
		tfContent = new TFTable();
//		tfUrlsContent = new TFTable();
//		originalURLContents = new ArrayList<String>();
		tipo = new String();		
	}	
		
	
	
	public void calculaTF()
	{
		int tope = content.size();
		for (int index=0; index<tope; index++)
		{
			tfContent.addWord(content.get(index));
		}
	}
	
	
	
	public BigInteger getTweetid() {
		return tweetid;
	}

	public void setTweetid(BigInteger tweetid) {
		this.tweetid = tweetid;
	}
/*
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
*/

	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = content;
	}

	public List<String> getHastags() {
		return hastags;
	}

	public void setHastags(List<String> hastags) {
		this.hastags = hastags;
	}

	public List<String> getUsersRef() {
		return usersRef;
	}

	public void setUsersRef(List<String> usersRef) {
		this.usersRef = usersRef;
	}
/*
	public List<String> getUrlsContent() {
		return urlsContent;
	}

	public void setUrlsContent(List<String> urlsContent) {
		this.urlsContent = urlsContent;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
*/
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
/*
	public Sentiments getSentiments() {
		return sentiments;
	}

	public void setSentiments(Sentiments sentiments) {
		this.sentiments = sentiments;
	}
*/
	public Topics getTopics() {
		return topics;
	}

	public void setTopics(Topics topics) {
		this.topics = topics;
	}
/*
	public BigInteger getIdusuario() {
		return idusuario;
	}

	public void setIdusuario(BigInteger idusuario) {
		this.idusuario = idusuario;
	}
*/
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public TFTable getTfContent() {
		return tfContent;
	}

	public void setTfContent(TFTable tfContent) {
		this.tfContent = tfContent;
	}
	

/*
	public TFTable getTfUrlsContent() {
		return tfUrlsContent;
	}

	public void setTfUrlsContent(TFTable tfUrlsContent) {
		this.tfUrlsContent = tfUrlsContent;
	}



	public List<String> getOriginalURLContents() {
		return originalURLContents;
	}



	public void setOriginalURLContents(List<String> originalURLContents) {
		this.originalURLContents = originalURLContents;
	}
*/
//18/07/2013
//Intento de hacer co-training
	public void addTopic(String topic)
	{
		if (topics==null)
			topics = new Topics();
		this.topics.getTopic().add(topic);
	}



	public void doStemming()
	{
		int tope = content.size();
		for(int ind=0; ind<tope; ind++)
		{
			content = Stemmer.stemming(content, lang);			
		}
		calculaTF();
	}
	
	public void doLemmatization()
	{
		String lemmatizar= "";
		for (String s : content)
		{
			lemmatizar += s;
		}
		content = Lemmatizer.lemmatize(lemmatizar);		
	}



	public List<String> getContentStem() {
		return contentStem;
	}



	public void setContentStem(List<String> contentStem) {
		this.contentStem = contentStem;
	}



	public List<String> getContentLemma() {
		return contentLemma;
	}



	public void setContentLemma(List<String> contentLemma) {
		this.contentLemma = contentLemma;
	}


	public void getStemming(){
		content = contentStem;
	}
	
	public void getLemma(){
		content = contentLemma;
	}
	
}
