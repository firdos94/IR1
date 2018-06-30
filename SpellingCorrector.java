package information.retrievial;
import java.util.ArrayList;
import java.util.List;

/*
 * SpellingCorrector class
 * */
public class SpellingCorrector {
      /**
       * Get all possible edits of given word by swapping, inserting and deleting
       */
	
      public static List<String> getEdits(String word) {
            List<String> edits = new ArrayList<String>();
            int wordLen = word.length();
            for (int i = 1; i < wordLen - 1; i++) {
                  edits.add(word.substring(0, i) + word.charAt(i + 1)
                              + word.charAt(i) + word.substring(i + 2));
            }

            // deleting one char, skipping i
            for (int i = 0; i < wordLen; i++) {
            	//System.out.println("DELETT");
                  edits.add(word.substring(0, i) + word.substring(i + 1));
            }

            // inserting one char
            for (int i = 0; i < wordLen + 1; i++) {
                  for (char j = 'a'; j <= 'z'; j++)
                        edits.add(word.substring(0, i) + j + word.substring(i));
            }
       //    System.out.println(edits);
            return edits;
      }

      /**
       * For given word, return closest match correct spelling
       */
      public static String correctWord(String word) {

            if (Dictionary.dictionary.contains(word))
                  return word;

            
            List<String> edits = getEdits(word);
            if (edits.size() > 0) {
                  List<String> suggestions = new ArrayList< String>();
                  for (String edit : edits) {
                        if (Dictionary.dictionary.contains(edit)) {
                     
                              suggestions.add(edit);
                        
                  }
                  }
                  if (suggestions.size() <= 0) {
                	//  System.out.println("nullllll");
                        List<String> tmpEdits = null;
                        for (String edit : edits) {
                              tmpEdits = getEdits(word);
                              for (String tmpEdit : tmpEdits) {
                                    if (Dictionary.dictionary.contains(edit)) {
                                          suggestions.add( tmpEdit);
                                    }
                              }
                        }
                  }
                  
                  if (suggestions.size() > 0) {
                	//  System.out.println("EDitt");
                        return mostPopular(suggestions);
                  }
            }
            return null;
      }

      public static String mostPopular(List<String> possibleList) {
            return possibleList.get(0);
      }

}




