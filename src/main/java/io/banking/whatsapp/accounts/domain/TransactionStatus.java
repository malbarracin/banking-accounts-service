package io.banking.whatsapp.accounts.domain;

/**
 * Enum representing the different statuses a transaction can have.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
public enum TransactionStatus {
	/**
	 * Transaction has been initiated but not yet processed
	 */
	PENDING,

	/**
	 * Transaction has been successfully processed
	 */
	COMPLETED,

	/**
	 * Transaction has failed
	 */
	FAILED,

	/**
	 * Transaction has been cancelled
	 */
	CANCELLED,

	/**
	 * Transaction is being processed
	 */
	PROCESSING
}