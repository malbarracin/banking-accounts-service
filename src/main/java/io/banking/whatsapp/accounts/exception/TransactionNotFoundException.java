package io.banking.whatsapp.accounts.exception;

/**
 * Exception thrown when a transaction is not found in the system.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
public class TransactionNotFoundException extends RuntimeException {

	public TransactionNotFoundException(String message) {
		super(message);
	}

	public TransactionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public static TransactionNotFoundException withId(String id) {
		return new TransactionNotFoundException("Transaction not found with ID: " + id);
	}
}