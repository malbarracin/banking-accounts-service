package io.banking.whatsapp.accounts.exception;

/**
 * Exception thrown when attempting to create an account with a number that
 * already exists.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
public class DuplicateAccountException extends RuntimeException {

	public DuplicateAccountException(String message) {
		super(message);
	}

	public DuplicateAccountException(String message, Throwable cause) {
		super(message, cause);
	}

	public static DuplicateAccountException withAccountNumber(String accountNumber) {
		return new DuplicateAccountException("Account with number " + accountNumber + " already exists");
	}
}