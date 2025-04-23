package io.banking.whatsapp.accounts.domain;

/**
 * Enum representing the different types of transactions that can be performed
 * on an account.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
public enum TransactionType {
	/**
	 * Money added to the account
	 */
	DEPOSIT,

	/**
	 * Money removed from the account
	 */
	WITHDRAWAL,

	/**
	 * Money moved from one account to another
	 */
	TRANSFER,

	/**
	 * Payment made from the account
	 */
	PAYMENT,

	/**
	 * Fee charged to the account
	 */
	FEE,

	/**
	 * Interest added to the account
	 */
	INTEREST
}