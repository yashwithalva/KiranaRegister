package com.dbtest.yashwith.controller;

import com.dbtest.yashwith.model.transaction.TransactionCreateRequest;
import com.dbtest.yashwith.model.transaction.TransactionDto;
import com.dbtest.yashwith.service.TransactionService;
import java.util.List;
import lombok.AllArgsConstructor;
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
     *
     * @return create a new transaction
     */
    @PostMapping("/makePayment")
    public ResponseEntity<String> createNewTransaction(
            @RequestBody TransactionCreateRequest transactionCreateRequest,
            @RequestHeader("Authorization") String jwtToken) {
        String transactionId =
                transactionService.createNewTransaction(transactionCreateRequest, jwtToken);
        if (!StringUtils.hasText(transactionId)) {
            return ResponseEntity.badRequest().body("Couldn't create a new Transaction");
        } else {
            return ResponseEntity.ok().body(transactionId);
        }
    }
}
