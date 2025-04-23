package io.banking.whatsapp.accounts.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.banking.whatsapp.accounts.domain.Transaction;
import io.banking.whatsapp.accounts.domain.TransactionStatus;
import io.banking.whatsapp.accounts.domain.dto.TransactionRequestDTO;
import io.banking.whatsapp.accounts.domain.dto.TransactionResponseDTO;
import io.banking.whatsapp.accounts.exception.AccountNotFoundException;
import io.banking.whatsapp.accounts.exception.InsufficientFundsException;
import io.banking.whatsapp.accounts.exception.InvalidTransactionTypeException;
import io.banking.whatsapp.accounts.exception.TransactionNotFoundException;
import io.banking.whatsapp.accounts.mapper.TransactionMapper;
import io.banking.whatsapp.accounts.repository.AccountRepository;
import io.banking.whatsapp.accounts.repository.TransactionRepository;
import io.banking.whatsapp.accounts.service.TransactionService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the TransactionService interface. Provides business logic
 * for transaction operations.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
	private final AccountRepository accountRepository;
	private final TransactionMapper transactionMapper;
	private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

	/**
	 * Creates a new transaction and updates the account balance accordingly. The
	 * method validates the account exists and has sufficient funds for withdrawals
	 * and transfers.
	 *
	 * @param request the transaction data to create
	 * @return a Mono containing the created transaction
	 * @throws ResponseStatusException if the account is not found or has
	 *                                 insufficient funds
	 */
	@Override
	public Mono<TransactionResponseDTO> createTransaction(TransactionRequestDTO request) {
		log.debug("Creating transaction for account ID: {}", request.getAccountId());

		return accountRepository.findById(request.getAccountId())
				.switchIfEmpty(Mono.error(AccountNotFoundException.withId(request.getAccountId()))).flatMap(account -> {
					Transaction transaction = Transaction.builder().accountId(request.getAccountId())
							.type(request.getType()).amount(request.getAmount()).description(request.getDescription())
							.reference(request.getReference()).transactionDate(LocalDateTime.now())
							.status(TransactionStatus.COMPLETED).build();

					// Update account balance based on transaction type
					switch (request.getType()) {
					case DEPOSIT:
						account.setBalance(account.getBalance().add(request.getAmount()));
						break;
					case WITHDRAWAL:
						if (account.getBalance().compareTo(request.getAmount()) < 0) {
							return Mono.error(InsufficientFundsException.forWithdrawal(request.getAmount(),
									account.getBalance()));
						}
						account.setBalance(account.getBalance().subtract(request.getAmount()));
						break;
					case TRANSFER:
						if (account.getBalance().compareTo(request.getAmount()) < 0) {
							return Mono.error(
									InsufficientFundsException.forTransfer(request.getAmount(), account.getBalance()));
						}
						account.setBalance(account.getBalance().subtract(request.getAmount()));
						break;
					default:
						return Mono.error(
								new InvalidTransactionTypeException("Invalid transaction type: " + request.getType()));
					}

					// Update the account first, then save the transaction
					return accountRepository.save(account).then(transactionRepository.save(transaction))
							.map(transactionMapper::toDto).doOnSuccess(dto -> log
									.info("Transaction created successfully for account: {}", dto.getAccountId()));
				});
	}

	/**
	 * Retrieves a transaction by its ID.
	 * 
	 * @param id the ID of the transaction to retrieve
	 * @return a Mono containing the transaction if found
	 * @throws ResponseStatusException if the transaction is not found
	 */
	@Override
	public Mono<TransactionResponseDTO> getTransactionById(String id) {
		log.debug("Getting transaction by ID: {}", id);

		return transactionRepository.findById(id).switchIfEmpty(Mono.error(TransactionNotFoundException.withId(id)))
				.map(transactionMapper::toDto).doOnSuccess(dto -> log.debug("Found transaction: {}", dto));
	}

	/**
	 * Retrieves the latest transactions for an account, ordered by transaction date
	 * descending. The method validates the account exists before retrieving its
	 * transactions.
	 *
	 * @param accountId the ID of the account to get transactions for
	 * @param limit     the maximum number of transactions to return
	 * @return a Flux of transactions for the account
	 * @throws ResponseStatusException if the account is not found
	 */
	@Override
	public Flux<TransactionResponseDTO> getTransactionsByAccountId(String accountId, int limit) {
		log.debug("Getting transactions for account ID: {} (limit: {})", accountId, limit);

		return accountRepository.findById(accountId)
				.switchIfEmpty(Mono.error(AccountNotFoundException.withId(accountId))).flatMapMany(account -> {
					PageRequest pageRequest = PageRequest.of(0, limit);
					return transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId, pageRequest)
							.map(transactionMapper::toDto).doOnComplete(
									() -> log.debug("Completed fetching transactions for account ID: {}", accountId));
				});
	}
}