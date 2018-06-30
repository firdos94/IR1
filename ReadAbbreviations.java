package information.retrievial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * read AbbreviationsWord.txt
 * check if word is abbreviation.
 * */
public class ReadAbbreviations {

static Map <String, String> AbbreviationsWors = new HashMap<String, String>();

	static public Map<String, String> getAbbreviationsWors(){
	 BufferedReader reader = null;

	 try {
	     File file = new File("AbbreviationsWords");
	     reader = new BufferedReader(new FileReader(file));

	     String line;
	     while ((line = reader.readLine()) != null) {
	    	 String[] words = line.split("\\s+", 2);
	    	 AbbreviationsWors.put(words[0],words[1]);
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
	return AbbreviationsWors;
	 }
}