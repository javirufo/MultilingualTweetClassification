package experiments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import topics.TopicDesc;
import topics.TopicsList;
import topics.TopicsSerialization;
import tweets.ProcessTweets;
import tweets.ProcessedTweet;
import tweets.ProcessedTweetSerialization;
import utils.BaselineClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import xml.es.daedalus.tass.tweets.Tweet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


import edu.stanford.nlp.ling.CoreAnnotations.TopicAnnotation;

public class Experiment {
	public final static int TRAIN_POLYLINGUAL = 0;
	public final static int TRAIN_TRA1 = 1;
	public final static int TRAIN_TRA2 = 2;
	public final static int TRAIN_TES1 = 3;
	public final static int TRAIN_TES2 = 4;
	private boolean stemming;
	private boolean lematization;
	private boolean urls;
	private boolean hashtags;
	private boolean mentions;
	private boolean unigrams;
	private boolean bigrams;
	private boolean tf;
	private boolean tfidf;
	private List<Tweet> traincorpus;
	private List<Tweet> testcorpus;
	private float precision;
	private float recall;
	private float fmeasure;
	private boolean baseline;
	private String id;
	private int maxFeatures;

	public Experiment(String id, boolean stemming, boolean lematization, boolean urls,
			boolean hashtags, boolean mentions, boolean unigrams,
			boolean bigrams, boolean tf, boolean tfidf, int maxFeatures,
			List<Tweet> traincorpus, List<Tweet> testcorpus) {
		baseline = false;
		this.id = id;
		this.stemming = stemming;
		this.lematization = lematization;
		this.urls = urls;
		this.hashtags = hashtags;
		this.mentions = mentions;
		this.unigrams = unigrams;
		this.bigrams = bigrams;
		this.tf = tf;
		this.tfidf = tfidf;
		this.traincorpus = traincorpus;
		this.testcorpus = testcorpus;
		this.precision=0;
		this.recall=0;
		this.fmeasure=0;
		this.maxFeatures = maxFeatures;
	}

	public Experiment(String config, List<Tweet> traincorpus, List<Tweet> testcorpus)
	{
		baseline = false;
		String [] options = config.split(";");
		this.stemming = Boolean.valueOf(options[0]);
		this.lematization = Boolean.valueOf(options[1]);
		this.urls = Boolean.valueOf(options[2]);
		this.hashtags = Boolean.valueOf(options[3]);
		this.mentions = Boolean.valueOf(options[4]);
		this.unigrams = Boolean.valueOf(options[5]);
		this.bigrams = Boolean.valueOf(options[6]);
		this.tf = Boolean.valueOf(options[7]);
		this.tfidf = Boolean.valueOf(options[8]);
		this.id = options[9];
		this.maxFeatures = Integer.valueOf(options[10]);
		this.traincorpus = traincorpus;
		this.testcorpus = testcorpus;
		this.precision=0;
		this.recall=0;
		this.fmeasure=0;
	}
	
	public Experiment(List<Tweet> traincorpus, List<Tweet> testcorpus, String id){		
		this.traincorpus = traincorpus;
		this.testcorpus = testcorpus;
		this.precision=0;
		this.recall=0;
		this.fmeasure=0;
		baseline = true;
		this.id = id;
	}
	
	public boolean isStemming() {
		return stemming;
	}

	public void setStemming(boolean stemming) {
		this.stemming = stemming;
	}

	public boolean isLematization() {
		return lematization;
	}

	public void setLematization(boolean lematization) {
		this.lematization = lematization;
	}

	public boolean isUrls() {
		return urls;
	}

	public void setUrls(boolean urls) {
		this.urls = urls;
	}

	public boolean isHashtags() {
		return hashtags;
	}

	public void setHashtags(boolean hashtags) {
		this.hashtags = hashtags;
	}

	public boolean isMentions() {
		return mentions;
	}

	public void setMentions(boolean mentions) {
		this.mentions = mentions;
	}

	public boolean isUnigrams() {
		return unigrams;
	}

	public void setUnigrams(boolean unigrams) {
		this.unigrams = unigrams;
	}

	public boolean isBigrams() {
		return bigrams;
	}

	public void setBigrams(boolean bigrams) {
		this.bigrams = bigrams;
	}

	public boolean isTf() {
		return tf;
	}

	public void setTf(boolean tf) {
		this.tf = tf;
	}

	public boolean isTfidf() {
		return tfidf;
	}

	public void setTfidf(boolean tfidf) {
		this.tfidf = tfidf;
	}


	public List<Tweet> getTraincorpus() {
		return traincorpus;
	}

	public void setTraincorpus(List<Tweet> traincorpus) {
		this.traincorpus = traincorpus;
	}

	public List<Tweet> getTestcorpus() {
		return testcorpus;
	}

	public void setTestcorpus(List<Tweet> testcorpus) {
		this.testcorpus = testcorpus;
	}

	
	public void exec(PrintWriter printer){
		try{			
			FileWriter outFile=null;
			PrintWriter out=null;
			if (printer==null)
			{
				outFile = new FileWriter(id+".results");			 
				out = new PrintWriter(outFile);
			}
			else
				out = printer;
			
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");	
			ProcessTweets tweetsProcessor=null;
			System.out.println("***************************************");
			System.out.println("***\tEXECUTING TEST\t"+id+"***");
			System.out.println("+++++++++++++++++++++++++++++++++++++++");		
			System.out.println("Train size:"+traincorpus.size());
			System.out.println("Test size:"+testcorpus.size());
			out.println("***************************************");
			out.println("***\tEXECUTING TEST\t***");
			out.println("+++++++++++++++++++++++++++++++++++++++");		
			out.println("Train size:"+traincorpus.size());
			out.println("Test size:"+testcorpus.size());
			String cloneID="";
			boolean clonar=false;
			if (baseline)
			{
				System.out.println("***************************************");
				System.out.println("***\tEXECUTING TEST BASELINE\t***");
				System.out.println("+++++++++++++++++++++++++++++++++++++++");		
				System.out.println("Train size:"+traincorpus.size());
				System.out.println("Test size:"+testcorpus.size());
				out.println("***************************************");
				out.println("***\tEXECUTING TEST\t***");
				out.println("+++++++++++++++++++++++++++++++++++++++");		
				out.println("Train size:"+traincorpus.size());
				out.println("Test size:"+testcorpus.size());

				BaselineClassifier base = new BaselineClassifier(testcorpus, 8);
				precision = base.getPrecision();
				recall = base.getRecall();
				fmeasure = base.getFmeasure();
				System.out.println("+++++++++++++++++++++++++++++++++++++++");
				System.out.printf("Precision: %.3f\n", precision);
				System.out.printf("Recall: %.3f\n", recall);
				System.out.printf("F-measure: %.3f\n", fmeasure);		
				System.out.println("***************************************");
				out.println("+++++++++++++++++++++++++++++++++++++++");
				out.printf("Precision: %.3f\n", precision);
				out.printf("Recall: %.3f\n", recall);
				out.printf("F-measure: %.3f\n", fmeasure);		
				out.println("***************************************");			
				out.flush();
				out.close();
				return;
			}
			else
			{
				System.out.println("Stemming: "+stemming);
				System.out.println("Lematization:"+lematization);
				System.out.println("URLs:"+urls);
				System.out.println("Hashtags:"+hashtags);
				System.out.println("Mentions:"+mentions);
				System.out.println("Unigrams:"+unigrams);
				System.out.println("Bigrams:"+bigrams);
				System.out.println("TF:"+tf);
				System.out.println("TF-IDF:"+tfidf);
				out.println("Stemming: "+stemming);
				out.println("Lematization:"+lematization);
				out.println("URLs:"+urls);
				out.println("Hashtags:"+hashtags);
				out.println("Mentions:"+mentions);
				out.println("Unigrams:"+unigrams);
				out.println("Bigrams:"+bigrams);
				out.println("TF:"+tf);
				out.println("TF-IDF:"+tfidf);				
			}
	//Si tengo los tweets procesados, me evito un nuevo proceso		
			System.out.println("1-Process tweets "+dateFormat.format(new Date()));
			out.println("1-Process tweets "+dateFormat.format(new Date()));			
 			
			List<ProcessedTweet> train=null;
			String[] ids = id.split("-");
			cloneID = ids[0]+"-"+(Integer.valueOf(ids[1])+6);
			if (((Integer.valueOf(ids[1]) / 6) % 2)==0)
				clonar = true;
 			
			if (new File(id+"-train.ptweets").exists())
			{
				train = ProcessedTweetSerialization.fromFile(id+"-train.ptweets");
				tweetsProcessor = new ProcessTweets(stemming, lematization, urls, hashtags, mentions, unigrams, bigrams);
				if (lematization)
				{
					tweetsProcessor.doLematization(train);
				}
				if (stemming)
				{
					tweetsProcessor.doStemming(train);
				}
			}
			else	
			{
				tweetsProcessor = new ProcessTweets(stemming, lematization, urls, hashtags, mentions, unigrams, bigrams);
//Esto del set training es un añadido para poder diferenciar los idiomas de las url en el corpus paralelo				
//				tweetsProcessor.setTraining(true);
				train = tweetsProcessor.processTweets(traincorpus);
//				tweetsProcessor.setTraining(false);
				ProcessedTweetSerialization.toFile(id+"-train.ptweets", train);
/*				
				if (clonar)
				{
					File f = new File (id+"-train.ptweets");
					Path p = f.toPath();			
					CopyOption[] options = new CopyOption[]{
						      StandardCopyOption.REPLACE_EXISTING,
						      StandardCopyOption.COPY_ATTRIBUTES
						     }; 					
					Files.copy(p, new File (cloneID+"-train.ptweets").toPath(), options);
					Files.copy(p, new File (ids[0]+"-"+(Integer.valueOf(ids[1])+12)+"-train.ptweets").toPath(), options);
					Files.copy(p, new File (ids[0]+"-"+(Integer.valueOf(ids[1])+18)+"-train.ptweets").toPath(), options);
					Files.copy(p, new File (ids[0]+"-"+(Integer.valueOf(ids[1])+24)+"-train.ptweets").toPath(), options);
					Files.copy(p, new File (ids[0]+"-"+(Integer.valueOf(ids[1])+30)+"-train.ptweets").toPath(), options);
				}
*/				
			}

	//Generamos las BOW. Igual que antes, si existen no las creo.
			System.out.println("2-Fill topics "+dateFormat.format(new Date()));		
			out.println("2-Fill topics "+dateFormat.format(new Date()));
			TopicsList topics = null;
			if (new File(id+".topics").exists())
			{
				topics = TopicsSerialization.fromFile(id+".topics");
				if (tf)
					topics.setSelectionFeature(TopicDesc.TERM_TF);
				else
					topics.setSelectionFeature(TopicDesc.TERM_TF_IDF);
				topics.prepareTopics();
			}
			else
			{

				topics = new TopicsList();
				if (tf)
					topics.setSelectionFeature(TopicDesc.TERM_TF);
				else
					topics.setSelectionFeature(TopicDesc.TERM_TF_IDF);
				System.out.println("Filling topics "+dateFormat.format(new Date()));
				topics.fillTopics(train);
				System.out.println("Preparing topics topics "+dateFormat.format(new Date()));
//Aquí tengo que serializar antes de preparar, porque si no no puedo calcular los tf y tfidf
				System.out.println("Serializing topics topics "+dateFormat.format(new Date()));
/*				
				if (clonar)					
				{
					TopicsSerialization.toFile(cloneID+".topics", topics);
				}
*/				
				topics.prepareTopics();				
				TopicsSerialization.toFile(id+".topics", topics);
			
			}
			System.out.println("3-Generate arff train file "+dateFormat.format(new Date()));
			out.println("3-Generate arff train file "+dateFormat.format(new Date()));

	//Si el fichero arff no existe, lo creo. en caso contrario vengo haciendo lo que hasta ahora,
	//aprovechar trabajo previo
			if (!new File(id+"-train.arff").exists())
			{
				
				BufferedWriter bw = topics.generateArffHeader(id+"-train.arff");
				int tope = traincorpus.size();
				if (tweetsProcessor==null)
					tweetsProcessor = new ProcessTweets(stemming, lematization, urls, hashtags, mentions, unigrams, bigrams);
				for (int indTweet=0; indTweet<tope; indTweet++)
				{					
					topics.generateArffVector(bw, train.get(indTweet));
				}	
				bw.flush();
				bw.close();
			}

//Ahora proceso los datos de test
			System.out.println("5-build test dataset "+dateFormat.format(new Date()));			
			out.println("5-build test dataset "+dateFormat.format(new Date()));

			List<ProcessedTweet> test=null;
			if (new File(id+"-test.ptweets").exists())
				test = ProcessedTweetSerialization.fromFile(id+"-test.ptweets");
			else	
			{
				if (tweetsProcessor==null)
					tweetsProcessor = new ProcessTweets(stemming, lematization, urls, hashtags, mentions, unigrams, bigrams);
				test = tweetsProcessor.processTweets(testcorpus);
				ProcessedTweetSerialization.toFile(id+"-test.ptweets", test);
/*				
				if (clonar)
				{
					File f = new File (id+"-test.ptweets");
					Path p = f.toPath();			
					CopyOption[] options = new CopyOption[]{
						      StandardCopyOption.REPLACE_EXISTING,
						      StandardCopyOption.COPY_ATTRIBUTES
						     }; 					
					Files.copy(p, new File (cloneID+"-test.ptweets").toPath(), options);
				}
*/				
				
			}

			//Si el fichero arff no existe, lo creo. en caso contrario vengo haciendo lo que hasta ahora,
			//aprovechar trabajo previo
			if (!new File(id+"-test.arff").exists())
			{
				BufferedWriter bw = topics.generateArffHeader(id+"-test.arff");
				int tope = testcorpus.size();
				if (tweetsProcessor==null)
					tweetsProcessor = new ProcessTweets(stemming, lematization, urls, hashtags, mentions, unigrams, bigrams);
				for (int indTweet=0; indTweet<tope; indTweet++)
				{
					topics.generateArffVector(bw, test.get(indTweet));
				}	
				bw.flush();
				bw.close();				
			}	
			int topeTopics = topics.getTopicsList().size();		
			topics.getTopicsList().clear();
			//Genero el clasificador
			//FJRM 25-08-2013 Lo cambio de orden para intentar liberar la memoria de los topics y tener más libre
			System.out.println("4-Generate classifier "+dateFormat.format(new Date()));
			out.println("4-Generate classifier "+dateFormat.format(new Date()));
			
			Classifier cls=null;
			DataSource sourceTrain = null;
			Instances dataTrain = null; 
			if (new File(id+"-MNB.classifier").exists())
			{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(id+"-MNB.classifier"));
				cls = (Classifier) ois.readObject();
				ois.close();
			}
			else
			{
				sourceTrain = new DataSource(id+"-train.arff");
				dataTrain = sourceTrain.getDataSet();
				if (dataTrain.classIndex() == -1)
					dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
	//Entreno el clasificador
				cls = new weka.classifiers.bayes.NaiveBayesMultinomial();
				int clase = dataTrain.numAttributes()-1;				
				dataTrain.setClassIndex(clase);				
				cls.buildClassifier(dataTrain);
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(id+"-MNB.classifier"));
				oos.writeObject(cls);
				oos.flush();
				oos.close();
				//data.delete();//no borro para el svm
			}
//Ahora evaluo el clasificador con los datos de test
			System.out.println("6-Evaluate classifier MNB "+dateFormat.format(new Date()));			
			out.println("6-Evaluate classifier MNB"+dateFormat.format(new Date()));
			DataSource sourceTest = new DataSource(id+"-test.arff");
			Instances dataTest = sourceTest.getDataSet();
			int clase = dataTest.numAttributes()-1;				
			dataTest.setClassIndex(clase);
			Evaluation eval = new Evaluation(dataTest);				
			eval.evaluateModel(cls, dataTest);
//Ahora calculo los valores precision, recall y fmeasure. Además saco las matrices de confusion

			precision=0;
			recall=0;
			fmeasure=0;
			for(int ind=0; ind<topeTopics; ind++)
			{
				precision += eval.precision(ind);
				recall += eval.recall(ind);
				fmeasure += eval.fMeasure(ind);
			}
			precision = precision / topeTopics;
			recall = recall / topeTopics;
			fmeasure = fmeasure / topeTopics;			
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.println(eval.toMatrixString());
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.printf("Precision: %.3f\n", precision);
			System.out.printf("Recall: %.3f\n", recall);
			System.out.printf("F-measure: %.3f\n", fmeasure);		
			System.out.println("***************************************");
			out.println("+++++++++++++++++++++++++++++++++++++++");
			out.println(eval.toMatrixString());
			out.println("+++++++++++++++++++++++++++++++++++++++");
			out.printf("Precision: %.3f\n", precision);
			out.printf("Recall: %.3f\n", recall);
			out.printf("F-measure: %.3f\n", fmeasure);		
			out.println("***************************************");		
/*			NO BORRAR
			System.out.println("7-Evaluate classifier SVM"+dateFormat.format(new Date()));			
			out.println("7-Evaluate classifier SVM"+dateFormat.format(new Date()));
			if (new File(id+"-SVM.classifier").exists())
			{
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(id+"-SVM.classifier"));
				cls = (Classifier) ois.readObject();
				ois.close();
			}
			else
			{
				if (dataTrain==null)
				{
					sourceTrain = new DataSource(id+"-train.arff");
					dataTrain = sourceTrain.getDataSet();
					if (dataTrain.classIndex() == -1)
						dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
				}
	//Entreno el clasificador
				cls = new weka.classifiers.functions.LibSVM();
				clase = dataTrain.numAttributes()-1;				
				dataTrain.setClassIndex(clase);				
				cls.buildClassifier(dataTrain);
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(id+"-SVM.classifier"));
				oos.writeObject(cls);
				oos.flush();
				oos.close();
				dataTrain.delete();
			}						
			eval.evaluateModel(cls, dataTest);
			precision=0;
			recall=0;
			fmeasure=0;
			for(int ind=0; ind<topeTopics; ind++)
			{
				precision += eval.precision(ind);
				recall += eval.recall(ind);
				fmeasure += eval.fMeasure(ind);
			}
			precision = precision / topeTopics;
			recall = recall / topeTopics;
			fmeasure = fmeasure / topeTopics;			
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.println(eval.toMatrixString());
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.printf("Precision: %.3f\n", precision);
			System.out.printf("Recall: %.3f\n", recall);
			System.out.printf("F-measure: %.3f\n", fmeasure);		
			System.out.println("***************************************");
			out.println("+++++++++++++++++++++++++++++++++++++++");
			out.println(eval.toMatrixString());
			out.println("+++++++++++++++++++++++++++++++++++++++");
			out.printf("Precision: %.3f\n", precision);
			out.printf("Recall: %.3f\n", recall);
			out.printf("F-measure: %.3f\n", fmeasure);		
			out.println("***************************************");		
*/
			System.out.println("Done "+dateFormat.format(new Date()));
			out.println("Done "+dateFormat.format(new Date()));
			if (printer==null)
			{
				out.flush();
				out.close();
			}
//Intento de liberar memoria			
			if (dataTrain!=null)
				dataTrain.delete();
			if (dataTest!=null)
				dataTest.delete();			
			if (train!=null)
				train.clear();
			if (test!=null)
				test.clear();
			if (topics!=null)
			{
				topics.getTopicsList().clear();
				topics = null;
			}
			if (dataTest!=null)
				dataTest.delete();
			if (cls!=null)
				cls = null;
			if (tweetsProcessor!=null)
				tweetsProcessor=null;
			System.gc();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	
	public static void execSVM(String expName)
	{
		try {
			FileWriter outFile=null;
			PrintWriter out=null;
			outFile = new FileWriter(expName+"-SVM.results");			 
			out = new PrintWriter(outFile);				
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");	
			ProcessTweets tweetsProcessor=null;
			System.out.println("***************************************");
			System.out.println("***\tEXECUTING TEST\t"+expName+"***");
			System.out.println("+++++++++++++++++++++++++++++++++++++++");		
			out.println("***************************************");
			out.println("***\tEXECUTING TEST\t"+expName+"***");
			out.println("+++++++++++++++++++++++++++++++++++++++");	
			out.println("4-Generate classifier "+dateFormat.format(new Date()));
			
			Classifier cls=null;
			DataSource sourceTrain = new DataSource(expName+"-train.arff");
			Instances dataTrain = sourceTrain.getDataSet(); 
			if (dataTrain.classIndex() == -1)
				dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
//Entreno el clasificador
			//cls = new weka.classifiers.functions.LibSVM();
			int clase = dataTrain.numAttributes()-1;
			cls = new weka.classifiers.bayes.ComplementNaiveBayes();							
			dataTrain.setClassIndex(clase);				
			cls.buildClassifier(dataTrain);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(expName+"-SVM.classifier"));
			oos.writeObject(cls);
			oos.flush();
			oos.close();
			DataSource sourceTest = new DataSource(expName+"-test.arff");
			Instances dataTest = sourceTest.getDataSet();
			dataTest.setClassIndex(clase);
			Evaluation eval = new Evaluation(dataTest);				
			eval.evaluateModel(cls, dataTest);
//Ahora calculo los valores precision, recall y fmeasure. Además saco las matrices de confusion
					
			float precision=0;
			float recall=0;
			float fmeasure=0;
			int topeTopics = 8;
			for(int ind=0; ind<topeTopics; ind++)
			{
				precision += eval.precision(ind);
				recall += eval.recall(ind);
				fmeasure += eval.fMeasure(ind);
			}
			precision = precision / topeTopics;
			recall = recall / topeTopics;
			fmeasure = fmeasure / topeTopics;			
			System.out.println("++++++++++++++ CNB ++++++++++++++++++++");
			System.out.println(eval.toMatrixString());
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.printf("Precision: %.3f\n", precision);
			System.out.printf("Recall: %.3f\n", recall);
			System.out.printf("F-measure: %.3f\n", fmeasure);		
			System.out.println("***************************************");
			out.println("++++++++++++++ CNB ++++++++++++++++++++");
			out.println(eval.toMatrixString());
			out.println("+++++++++++++++++++++++++++++++++++++++");
			out.printf("Precision: %.3f\n", precision);
			out.printf("Recall: %.3f\n", recall);
			out.printf("F-measure: %.3f\n", fmeasure);		
			out.println("***************************************");	
//OTRO CLASIFICADOR ZeroR
			cls = new weka.classifiers.rules.ZeroR();							
			dataTrain.setClassIndex(clase);				
			cls.buildClassifier(dataTrain);
			eval = new Evaluation(dataTest);				
			eval.evaluateModel(cls, dataTest);
			precision=0;
			recall=0;
			fmeasure=0;
			for(int ind=0; ind<topeTopics; ind++)
			{
				precision += eval.precision(ind);
				recall += eval.recall(ind);
				fmeasure += eval.fMeasure(ind);

			}
			precision = precision / topeTopics;
			recall = recall / topeTopics;
			fmeasure = fmeasure / topeTopics;			
			System.out.println("++++++++++++++ ZEROR ++++++++++++++++++++");
			System.out.println(eval.toMatrixString());
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.printf("Precision: %.3f\n", precision);
			System.out.printf("Recall: %.3f\n", recall);
			System.out.printf("F-measure: %.3f\n", fmeasure);		
			System.out.println("***************************************");
			out.println("++++++++++++++ ZEROR ++++++++++++++++++++");
			out.println(eval.toMatrixString());
			out.println("+++++++++++++++++++++++++++++++++++++++");
			out.printf("Precision: %.3f\n", precision);
			out.printf("Recall: %.3f\n", recall);
			out.printf("F-measure: %.3f\n", fmeasure);		
			out.println("***************************************");				
//OTRO CLASIFICADOR J48
/*			
			cls = new weka.classifiers.trees.J48();							
			dataTrain.setClassIndex(clase);				
			cls.buildClassifier(dataTrain);
			eval = new Evaluation(dataTest);				
			eval.evaluateModel(cls, dataTest);
			precision=0;
			recall=0;
			fmeasure=0;
			for(int ind=0; ind<topeTopics; ind++)
			{
				precision += eval.precision(ind);
				recall += eval.recall(ind);
				fmeasure += eval.fMeasure(ind);
			}
			precision = precision / topeTopics;
			recall = recall / topeTopics;
			fmeasure = fmeasure / topeTopics;			
			System.out.println("++++++++++++++ J48 ++++++++++++++++++++");
			System.out.println(eval.toMatrixString());
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.printf("Precision: %.3f\n", precision);
			System.out.printf("Recall: %.3f\n", recall);
			System.out.printf("F-measure: %.3f\n", fmeasure);		
			System.out.println("***************************************");
			out.println("++++++++++++++ J48 ++++++++++++++++++++");
			out.println(eval.toMatrixString());
			out.println("+++++++++++++++++++++++++++++++++++++++");
			out.printf("Precision: %.3f\n", precision);
			out.printf("Recall: %.3f\n", recall);
			out.printf("F-measure: %.3f\n", fmeasure);		
			out.println("***************************************");
			
//OTRO SMO			
			cls = new weka.classifiers.functions.SMO();							
			dataTrain.setClassIndex(clase);				
			cls.buildClassifier(dataTrain);
			eval = new Evaluation(dataTest);				
			eval.evaluateModel(cls, dataTest);
			precision=0;
			recall=0;
			fmeasure=0;
			for(int ind=0; ind<topeTopics; ind++)
			{
				precision += eval.precision(ind);
				recall += eval.recall(ind);
				fmeasure += eval.fMeasure(ind);
			}
			precision = precision / topeTopics;
			recall = recall / topeTopics;
			fmeasure = fmeasure / topeTopics;			
			System.out.println("++++++++++++++ SMO ++++++++++++++++++++");
			System.out.println(eval.toMatrixString());
			System.out.println("+++++++++++++++++++++++++++++++++++++++");
			System.out.printf("Precision: %.3f\n", precision);
			System.out.printf("Recall: %.3f\n", recall);
			System.out.printf("F-measure: %.3f\n", fmeasure);		
			System.out.println("***************************************");
			out.println("++++++++++++++ SMO ++++++++++++++++++++");
			out.println(eval.toMatrixString());
			out.println("+++++++++++++++++++++++++++++++++++++++");
			out.printf("Precision: %.3f\n", precision);
			out.printf("Recall: %.3f\n", recall);
			out.printf("F-measure: %.3f\n", fmeasure);		
			out.println("***************************************");	
*/
			out.flush();
			out.close();
			dataTest.delete();
			dataTrain.delete();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	public boolean isBaseline() {
		return baseline;
	}

	public void setBaseline(boolean baseline) {
		this.baseline = baseline;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
