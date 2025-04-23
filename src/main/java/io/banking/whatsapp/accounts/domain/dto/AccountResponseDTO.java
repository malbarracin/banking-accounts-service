package io.banking.whatsapp.accounts.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.banking.whatsapp.accounts.domain.AccountStatus;
import io.banking.whatsapp.accounts.domain.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for account responses. Contains all account information
 * returned to clients.
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
@Schema(description = "Data transfer object for account responses")
public class AccountResponseDTO {

	@Schema(description = "Account ID", example = "60f1a5b3e8c7f12345678901")
	private String id;

	@Schema(description = "Account number", example = "1234567890")
	private String accountNumber;

	@Schema(description = "Account type", example = "SAVINGS", allowableValues = { "SAVINGS", "CHECKING", "CREDIT" })
	private AccountType accountType;

	@Schema(description = "Account balance", example = "1000.00")
	private BigDecimal balance;

	@Schema(description = "Currency code (ISO 4217)", example = "USD")
	private String currency;

	@Schema(description = "User ID that owns this account", example = "user123")
	private String userId;

	@Schema(description = "User's DNI (National ID)", example = "12345678")
	private String userDni;

	@Schema(description = "User's phone number", example = "+1234567890")
	private String userPhoneNumber;

	@Schema(description = "Account status", example = "ACTIVE", allowableValues = { "ACTIVE", "INACTIVE", "CLOSED",
			"BLOCKED" })
	private AccountStatus status;

	@Schema(description = "Account creation timestamp", example = "2023-07-16T10:30:00")
	private LocalDateTime createdAt;

	@Schema(description = "Account last update timestamp", example = "2023-07-16T10:30:00")
	private LocalDateTime updatedAt;
}