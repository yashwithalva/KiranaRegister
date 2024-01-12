package com.dbtest.yashwith.repository;

import com.dbtest.yashwith.entities.Transaction;
import com.dbtest.yashwith.enums.TransactionType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    public List<Transaction> findByCreatedAtBetween(Date from, Date to);
}
