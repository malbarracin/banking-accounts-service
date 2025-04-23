package io.banking.whatsapp.accounts.domain.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object that contains user information, accounts and their
 * transactions. Used to provide a comprehensive view of a user's financial
 * information.
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
@Schema(description = "User information with accounts and transactions")
public class UserAccountsTransactionsDTO {

	@Schema(description = "User ID", example = "user123")
	private String userId;

	@Schema(description = "User's DNI (National ID)", example = "12345678")
	private String userDni;

	@Schema(description = "User's phone number", example = "+1234567890")
	private String userPhoneNumber;

	@Schema(description = "List of accounts with their transactions")
	private List<AccountWithTransactionsDTO> accounts;
}