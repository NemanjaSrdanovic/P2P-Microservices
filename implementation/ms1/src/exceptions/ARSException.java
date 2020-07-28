package exceptions;

public class ARSException extends Exception {
	
	/**
	 * Custom made Exception in order to track errors that can occur when system runs
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	public ARSException(String name ,String message) {
		
		super(message);
		
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
