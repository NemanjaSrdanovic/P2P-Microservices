package exceptions;

/**
 * The CDGException class is...
 *
 * @author Nemanja Srdanovic
 * @version 1.0
 * @since 2020-04-22
 */
public class CDGException extends Exception {

	/*-------------------------------------VARIABLES-------------------------------------*/
	private static final long serialVersionUID = 1L;
	private String name;

	/*-------------------------------------CONSTRUCTOR(S)-------------------------------------*/

	public CDGException(String name, String message) {
		super(message);
		this.name = name;
	}

	/*------------------------------------- GETTERS -------------------------------------*/
	public String getName() {
		return name;
	}

}
