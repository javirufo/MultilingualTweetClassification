package ejecutables.translation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import translation.Translator;
import tweets.TweetLoader;
import utils.URLUtils;
import xml.es.daedalus.tass.tweets.Tweet;
import xml.es.daedalus.tass.tweets.Tweets;

public class TranslateCorpus {

	public static int WAIT_TIME = 10000;
	
	public static int creatTranslatedFiles(List<Tweet> corpus, String sourceLang, String destLang)
	{
		
		int tweetTope = corpus.size();
		try {
		int pasos = 0;
		FileWriter outFile = new FileWriter("temp"+pasos+".txt");
		PrintWriter out = new PrintWriter(outFile);
		int translated = 0;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");	
//Empaqueto los mensajes
	
		for (int indTweet=0; indTweet<tweetTope; indTweet++)
		{
			Tweet tweet = corpus.get(indTweet);
			if (tweet.getLang().compareTo(sourceLang)==0)
			{
//				tweet.setContent(Translator.translate(tweet.getContent(), args[1], args[2]));
					String content = tweet.getContent().replaceAll("\n", "");
					content = content.replaceAll("\r", "");
					out.println(content);//tweet.getContent());
					translated++;
					if ((translated%150==0)&&(indTweet>1))
					{				
						pasos++;
						out.close();
						outFile.close();
						outFile = new FileWriter("temp"+pasos+".txt");
						out = new PrintWriter(outFile);
					}
			}			
		}
	 	out.close();
	 	outFile.close();
	 	if (pasos>0)
	 		pasos++;
//Los traduzco
		for(int indPasos=0; indPasos<pasos; indPasos++)
		{
			String tr = Translator.translate(new File("temp"+indPasos+".txt"), sourceLang, destLang);
			while(tr.startsWith("<HTML><HEAD><meta" ))
			{
//Si he superado el límite, descando un buen tiempo, pero no paro la ejecución
				System.out.println("Limite de traducciones superado. Pausando..."+dateFormat.format(new Date()));
				Thread.sleep(1000*60*5);
				tr = Translator.translate(new File("temp"+indPasos+".txt"), sourceLang, destLang);
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter("temp"+indPasos+".tra"));			
			writer.write(tr);
			writer.flush();
			writer.close();
			new File("temp"+indPasos+".txt").delete();
			Thread.sleep(WAIT_TIME);
			
		}	
		return pasos;
		}catch(Exception e)
		{
			return 0;
		}		
	}
	
	
	
	public static void jointFiles(String baseFilename, int files, String outputFile)
	{
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
			for (int indFile=0; indFile<files; indFile++)
			{
				BufferedReader fileReader = new BufferedReader(new FileReader(baseFilename+indFile+".tra"));	
				int lineas=0;
				String linea;
				while ((linea=fileReader.readLine())!=null)
				{
					if (lineas==0)
						linea = linea.replaceFirst("<pre>", "");
					else
						linea = linea.replaceFirst("</pre>", "");
					linea = linea.replaceAll("@ ", "@");
					linea = linea.replaceAll("# ", "#");
					linea = linea + "\n";
					if (linea.length()>0)
						writer.write(linea);
					lineas++;
				}
				fileReader.close();
				new File(baseFilename+indFile+".tra").delete();
			}
			writer.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		//BufferedWriter writer = new BufferedWriter(new FileWriter(args[3]+indPasos+".tra"));
	}
	
	
	public static List<Tweet> createTranslatedCorpus(List<Tweet> corpus, String fileTranslated, String originalLanguage, String destLanguage)
	{
		int topeCorpus = corpus.size();
		List<Tweet> translatedCorpus = new ArrayList<Tweet>();
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(fileTranslated));
			for (int indCorpus=0; indCorpus<topeCorpus; indCorpus++)
			{
				Tweet tweet = corpus.get(indCorpus);
				if (tweet.getLang().compareTo(originalLanguage)==0)
				{
					tweet.setContent(fileReader.readLine());
					tweet.setLang(destLanguage);
					translatedCorpus.add(tweet);
				}
			}
			fileReader.close();
			new File(fileTranslated).delete();
			return translatedCorpus;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Tweet>();
		}		
	}
	
	/**
	 * @param args
	 * args[0] - Corpus to translate
	 * args[1] - Original language
	 * args[2] - Destination language
	 * args[3] - File corpus translated
	 * @throws InterruptedException 
	 */
	public static void main(String[] args){
		if (args.length != 4)
		{
			System.out.println("Formato de invocación erróneo. El formato es:");
			System.out.println("java -jar TranslateCorpus.jar Corpus Idioma_origen Idioma_destino Corpus_traducido");
			return;
		}			
		List<Tweet> corpus = TweetLoader.LoadFromXML(args[0], false).getTweet();
		int files = TranslateCorpus.creatTranslatedFiles(corpus, args[1], args[2]);
		TranslateCorpus.jointFiles("temp", files, "joint");
		List<Tweet> corpusTranslated = TranslateCorpus.createTranslatedCorpus(corpus, "joint", args[1], args[2]);
		Tweets corpusT = new Tweets();
		corpusT.getTweet().addAll(corpusTranslated);
		TweetLoader.SaveToXML(args[3], corpusT);		
	}

}
