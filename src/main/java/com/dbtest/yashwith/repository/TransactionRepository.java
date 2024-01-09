package com.dbtest.yashwith.repository;

import com.dbtest.yashwith.entities.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {}
