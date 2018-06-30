package information.retrievial;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * 
 * reading nfL6 dataset
 * yahoo dataset: question,id-question,main category,
 *  list of answers and best answer.
 * 
 * */
 public class ReadJsonData {
    static public List<QA> DATA = new ArrayList<QA>();	
    
   	ReadJsonData() throws FileNotFoundException, IOException, ParseException{
   	JSONParser parser = new JSONParser();
	 String datapath=("C:\\Users\\firdo\\Desktop\\nfL6.json");
	 JSONArray a = (JSONArray) parser.parse(new FileReader(datapath));

	  for (Object o : a)
	  {
	    JSONObject QR = (JSONObject) o;
        
	    String  main_category= (String) QR.get("main_category");

	    String question = (String) QR.get("question");

	    String answer = (String) QR.get("answer");
        
	    String id =  (String) QR.get("id");
	   	    
	    List <String> ans=new ArrayList<String>();
	    JSONArray nbestanswers = (JSONArray) QR.get("nbestanswers");
	   for(int i=0;i<nbestanswers.size();i++){
	    ans.add((String) nbestanswers.get(i));	  
	   }
	    QA q=new QA(main_category,id,question,ans,answer);
		 DATA.add(q);
	  }
   	}
	public static void main(String[] args) throws IOException, ParseException {
	new ReadJsonData();
	//System.out.println(DATA.get(1).getAnswer());
}
}
