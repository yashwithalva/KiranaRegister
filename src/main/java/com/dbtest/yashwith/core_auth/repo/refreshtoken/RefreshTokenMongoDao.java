package com.dbtest.yashwith.core_auth.repo.refreshtoken;

import com.dbtest.yashwith.core_auth.entities.RefreshTokenMongo;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenMongoDao {
    private final RefreshTokenMongoRepo refreshTokenMongoRepo;

    @Autowired
    public RefreshTokenMongoDao(RefreshTokenMongoRepo refreshTokenMongoRepo) {
        this.refreshTokenMongoRepo = refreshTokenMongoRepo;
    }

    /**
     * Saves a refresh token entity in db
     *
     * @param refreshToken the entity to save
     * @return the saved entity with id
     */
    public RefreshTokenMongo save(RefreshTokenMongo refreshToken) {
        return refreshTokenMongoRepo.save(refreshToken);
    }

    /**
     * checks if a user exists by id and user id
     *
     * @param id the search entity
     * @param userId the user id
     * @return a boolean value indicating if a user exists by given params
     */
    public boolean existsByIdAndUserId(String id, String userId) {
        return refreshTokenMongoRepo.existsByIdAndUserId(id, userId);
    }

    public Optional<RefreshTokenMongo> getRefreshTokenByUserId(String userId) {
        return refreshTokenMongoRepo.findByUserId(userId);
    }

    /**
     * finds a list of refresh tokens in mongo by phone number
     *
     * @param phoneNumber the phone number of the user to search for
     * @return a list of refresh tokens in mongo db
     */
    public List<RefreshTokenMongo> findByPhoneNumber(String phoneNumber) {
        return refreshTokenMongoRepo.findByPhoneNumber(phoneNumber);
    }

    /**
     * finds a list of refresh tokens in mongo by email
     *
     * @param email the email of the user to search for
     * @return a list of refresh token in mongo db
     */
    public List<RefreshTokenMongo> findByEmail(String email) {
        return refreshTokenMongoRepo.findByEmail(email);
    }

    /**
     * finds a list of refresh tokens according to user id and email.
     *
     * @param userId the user id to search for
     * @param email the email of the user to search for
     * @return a list of refresh tokens in mongo db
     */
    public List<RefreshTokenMongo> findByUserIdAndEmail(String userId, String email) {
        return refreshTokenMongoRepo.findByUserIdAndEmail(userId, email);
    }

    /**
     * finds a list of refresh tokens according to user id and phoneNumber
     *
     * @param userId the user id to search for
     * @param phoneNumber the app id to search for
     * @return a list of refresh tokens in mongo to search for
     */
    public List<RefreshTokenMongo> findByUserIdAndPhoneNumber(String userId, String phoneNumber) {
        return refreshTokenMongoRepo.findByUserIdAndPhoneNumber(userId, phoneNumber);
    }

    /**
     * finds a list of refresh tokens according to user id, app id, device id
     *
     * @param userId the user's id
     * @param email the user's email
     * @param phoneNumber the user's phoneNumber
     * @return a list of refresh tokens in mongo db
     */
    public List<RefreshTokenMongo> findByUserIdAndEmailAndPhoneNumber(
            String userId, String email, String phoneNumber) {
        return refreshTokenMongoRepo.findByUserIdAndEmailAndPhoneNumber(userId, email, phoneNumber);
    }

    /**
     * finds an Optional of refresh tokens according to token
     *
     * @param token the search entity
     * @return an optional list, empty if nothing found
     */
    public Optional<RefreshTokenMongo> findByToken(String token) {
        return refreshTokenMongoRepo.findByToken(token);
    }

    /**
     * checks if an entity exists by phone number
     *
     * @param phoneNumber the phone number to search for
     * @return a boolean value
     */
    public Boolean existsByPhoneNumber(String phoneNumber) {
        return refreshTokenMongoRepo.existsByPhoneNumber(phoneNumber);
    }

    /**
     * checks if an entity exists by email
     *
     * @param email the phone number to search for
     * @return a boolean value
     */
    public Boolean existsByEmail(String email) {
        return refreshTokenMongoRepo.existsByEmail(email);
    }

    /**
     * checks if an entity exists by phone number and email
     *
     * @param phoneNumber the phone number to search for
     * @param email the user's email
     * @return a boolean value
     */
    public Boolean existsByPhoneNumberAndEmail(String phoneNumber, String email) {
        return refreshTokenMongoRepo.existsByPhoneNumberAndEmail(phoneNumber, email);
    }

    /**
     * checks if an entity exists by phone number and email
     *
     * @param userId userId of the user
     * @param phoneNumber the user's phoneNumber
     * @return a boolean value
     */
    public Boolean existsByUserIdAndPhoneNumber(String userId, String phoneNumber) {
        return refreshTokenMongoRepo.existsByUserIdAndPhoneNumber(userId, phoneNumber);
    }

    /**
     * checks if an entity exists by user id and device id
     *
     * @param userId the user id to search for
     * @return a boolean value
     */
    public Boolean existsByUserId(String userId) {
        return refreshTokenMongoRepo.existsByUserId(userId);
    }

    /**
     * Count all distinct phoneNumbers greater than a specific date.
     *
     * @param phoneNumber the search param
     * @param date Date
     * @return an integer value specifying the unique count of all device ids registered by phone
     *     number
     */
    public int countDistinctPhoneNumberAndTimeoutGreaterThan(String phoneNumber, Date date) {
        return refreshTokenMongoRepo.countDistinctPhoneNumberAndTimeoutGreaterThan(
                phoneNumber, date);
    }

    /**
     * Count all distinct email greater than a specific date.
     *
     * @param email the search param
     * @param date Date
     * @return an integer value specifying the unique count of all device ids registered by phone
     *     number
     */
    public int countDistinctEmailAndTimeoutGreaterThan(String email, Date date) {
        return refreshTokenMongoRepo.countDistinctEmailAndTimeoutGreaterThan(email, date);
    }

    /**
     * Count all distinct userId greater than a specific date.
     *
     * @param userId the search param
     * @param date Date
     * @return an integer value specifying the unique count of all device ids registered by phone
     *     number
     */
    public int countDistinctUserIdAndTimeoutGreaterThan(String userId, Date date) {
        return refreshTokenMongoRepo.countDistinctUserIdAndTimeoutGreaterThan(userId, date);
    }

    public int countByUserIDIn(String userId) {
        return refreshTokenMongoRepo.countByUserIdIn(userId);
    }

    /**
     * finds an optional list by id
     *
     * @param id the search param
     * @return an optional list, empty if nothing found
     */
    public Optional<RefreshTokenMongo> findById(String id) {
        return refreshTokenMongoRepo.findById(id);
    }

    /**
     * finds an optional list by id
     *
     * @param userId the search param
     * @return an optional list, empty if nothing found
     */
    public Optional<RefreshTokenMongo> findByUserId(String userId) {
        return refreshTokenMongoRepo.findByUserId(userId);
    }

    /**
     * deletes an entity from db
     *
     * @param refreshTokenMongo the entity to be deleted
     */
    public void delete(RefreshTokenMongo refreshTokenMongo) {
        refreshTokenMongoRepo.deleteBy(refreshTokenMongo);
    }

    /**
     * finds a list of all RefreshTokenMongo by user id
     *
     * @param id the user id to search
     * @return an ArrayList
     */
    public List<RefreshTokenMongo> findAllByUserId(String id) {
        return refreshTokenMongoRepo.findAllByUserId(id);
    }
}
