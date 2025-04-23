package io.banking.whatsapp.accounts.exception;

/**
 * Exception thrown when an account is not found in the system.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
public class AccountNotFoundException extends RuntimeException {

	public AccountNotFoundException(String message) {
		super(message);
	}

	public AccountNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public static AccountNotFoundException withId(String id) {
		return new AccountNotFoundException("Account not found with ID: " + id);
	}

	public static AccountNotFoundException withAccountNumber(String accountNumber) {
		return new AccountNotFoundException("Account not found with number: " + accountNumber);
	}
}