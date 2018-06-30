package information.retrievial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;


public class Dictionary {
    public static Set<String> dictionary = new HashSet<String>();
	private static BufferedReader in;
	
    /*
     * check if word in dictionary return true else false.
     * */
    public static boolean checkForWords(String word) throws IOException {
	            in = new BufferedReader(new FileReader("wordsDictionary"));
	            String str ;
	            while ((str = in.readLine()) != null) {
	            	str=str.toLowerCase();
	                if (str.indexOf(word) != -1) {
	                    return true;
	                }
	            }
	            in.close();
	        return false;
	    }
	 
	 
	 public static void readDic() throws IOException{
		 BufferedReader in = new BufferedReader(new FileReader("wordsDictionary"));
         String str =null;
         while ((str = in.readLine()) != null) {
        	 
         	str=str.toLowerCase();
         //	System.out.println(str);
         	if(!str.equals(null))
            dictionary.add(str);
         }
         in.close();
	 }
	 


	public static Set< String> getDictionary() {
		return dictionary;
	}


	

}
