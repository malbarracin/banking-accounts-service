package io.banking.whatsapp.accounts.service;

import io.banking.whatsapp.accounts.domain.dto.TransactionRequestDTO;
import io.banking.whatsapp.accounts.domain.dto.TransactionResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for transaction operations. Defines methods for creating
 * and retrieving transactions.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
public interface TransactionService {

	/**
	 * Creates a new transaction.
	 *
	 * @param request the transaction data to create
	 * @return a Mono containing the created transaction
	 */
	Mono<TransactionResponseDTO> createTransaction(TransactionRequestDTO request);

	/**
	 * Retrieves a transaction by its ID.
	 *
	 * @param id the ID of the transaction to retrieve
	 * @return a Mono containing the transaction if found
	 */
	Mono<TransactionResponseDTO> getTransactionById(String id);

	/**
	 * Retrieves the latest transactions for an account.
	 *
	 * @param accountId the ID of the account to get transactions for
	 * @param limit     the maximum number of transactions to return
	 * @return a Flux of transactions for the account
	 */
	Flux<TransactionResponseDTO> getTransactionsByAccountId(String accountId, int limit);
}