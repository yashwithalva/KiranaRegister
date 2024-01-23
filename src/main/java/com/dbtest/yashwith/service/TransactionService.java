package com.dbtest.yashwith.service;

import com.dbtest.yashwith.entities.Transaction;
import com.dbtest.yashwith.enums.TransactionType;
import com.dbtest.yashwith.mappers.TransactionMapper;
import com.dbtest.yashwith.model.transaction.ExchangeRates;
import com.dbtest.yashwith.model.transaction.ReportDTO;
import com.dbtest.yashwith.model.transaction.TransactionCreateRequest;
import com.dbtest.yashwith.model.transaction.TransactionDto;
import com.dbtest.yashwith.repository.TransactionRepository;
import com.dbtest.yashwith.response.ApiResponse;
import com.dbtest.yashwith.security.TokenUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.yashwith.frameworks.service.RateLimitingService;

@Service
@Slf4j
public class TransactionService {
  
    private final TransactionRepository transactionRepository;
    private final TokenUtils tokenUtils;
    private final RestTemplate restTemplate;
    private final RateLimitingService rateLimitingService;

    public TransactionService(
            TransactionRepository transactionRepository,
            TokenUtils tokenUtils,
            RestTemplate restTemplate,
            RateLimitingService rateLimitingService) {
        this.transactionRepository = transactionRepository;
        this.restTemplate = restTemplate;
        this.tokenUtils = tokenUtils;
        this.rateLimitingService = rateLimitingService;
    }

    public ApiResponse getAllTransactions() {
        ApiResponse apiResponse = new ApiResponse();

        if (rateLimitingService.isRateLimitBreached("JWT")) {
            apiResponse.setErrorCode("429");
            apiResponse.setSuccess(false);
            apiResponse.setStatus("TOO MANY REQUESTS");
            return apiResponse;
        }

        List<TransactionDto> transactionDtos = new ArrayList<>();
        transactionRepository
                .findAll()
                .forEach(
                        transaction -> {
                            transactionDtos.add(
                                    TransactionMapper.INSTANCE.transactionToDto(transaction));
                        });
        apiResponse.setData(transactionDtos);
        apiResponse.setSuccess(true);
        apiResponse.setStatus("SUCCESS");
        apiResponse.setErrorCode("200");
        return apiResponse;
    }

    /**
     * Create a new Transaction.
     *
     * @param transactionCreateRequest Request for creating a new Transaction
     * @param jwtToken Jwt token from the header
     * @return
     */
    public ApiResponse createNewTransaction(
            TransactionCreateRequest transactionCreateRequest, String jwtToken) {
        ApiResponse apiResponse = new ApiResponse();

        Transaction transaction =
                TransactionMapper.INSTANCE.transactionRequestToTransaction(
                        transactionCreateRequest);

        String token = jwtToken.substring(7);
        transaction.setUserId(tokenUtils.extractUserId(token));
        double exchangeRate = getCurrentExchangeForCurrency(transaction.getCurrency().toString());
        transaction.setAmount(transaction.getOriginalAmount() / exchangeRate);
        transaction.setCreatedAt(new Date());
        ;
        log.debug(transaction.toString());
        String transactionId = transactionRepository.save(transaction).getId();
        System.out.println("Transaction Id: " + transactionId);

        if (StringUtils.hasText(transactionId)) {
            apiResponse.setSuccess(true);
            apiResponse.setErrorCode("201");
            apiResponse.setData(transactionId);
            apiResponse.setStatus("CREATED");
        } else {
            apiResponse.setSuccess(false);
            apiResponse.setErrorMessage("429");
            apiResponse.setStatus("TOO MANY REQUESTS");
        }

        return apiResponse;
    }

    /**
     * Generate report between start and end-date.
     *
     * @param startDate
     * @param endDate
     * @return reportDTO.
     */
    public ApiResponse getReportBetweenDates(Date startDate, Date endDate) {
        ApiResponse apiResponse = new ApiResponse();
        if (rateLimitingService.isRateLimitBreached("JWT")) {
            apiResponse.setErrorCode("429");
            apiResponse.setSuccess(false);
            apiResponse.setStatus("TOO MANY REQUESTS");
            return apiResponse;
        }

        ReportDTO reportDTO = new ReportDTO();
        List<Transaction> transactions =
                transactionRepository.findByCreatedAtBetween(startDate, endDate);
        reportDTO.setTotalDebit(getTotalCashFlow(transactions, TransactionType.DEBIT));
        reportDTO.setTotalCredit(getTotalCashFlow(transactions, TransactionType.CREDIT));
        reportDTO.setNetFlow(reportDTO.getTotalCredit() - reportDTO.getTotalDebit());

        apiResponse.setStatus("SUCCESS");
        apiResponse.setErrorCode("200");
        apiResponse.setData(reportDTO);
        return apiResponse;
    }

    /**
     * Generate detailed report between start and end-date
     *
     * @param startDate
     * @param endDate
     * @return DetailedReportDTO
     */
    public ApiResponse getDetailedReportBetween(Date startDate, Date endDate) {
        List<Transaction> transactions =
                transactionRepository.findByCreatedAtBetween(startDate, endDate);
        return null;
    }

    public double getCurrentExchangeForCurrency(String countryCode) {
        // TODO : Use this from application.properties
        String apiUrl = "https://api.fxratesapi.com/latest?base=INR";
        try{
            ExchangeRates exchangeRates = fixedRateService.getFixedRate(apiUrl);
            if (exchangeRates != null) {
                return exchangeRates.getExchangeRates(countryCode);
            } else {
                throw new RuntimeException("Unable to do currency conversion");
            }
        } catch (Exception e) {
            log.error("Invalid response or missing currency: {}", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private double getTotalCashFlow(List<Transaction> transactions, TransactionType type) {
        double answer = 0;
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getTransactionType() == type) {
                answer += transactions.get(i).getAmount();
            }
        }
        return answer;
    }
}
