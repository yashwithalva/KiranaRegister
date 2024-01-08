package com.dbtest.yashwith.service;

import com.dbtest.yashwith.entities.Transaction;
import com.dbtest.yashwith.mappers.TransactionMapper;
import com.dbtest.yashwith.model.transaction.TransactionDto;
import com.dbtest.yashwith.repository.TransactionRepository;
import com.dbtest.yashwith.security.TokenUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {
    private TransactionRepository transactionRepository;
    private TokenUtils tokenUtils;

    public List<TransactionDto> getAllTransactions() {
        List<TransactionDto> transactionDtos = new ArrayList<>();
        transactionRepository
                .findAll()
                .forEach(
                        transaction -> {
                            transactionDtos.add(
                                    TransactionMapper.INSTANCE.transactionToDto(transaction));
                        });
        return transactionDtos;
    }

    public String createNewTransaction(TransactionDto transactionDto, String jwtToken) {
        Transaction transaction = TransactionMapper.INSTANCE.dtoToTransaction(transactionDto);
        String token = jwtToken.substring(7);
        transaction.setUserId(tokenUtils.extractUserId(token));
        log.debug(transaction.toString());
        return transactionRepository.save(transaction).getUserId();
    }
}
