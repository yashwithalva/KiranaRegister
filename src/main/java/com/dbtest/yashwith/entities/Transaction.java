package com.dbtest.yashwith.entities;

import com.dbtest.yashwith.enums.Currency;
import com.dbtest.yashwith.enums.Role;
import com.dbtest.yashwith.enums.TransactionType;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("transaction")
@Data
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(
        def = "{'originalAmount' : 0.0, 'currency' : Currency.USD}",
        name = "amount_currency")
public class Transaction extends DateAudit {
    @Id private String id;
    private Role role;
    private TransactionType transactionType;
    private float originalAmount;
    private Currency currency;
    private float amount;
    private String userId;
}
