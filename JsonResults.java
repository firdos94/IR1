package information.retrievial;

import java.util.ArrayList;
import java.util.List;

public class JsonResults {
	
	String id;
	List<Answer> answers=new ArrayList<Answer>();
	public JsonResults(String idQuestion, List<Answer> listAnswer) {
		super();
		this.id = idQuestion;
		this.answers = listAnswer;
	}
	public JsonResults(List<Answer> listAn) {
		// TODO Auto-generated constructor stub
	}
	public List<Answer> getListAnswer() {
		return answers;
	}
	public void setListAnswer(List<Answer> listAnswer) {
		this.answers = listAnswer;
	}
	public String getIdQuestion() {
		return id;
	}
	public void setIdQuestion(String idQuestion) {
		this.id = idQuestion;
	}
	@Override
	public String toString() {
		return "JsonResults [idQuestion=" + id + ", listAnswer=" + answers + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
	
	

	
	

}
