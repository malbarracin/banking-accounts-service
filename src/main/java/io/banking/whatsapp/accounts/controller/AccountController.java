package io.banking.whatsapp.accounts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.banking.whatsapp.accounts.domain.dto.AccountRequestDTO;
import io.banking.whatsapp.accounts.domain.dto.AccountResponseDTO;
import io.banking.whatsapp.accounts.domain.dto.UserAccountsTransactionsDTO;
import io.banking.whatsapp.accounts.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
@Tag(name = "Accounts", description = "API for managing bank accounts")
public class AccountController {

	private final AccountService accountService;

	/**
	 * Creates a new bank account.
	 *
	 * @param accountRequestDTO The account data to create
	 * @return The created account
	 */
	@Operation(summary = "Create a new bank account", description = "Creates a new bank account with the provided information")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Account created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class), examples = @ExampleObject(value = "{\"id\":\"60f1a5b3e8c7f12345678901\",\"accountNumber\":\"1234567890\",\"accountType\":\"SAVINGS\",\"balance\":1000.00,\"currency\":\"USD\",\"userId\":\"user123\",\"userDni\":\"12345678\",\"userPhoneNumber\":\"+1234567890\",\"status\":\"ACTIVE\",\"createdAt\":\"2023-07-16T10:30:00\",\"updatedAt\":\"2023-07-16T10:30:00\"}"))),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\":\"Invalid account data\",\"details\":[\"Account number is required\"]}"))) })
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<AccountResponseDTO> createAccount(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Account data to create", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountRequestDTO.class), examples = @ExampleObject(value = "{\"accountNumber\":\"1234567890\",\"accountType\":\"SAVINGS\",\"balance\":1000.00,\"currency\":\"USD\",\"userId\":\"user123\",\"userDni\":\"12345678\",\"userPhoneNumber\":\"+1234567890\"}"))) @RequestBody AccountRequestDTO accountRequestDTO) {
		return accountService.createAccount(accountRequestDTO);
	}

	/**
	 * Retrieves an account by its ID.
	 *
	 * @param id The ID of the account to retrieve
	 * @return The account with the specified ID
	 */
	@Operation(summary = "Get account by ID", description = "Retrieves a bank account by its unique identifier")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Account found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class), examples = @ExampleObject(value = "{\"id\":\"60f1a5b3e8c7f12345678901\",\"accountNumber\":\"1234567890\",\"accountType\":\"SAVINGS\",\"balance\":1000.00,\"currency\":\"USD\",\"userId\":\"user123\",\"userDni\":\"12345678\",\"userPhoneNumber\":\"+1234567890\",\"status\":\"ACTIVE\",\"createdAt\":\"2023-07-16T10:30:00\",\"updatedAt\":\"2023-07-16T10:30:00\"}"))),
			@ApiResponse(responseCode = "404", description = "Account not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\":\"Account not found\",\"details\":[\"No account found with ID: 60f1a5b3e8c7f12345678901\"]}"))) })
	@GetMapping("/{id}")
	public Mono<AccountResponseDTO> getAccountById(
			@Parameter(description = "ID of the account to retrieve", example = "60f1a5b3e8c7f12345678901", required = true) @PathVariable String id) {
		return accountService.getAccountById(id);
	}

	/**
	 * Retrieves an account by its account number.
	 *
	 * @param accountNumber The account number to search for
	 * @return The account with the specified account number
	 */
	@Operation(summary = "Get account by account number", description = "Retrieves a bank account by its unique account number")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Account found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class), examples = @ExampleObject(value = "{\"id\":\"60f1a5b3e8c7f12345678901\",\"accountNumber\":\"1234567890\",\"accountType\":\"SAVINGS\",\"balance\":1000.00,\"currency\":\"USD\",\"userId\":\"user123\",\"userDni\":\"12345678\",\"userPhoneNumber\":\"+1234567890\",\"status\":\"ACTIVE\",\"createdAt\":\"2023-07-16T10:30:00\",\"updatedAt\":\"2023-07-16T10:30:00\"}"))),
			@ApiResponse(responseCode = "404", description = "Account not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\":\"Account not found\",\"details\":[\"No account found with account number: 1234567890\"]}"))) })
	@GetMapping("/number/{accountNumber}")
	public Mono<AccountResponseDTO> getAccountByNumber(
			@Parameter(description = "Account number to search for", example = "1234567890", required = true) @PathVariable String accountNumber) {
		return accountService.getAccountByNumber(accountNumber);
	}

	/**
	 * Retrieves all accounts belonging to a specific user.
	 *
	 * @param userId The ID of the user to find accounts for
	 * @return A flux of all accounts belonging to the user
	 */
	@Operation(summary = "Get accounts by user ID", description = "Retrieves all bank accounts belonging to a specific user")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "List of accounts found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class), examples = @ExampleObject(value = "[{\"id\":\"60f1a5b3e8c7f12345678901\",\"accountNumber\":\"1234567890\",\"accountType\":\"SAVINGS\",\"balance\":1000.00,\"currency\":\"USD\",\"userId\":\"user123\",\"userDni\":\"12345678\",\"userPhoneNumber\":\"+1234567890\",\"status\":\"ACTIVE\",\"createdAt\":\"2023-07-16T10:30:00\",\"updatedAt\":\"2023-07-16T10:30:00\"}]"))) })
	@GetMapping("/user/{userId}")
	public Flux<AccountResponseDTO> getAccountsByUserId(
			@Parameter(description = "User ID to find accounts for", example = "user123", required = true) @PathVariable String userId) {
		return accountService.getAccountsByUserId(userId);
	}

	/**
	 * Retrieves all accounts belonging to a user with the specified DNI.
	 *
	 * @param dni The DNI (National ID) of the user to find accounts for
	 * @return A flux of all accounts belonging to the user with the given DNI
	 */
	@Operation(summary = "Get accounts by user DNI", description = "Retrieves all bank accounts belonging to a user with the specified DNI (National ID)")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "List of accounts found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class), examples = @ExampleObject(value = "[{\"id\":\"60f1a5b3e8c7f12345678901\",\"accountNumber\":\"1234567890\",\"accountType\":\"SAVINGS\",\"balance\":1000.00,\"currency\":\"USD\",\"userId\":\"user123\",\"userDni\":\"12345678\",\"userPhoneNumber\":\"+1234567890\",\"status\":\"ACTIVE\",\"createdAt\":\"2023-07-16T10:30:00\",\"updatedAt\":\"2023-07-16T10:30:00\"}]"))) })
	@GetMapping("/user/dni/{dni}")
	public Flux<AccountResponseDTO> getAccountsByUserDni(
			@Parameter(description = "User DNI to find accounts for", example = "12345678", required = true) @PathVariable String dni) {
		return accountService.getAccountsByUserDni(dni);
	}

	/**
	 * Retrieves all accounts belonging to a user with the specified phone number.
	 *
	 * @param phoneNumber The phone number of the user to find accounts for
	 * @return A flux of all accounts belonging to the user with the given phone
	 *         number
	 */
	@Operation(summary = "Get accounts by user phone number", description = "Retrieves all bank accounts belonging to a user with the specified phone number")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "List of accounts found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class), examples = @ExampleObject(value = "[{\"id\":\"60f1a5b3e8c7f12345678901\",\"accountNumber\":\"1234567890\",\"accountType\":\"SAVINGS\",\"balance\":1000.00,\"currency\":\"USD\",\"userId\":\"user123\",\"userDni\":\"12345678\",\"userPhoneNumber\":\"+1234567890\",\"status\":\"ACTIVE\",\"createdAt\":\"2023-07-16T10:30:00\",\"updatedAt\":\"2023-07-16T10:30:00\"}]"))) })
	@GetMapping("/user/phone/{phoneNumber}")
	public Flux<AccountResponseDTO> getAccountsByUserPhoneNumber(
			@Parameter(description = "User phone number to find accounts for", example = "+1234567890", required = true) @PathVariable String phoneNumber) {
		return accountService.getAccountsByUserPhoneNumber(phoneNumber);
	}

	/**
	 * Retrieves all accounts in the system.
	 *
	 * @return A flux of all accounts
	 */
	@Operation(summary = "Get all accounts", description = "Retrieves all bank accounts in the system")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "List of all accounts", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class), examples = @ExampleObject(value = "[{\"id\":\"60f1a5b3e8c7f12345678901\",\"accountNumber\":\"1234567890\",\"accountType\":\"SAVINGS\",\"balance\":1000.00,\"currency\":\"USD\",\"userId\":\"user123\",\"userDni\":\"12345678\",\"userPhoneNumber\":\"+1234567890\",\"status\":\"ACTIVE\",\"createdAt\":\"2023-07-16T10:30:00\",\"updatedAt\":\"2023-07-16T10:30:00\"}]"))) })
	@GetMapping
	public Flux<AccountResponseDTO> getAllAccounts() {
		return accountService.getAllAccounts();
	}

	/**
	 * Updates an existing account.
	 *
	 * @param id                The ID of the account to update
	 * @param accountRequestDTO The updated account data
	 * @return The updated account
	 */
	@Operation(summary = "Update an account", description = "Updates an existing bank account with the provided information")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Account updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class), examples = @ExampleObject(value = "{\"id\":\"60f1a5b3e8c7f12345678901\",\"accountNumber\":\"1234567890\",\"accountType\":\"SAVINGS\",\"balance\":1500.00,\"currency\":\"USD\",\"userId\":\"user123\",\"userDni\":\"12345678\",\"userPhoneNumber\":\"+1234567890\",\"status\":\"ACTIVE\",\"createdAt\":\"2023-07-16T10:30:00\",\"updatedAt\":\"2023-07-16T11:15:00\"}"))),
			@ApiResponse(responseCode = "404", description = "Account not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\":\"Account not found\",\"details\":[\"No account found with ID: 60f1a5b3e8c7f12345678901\"]}"))),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\":\"Invalid account data\",\"details\":[\"Balance must be positive\"]}"))) })
	@PutMapping("/{id}")
	public Mono<AccountResponseDTO> updateAccount(
			@Parameter(description = "ID of the account to update", example = "60f1a5b3e8c7f12345678901", required = true) @PathVariable String id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated account data", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountRequestDTO.class), examples = @ExampleObject(value = "{\"accountNumber\":\"1234567890\",\"accountType\":\"SAVINGS\",\"balance\":1500.00,\"currency\":\"USD\",\"userId\":\"user123\",\"userDni\":\"12345678\",\"userPhoneNumber\":\"+1234567890\"}"))) @RequestBody AccountRequestDTO accountRequestDTO) {
		return accountService.updateAccount(id, accountRequestDTO);
	}

	/**
	 * Deletes an account by its ID.
	 *
	 * @param id The ID of the account to delete
	 * @return A Mono<Void> indicating completion
	 */
	@Operation(summary = "Delete an account", description = "Deletes a bank account by its unique identifier")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Account not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\":\"Account not found\",\"details\":[\"No account found with ID: 60f1a5b3e8c7f12345678901\"]}"))) })
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteAccount(
			@Parameter(description = "ID of the account to delete", example = "60f1a5b3e8c7f12345678901", required = true) @PathVariable String id) {
		return accountService.deleteAccount(id);
	}

	/**
	 * Retrieves user information, accounts and transactions by phone number.
	 *
	 * @param phoneNumber The phone number of the user
	 * @param limit       Maximum number of transactions to return per account
	 *                    (optional, default 10)
	 * @return User information with accounts and their transactions
	 */
	@Operation(summary = "Get user information, accounts and transactions by phone number", description = "Retrieves user information, all accounts and their transactions by the user's phone number")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "User information with accounts and transactions found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountsTransactionsDTO.class), examples = @ExampleObject(value = "{\"userId\":\"user123\",\"userDni\":\"12345678\",\"userPhoneNumber\":\"+1234567890\",\"accounts\":[{\"account\":{\"id\":\"60f1a5b3e8c7f12345678901\",\"accountNumber\":\"1234567890\",\"accountType\":\"SAVINGS\",\"balance\":1000.00,\"currency\":\"USD\",\"userId\":\"user123\",\"userDni\":\"12345678\",\"userPhoneNumber\":\"+1234567890\",\"status\":\"ACTIVE\",\"createdAt\":\"2023-07-16T10:30:00\",\"updatedAt\":\"2023-07-16T10:30:00\"},\"transactions\":[{\"id\":\"60f1a5b3e8c7f12345678902\",\"accountId\":\"60f1a5b3e8c7f12345678901\",\"type\":\"DEPOSIT\",\"amount\":500.00,\"description\":\"Salary deposit\",\"reference\":\"DEP123\",\"transactionDate\":\"2023-07-16T10:35:00\",\"status\":\"COMPLETED\"}]}]}"))),
			@ApiResponse(responseCode = "404", description = "No accounts found for the phone number", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\":\"No accounts found for phone number: +1234567890\",\"details\":[]}"))) })
	@GetMapping("/user/phone/{phoneNumber}/complete")
	public Mono<UserAccountsTransactionsDTO> getUserAccountsTransactionsByPhoneNumber(
			@Parameter(description = "User phone number to find accounts and transactions for", example = "+1234567890", required = true) @PathVariable String phoneNumber,
			@Parameter(description = "Maximum number of transactions to return per account", example = "10") @RequestParam(defaultValue = "10") int limit) {
		return accountService.getUserAccountsTransactionsByPhoneNumber(phoneNumber, limit);
	}
}