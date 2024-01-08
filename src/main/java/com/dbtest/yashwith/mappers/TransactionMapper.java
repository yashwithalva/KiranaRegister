package com.dbtest.yashwith.mappers;

import com.dbtest.yashwith.entities.Transaction;
import com.dbtest.yashwith.model.transaction.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "role", source = "role")
    @Mapping(target = "transactionType", source = "transactionType")
    @Mapping(target = "originalAmount", source = "originalAmount")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "amount", source = "amount")
    Transaction dtoToTransaction(TransactionDto transactionDto);

    @Mapping(target = "role", source = "role")
    @Mapping(target = "transactionType", source = "transactionType")
    @Mapping(target = "originalAmount", source = "originalAmount")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "amount", source = "amount")
    TransactionDto transactionToDto(Transaction transaction);
}
