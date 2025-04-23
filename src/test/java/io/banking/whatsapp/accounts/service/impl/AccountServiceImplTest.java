package io.banking.whatsapp.accounts.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.banking.whatsapp.accounts.domain.Account;
import io.banking.whatsapp.accounts.domain.AccountStatus;
import io.banking.whatsapp.accounts.domain.AccountType;
import io.banking.whatsapp.accounts.domain.Transaction;
import io.banking.whatsapp.accounts.domain.TransactionStatus;
import io.banking.whatsapp.accounts.domain.TransactionType;
import io.banking.whatsapp.accounts.domain.dto.AccountRequestDTO;
import io.banking.whatsapp.accounts.domain.dto.AccountResponseDTO;
import io.banking.whatsapp.accounts.domain.dto.TransactionResponseDTO;
import io.banking.whatsapp.accounts.domain.dto.UserAccountsTransactionsDTO;
import io.banking.whatsapp.accounts.exception.AccountNotFoundException;
import io.banking.whatsapp.accounts.exception.DuplicateAccountException;
import io.banking.whatsapp.accounts.mapper.AccountMapper;
import io.banking.whatsapp.accounts.mapper.TransactionMapper;
import io.banking.whatsapp.accounts.repository.AccountRepository;
import io.banking.whatsapp.accounts.repository.TransactionRepository;
import io.banking.whatsapp.accounts.service.TransactionService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private AccountMapper accountMapper;

	@Mock
	private TransactionService transactionService;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private TransactionMapper transactionMapper;

	@InjectMocks
	private AccountServiceImpl accountService;

	private Account testAccount;
	private AccountResponseDTO testAccountResponseDTO;
	private AccountRequestDTO testAccountRequestDTO;
	private Transaction testTransaction;
	private TransactionResponseDTO testTransactionResponseDTO;

	@BeforeEach
	void setUp() {
		// Setup test account
		testAccount = Account.builder().id("acc123").accountNumber("1234567890").accountType(AccountType.SAVINGS)
				.balance(BigDecimal.valueOf(1000)).currency("USD").userId("user123").userDni("12345678")
				.userPhoneNumber("+1234567890").status(AccountStatus.ACTIVE).createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now()).build();

		testAccountResponseDTO = AccountResponseDTO.builder().id("acc123").accountNumber("1234567890")
				.accountType(AccountType.SAVINGS).balance(BigDecimal.valueOf(1000)).currency("USD").userId("user123")
				.userDni("12345678").userPhoneNumber("+1234567890").status(AccountStatus.ACTIVE)
				.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

		testAccountRequestDTO = AccountRequestDTO.builder().accountNumber("1234567890").accountType(AccountType.SAVINGS)
				.balance(BigDecimal.valueOf(1000)).currency("USD").userId("user123").userDni("12345678")
				.userPhoneNumber("+1234567890").status(AccountStatus.ACTIVE).build();

		// Setup test transaction
		testTransaction = Transaction.builder().id("tx123").accountId("acc123").type(TransactionType.DEPOSIT)
				.amount(BigDecimal.valueOf(500)).description("Test deposit").reference("REF123")
				.transactionDate(LocalDateTime.now()).status(TransactionStatus.COMPLETED).build();

		testTransactionResponseDTO = TransactionResponseDTO.builder().id("tx123").accountId("acc123")
				.type(TransactionType.DEPOSIT).amount(BigDecimal.valueOf(500)).description("Test deposit")
				.reference("REF123").transactionDate(LocalDateTime.now()).status(TransactionStatus.COMPLETED).build();
	}

	@Test
	void createAccount_Success() {
		// Given
		when(accountRepository.findByAccountNumber(anyString())).thenReturn(Mono.empty());
		when(accountMapper.toEntity(any(AccountRequestDTO.class))).thenReturn(testAccount);
		when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(testAccount));
		when(accountMapper.toDto(any(Account.class))).thenReturn(testAccountResponseDTO);

		// When
		Mono<AccountResponseDTO> result = accountService.createAccount(testAccountRequestDTO);

		// Then
		StepVerifier.create(result).expectNext(testAccountResponseDTO).verifyComplete();

		verify(accountRepository).findByAccountNumber(testAccountRequestDTO.getAccountNumber());
		verify(accountMapper).toEntity(testAccountRequestDTO);
		verify(accountRepository).save(testAccount);
		verify(accountMapper).toDto(testAccount);
	}

	@Test
	void createAccount_AccountNumberAlreadyExists() {
		// Given
		AccountRequestDTO request = new AccountRequestDTO();
		request.setAccountNumber("1234567890");

		Account existingAccount = new Account();
		existingAccount.setAccountNumber("1234567890");

		when(accountRepository.findByAccountNumber(request.getAccountNumber())).thenReturn(Mono.just(existingAccount));

		// When
		Mono<AccountResponseDTO> result = accountService.createAccount(request);

		// Then
		StepVerifier.create(result).expectErrorMatches(throwable -> throwable instanceof DuplicateAccountException
				&& throwable.getMessage().contains("1234567890")).verify();

		verify(accountRepository).findByAccountNumber("1234567890");
		verify(accountRepository, never()).save(any(Account.class));
	}

	@Test
	void getAccountById_Success() {
		// Given
		when(accountRepository.findById(anyString())).thenReturn(Mono.just(testAccount));
		when(accountMapper.toDto(any(Account.class))).thenReturn(testAccountResponseDTO);

		// When
		Mono<AccountResponseDTO> result = accountService.getAccountById("acc123");

		// Then
		StepVerifier.create(result).expectNext(testAccountResponseDTO).verifyComplete();

		verify(accountRepository).findById("acc123");
		verify(accountMapper).toDto(testAccount);
	}

	@Test
	void getAccountById_whenAccountNotFound_shouldThrowAccountNotFoundException() {
		// Arrange
		String id = "non-existent-id";
		when(accountRepository.findById(id)).thenReturn(Mono.empty());

		// Act & Assert
		StepVerifier.create(accountService.getAccountById(id)).expectErrorMatches(
				throwable -> throwable instanceof AccountNotFoundException && throwable.getMessage().contains(id))
				.verify();
	}

	@Test
	void getAccountByNumber_whenAccountNotFound_shouldThrowAccountNotFoundException() {
		// Arrange
		String accountNumber = "non-existent-number";
		when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Mono.empty());

		// Act & Assert
		StepVerifier.create(accountService.getAccountByNumber(accountNumber))
				.expectErrorMatches(throwable -> throwable instanceof AccountNotFoundException
						&& throwable.getMessage().contains(accountNumber))
				.verify();
	}

	@Test
	void createAccount_whenAccountNumberExists_shouldThrowDuplicateAccountException() {
		// Arrange
		AccountRequestDTO request = new AccountRequestDTO();
		request.setAccountNumber("existing-number");

		Account existingAccount = new Account();
		existingAccount.setAccountNumber(request.getAccountNumber());

		when(accountRepository.findByAccountNumber(request.getAccountNumber())).thenReturn(Mono.just(existingAccount));

		// Act & Assert
		StepVerifier.create(accountService.createAccount(request))
				.expectErrorMatches(throwable -> throwable instanceof DuplicateAccountException
						&& throwable.getMessage().contains(request.getAccountNumber()))
				.verify();
	}

	@Test
	void updateAccount_whenAccountNotFound_shouldThrowAccountNotFoundException() {
		// Arrange
		String id = "non-existent-id";
		AccountRequestDTO request = new AccountRequestDTO();

		when(accountRepository.findById(id)).thenReturn(Mono.empty());

		// Act & Assert
		StepVerifier.create(accountService.updateAccount(id, request)).expectErrorMatches(
				throwable -> throwable instanceof AccountNotFoundException && throwable.getMessage().contains(id))
				.verify();
	}

	@Test
	void deleteAccount_whenAccountNotFound_shouldThrowAccountNotFoundException() {
		// Arrange
		String id = "non-existent-id";
		when(accountRepository.findById(id)).thenReturn(Mono.empty());

		// Act & Assert
		StepVerifier.create(accountService.deleteAccount(id)).expectErrorMatches(
				throwable -> throwable instanceof AccountNotFoundException && throwable.getMessage().contains(id))
				.verify();
	}

	@Test
	void getAccountTransactions_whenAccountNotFound_shouldThrowAccountNotFoundException() {
		// Arrange
		String accountId = "non-existent-id";
		when(accountRepository.findById(accountId)).thenReturn(Mono.empty());

		// Act & Assert
		StepVerifier.create(accountService.getAccountTransactions(accountId, 10))
				.expectErrorMatches(throwable -> throwable instanceof AccountNotFoundException
						&& throwable.getMessage().contains(accountId))
				.verify();
	}

	@Test
	void getUserAccountsTransactionsByPhoneNumber_whenNoAccountsFound_shouldThrowAccountNotFoundException() {
		// Arrange
		String phoneNumber = "non-existent-phone";
		when(accountRepository.findByUserPhoneNumber(phoneNumber)).thenReturn(Flux.empty());

		// Act & Assert
		StepVerifier.create(accountService.getUserAccountsTransactionsByPhoneNumber(phoneNumber, 10))
				.expectErrorMatches(throwable -> throwable instanceof AccountNotFoundException
						&& throwable.getMessage().contains(phoneNumber))
				.verify();
	}

	@Test
	void getUserAccountsTransactionsByPhoneNumber_NoAccountsFound() {
		// Given
		String phoneNumber = "+9999999999";
		when(accountRepository.findByUserPhoneNumber(phoneNumber)).thenReturn(Flux.empty());

		// When
		Mono<UserAccountsTransactionsDTO> result = accountService.getUserAccountsTransactionsByPhoneNumber(phoneNumber,
				10);

		// Then
		StepVerifier.create(result).expectErrorMatches(throwable -> throwable instanceof AccountNotFoundException
				&& throwable.getMessage().contains(phoneNumber)).verify();

		verify(accountRepository).findByUserPhoneNumber(phoneNumber);
	}
}