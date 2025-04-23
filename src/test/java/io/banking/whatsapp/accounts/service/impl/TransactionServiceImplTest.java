package io.banking.whatsapp.accounts.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
import org.springframework.data.domain.PageRequest;

import io.banking.whatsapp.accounts.domain.Account;
import io.banking.whatsapp.accounts.domain.AccountStatus;
import io.banking.whatsapp.accounts.domain.AccountType;
import io.banking.whatsapp.accounts.domain.Transaction;
import io.banking.whatsapp.accounts.domain.TransactionStatus;
import io.banking.whatsapp.accounts.domain.TransactionType;
import io.banking.whatsapp.accounts.domain.dto.TransactionRequestDTO;
import io.banking.whatsapp.accounts.domain.dto.TransactionResponseDTO;
import io.banking.whatsapp.accounts.exception.AccountNotFoundException;
import io.banking.whatsapp.accounts.exception.InsufficientFundsException;
import io.banking.whatsapp.accounts.exception.InvalidTransactionTypeException;
import io.banking.whatsapp.accounts.exception.TransactionNotFoundException;
import io.banking.whatsapp.accounts.mapper.TransactionMapper;
import io.banking.whatsapp.accounts.repository.AccountRepository;
import io.banking.whatsapp.accounts.repository.TransactionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private TransactionMapper transactionMapper;

	@InjectMocks
	private TransactionServiceImpl transactionService;

	private Account testAccount;
	private Transaction testTransaction;
	private TransactionRequestDTO testTransactionRequestDTO;
	private TransactionResponseDTO testTransactionResponseDTO;

	@BeforeEach
	void setUp() {
		// Setup test account
		testAccount = Account.builder().id("acc123").accountNumber("1234567890").accountType(AccountType.SAVINGS)
				.balance(BigDecimal.valueOf(1000)).currency("USD").userId("user123").userDni("12345678")
				.userPhoneNumber("+1234567890").status(AccountStatus.ACTIVE).createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now()).build();

		// Setup test transaction
		testTransaction = Transaction.builder().id("tx123").accountId("acc123").type(TransactionType.DEPOSIT)
				.amount(BigDecimal.valueOf(500)).description("Test deposit").reference("REF123")
				.transactionDate(LocalDateTime.now()).status(TransactionStatus.COMPLETED).build();

		// Setup test transaction request DTO
		testTransactionRequestDTO = TransactionRequestDTO.builder().accountId("acc123").type(TransactionType.DEPOSIT)
				.amount(BigDecimal.valueOf(500)).description("Test deposit").reference("REF123").build();

		// Setup test transaction response DTO
		testTransactionResponseDTO = TransactionResponseDTO.builder().id("tx123").accountId("acc123")
				.type(TransactionType.DEPOSIT).amount(BigDecimal.valueOf(500)).description("Test deposit")
				.reference("REF123").transactionDate(LocalDateTime.now()).status(TransactionStatus.COMPLETED).build();
	}

	@Test
	void createTransaction_DepositSuccess() {
		// Given
		when(accountRepository.findById(anyString())).thenReturn(Mono.just(testAccount));
		when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(testAccount));
		when(transactionRepository.save(any(Transaction.class))).thenReturn(Mono.just(testTransaction));
		when(transactionMapper.toDto(any(Transaction.class))).thenReturn(testTransactionResponseDTO);

		// When
		Mono<TransactionResponseDTO> result = transactionService.createTransaction(testTransactionRequestDTO);

		// Then
		StepVerifier.create(result).expectNext(testTransactionResponseDTO).verifyComplete();

		verify(accountRepository).findById("acc123");
		verify(accountRepository).save(any(Account.class));
		verify(transactionRepository).save(any(Transaction.class));
		verify(transactionMapper).toDto(any(Transaction.class));
	}

	@Test
	void createTransaction_WithdrawalSuccess() {
		// Given
		testTransactionRequestDTO.setType(TransactionType.WITHDRAWAL);
		testTransaction.setType(TransactionType.WITHDRAWAL);
		testTransactionResponseDTO.setType(TransactionType.WITHDRAWAL);

		when(accountRepository.findById(anyString())).thenReturn(Mono.just(testAccount));
		when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(testAccount));
		when(transactionRepository.save(any(Transaction.class))).thenReturn(Mono.just(testTransaction));
		when(transactionMapper.toDto(any(Transaction.class))).thenReturn(testTransactionResponseDTO);

		// When
		Mono<TransactionResponseDTO> result = transactionService.createTransaction(testTransactionRequestDTO);

		// Then
		StepVerifier.create(result).expectNext(testTransactionResponseDTO).verifyComplete();

		verify(accountRepository).findById("acc123");
		verify(accountRepository).save(any(Account.class));
		verify(transactionRepository).save(any(Transaction.class));
		verify(transactionMapper).toDto(any(Transaction.class));
	}

	@Test
	void createTransaction_WithdrawalInsufficientFunds() {
		// Given
		testTransactionRequestDTO.setType(TransactionType.WITHDRAWAL);
		testTransactionRequestDTO.setAmount(BigDecimal.valueOf(2000)); // More than account balance

		when(accountRepository.findById(anyString())).thenReturn(Mono.just(testAccount));

		// When
		Mono<TransactionResponseDTO> result = transactionService.createTransaction(testTransactionRequestDTO);

		// Then
		StepVerifier.create(result).expectErrorMatches(throwable -> throwable instanceof InsufficientFundsException
				&& throwable.getMessage().contains("Insufficient funds for withdrawal")).verify();

		verify(accountRepository).findById("acc123");
		verify(accountRepository, never()).save(any(Account.class));
		verify(transactionRepository, never()).save(any(Transaction.class));
	}

	@Test
	void createTransaction_TransferSuccess() {
		// Given
		testTransactionRequestDTO.setType(TransactionType.TRANSFER);
		testTransaction.setType(TransactionType.TRANSFER);
		testTransactionResponseDTO.setType(TransactionType.TRANSFER);

		when(accountRepository.findById(anyString())).thenReturn(Mono.just(testAccount));
		when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(testAccount));
		when(transactionRepository.save(any(Transaction.class))).thenReturn(Mono.just(testTransaction));
		when(transactionMapper.toDto(any(Transaction.class))).thenReturn(testTransactionResponseDTO);

		// When
		Mono<TransactionResponseDTO> result = transactionService.createTransaction(testTransactionRequestDTO);

		// Then
		StepVerifier.create(result).expectNext(testTransactionResponseDTO).verifyComplete();

		verify(accountRepository).findById("acc123");
		verify(accountRepository).save(any(Account.class));
		verify(transactionRepository).save(any(Transaction.class));
		verify(transactionMapper).toDto(any(Transaction.class));
	}

	@Test
	void createTransaction_whenAccountNotFound_shouldThrowAccountNotFoundException() {
		// Given
		TransactionRequestDTO request = new TransactionRequestDTO();
		request.setAccountId("non-existent-id");
		request.setType(TransactionType.DEPOSIT);
		request.setAmount(new BigDecimal("100.00"));

		when(accountRepository.findById(request.getAccountId())).thenReturn(Mono.empty());

		// Act & Assert
		StepVerifier.create(transactionService.createTransaction(request))
				.expectErrorMatches(throwable -> throwable instanceof AccountNotFoundException
						&& throwable.getMessage().contains(request.getAccountId()))
				.verify();
	}

	@Test
	void createTransaction_whenWithdrawalWithInsufficientFunds_shouldThrowInsufficientFundsException() {
		// Given
		String accountId = "account-id";
		TransactionRequestDTO request = new TransactionRequestDTO();
		request.setAccountId(accountId);
		request.setType(TransactionType.WITHDRAWAL);
		request.setAmount(new BigDecimal("1000.00"));

		Account account = new Account();
		account.setId(accountId);
		account.setBalance(new BigDecimal("500.00"));

		when(accountRepository.findById(accountId)).thenReturn(Mono.just(account));

		// Act & Assert
		StepVerifier.create(transactionService.createTransaction(request))
				.expectErrorMatches(throwable -> throwable instanceof InsufficientFundsException
						&& throwable.getMessage().contains("Insufficient funds for withdrawal"))
				.verify();
	}

	@Test
	void createTransaction_whenTransferWithInsufficientFunds_shouldThrowInsufficientFundsException() {
		// Given
		String accountId = "account-id";
		TransactionRequestDTO request = new TransactionRequestDTO();
		request.setAccountId(accountId);
		request.setType(TransactionType.TRANSFER);
		request.setAmount(new BigDecimal("1000.00"));

		Account account = new Account();
		account.setId(accountId);
		account.setBalance(new BigDecimal("500.00"));

		when(accountRepository.findById(accountId)).thenReturn(Mono.just(account));

		// Act & Assert
		StepVerifier.create(transactionService.createTransaction(request))
				.expectErrorMatches(throwable -> throwable instanceof InsufficientFundsException
						&& throwable.getMessage().contains("Insufficient funds for transfer"))
				.verify();
	}

	@Test
	void createTransaction_whenInvalidTransactionType_shouldThrowInvalidTransactionTypeException() {
		// Given
		String accountId = "account-id";
		TransactionRequestDTO request = new TransactionRequestDTO();
		request.setAccountId(accountId);
		// Setting an invalid transaction type that will cause the switch to default
		request.setType(TransactionType.FEE); // Using FEE as an example of a type not handled in the switch
		request.setAmount(new BigDecimal("100.00"));

		Account account = new Account();
		account.setId(accountId);
		account.setBalance(new BigDecimal("500.00"));

		when(accountRepository.findById(accountId)).thenReturn(Mono.just(account));

		// Act & Assert
		StepVerifier.create(transactionService.createTransaction(request))
				.expectErrorMatches(throwable -> throwable instanceof InvalidTransactionTypeException
						&& throwable.getMessage().contains("Invalid transaction type"))
				.verify();
	}

	@Test
	void getTransactionById_whenTransactionNotFound_shouldThrowTransactionNotFoundException() {
		// Given
		String id = "non-existent-id";
		when(transactionRepository.findById(id)).thenReturn(Mono.empty());

		// Act & Assert
		StepVerifier.create(transactionService.getTransactionById(id)).expectErrorMatches(
				throwable -> throwable instanceof TransactionNotFoundException && throwable.getMessage().contains(id))
				.verify();
	}

	@Test
	void getTransactionsByAccountId_whenAccountNotFound_shouldThrowAccountNotFoundException() {
		// Given
		String accountId = "non-existent-id";
		when(accountRepository.findById(accountId)).thenReturn(Mono.empty());

		// Act & Assert
		StepVerifier.create(transactionService.getTransactionsByAccountId(accountId, 10))
				.expectErrorMatches(throwable -> throwable instanceof AccountNotFoundException
						&& throwable.getMessage().contains(accountId))
				.verify();
	}

	@Test
	void getTransactionById_Success() {
		// Given
		when(transactionRepository.findById(anyString())).thenReturn(Mono.just(testTransaction));
		when(transactionMapper.toDto(any(Transaction.class))).thenReturn(testTransactionResponseDTO);

		// When
		Mono<TransactionResponseDTO> result = transactionService.getTransactionById("tx123");

		// Then
		StepVerifier.create(result).expectNext(testTransactionResponseDTO).verifyComplete();

		verify(transactionRepository).findById("tx123");
		verify(transactionMapper).toDto(testTransaction);
	}

	@Test
	void getTransactionById_NotFound() {
		// Given
		when(transactionRepository.findById(anyString())).thenReturn(Mono.empty());

		// When
		Mono<TransactionResponseDTO> result = transactionService.getTransactionById("nonexistent");

		// Then
		StepVerifier.create(result).expectErrorMatches(throwable -> throwable instanceof TransactionNotFoundException
				&& throwable.getMessage().contains("nonexistent")).verify();

		verify(transactionRepository).findById("nonexistent");
	}

	@Test
	void getTransactionsByAccountId_Success() {
		// Given
		when(accountRepository.findById(anyString())).thenReturn(Mono.just(testAccount));
		when(transactionRepository.findByAccountIdOrderByTransactionDateDesc(anyString(), any(PageRequest.class)))
				.thenReturn(Flux.just(testTransaction));
		when(transactionMapper.toDto(any(Transaction.class))).thenReturn(testTransactionResponseDTO);

		// When
		Flux<TransactionResponseDTO> result = transactionService.getTransactionsByAccountId("acc123", 10);

		// Then
		StepVerifier.create(result).expectNext(testTransactionResponseDTO).verifyComplete();

		verify(accountRepository).findById("acc123");
		verify(transactionRepository).findByAccountIdOrderByTransactionDateDesc(eq("acc123"), any(PageRequest.class));
		verify(transactionMapper).toDto(testTransaction);
	}

	@Test
	void getTransactionsByAccountId_AccountNotFound() {
		// Given
		when(accountRepository.findById(anyString())).thenReturn(Mono.empty());

		// When
		Flux<TransactionResponseDTO> result = transactionService.getTransactionsByAccountId("nonexistent", 10);

		// Then
		StepVerifier.create(result).expectErrorMatches(throwable -> throwable instanceof AccountNotFoundException
				&& throwable.getMessage().contains("nonexistent")).verify();

		verify(accountRepository).findById("nonexistent");
		verify(transactionRepository, never()).findByAccountIdOrderByTransactionDateDesc(anyString(),
				any(PageRequest.class));
	}
}