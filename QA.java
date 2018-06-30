package information.retrievial;

import java.util.ArrayList;
import java.util.List;

public class QA {
	private String main_category;
	private String id;
	private String question;
	private List<String> nbestanswers = new ArrayList<String>();
	private String answer;
	
	
	
	
	
	public QA(String main_category, String id, String question, List<String> nbestanswers, String answer) {
		super();
		this.main_category = main_category;
		this.id = id;
		this.question = question;
		this.nbestanswers = nbestanswers;
		this.answer = answer;
	}
	public String getMain_category() {
		return main_category;
	}
	public void setMain_category(String main_category) {
		this.main_category = main_category;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public List<String> getNbestanswers() {
		return nbestanswers;
	}
	public void setNbestanswers(List<String> nbestanswers) {
		this.nbestanswers = nbestanswers;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	@Override
	public String toString() {
		return "QA [main_category=" + main_category + ", id=" + id + ", question=" + question + ", nbestanswers="
				+ nbestanswers + ", answer=" + answer + "]";
	}
	
	
	
    


}
