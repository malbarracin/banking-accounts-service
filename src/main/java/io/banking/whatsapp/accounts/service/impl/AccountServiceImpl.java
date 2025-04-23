package io.banking.whatsapp.accounts.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.banking.whatsapp.accounts.domain.Account;
import io.banking.whatsapp.accounts.domain.dto.AccountRequestDTO;
import io.banking.whatsapp.accounts.domain.dto.AccountResponseDTO;
import io.banking.whatsapp.accounts.domain.dto.AccountWithTransactionsDTO;
import io.banking.whatsapp.accounts.domain.dto.TransactionResponseDTO;
import io.banking.whatsapp.accounts.domain.dto.UserAccountsTransactionsDTO;
import io.banking.whatsapp.accounts.exception.AccountNotFoundException;
import io.banking.whatsapp.accounts.exception.DuplicateAccountException;
import io.banking.whatsapp.accounts.mapper.AccountMapper;
import io.banking.whatsapp.accounts.mapper.TransactionMapper;
import io.banking.whatsapp.accounts.repository.AccountRepository;
import io.banking.whatsapp.accounts.repository.TransactionRepository;
import io.banking.whatsapp.accounts.service.AccountService;
import io.banking.whatsapp.accounts.service.TransactionService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the AccountService interface. Provides business logic for
 * account operations.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;
	private final TransactionService transactionService;
	private final TransactionRepository transactionRepository;
	private final TransactionMapper transactionMapper;
	private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

	/**
	 * Creates a new account. Validates that the account number is unique before
	 * creating the account.
	 *
	 * @param request the account data to create
	 * @return a Mono containing the created account
	 * @throws ResponseStatusException with HTTP 409 CONFLICT if an account with the
	 *                                 same number already exists
	 */
	@Override
	public Mono<AccountResponseDTO> createAccount(AccountRequestDTO request) {
		log.debug("Creating account with number: {}", request.getAccountNumber());

		return accountRepository.findByAccountNumber(request.getAccountNumber())
				.flatMap(existingAccount -> Mono.<AccountResponseDTO>error(
						DuplicateAccountException.withAccountNumber(request.getAccountNumber())))
				.switchIfEmpty(Mono.defer(() -> {
					Account account = accountMapper.toEntity(request);
					return accountRepository.save(account).map(accountMapper::toDto)
							.doOnSuccess(dto -> log.info("Account created successfully: {}", dto.getAccountNumber()));
				}));
	}

	/**
	 * Retrieves an account by its ID.
	 *
	 * @param id the ID of the account to retrieve
	 * @return a Mono containing the account if found
	 * @throws ResponseStatusException with HTTP 404 NOT_FOUND if the account does
	 *                                 not exist
	 */
	@Override
	public Mono<AccountResponseDTO> getAccountById(String id) {
		log.debug("Getting account by ID: {}", id);

		return accountRepository.findById(id).switchIfEmpty(Mono.error(AccountNotFoundException.withId(id)))
				.map(accountMapper::toDto).doOnSuccess(dto -> log.debug("Found account: {}", dto));
	}

	/**
	 * Retrieves an account by its account number.
	 *
	 * @param accountNumber the account number to search for
	 * @return a Mono containing the account if found
	 * @throws ResponseStatusException with HTTP 404 NOT_FOUND if the account does
	 *                                 not exist
	 */
	@Override
	public Mono<AccountResponseDTO> getAccountByNumber(String accountNumber) {
		log.debug("Getting account by number: {}", accountNumber);

		return accountRepository.findByAccountNumber(accountNumber)
				.switchIfEmpty(Mono.error(AccountNotFoundException.withAccountNumber(accountNumber)))
				.map(accountMapper::toDto).doOnSuccess(dto -> log.debug("Found account: {}", dto));
	}

	/**
	 * Retrieves all accounts belonging to a specific user.
	 *
	 * @param userId the ID of the user to find accounts for
	 * @return a Flux of all accounts belonging to the user
	 */
	@Override
	public Flux<AccountResponseDTO> getAccountsByUserId(String userId) {
		log.debug("Getting accounts for user ID: {}", userId);

		return accountRepository.findByUserId(userId).map(accountMapper::toDto)
				.doOnComplete(() -> log.debug("Completed fetching accounts for user ID: {}", userId));
	}

	/**
	 * Retrieves all accounts belonging to a user with the specified DNI.
	 *
	 * @param dni the DNI (National ID) of the user to find accounts for
	 * @return a Flux of all accounts belonging to the user with the specified DNI
	 */
	@Override
	public Flux<AccountResponseDTO> getAccountsByUserDni(String dni) {
		log.debug("Getting accounts for user DNI: {}", dni);

		return accountRepository.findByUserDni(dni).map(accountMapper::toDto)
				.doOnComplete(() -> log.debug("Completed fetching accounts for user DNI: {}", dni));
	}

	/**
	 * Retrieves all accounts belonging to a user with the specified phone number.
	 *
	 * @param phoneNumber the phone number of the user to find accounts for
	 * @return a Flux of all accounts belonging to the user with the specified phone
	 *         number
	 */
	@Override
	public Flux<AccountResponseDTO> getAccountsByUserPhoneNumber(String phoneNumber) {
		log.debug("Getting accounts for user phone number: {}", phoneNumber);

		return accountRepository.findByUserPhoneNumber(phoneNumber).map(accountMapper::toDto)
				.doOnComplete(() -> log.debug("Completed fetching accounts for user phone number: {}", phoneNumber));
	}

	/**
	 * Retrieves all accounts in the system.
	 *
	 * @return a Flux of all accounts
	 */
	@Override
	public Flux<AccountResponseDTO> getAllAccounts() {
		log.debug("Getting all accounts");

		return accountRepository.findAll().map(accountMapper::toDto)
				.doOnComplete(() -> log.debug("Completed fetching all accounts"));
	}

	/**
	 * Updates an existing account.
	 *
	 * @param id      the ID of the account to update
	 * @param request the updated account data
	 * @return a Mono containing the updated account
	 * @throws ResponseStatusException with HTTP 404 NOT_FOUND if the account does
	 *                                 not exist
	 */
	@Override
	public Mono<AccountResponseDTO> updateAccount(String id, AccountRequestDTO request) {
		log.debug("Updating account with ID: {}", id);

		return accountRepository.findById(id).switchIfEmpty(Mono.error(AccountNotFoundException.withId(id)))
				.flatMap(existingAccount -> {
					accountMapper.updateEntity(request, existingAccount);
					return accountRepository.save(existingAccount);
				}).map(accountMapper::toDto)
				.doOnSuccess(dto -> log.info("Account updated successfully: {}", dto.getAccountNumber()));
	}

	/**
	 * Deletes an account by its ID.
	 *
	 * @param id the ID of the account to delete
	 * @return a Mono that completes when the account is deleted
	 * @throws ResponseStatusException with HTTP 404 NOT_FOUND if the account does
	 *                                 not exist
	 */
	@Override
	public Mono<Void> deleteAccount(String id) {
		log.debug("Deleting account with ID: {}", id);

		return accountRepository.findById(id).switchIfEmpty(Mono.error(AccountNotFoundException.withId(id)))
				.flatMap(account -> {
					log.info("Deleting account: {}", account.getAccountNumber());
					return accountRepository.delete(account);
				});
	}

	/**
	 * Retrieves the latest transactions for an account.
	 *
	 * @param accountId the ID of the account to get transactions for
	 * @param limit     the maximum number of transactions to return
	 * @return a Flux of transactions for the account
	 * @throws ResponseStatusException with HTTP 404 NOT_FOUND if the account does
	 *                                 not exist
	 */
	@Override
	public Flux<TransactionResponseDTO> getAccountTransactions(String accountId, int limit) {
		log.debug("Getting transactions for account ID: {} (limit: {})", accountId, limit);

		return accountRepository.findById(accountId)
				.switchIfEmpty(Mono.error(AccountNotFoundException.withId(accountId))).flatMapMany(account -> {
					PageRequest pageRequest = PageRequest.of(0, limit);
					return transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId, pageRequest)
							.map(transactionMapper::toDto).doOnComplete(
									() -> log.debug("Completed fetching transactions for account ID: {}", accountId));
				});
	}

	/**
	 * Gets user information, accounts and transactions by phone number. This method
	 * aggregates data from multiple sources to provide a comprehensive view of a
	 * user's financial information based on their phone number.
	 *
	 * @param phoneNumber      The phone number of the user
	 * @param transactionLimit Maximum number of transactions to return per account
	 * @return A DTO containing user information, accounts and transactions
	 * @throws ResponseStatusException with HTTP 404 NOT_FOUND if no accounts are
	 *                                 found for the phone number
	 */
	@Override
	public Mono<UserAccountsTransactionsDTO> getUserAccountsTransactionsByPhoneNumber(String phoneNumber,
			int transactionLimit) {
		log.debug("Getting user accounts and transactions by phone number: {}", phoneNumber);

		return accountRepository.findByUserPhoneNumber(phoneNumber).collectList().flatMap(accounts -> {
			if (accounts.isEmpty()) {
				return Mono.error(new AccountNotFoundException("No accounts found for phone number: " + phoneNumber));
			}

			// Get the first account to extract user information
			Account firstAccount = accounts.get(0);

			// Create a list of Monos, each containing an account with its transactions
			List<Mono<AccountWithTransactionsDTO>> accountsWithTransactions = accounts.stream().map(account -> {
				Mono<AccountResponseDTO> accountDto = Mono.just(accountMapper.toDto(account));

				Flux<TransactionResponseDTO> transactions = transactionService
						.getTransactionsByAccountId(account.getId(), transactionLimit);

				return Mono.zip(accountDto, transactions.collectList()).map(tuple -> AccountWithTransactionsDTO
						.builder().account(tuple.getT1()).transactions(tuple.getT2()).build());
			}).collect(Collectors.toList());

			// Combine all the Monos into a single Mono<List<AccountWithTransactionsDTO>>
			return Flux.fromIterable(accountsWithTransactions).flatMap(mono -> mono).collectList()
					.map(accountsWithTxs -> UserAccountsTransactionsDTO.builder().userId(firstAccount.getUserId())
							.userDni(firstAccount.getUserDni()).userPhoneNumber(firstAccount.getUserPhoneNumber())
							.accounts(accountsWithTxs).build());
		}).doOnSuccess(dto -> log.info("Successfully retrieved user accounts and transactions for phone number: {}",
				phoneNumber));
	}
}
