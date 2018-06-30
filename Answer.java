package information.retrievial;

public class Answer {
	String answer;
	float score;
	
	public Answer(float score, String ans) {
		super();
		this.score = (float) score;
		this.answer = ans;
	}
	public Answer() {
		// TODO Auto-generated constructor stub
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	
	
	public String getAns() {
		return answer;
	}
	
	
	public void setAns(String ans) {
		this.answer = ans;
	}
//	@Override
//	public String toString() {
//		return "Answer [answer=" + answer + ", score=" + score + "]";
//	}

	
	

}
