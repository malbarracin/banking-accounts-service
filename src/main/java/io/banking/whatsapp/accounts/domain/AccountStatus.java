package io.banking.whatsapp.accounts.domain;

/**
 * Enum representing the possible statuses of a bank account.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
public enum AccountStatus {
	/**
	 * Account is operational and can be used for transactions.
	 */
	ACTIVE,

	/**
	 * Account is temporarily disabled.
	 */
	INACTIVE,

	/**
	 * Account is permanently closed.
	 */
	CLOSED,

	/**
	 * Account is blocked due to suspicious activity.
	 */
	BLOCKED
}