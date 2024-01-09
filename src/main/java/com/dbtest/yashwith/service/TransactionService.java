package com.dbtest.yashwith.service;

import com.dbtest.yashwith.entities.Transaction;
import com.dbtest.yashwith.mappers.TransactionMapper;
import com.dbtest.yashwith.model.transaction.ExchangeRates;
import com.dbtest.yashwith.model.transaction.TransactionCreateRequest;
import com.dbtest.yashwith.model.transaction.TransactionDto;
import com.dbtest.yashwith.repository.TransactionRepository;
import com.dbtest.yashwith.security.TokenUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {
    private TransactionRepository transactionRepository;
    private TokenUtils tokenUtils;
    private RestTemplate restTemplate;

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

    /**
     * Create a new Transaction.
     *
     * @param transactionCreateRequest Request for creating a new Transaction
     * @param jwtToken Jwt token from the header
     * @return
     */
    public String createNewTransaction(
            TransactionCreateRequest transactionCreateRequest, String jwtToken) {
        Transaction transaction =
                TransactionMapper.INSTANCE.transactionRequestToTransaction(
                        transactionCreateRequest);
        String token = jwtToken.substring(7);
        transaction.setUserId(tokenUtils.extractUserId(token));
        double exchangeRate = getCurrentExchangeForCurrency(transaction.getCurrency().toString());
        transaction.setAmount(transaction.getOriginalAmount() / exchangeRate);
        log.debug(transaction.toString());
        return transactionRepository.save(transaction).getUserId();
    }

    public double getCurrentExchangeForCurrency(String countryCode) {
        String apiUrl = "https://api.fxratesapi.com/latest?base=INR";
        ExchangeRates exchangeRates = restTemplate.getForObject(apiUrl, ExchangeRates.class);

        if (exchangeRates != null) {
            return exchangeRates.getExchangeRates(countryCode);
        } else {
            return 0.0;
        }
    }
}
