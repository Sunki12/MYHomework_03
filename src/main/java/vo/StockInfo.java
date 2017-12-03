package vo;

public class StockInfo {
	private String id;
	private String title;
	private String author;
	private String date;
	private String lastUpdate;
	private String content;
	private String answerAuthor;
	private String answer;

	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getTitle() {
		return title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setAnswerAuthor(String answerAuthor) {
		this.answerAuthor = answerAuthor;
	}

	public String getAnswerAuthor() {
		return answerAuthor;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnswer() {
		return answer;
	}
}

