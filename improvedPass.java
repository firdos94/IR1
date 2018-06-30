/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required byOCP applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * create documents
 * each document contain: id for question and one answer
 * indexing data, using StandardAnalyzer with additional stopWords list
 * reading queries file(input)
 * apply first similarity searching, based on BM25Similarity
 * given 50 topDocs and calculate best answers based on second similarity 
 * (CalulateSimBasedWordNet.java)
 * 
 * */
public class improvedPass {
	public static ArrayList<JsonResults> js= new ArrayList<JsonResults>();
    static List<Answer> listAn=new ArrayList<Answer>();

	
    public static void main(String[] args) throws Exception {
    	new ReadJsonData();
        List<QueryLine>queries= new ArrayList<QueryLine>();
        List <AnsDetails> returnTop=new ArrayList<AnsDetails>();

        
        queries=ReadQuery.read();
    	List<QA> DATA1 = ReadJsonData.DATA;
    	//System.out.println(DATA1.size());
        List<String> returnTermsForQuery =new ArrayList<String>();
       List <AnsDetails> topAnswers=new ArrayList<AnsDetails>();
		Document doc1 = null ;
        try (Directory dir = newDirectory();
                Analyzer analyzer = newAnalyzer()) {
            // Index
     //   	System.out.println("INDEX TIME");
            try (IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(analyzer))) {
                for ( QA docData : DATA1) {
                   for(int i=0;i<docData.getNbestanswers().size();i++){
                 	   doc1 = new Document();    
                       doc1.add(new StringField(Constants.IDans, docData.getId(), Store.YES));
                   //    doc1.add(new StringField(Constants.mainCategory, docData.getMain_category(), Store.NO));
                       doc1.add(new Field(Constants.nbestanswers, docData.getNbestanswers().get(i), Constants.TERM_VECTOR_TYPE));
                       writer.addDocument(doc1);
                   }
                }              
            }        
            int o=0;
            try (DirectoryReader reader = DirectoryReader.open(dir)) {
       //     	System.out.println("Searcher. . . ");
                for(QueryLine query: queries ){ 
                	o++;
                	System.out.println("Query: "+o);
               topAnswers.clear();
                returnTermsForQuery.clear();
            	String searchString =query.getQuestion();
            	String queryId=query.getqId();
                QueryParser parser = new QueryParser(Constants.nbestanswers, analyzer);
				Query q = parser.parse(QueryParser.escape(searchString));
                returnTermsForQuery = getTermsQuery(searchString);

                IndexSearcher searcher = new IndexSearcher(reader);    
                searcher.setSimilarity(new BM25Similarity());

                final TopDocs td = searcher.search(q,10);   
                for (final ScoreDoc sd : td.scoreDocs ) {
                	final Document docT = searcher.doc(sd.doc);    
                
                	List <String> a=getTermsQuery(docT.get(Constants.nbestanswers));
                	AnsDetails ans=new AnsDetails(docT.get(Constants.IDans),queryId,docT.get(Constants.nbestanswers),a,sd.score);
//                	System.out.println("top doc "+sd.score+"  id "+ docT.get(Constants.IDans));
//                	System.out.println(docT.get(Constants.nbestanswers));
                	topAnswers.add(ans);
                }
                
          
            if(returnTermsForQuery!=null && !topAnswers.isEmpty()) {
              returnTop=null;
              returnTop=CalculateSimBasedWordNet.getScore(returnTermsForQuery,topAnswers);      
           }
                
            if(returnTop==null || topAnswers.isEmpty()){
                if(topAnswers.isEmpty())
                {
                	List <String> a= new ArrayList<String>();
                	AnsDetails element=new AnsDetails(null, queryId, null, a, 0);
                	topAnswers.add(element);
                }
                int u=5;
				if(topAnswers.size()<5)
                u=topAnswers.size();
				listAn.clear();
                for(int i=0;i<u;i++) {
                Answer ans=new Answer(topAnswers.get(i).getScore(),topAnswers.get(i).getStringAnswer());
                listAn.add(ans);
                }
            	JsonResults e=new JsonResults(queryId, listAn);
				js.add(e);
            }}             
                try(Writer writer =new FileWriter("jsonResults.json",true)){
            		Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
            		gson1.toJson(improvedPass.js, writer);

                
           	}    }
            
            }
    }
	static final String INDEX_DIRECTORY = "demo1";
	static final String QUESTION_DIRECTORY = "demo2";


	private static Directory newDirectory1() throws IOException {
	    return FSDirectory.open(new File(QUESTION_DIRECTORY).toPath());
	}


    private static Directory newDirectory() throws IOException {
        return FSDirectory.open(new File(INDEX_DIRECTORY).toPath());
    }

    private static Analyzer newAnalyzer() {
    	CharArraySet stopWords=CharArraySet.copy(StandardAnalyzer.STOP_WORDS_SET);
    //	StandardAnalyzer.
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
      	
    //    System.out.println("query info:" +sen);
        final Fields fields = MultiFields.getFields(reader);
        for (final String field : fields) {
            final Terms terms = MultiFields.getTerms(reader, field);
            if(terms != null) {
            final TermsEnum termsEnum = terms.iterator();
            while (termsEnum.next()!=null) {
                String t=termsEnum.term().utf8ToString();
          //      System.out.println("termQuery "+t);
                if(Dictionary.checkForWords(t)) // check if term in dictionary
                	myTerms.add(t);
                else  //  check if term is spelling correct
                {
                	String correctTerm=SpellingCorrector.correctWord(t);
                 	if ((correctTerm!=null && correctTerm.equals(t)))
                	myTerms.add(SpellingCorrector.correctWord(t));
            	}} 
           }else {
        	   myTerms=null;
        	   
           
           }
        	   
            }
        }   
      	}
        return myTerms;
   }
      	  };
    

