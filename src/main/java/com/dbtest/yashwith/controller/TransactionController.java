package com.dbtest.yashwith.controller;

import com.dbtest.yashwith.model.transaction.TransactionCreateRequest;
import com.dbtest.yashwith.model.transaction.TransactionDto;
import com.dbtest.yashwith.response.ApiResponse;
import com.dbtest.yashwith.service.TransactionService;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * Get all transactions.
     *
     * @return All transactions
     */
    @GetMapping("/")
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    /**
     * Create a new transaction.
     * @return create a new transaction
     */
    @PostMapping("/makePayment")
    public ResponseEntity<ApiResponse> createNewTransaction(
            @RequestBody TransactionCreateRequest transactionCreateRequest,
            @RequestHeader("Authorization") String jwtToken) {
        return ResponseEntity.ok(transactionService.createNewTransaction(transactionCreateRequest, jwtToken));
    }

    /**
     * Generate report of transactions between Dates
     * @param startDate - Start Date
     * @param toDate - End Date
     * @return Generate Report DTO
     */
    @GetMapping("/report")
    public ResponseEntity<ApiResponse> getReportBetweenDate(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate){
            return ResponseEntity.ok(transactionService.getReportBetweenDates(startDate, toDate));
    }
}
