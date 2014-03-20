package ejecutables.translation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import textprocess.Tokenizer;
import translation.Translator;
import tweets.TweetLoader;
import utils.URLUtils;
import xml.es.daedalus.tass.tweets.Tweet;
import xml.es.daedalus.tass.tweets.Tweets;

public class TranslateURLs {

	
	
	public static int creatTranslatedFiles(List<String> urlList, String sourceLang, String destLang)
	{
		
		int urlTope = urlList.size();
		try {
		int pasos = 0;
		FileWriter outFile = new FileWriter("tempurl"+pasos+".txt");
		PrintWriter out = new PrintWriter(outFile);
		int translated = 0;
//Empaqueto los mensajes
	
		for (int indTweet=0; indTweet<urlTope; indTweet++)
		{
			String url = urlList.get(indTweet);
			String content = url.replaceAll("\n", "");
			content = content.replaceAll("\r", "");
			out.println(content);//tweet.getContent());
			translated++;
			if ((translated%150==0)&&(indTweet>1))
			{				
				pasos++;
				out.close();
				outFile.close();
				outFile = new FileWriter("tempurl"+pasos+".txt");
				out = new PrintWriter(outFile);
			}
		}					
	 	out.close();
	 	outFile.close();
	 	if (pasos>0)
	 		pasos++;
//Los traduzco
		for(int indPasos=0; indPasos<pasos; indPasos++)
		{
			String tr = Translator.translate(new File("tempurl"+indPasos+".txt"), sourceLang, destLang);
			while(tr.startsWith("<HTML><HEAD><meta" ))
			{
//Si he superado el límite, descando un buen tiempo, pero no paro la ejecución
				System.out.println("Limite de traducciones superado. Pausando...");
				Thread.sleep(1000*60*5);
				tr = Translator.translate(new File("tempurl"+indPasos+".txt"), sourceLang, destLang);
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter("tempurl"+indPasos+".tra"));			
			writer.write(tr);
			writer.flush();
			writer.close();
			new File("tempurl"+indPasos+".txt").delete();
			Thread.sleep(10000);
			
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
	
	
	public static List<String> createTranslatedCorpus(String fileTranslated)
	{
		List<String> urls = new ArrayList<String>();
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(fileTranslated));
			String linea;
			while ((linea=fileReader.readLine())!=null)
			{
				urls.add(linea);
			}
			fileReader.close();
			new File(fileTranslated).delete();
			return urls;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}		
	}
	
	/**
	 * @param args
	 * 
	 * args[0] - URLsFile
	 * args[1] - Source language
	 * args[2] - Destination language
	 * args[3] - File urls translated
	 * args[4...] - Corpus with tweets
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) {	
		if (args.length <5)
		{
			System.out.println("Formato de invocación erróneo. El formato es:");
			System.out.println("java -jar TranslateURLs.jar Fichero_urls Idioma_origen Idioma_destino Fichero_urls_traducidas Resto_ficheros_tweets");
			return;
		}				
		List<String> transURL = new ArrayList<String>();
		List<String> transURLContent = new ArrayList<String>();
		Tweets tweets=new Tweets();
		for (int i=4; i<args.length; i++)
		{
			 tweets.getTweet().addAll(TweetLoader.LoadFromXML(args[i], false).getTweet());
		}
		URLUtils.loadFromFile(args[0]);
		int topTweets = tweets.getTweet().size();
//Genero el paquete de URLs susceptibles de ser traducidas		
		for(int indTweet=0; indTweet<topTweets; indTweet++)
		{
			Tweet tweet = tweets.getTweet().get(indTweet);
			//if (tweet.getLang().compareTo(args[1])==0)
			{
				List<String> urls = Tokenizer.getURLs(tweet.getContent(), "http://", Tokenizer.REG_URLS);
				int topURL = urls.size();
				for (int indURL=0; indURL<topURL; indURL++)
				{
					transURL.add(urls.get(indURL));
					transURLContent.add(URLUtils.getURLContent(urls.get(indURL)));
				}				
			}
		}
//estas 3 lineas borrar		
		URLUtils.setContents(transURLContent);
		URLUtils.setUrls(transURL);
		URLUtils.saveToFile(args[3]);
		/*
//Me he quedado sólo con las URL del idioma que deseo traducir. Ahora troceo y preparo para traducir		
		int files = TranslateURLs.creatTranslatedFiles(transURLContent, args[1], args[2]);
		TranslateURLs.jointFiles("tempurl", files, "urljoint");
		transURLContent = TranslateURLs.createTranslatedCorpus("urljoint");
		URLUtils.setContents(transURLContent);
		int topURL = transURL.size();
		for (int indURL=0; indURL<topURL; indURL++)
		{		
			transURL.set(indURL, args[2]+"_"+transURL.get(indURL));
		}
		URLUtils.setUrls(transURL);
		URLUtils.saveToFile(args[3]);
		*/
	}
}
