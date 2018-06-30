package information.retrievial;

/*
 * line in queries.txt input.
 * 
 * */
public class QueryLine {
	String qId;
	String question;
	public QueryLine(String qId, String question) {
		super();
		this.qId = qId;
		this.question = question;
	}
	public String getqId() {
		return qId;
	}
	public void setqId(String qId) {
		this.qId = qId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	@Override
	public String toString() {
		return "QueryLine [qId=" + qId + ", question=" + question + "]";
	}
	
	
	

}
