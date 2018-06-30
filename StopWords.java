package information.retrievial;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/*
 * read stop words file to add to analyzer.
 * */
public class StopWords {
	static  Set<String> stopWords = new HashSet<String>();
	
	static public Set<String> StopWords(){
	 BufferedReader reader = null;

	 try {
	     File file = new File("stopWords");
	     reader = new BufferedReader(new FileReader(file));

	     String line;
	     while ((line = reader.readLine()) != null) {
	    	 stopWords.add(line);
	   //      System.out.println(line);
	     }

	 } catch (IOException e) {
	     e.printStackTrace();
	 } finally {
	     try {
	         reader.close();
	     } catch (IOException e) {
	         e.printStackTrace();
	     }
	 }
	return stopWords;
	 }
}
