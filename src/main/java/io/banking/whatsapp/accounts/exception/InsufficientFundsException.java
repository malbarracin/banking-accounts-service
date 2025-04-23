package io.banking.whatsapp.accounts.exception;

import java.math.BigDecimal;

/**
 * Exception thrown when an account has insufficient funds for a transaction.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
public class InsufficientFundsException extends RuntimeException {

	private final BigDecimal requestedAmount;
	private final BigDecimal availableBalance;

	public InsufficientFundsException(String message, BigDecimal requestedAmount, BigDecimal availableBalance) {
		super(message);
		this.requestedAmount = requestedAmount;
		this.availableBalance = availableBalance;
	}

	public BigDecimal getRequestedAmount() {
		return requestedAmount;
	}

	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}

	public static InsufficientFundsException forWithdrawal(BigDecimal requestedAmount, BigDecimal availableBalance) {
		return new InsufficientFundsException(
				"Insufficient funds for withdrawal. Requested: " + requestedAmount + ", Available: " + availableBalance,
				requestedAmount, availableBalance);
	}

	public static InsufficientFundsException forTransfer(BigDecimal requestedAmount, BigDecimal availableBalance) {
		return new InsufficientFundsException(
				"Insufficient funds for transfer. Requested: " + requestedAmount + ", Available: " + availableBalance,
				requestedAmount, availableBalance);
	}
}