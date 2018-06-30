package information.retrievial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
 * class for reading queries file
 * format: id question.
 * */
public class ReadQuery {
    static List<QueryLine>queries= new ArrayList<QueryLine>();
	public static List<QueryLine> read() throws IOException {
	      BufferedReader in = new BufferedReader(new FileReader("queries"));
          String str ;
          while ((str = in.readLine()) != null) {
       	      String[] myQuery = str.split("\\s+",2);
        	  QueryLine q=new QueryLine(myQuery[0],myQuery[1]);
        	  queries.add(q);
          }
		return queries;
	}
}
