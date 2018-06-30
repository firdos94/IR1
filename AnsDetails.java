package information.retrievial;

import java.util.ArrayList;
import java.util.List;

public class  AnsDetails  implements Comparable{
	String id; // answer id
	String quesId; // ques id
	List<String> answer=new ArrayList<String>(); // terms of ans
	String stringAnswer; // complete ans
	float score;
	
	public AnsDetails(String id,String quesId,String stringAnswer, List<String> answer,float score) {
		super();
		this.id = id;
		this.answer = answer;
		this.quesId=quesId;
		this.stringAnswer=stringAnswer;
		this.score=score;
	}
	

	public String getQuesId() {
		return quesId;
	}


	public void setQuesId(String quesId) {
		this.quesId = quesId;
	}
	
	public String getStringAnswer() {
		return stringAnswer;
	}


	public void setStringAnswer(String stringAnswer) {
		this.stringAnswer = stringAnswer;
	}


	public float getScore() {
		return score;
	}



	public void setScore(float score) {
		this.score = score;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getAnswer() {
		return answer;
	}

	public void setAnswer(List<String> answer) {
		this.answer = answer;
	}
	

	public int compareTo(Object o) {
		 double compareage=((AnsDetails)o).getScore();
	
		 return Double.compare(compareage,this.getScore());		 

	}


	@Override
	public String toString() {
		return "AnsDetails [id=" + id + ", quesId=" + quesId + ", stringAnswer=" + stringAnswer + ", score=" + score
				+ "]";
	}
	
	


};