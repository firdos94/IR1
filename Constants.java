package information.retrievial;

import java.util.List;

import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;

import java.lang.reflect.Type;
import java.util.Collection;
import com.google.gson.reflect.TypeToken;

final public class Constants{ 
    public static final List<QA> DATA=ReadJsonData.DATA;
	public static final String  IDans = "id";
	public static final String  IDquery = "id";
	public final static Type JsonResults = new TypeToken<Collection<JsonResults>>() {}.getType();
	public static final String  mainCategory = "mainCategory";
	public static final String  question = "question";
	public static final String 	nbestanswers = "	nbestanswers";
	public static final String 	answer = "	answer";



    public static final FieldType TERM_VECTOR_TYPE;
    static {
    	
        TERM_VECTOR_TYPE = new FieldType(TextField.TYPE_STORED);
        TERM_VECTOR_TYPE.setStoreTermVectors(true);
        TERM_VECTOR_TYPE.setStoreTermVectorPositions(true);
        TERM_VECTOR_TYPE.setStoreTermVectorOffsets(true);
        TERM_VECTOR_TYPE.freeze();
    }
    
//	 static String[] Categories={"Business Finance", "Yahoo Product", "Health", "Society Culture",
//			 "Local Bsinesses",
//			 "Politics Government",
//			 "Food Drink",
//			 "Social Science",
//			 "Yahoo!7 Products",
//			 "Computer internet",
//			 "Education Reference",
//			 "Consumer Electronic",
//			 "Pet",
//			 "Travel",
//			 "Science Mathematics",
//			 "Dining Out",
//			 "Asia Pacific",
//			 "Game Recreation",
//			 "Car Transportation",
//			 "News Event",
//			 "Art Humanity"
//};
    static String[] Categories={"Right-click computer, select properties,click hardware tab click device manage. Click Modems see enabled.enable",
    		"becom crikater work hard struggle win",
    		"copy hot stars hairstyle Brad Pitt hes hot.",
    		"came tv comerical baseball, hot dogs, apple pie chevorlet",
    		"Wow somebody wants to have your babies or bebes"};


}
