package com.dbtest.yashwith.model.transaction;

import com.dbtest.yashwith.enums.Currency;
import com.dbtest.yashwith.enums.Role;
import com.dbtest.yashwith.enums.TransactionType;
import lombok.Data;

@Data
public class TransactionDto {
    private Role role;
    private TransactionType transactionType;
    private double originalAmount;
    private Currency currency;
    private double amount;
}
