package io.banking.whatsapp.accounts.service;

import io.banking.whatsapp.accounts.domain.dto.AccountRequestDTO;
import io.banking.whatsapp.accounts.domain.dto.AccountResponseDTO;
import io.banking.whatsapp.accounts.domain.dto.TransactionResponseDTO;
import io.banking.whatsapp.accounts.domain.dto.UserAccountsTransactionsDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for account operations. Defines methods for CRUD operations
 * on accounts.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
public interface AccountService {

	/**
	 * Creates a new account.
	 *
	 * @param request the account data to create
	 * @return a Mono containing the created account
	 */
	Mono<AccountResponseDTO> createAccount(AccountRequestDTO request);

	/**
	 * Retrieves an account by its ID.
	 *
	 * @param id the ID of the account to retrieve
	 * @return a Mono containing the account if found
	 */
	Mono<AccountResponseDTO> getAccountById(String id);

	/**
	 * Retrieves an account by its account number.
	 *
	 * @param accountNumber the account number to search for
	 * @return a Mono containing the account if found
	 */
	Mono<AccountResponseDTO> getAccountByNumber(String accountNumber);

	/**
	 * Retrieves all accounts belonging to a specific user.
	 *
	 * @param userId the ID of the user to find accounts for
	 * @return a Flux of all accounts belonging to the user
	 */
	Flux<AccountResponseDTO> getAccountsByUserId(String userId);

	/**
	 * Retrieves all accounts in the system.
	 *
	 * @return a Flux of all accounts
	 */
	Flux<AccountResponseDTO> getAllAccounts();

	/**
	 * Updates an existing account.
	 *
	 * @param id      the ID of the account to update
	 * @param request the updated account data
	 * @return a Mono containing the updated account
	 */
	Mono<AccountResponseDTO> updateAccount(String id, AccountRequestDTO request);

	/**
	 * Deletes an account by its ID.
	 *
	 * @param id the ID of the account to delete
	 * @return a Mono that completes when the account is deleted
	 */
	Mono<Void> deleteAccount(String id);

	/**
	 * Retrieves the latest transactions for an account.
	 *
	 * @param accountId the ID of the account to get transactions for
	 * @param limit     the maximum number of transactions to return
	 * @return a Flux of transactions for the account
	 */
	Flux<TransactionResponseDTO> getAccountTransactions(String accountId, int limit);

	/**
	 * Gets user information, accounts and transactions by phone number.
	 *
	 * @param phoneNumber      The phone number of the user
	 * @param transactionLimit Maximum number of transactions to return per account
	 * @return A DTO containing user information, accounts and transactions
	 */
	Mono<UserAccountsTransactionsDTO> getUserAccountsTransactionsByPhoneNumber(String phoneNumber,
			int transactionLimit);

	/**
	 * Retrieves all accounts belonging to a user with the specified DNI.
	 *
	 * @param dni the DNI (National ID) of the user to find accounts for
	 * @return a Flux of all accounts belonging to the user with the specified DNI
	 */
	Flux<AccountResponseDTO> getAccountsByUserDni(String dni);

	/**
	 * Retrieves all accounts belonging to a user with the specified phone number.
	 *
	 * @param phoneNumber the phone number of the user to find accounts for
	 * @return a Flux of all accounts belonging to the user with the specified phone
	 *         number
	 */
	Flux<AccountResponseDTO> getAccountsByUserPhoneNumber(String phoneNumber);
}