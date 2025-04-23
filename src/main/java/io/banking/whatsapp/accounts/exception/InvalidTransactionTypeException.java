package io.banking.whatsapp.accounts.exception;

/**
 * Exception thrown when an invalid transaction type is provided.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
public class InvalidTransactionTypeException extends RuntimeException {

	public InvalidTransactionTypeException(String message) {
		super(message);
	}

	public InvalidTransactionTypeException(String message, Throwable cause) {
		super(message, cause);
	}
}