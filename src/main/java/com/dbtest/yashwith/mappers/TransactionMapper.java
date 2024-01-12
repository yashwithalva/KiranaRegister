package com.dbtest.yashwith.mappers;

import com.dbtest.yashwith.entities.Transaction;
import com.dbtest.yashwith.model.transaction.ReportDTO;
import com.dbtest.yashwith.model.transaction.TransactionCreateRequest;
import com.dbtest.yashwith.model.transaction.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    /**
     * Convert Transaction DTO to Transaction.
     * @param transactionDto DTO object of mongo transaction data.
     * @return Transaction
     */
    @Mapping(target = "role", source = "role")
    @Mapping(target = "transactionType", source = "transactionType")
    @Mapping(target = "originalAmount", source = "originalAmount")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "amount", source = "amount")
    Transaction dtoToTransaction(TransactionDto transactionDto);


    /**
     * Converts transaction to DTO
     * @param transaction Transaction data stored in database.
     * @return Transaction DTO.
     */
    @Mapping(target = "role", source = "role")
    @Mapping(target = "transactionType", source = "transactionType")
    @Mapping(target = "originalAmount", source = "originalAmount")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "amount", source = "amount")
    TransactionDto transactionToDto(Transaction transaction);

    /**
     * Transaction Creation request to Transaction Entity.
     * @param transactionCreateRequest Request for creating a new Transaction.
     * @return
     */
    @Mapping(target = "role", source = "role")
    @Mapping(target = "transactionType", source = "transactionType")
    @Mapping(target = "originalAmount", source = "originalAmount")
    @Mapping(target = "currency", source = "currency")
    Transaction transactionRequestToTransaction(TransactionCreateRequest transactionCreateRequest);

}
