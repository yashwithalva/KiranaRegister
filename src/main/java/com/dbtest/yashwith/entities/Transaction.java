package com.dbtest.yashwith.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@Document("transaction")
@Data
public class Transaction {
    @Id private String id;
    private Date timeStamp;
    private String role;
    private String transactionType;
    private float amount;
}
