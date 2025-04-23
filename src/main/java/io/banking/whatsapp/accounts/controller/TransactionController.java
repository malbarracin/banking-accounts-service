package io.banking.whatsapp.accounts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.banking.whatsapp.accounts.domain.dto.ErrorResponseDTO;
import io.banking.whatsapp.accounts.domain.dto.TransactionRequestDTO;
import io.banking.whatsapp.accounts.domain.dto.TransactionResponseDTO;
import io.banking.whatsapp.accounts.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST Controller for managing Transaction operations. This controller provides
 * endpoints for creating and retrieving transactions.
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transaction management APIs")
public class TransactionController {

	private final TransactionService transactionService;
	private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

	/**
	 * Creates a new transaction.
	 *
	 * @param request The transaction data to create
	 * @return The created transaction information
	 */
	@Operation(summary = "Create a new transaction", description = "Creates a new transaction with the provided information")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Transaction created successfully", content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<TransactionResponseDTO> createTransaction(@Valid @RequestBody TransactionRequestDTO request) {
		return transactionService.createTransaction(request);
	}

	/**
	 * Retrieves a transaction by its ID.
	 *
	 * @param id The ID of the transaction to retrieve
	 * @return The transaction information if found
	 */
	@Operation(summary = "Get transaction by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Transaction found", content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@GetMapping("/{id}")
	public Mono<TransactionResponseDTO> getTransactionById(
			@Parameter(description = "Transaction ID", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable String id) {
		return transactionService.getTransactionById(id);
	}

	/**
	 * Retrieves the latest transactions for an account.
	 *
	 * @param accountId The ID of the account to get transactions for
	 * @param limit     The maximum number of transactions to return (optional,
	 *                  defaults to 10)
	 * @return A flux of transactions for the account
	 */
	@Operation(summary = "Get account transactions")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "List of transactions found"),
			@ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })
	@GetMapping("/account/{accountId}")
	public Flux<TransactionResponseDTO> getTransactionsByAccountId(
			@Parameter(description = "Account ID", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable String accountId,
			@Parameter(description = "Maximum number of transactions to return", example = "10") @RequestParam(defaultValue = "10") int limit) {
		return transactionService.getTransactionsByAccountId(accountId, limit);
	}
}