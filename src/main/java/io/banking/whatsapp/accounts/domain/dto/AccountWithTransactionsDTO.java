package io.banking.whatsapp.accounts.domain.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object that contains account information and its transactions.
 * Used to provide a comprehensive view of an account with its transaction
 * history.
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
@Schema(description = "Account information with its transactions")
public class AccountWithTransactionsDTO {

	@Schema(description = "Account information")
	private AccountResponseDTO account;

	@Schema(description = "List of transactions for this account")
	private List<TransactionResponseDTO> transactions;
}