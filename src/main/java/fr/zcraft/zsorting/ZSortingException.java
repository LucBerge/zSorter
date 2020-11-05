package fr.zcraft.zsorting;

/**
 * Exception linked to the ZSorting plugin.
 * @author Lucas
 *
 */
public class ZSortingException extends Exception {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5599133057670917005L;

	/**
	 * Constructs a ZSortingException
	 * @param message - Message of the exception.
	 */
	public ZSortingException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a ZSortingException
	 * @param cause - The cause of the exception.
	 */
	public ZSortingException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a ZSortingException
	 * @param message - Message of the exception.
	 * @param cause - The cause of the exception.
	 */
	public ZSortingException(String message, Throwable cause) {
		super(message, cause);
	}
}
