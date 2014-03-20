package textprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


//Lleva a cabo la tokenizaci�n del tweet y elimina las stopwords
public class StopWords {
		
	private final String LANG_ES = "es";
	private final String LANG_EN = "en";
	private final String LANG_FR = "fr";
	private final int MIN_LENGTH = 4;
	private final ArrayList<String> STOP_WORDS_EN = new ArrayList<String>(Arrays.asList( 
			"quot", "what", "for", "http", "de", "rt", "del", "que", "se", "no", "al","a","able","about","above","abst","accordance","according","accordingly","across","act","actually","added","adj","affected","affecting","affects","after","afterwards","again","against","ah","all","almost","alone","along","already","also","although","always","am","among","amongst","an","and","announce","another","any","anybody","anyhow","anymore","anyone","anything","anyway","anyways","anywhere","apparently","approximately","are","aren","arent","arise","around","as","aside","ask","asking","at","auth","available","away","awfully","b","back","be","became","because","become","becomes","becoming","been","before","beforehand","begin","beginning","beginnings","begins","behind","being","believe","below","beside","besides","between","beyond","biol","both","brief","briefly","but","by","c","ca","came","can","cannot","can't","cause","causes","certain","certainly","co","com","come","comes","contain","containing","contains","could","couldnt","d","date","did","didn't","different","do","does","doesn't","doing","done","don't","down","downwards","due","during","e","each","ed","edu","effect","eg","eight","eighty","either","else","elsewhere","end","ending","enough","especially","et","et-al","etc","even","ever","every","everybody","everyone","everything","everywhere","ex","except","f","far","few","ff","fifth","first","five","fix","followed","following","follows","for","former","formerly","forth","found","four","from","further","furthermore","g","gave","get","gets","getting","give","given","gives","giving","go","goes","gone","got","gotten","h","had","happens","hardly","has","hasn't","have","haven't","having","he","hed","hence","her","here","hereafter","hereby","herein","heres","hereupon","hers","herself","hes","hi","hid","him","himself","his","hither","home","how","howbeit","however","hundred","i","id","ie","if","i'll","im","immediate","immediately","importance","important","in","inc","indeed","index","information","instead","into","invention","inward","is","isn't","it","itd","it'll","its","itself","i've","j","just","k","keep","keeps","kept","kg","km","know","known","knows","l","largely","last","lately","later","latter","latterly","least","less","lest","let","lets","like","liked","likely","line","little","'ll","look","looking","looks","ltd","m","made","mainly","make","makes","many","may","maybe","me","mean","means","meantime","meanwhile","merely","mg","might","million","miss","ml","more","moreover","most","mostly","mr","mrs","much","mug","must","my","myself","n","na","name","namely","nay","nd","near","nearly","necessarily","necessary","need","needs","neither","never","nevertheless","new","next","nine","ninety","no","nobody","non","none","nonetheless","noone","nor","normally","nos","not","noted","nothing","now","nowhere","o","obtain","obtained","obviously","of","off","often","oh","ok","okay","old","omitted","on","once","one","ones","only","onto","or","ord","other","others","otherwise","ought","our","ours","ourselves","out","outside","over","overall","owing","own","p","page","pages","part","particular","particularly","past","per","perhaps","placed","please","plus","poorly","possible","possibly","potentially","pp","predominantly","present","previously","primarily","probably","promptly","proud","provides","put","q","que","quickly","quite","qv","r","ran","rather","rd","re","readily","really","recent","recently","ref","refs","regarding","regardless","regards","related","relatively","research","respectively","resulted","resulting","results","right","run","s","said","same","saw","say","saying","says","sec","section","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sent","seven","several","shall","she","shed","she'll","shes","should","shouldn't","show","showed","shown","showns","shows","significant","significantly","similar","similarly","since","six","slightly","so","some","somebody","somehow","someone","somethan","something","sometime","sometimes","somewhat","somewhere","soon","sorry","specifically","specified","specify","specifying","still","stop","strongly","sub","substantially","successfully","such","sufficiently","suggest","sup","sure","t","take","taken","taking","tell","tends","th","than","thank","thanks","thanx","that","that'll","thats","that've","the","their","theirs","them","themselves","then","thence","there","thereafter","thereby","thered","therefore","therein","there'll","thereof","therere","theres","thereto","thereupon","there've","these","they","theyd","they'll","theyre","they've","think","this","those","thou","though","thoughh","thousand","throug","through","throughout","thru","thus","til","tip","to","together","too","took","toward","towards","tried","tries","truly","try","trying","ts","twice","two","u","un","under","unfortunately","unless","unlike","unlikely","until","unto","up","upon","ups","us","use","used","useful","usefully","usefulness","uses","using","usually","v","value","various","'ve","very","via","viz","vol","vols","vs","w","want","wants","was","wasn't","way","we","wed","welcome","we'll","went","were","weren't","we've","what","whatever","what'll","whats","when","whence","whenever","where","whereafter","whereas","whereby","wherein","wheres","whereupon","wherever","whether","which","while","whim","whither","who","whod","whoever","whole","who'll","whom","whomever","whos","whose","why","widely","willing","wish","with","within","without","won't","words","world","would","wouldn't","www","x","y","yes","yet","you","youd","you'll","your","youre","yours","yourself","yourselves","you've","z","zero"
			));
	private final ArrayList<String> STOP_WORDS_ES = new ArrayList<String>(Arrays.asList(
			"pero", "aquí", "esta", "gran", "ahora", "quot", "para", "que", "http", "de", "rt", "del", "que", "se", "no","un","una","unas","unos","uno","sobre","todo","también","tras","otro","algún","alguno","alguna","algunos","algunas","ser","es","soy","eres","somos","sois","estoy","esta","estamos","estais","estan","como","en","para","atras","porque","por qué","estado","estaba","ante","antes","siendo","ambos","pero","por","poder","puede","puedo","podemos","podeis","pueden","fui","fue","fuimos","fueron","hacer","hago","hace","hacemos","haceis","hacen","cada","fin","incluso","primero	desde","conseguir","consigo","consigue","consigues","conseguimos","consiguen","ir","voy","va","vamos","vais","van","vaya","gueno","ha","tener","tengo","tiene","tenemos","teneis","tienen","el","la","lo","las","los","su","aqui","mio","tuyo","ellos","ellas","nos","nosotros","vosotros","vosotras","si","dentro","solo","solamente","saber","sabes","sabe","sabemos","sabeis","saben","ultimo","largo","bastante","haces","muchos","aquellos","aquellas","sus","entonces","tiempo","verdad","verdadero","verdadera	cierto","ciertos","cierta","ciertas","intentar","intento","intenta","intentas","intentamos","intentais","intentan","dos","bajo","arriba","encima","usar","uso","usas","usa","usamos","usais","usan","emplear","empleo","empleas","emplean","ampleamos","empleais","valor","muy","era","eras","eramos","eran","modo","bien","cual","cuando","donde","mientras","quien","con","entre","sin","trabajo","trabajar","trabajas","trabaja","trabajamos","trabajais","trabajan","podria","podrias","podriamos","podrian","podriais","yo","aquel"
			));
 
	
	private final ArrayList<String> STOP_WORDS_FR = new ArrayList<String>(Arrays.asList(
			"quot", "http", "de", "rt", "del", "que", "se", "no","alors","au","aucuns","aussi","autre","avant","avec","avoir","bon","car","ce","cela","ces","ceux","chaque","ci","comme","comment","dans","des","du","dedans","dehors","depuis","deux","devrait","doit","donc","dos","droite","début","elle","elles","en","encore","essai","est","et","eu","fait","faites","fois","font","force","haut","hors","ici","il","ils","je	juste","la","le","les","leur","là","ma","maintenant","mais","mes","mine","moins","mon","mot","même","ni","nommés","notre","nous","nouveaux","ou","où","par","parce","parole","pas","personnes","peut","peu","pièce","plupart","pour","pourquoi","quand","que","quel","quelle","quelles","quels","qui","sa","sans","ses","seulement","si","sien","son","sont","sous","soyez	sujet","sur","ta","tandis","tellement","tels","tes","ton","tous","tout","trop","très","tu","valeur","voie","voient","vont","votre","vous","vu","ça","étaient","état","étions","été","être"
			));
	
	
	
	
	
	public StopWords(){
//		setStopWordSet();
	}
	
/*	
	private void setStopWordSet()
	{
		stopWordSet = new HashSet<String>();
		ArrayList<String> StopWordPool = new ArrayList<String>();		
		StopWordPool.addAll(LineIterator.getLines(fileStopWords));				
		for(String stopWord:StopWordPool)
		{
			stopWordSet.add(stopWord.toLowerCase());
		}
	}
	
*/		
	public boolean isStopWord(String word, String lang)
	{
		if (word.length()<MIN_LENGTH)
			return true;
		switch(lang)
		{
		case LANG_ES:
			return STOP_WORDS_ES.contains(word);			
		case LANG_EN:
			return STOP_WORDS_EN.contains(word);			
		case LANG_FR:
			return STOP_WORDS_FR.contains(word);
		}
		return false;
		
	}
	
	
	public List<String> deleteStopWords(List<String> texto, String lang){
		ArrayList<String> result = new ArrayList<String>();	
	    try{ 
	    		for (int i = 0; i < texto.size(); i++) {
	    			if (!isStopWord(texto.get(i).toLowerCase(),lang))
	    				result.add(texto.get(i).toLowerCase());
	    		}
	    }catch (Exception ex) {
	    	System.out.println(ex.getMessage());
	    }
		return result;
	}
	

}
