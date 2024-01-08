package com.dbtest.yashwith.repository;

import com.dbtest.yashwith.entities.User;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByFirstName(String firstname);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumberAndCountryCode(String countryCode, String phoneNumber);

    List<User> findAllByIdIn(List<String> id);

    Optional<User> findFirstByIdOrPhoneNumber(String id, String phoneNumber);

    List<User> findAllByPhoneNumberIn(List<String> phoneNumber);

    Integer countByPhoneNumberIn(List<String> phoneNumber);

    Page<User> findByCreatedAtBetween(Date startDate, Date endDate, Pageable pageable);
}
