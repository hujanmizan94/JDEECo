package cz.cuni.mff.d3s.deeco.knowledge.container;

public class KnowledgeAccessException extends Exception {

	public KnowledgeAccessException() {
	}

	public KnowledgeAccessException(String message) {
		super(message);
	}

	public KnowledgeAccessException(Throwable cause) {
		super(cause);
	}

	public KnowledgeAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public KnowledgeAccessException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}