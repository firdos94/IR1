
package information.retrievial;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/* 
 * 
 * reading yahoo dataset nfL6 
 * create documents.
 * each document contain: id for question and one answer
 * indexing data, using StandardAnalyzer with additional stopWords list
 * reading queries file(input)
 * apply similarity searching, based on BM25Similarity

 * */
public class Main {
	public static ArrayList<JsonResults> js= new ArrayList<JsonResults>();
	
    public static void main(String[] args) throws Exception {
    	new ReadJsonData();
        List<QueryLine>queries= new ArrayList<QueryLine>();
        queries=ReadQuery.read();
    	List<QA> DATA1 = ReadJsonData.DATA;
        List <AnsDetails> topAnswers=new ArrayList<AnsDetails>();
		Document doc1 = null ;
        try (Directory dir = newDirectory();
                Analyzer analyzer = newAnalyzer()) {
            try (IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(analyzer))) {
                for ( QA docData : DATA1) {
                   for(int i=0;i<docData.getNbestanswers().size();i++){
                 	   doc1 = new Document();    
                       doc1.add(new StringField(Constants.IDans, docData.getId(), Store.YES));
                       doc1.add(new Field(Constants.nbestanswers, docData.getNbestanswers().get(i),Constants.TERM_VECTOR_TYPE));
                       writer.addDocument(doc1);
                   }
                }              
            }      
            int o=0;
            try (DirectoryReader reader = DirectoryReader.open(dir)) {
                for(QueryLine query: queries ){ 
                	o++;
                System.out.println("n. query: "+ o);
                topAnswers.clear();
				String searchString =query.getQuestion();
            	String queryId=query.getqId();
                QueryParser parser = new QueryParser(Constants.nbestanswers, analyzer);
				Query q = parser.parse(QueryParser.escape(searchString));
             
                IndexSearcher searcher = new IndexSearcher(reader);    
                searcher.setSimilarity(new BM25Similarity());

                final TopDocs td = searcher.search(q,5);   
                for (final ScoreDoc sd : td.scoreDocs ) {
                	final Document docT = searcher.doc(sd.doc);    
                
                	List <String> a=getTermsQuery(docT.get(Constants.nbestanswers));
                	AnsDetails ans=new AnsDetails(docT.get(Constants.IDans),queryId,docT.get(Constants.nbestanswers),a,sd.score);
                	topAnswers.add(ans);
                }
                int u=5;
				if(topAnswers.size()<5)
                  u=topAnswers.size();

                List<Answer> listAn=new ArrayList<Answer>();
                if(!topAnswers.isEmpty()) {
                
                for(int i=0;i<u;i++){
                Answer ans=new Answer(topAnswers.get(i).getScore(),topAnswers.get(i).getStringAnswer());
                listAn.add(ans);
                }}
            	JsonResults e=new JsonResults(queryId, listAn);
				js.add(e);
                }
       
                try(Writer writer =new FileWriter("jsonResults.json",true)){
            		Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
            		gson1.toJson(js, writer);
                }	}    
            
            
    }}
	static final String INDEX_DIRECTORY = "demo3";
	static final String QUESTION_DIRECTORY = "demo4";


	private static Directory newDirectory1() throws IOException {
	    return FSDirectory.open(new File(QUESTION_DIRECTORY).toPath());
	}


    private static Directory newDirectory() throws IOException {
        return FSDirectory.open(new File(INDEX_DIRECTORY).toPath());
    }

    private static Analyzer newAnalyzer() {
    	CharArraySet stopWords=CharArraySet.copy(StandardAnalyzer.STOP_WORDS_SET);
    	Set<String> myStopWord = StopWords.StopWords();
    	stopWords.addAll(myStopWord);
    	StandardAnalyzer analyzer = new StandardAnalyzer(stopWords);
    	return analyzer;
    }
    private static IndexWriterConfig newIndexWriterConfig(Analyzer analyzer) {
        return new IndexWriterConfig(analyzer)
                .setOpenMode(OpenMode.CREATE)
                .setCodec(new SimpleTextCodec())
                .setSimilarity(new BM25Similarity())
                .setCommitOnClose(true);
    }


    public static List<String> getTermsQuery(String sen) throws IOException {
    	List <String> myTerms=new ArrayList<String>();
		Document doc = null ;

    	try (Directory dir1 = newDirectory1();
          		Analyzer analyzer1 = newAnalyzer())
          {
      	  try (IndexWriter writer1 = new IndexWriter(dir1, newIndexWriterConfig(analyzer1))) {
      		  doc = new Document() ;
      		  doc.add(new Field(Constants.question, sen, Constants.TERM_VECTOR_TYPE));
              writer1.addDocument(doc);
      	  }
      	try (DirectoryReader reader = DirectoryReader.open(dir1)){
      	
        final Fields fields = MultiFields.getFields(reader);
        for (final String field : fields) {        	
            final Terms terms = MultiFields.getTerms(reader, field);
            if(terms != null){
            final TermsEnum termsEnum = terms.iterator();
            while (termsEnum.next() != null) {
                String t=termsEnum.term().utf8ToString();
           if(Dictionary.checkForWords(t))
                myTerms.add(t);
            }}}}      	   	
          		}
        return myTerms;
   }
      	  };