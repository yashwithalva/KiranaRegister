package com.dbtest.yashwith.repository;

import com.dbtest.yashwith.entities.Transaction;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    public List<Transaction> findByCreatedAtBetween(Date from, Date to);
}
