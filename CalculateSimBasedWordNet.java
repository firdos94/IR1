package information.retrievial;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.json.simple.parser.ParseException;
import org.tartarus.snowball.ext.EnglishStemmer;

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.lexical_db.data.Concept;
import edu.cmu.lti.ws4j.Relatedness;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.Lin;


/*
 * given terms (correct spelling, no stop words, in dictionary).

 * calculate score based to: 
 * stemming Snowball
 * check AbbreviationsWords
 * similarity between words based wordNet 
 * 
 * */
public class CalculateSimBasedWordNet {

	/*
	 * function calculate similarity between two words
	 * return float 0 <number< 1 */
     public static float sim(String word1,String word2) {
    	ILexicalDatabase db = new NictWordNet();  // wordNet dictionary 
    	RelatednessCalculator rc = new Lin(db);
    //	RelatednessCalculator rc = new WuPalmer(db);
    //	RelatednessCalculator rc = new Path(db);
    	
    	List<POS[]> posPairs = rc.getPOSPairs();
    	float maxScore = 0;
    	
    	for(POS[] posPair: posPairs) {
    	    List<Concept> synsets1 = (List<Concept>)db.getAllConcepts(word1, posPair[0].toString());
    	    List<Concept> synsets2 = (List<Concept>)db.getAllConcepts(word2, posPair[1].toString());
    	    for(Concept synset1: synsets1) {
    	        for (Concept synset2: synsets2) {
    	            Relatedness relatedness = rc.calcRelatednessOfSynset(synset1, synset2);
    	            float score = (float) relatedness.getScore();
    	            if (score > maxScore) { 
    	                maxScore = score;
    	            }
    	        }
    	    }
    	}

    	if (maxScore == -1D) {
    	    maxScore = (float) 0.0;
    	}
    	if(maxScore>0)
    	System.out.println("sim('" + word1 + "', '" + word2 + "') =  " + maxScore);		
    	return maxScore; 
    }
     
     public static float simByRoots(String word1,String word2) { 
		 float score=0;
		EnglishStemmer stemmer = new EnglishStemmer();
		 String newWord2=null;
		 String newWord1=null;

		 score = CalculateSimBasedWordNet.sim(word1, word2); 
		 if(score==0) // no relation between words
		 {
		 newWord1= Snowball.stemString(word1, stemmer); // convert the words to the root form
     	 score = CalculateSimBasedWordNet.sim(newWord1,word2);
  
		 }
		 if(score==0){
	     newWord2=Snowball.stemString(word2, stemmer);
	  	 score = CalculateSimBasedWordNet.sim(word1,newWord2);
			 
		 }
		 if(score==0){
		  	 score = CalculateSimBasedWordNet.sim(newWord1,newWord2); 
		}
		 
		 
    	 return score;
     }
     /*
      * function check if one word is abbreviation using "AbbreviationsWords.txt"*/
     public static float simByAbbreviation(String word1,String word2) throws FileNotFoundException, IOException, ParseException {     	Map <String, String> AbbreviationsWords=ReadAbbreviations.getAbbreviationsWors();	
          float score=0;
    	  String[] myTerms1=null;
    	  String[] myTerms2=null;
    	  
     	  String s1= AbbreviationsWords.get(word1);
   		  String s2= AbbreviationsWords.get(word2);

    	  if( s1==null && s2==null){
    		if(Dictionary.checkForWords(word1) && (Dictionary.checkForWords(word2)))
	    	score=simByRoots(word1,word2);    
    	  }
    	  if(score==0) {
   	      if(s1!=null){
   	      myTerms1 = s1.split("\\s+");
   	     for(String check1 : myTerms1){
     	  score=+simByRoots(word2,check1);
   	     }}
   	  if(score==0){
  	      if(s2!=null){
  	      myTerms2 = s2.split("\\s+");
  	     for(String check2 : myTerms2){
    	  score=+simByRoots(word1,check2);
  	     }
  	     }
  	      }
  	     if(s1!=null && s2!=null){
  	   	     for(String ans : myTerms1){
  	    	     for(String ques : myTerms2){
  	    	     score=+simByRoots(ans,ques);
  	    	     }  
  	     }} 	
    	  }
    	  if(score>0.8)
    	  {
    		  score=score*2;
    	  }
    	 return score;
     }

/*
 * calculate global score between terms of query and top answers list
 * 
 * */
	public static List<AnsDetails> getScore(List<String> returnTermsForQuery, List<AnsDetails> topAnswers) throws FileNotFoundException, IOException, ParseException {
		for (AnsDetails a :topAnswers){
			a.setScore(0);
			if(a.getAnswer().size()>0)
			a.setScore(getScoreForSentences(returnTermsForQuery,a.getAnswer()));
		}
		extracted(topAnswers);
		
		int size=5;
		if(topAnswers.size()<5){
			size=topAnswers.size();
		}
		if(!topAnswers.isEmpty())
		writeJsonResults(topAnswers.subList(0, size));
//	System.out.println(topAnswers.subList(0, size));
		return topAnswers.subList(0, size);
	}

	/*
	 * add answers to js list.
	 * */
private static void writeJsonResults(List<AnsDetails> subList) throws IOException {
    String qId=null;
    float i=0;
    List<Answer> listAn= new ArrayList<Answer>();
    for(AnsDetails a: subList){
    	qId=a.getQuesId();
        i= a.getScore();
        Answer a1=new Answer();
    	a1.setScore(i);
    	a1.setAns(a.getStringAnswer());
    	listAn.add(a1);
    }
    
	JsonResults jsonObj=new JsonResults(qId, listAn);
	improvedPass.js.add(jsonObj);
}

private static void extracted(List<AnsDetails> topAnswers){
    Collections.sort(topAnswers);
}

private static float getScoreForSentences(List<String> returnTermsForQuery, List<String> list) throws FileNotFoundException, IOException, ParseException {
		float score=0;
	
		for(int i=0;i<returnTermsForQuery.size();i++)
		{
			if(list.size()>1) {
			for(int j=0;j<list.size();j++){
				score+= (float)simByAbbreviation(returnTermsForQuery.get(i),list.get(j));		
			}
		}
		}
		return  (score/list.size()); // normalization for score
	}
}