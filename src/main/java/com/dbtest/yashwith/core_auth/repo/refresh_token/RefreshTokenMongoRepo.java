package com.dbtest.yashwith.core_auth.repo.refresh_token;

import com.dbtest.yashwith.core_auth.entities.RefreshTokenMongo;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

interface RefreshTokenMongoRepo extends MongoRepository<RefreshTokenMongo, String> {
    boolean existsByIdAndUserId(String id, String userId);

    Optional<RefreshTokenMongo> findByUserId(String userId);

    List<RefreshTokenMongo> findByPhoneNumber(String phoneNumber);

    List<RefreshTokenMongo> findByEmail(String email);

    List<RefreshTokenMongo> findAllByUserId(String userId);

    List<RefreshTokenMongo> findByUserIdAndPhoneNumber(String userId, String phoneNumber);

    List<RefreshTokenMongo> findByUserIdAndEmail(String userId, String email);

    List<RefreshTokenMongo> findByUserIdAndEmailAndPhoneNumber(
            String userId, String email, String phoneNumber);

    Optional<RefreshTokenMongo> findByToken(String token);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByEmail(String email);

    int countDistinctEmailByPhoneNumberAndTimeoutGreaterThan(
            String phoneNumber, Date date, String email);

    int countDistinctUserIdByPhoneNumberAndTimeoutGreaterThan(
            String phoneNumber, Date date, String userId);

    void deleteBy(RefreshTokenMongo refreshTokenMongo);

    boolean existsByUserIdAndPhoneNumber(String userId, String phoneNumber);

    boolean existsByPhoneNumberAndEmail(String phoneNumber, String email);

    boolean existsByUserId(String userId);
}
