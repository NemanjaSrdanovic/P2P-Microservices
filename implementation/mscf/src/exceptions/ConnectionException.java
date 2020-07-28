package exceptions;

public class ConnectionException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorName;
	
	public ConnectionException(String errorName, String errorMessage) {
		
		super(errorMessage);
		
		this.errorName = errorName;
		
	}
	
	public String getErrorName() {
		
		return this.errorName;
	}
	
	
}
