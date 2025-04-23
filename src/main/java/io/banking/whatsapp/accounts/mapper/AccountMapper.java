package io.banking.whatsapp.accounts.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import io.banking.whatsapp.accounts.domain.Account;
import io.banking.whatsapp.accounts.domain.dto.AccountRequestDTO;
import io.banking.whatsapp.accounts.domain.dto.AccountResponseDTO;

/**
 * Mapper interface for converting between Account entities and DTOs. Uses
 * MapStruct for automatic implementation.
 *
 * @author Marcelo Alejandro Albarracín
 * @email marceloalejandro.albarracin@gmail.com
 * @version 1.0.0
 * @since 2024-03-19
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {

	/**
	 * Converts an AccountRequestDTO to an Account entity. Sets status to "ACTIVE"
	 * and initializes timestamps.
	 *
	 * @param dto the account request DTO to convert
	 * @return the corresponding Account entity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", constant = "ACTIVE")
	@Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
	@Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
	Account toEntity(AccountRequestDTO dto);

	/**
	 * Converts an Account entity to an AccountResponseDTO.
	 *
	 * @param account the account entity to convert
	 * @return the corresponding DTO representation
	 */
	AccountResponseDTO toDto(Account account);

	/**
	 * Updates an existing Account entity with data from an AccountRequestDTO.
	 * Preserves the id, status, and createdAt fields. Updates the updatedAt
	 * timestamp.
	 *
	 * @param dto     the account request DTO containing updated data
	 * @param account the existing account entity to update
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
	void updateEntity(AccountRequestDTO dto, @MappingTarget Account account);
}