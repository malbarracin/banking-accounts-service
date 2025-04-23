package io.banking.whatsapp.accounts.mapper;

import org.mapstruct.Mapper;

import io.banking.whatsapp.accounts.domain.Transaction;
import io.banking.whatsapp.accounts.domain.dto.TransactionResponseDTO;

/**
 * Mapper interface for converting between Transaction entities and DTOs. Uses
 * MapStruct for automatic implementation.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {

	/**
	 * Converts a Transaction entity to a TransactionResponseDTO.
	 *
	 * @param transaction the transaction entity to convert
	 * @return the corresponding DTO representation
	 */
	TransactionResponseDTO toDto(Transaction transaction);
}