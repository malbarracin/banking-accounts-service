package io.banking.whatsapp.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Banking Accounts Service. This service
 * provides APIs for managing bank accounts in the WhatsApp Banking system.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
@SpringBootApplication
public class BankingAccountsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingAccountsServiceApplication.class, args);
	}
}