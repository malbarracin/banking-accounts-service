package io.banking.whatsapp.accounts.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.banking.whatsapp.accounts.domain.TransactionStatus;
import io.banking.whatsapp.accounts.domain.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for transaction responses. Contains all transaction
 * information returned to clients.
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
@Schema(description = "Transaction information")
public class TransactionResponseDTO {

	@Schema(description = "Transaction ID", example = "60f1a5b3e8c7f12345678901")
	private String id;

	@Schema(description = "Account ID", example = "60f1a5b3e8c7f12345678902")
	private String accountId;

	@Schema(description = "Transaction type", example = "DEPOSIT")
	private TransactionType type;

	@Schema(description = "Transaction amount", example = "500.00")
	private BigDecimal amount;

	@Schema(description = "Transaction description", example = "Salary deposit")
	private String description;

	@Schema(description = "Transaction reference", example = "DEP123")
	private String reference;

	@Schema(description = "Transaction date", example = "2023-07-16T10:30:00")
	private LocalDateTime transactionDate;

	@Schema(description = "Transaction status", example = "COMPLETED")
	private TransactionStatus status;
}