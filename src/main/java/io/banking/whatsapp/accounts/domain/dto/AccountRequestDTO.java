package io.banking.whatsapp.accounts.domain.dto;

import java.math.BigDecimal;

import io.banking.whatsapp.accounts.domain.AccountStatus;
import io.banking.whatsapp.accounts.domain.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for account creation and update requests. Contains all
 * the fields that can be modified by clients.
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
@Schema(description = "Data transfer object for account creation and update requests")
public class AccountRequestDTO {

	@Schema(description = "Account number", example = "1234567890", required = true)
	private String accountNumber;

	@Schema(description = "Account type", example = "SAVINGS", required = true, allowableValues = { "SAVINGS",
			"CHECKING", "CREDIT" })
	private AccountType accountType;

	@Schema(description = "Account balance", example = "1000.00", required = true)
	private BigDecimal balance;

	@Schema(description = "Currency code (ISO 4217)", example = "USD", required = true)
	private String currency;

	@Schema(description = "User ID that owns this account", example = "user123", required = true)
	private String userId;

	@Schema(description = "User's DNI (National ID)", example = "12345678", required = true)
	private String userDni;

	@Schema(description = "User's phone number", example = "+1234567890", required = true)
	private String userPhoneNumber;

	@Schema(description = "Account status", example = "ACTIVE", required = false, allowableValues = { "ACTIVE",
			"INACTIVE", "CLOSED", "BLOCKED" })
	private AccountStatus status;
}