package io.banking.whatsapp.accounts.domain.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for error responses. Provides a standardized format for
 * API error responses.
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
public class ErrorResponseDTO {
	/**
	 * HTTP status code as string
	 */
	private String status;

	/**
	 * Error message describing what went wrong
	 */
	private String message;

	/**
	 * API endpoint path that generated the error
	 */
	private String path;

	/**
	 * Timestamp when the error occurred
	 */
	private LocalDateTime timestamp;
}