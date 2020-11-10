package fr.zcraft.zsorter;

/**
 * Exception linked to the ZSorter plugin.
 * @author Lucas
 *
 */
public class ZSorterException extends Exception {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5599133057670917005L;

	/**
	 * Constructs a ZSorterException
	 * @param message - Message of the exception.
	 */
	public ZSorterException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a ZSorterException
	 * @param cause - The cause of the exception.
	 */
	public ZSorterException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a ZSorterException
	 * @param message - Message of the exception.
	 * @param cause - The cause of the exception.
	 */
	public ZSorterException(String message, Throwable cause) {
		super(message, cause);
	}
}
