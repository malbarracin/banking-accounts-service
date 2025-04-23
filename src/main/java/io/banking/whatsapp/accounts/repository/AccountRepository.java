package io.banking.whatsapp.accounts.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import io.banking.whatsapp.accounts.domain.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository interface for Account entities. Provides methods for CRUD
 * operations on accounts.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {

	/**
	 * Finds an account by its account number.
	 *
	 * @param accountNumber the account number to search for
	 * @return a Mono containing the account if found
	 */
	Mono<Account> findByAccountNumber(String accountNumber);

	/**
	 * Finds all accounts belonging to a specific user.
	 *
	 * @param userId the ID of the user to find accounts for
	 * @return a Flux of all accounts belonging to the user
	 */
	Flux<Account> findByUserId(String userId);

	/**
	 * Finds all accounts belonging to a user with the specified DNI.
	 *
	 * @param dni the DNI (National ID) of the user to find accounts for
	 * @return a Flux of all accounts belonging to the user with the specified DNI
	 */
	Flux<Account> findByUserDni(String dni);

	/**
	 * Finds all accounts belonging to a user with the specified phone number.
	 *
	 * @param phoneNumber the phone number of the user to find accounts for
	 * @return a Flux of all accounts belonging to the user with the specified phone
	 *         number
	 */
	Flux<Account> findByUserPhoneNumber(String phoneNumber);
}