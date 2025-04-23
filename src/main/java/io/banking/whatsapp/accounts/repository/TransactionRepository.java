package io.banking.whatsapp.accounts.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import io.banking.whatsapp.accounts.domain.Transaction;
import reactor.core.publisher.Flux;

/**
 * Repository interface for Transaction entities. Provides methods for CRUD
 * operations on transactions.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {

	/**
	 * Finds transactions by account ID, ordered by transaction date in descending
	 * order.
	 *
	 * @param accountId the ID of the account to find transactions for
	 * @param pageable  pagination information
	 * @return a Flux of transactions for the account
	 */
	Flux<Transaction> findByAccountIdOrderByTransactionDateDesc(String accountId, Pageable pageable);
}