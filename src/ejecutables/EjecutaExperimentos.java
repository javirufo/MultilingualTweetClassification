package ejecutables;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import tweets.TweetLoader;
import utils.URLUtils;
import xml.es.daedalus.tass.tweets.Tweet;
import xml.es.daedalus.tass.tweets.Tweets;
import experiments.Experiment;

public class EjecutaExperimentos {

	/**
	 * @param args
	 * args[0] - Fichero de experimentos
	 * args[1] - Número de pasos en línea de aprendizaje
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 2)
		{
			System.out.println("Formato de invocación erróneo. El formato es:");
			System.out.println("java -jar EjecutaExperimentos.jar Fichero_experimentos pasos_curva_aprendizaje");
			return;
		}				
		try
		{
			//URLUtils.loadFromFile(args[3]);
			List<Tweet> trainCorpus=null;
			List<Tweet> testCorpus=null;
			BufferedReader br = new BufferedReader(new FileReader(args[0]));					
			String line;
			while((line=br.readLine())!=null)
			{
				if (line.startsWith("+"))
				{
					String []parameters = line.split(";");
					URLUtils.loadFromFile(parameters[1]);
					trainCorpus = TweetLoader.LoadFromXML(parameters[2], false).getTweet();
					testCorpus = TweetLoader.LoadFromXML(parameters[3], false).getTweet();										
					Experiment exp = new Experiment(trainCorpus, testCorpus, parameters[parameters.length-1]);
					exp.exec(null);						
				}
				else
				{					
					if (!line.startsWith("#"))
					{
						if (line.startsWith("@"))
						{
							line = line.substring(1);
							String [] options = line.split(";");
							FileWriter outFile=null;
							PrintWriter out=null;
							outFile = new FileWriter("CURVA-"+options[9]+".results");			 
							out = new PrintWriter(outFile);
							
							int pasos = Integer.valueOf(args[1]);
							List<Tweet> trainLinea = new ArrayList<Tweet>();
							for(int indPaso=0; indPaso<pasos; indPaso++)
							{
								trainLinea.clear();
								for(int ind=0; ind<trainCorpus.size(); ind++)
								{
									if (ind%pasos < indPaso)
									{
										trainLinea.add(trainCorpus.get(ind));
									}
								}
		//						Aquí construyo el semicorpus para hacer la línea de aprendizaje	
								if (trainLinea.size()>0)
								{
									Experiment exp = new Experiment(line, trainLinea, testCorpus);
									exp.setId("CURVA"+options[9]+"-"+indPaso);
									exp.exec(out);
									exp=null;
								}
							}
							out.flush();
							out.close();
						}
						else
						{
							System.out.println(Runtime.getRuntime().freeMemory());
							Experiment exp = new Experiment(line, trainCorpus, testCorpus);
							exp.exec(null);
							exp=null;
							System.gc();
						}
					}					
				}
			}
			br.close();		
//Ejecuto el baseline			
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
