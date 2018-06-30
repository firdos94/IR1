package information.retrievial;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import org.tartarus.snowball.ext.EnglishStemmer;


/*
 * stemming, process of reducing inflected words
 *  to their word stem, base or root form.
 * 
 * */
public class Snowball {	
	public static String stemString(String str, EnglishStemmer stemmer) {
	String stemmedString = "";
	String[] words = str.split("\\s+"); 
	for (int i = 0; i < words.length; i++) {
		String word = words[i];
		stemmer.setCurrent(word);
		stemmer.stem();
		stemmedString += stemmer.getCurrent();
		if (i != words.length)
			stemmedString += " ";
	}
//	System.out.println(stemmedString);
	return stemmedString.substring(0, stemmedString.length()-1);
}
    public static void main(String[] args) throws IOException, ParseException{
    	EnglishStemmer stemmer = new EnglishStemmer();

    	Snowball.stemString("", stemmer);
    }

}
