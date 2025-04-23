package io.banking.whatsapp.accounts.domain.dto;

import java.math.BigDecimal;

import io.banking.whatsapp.accounts.domain.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for transaction requests. Contains all the fields
 * required to create a new transaction.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Transaction request information")
public class TransactionRequestDTO {

	@NotBlank(message = "Account ID is required")
	@Schema(description = "Account ID", example = "60f1a5b3e8c7f12345678902", required = true)
	private String accountId;

	@NotNull(message = "Transaction type is required")
	@Schema(description = "Transaction type", example = "DEPOSIT", required = true)
	private TransactionType type;

	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be positive")
	@Schema(description = "Transaction amount", example = "500.00", required = true)
	private BigDecimal amount;

	@Schema(description = "Transaction description", example = "Salary deposit")
	private String description;

	@Schema(description = "Transaction reference", example = "DEP123")
	private String reference;
}