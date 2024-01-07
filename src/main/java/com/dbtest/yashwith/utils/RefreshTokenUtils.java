package com.dbtest.yashwith.utils;

import com.dbtest.yashwith.config.SystemDaoHelper;
import com.dbtest.yashwith.core_auth.entities.RefreshTokenMongo;
import com.dbtest.yashwith.core_auth.model.RefreshTokenModel;
// import org.joda.time.DateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenUtils {
    @Value("${auth.jwt.refresh.expiration.time}")
    private long refreshTokenExpirationTime;

    @Value("${auth.jwt.access.expiration.time}")
    private long accessTokenExpirationTime;

    @Value("${auth.max.login.sessions}")
    private int maxLoginSessionDevices;

    /**
     * Save refresh token only in Mongo
     *
     * @return
     */
    public RefreshTokenModel saveRefreshToken(String userId, String phoneNumber, String email) {
        String token = UUID.randomUUID().toString();
        String tokenHash = TokenHashUtil.getSHAString(token);
        RefreshTokenMongo refreshToken = new RefreshTokenMongo();

        refreshToken.setPhoneNumber(phoneNumber);
        refreshToken.setUserId(userId);
        refreshToken.setToken(tokenHash);
        refreshToken.setCreatedAt(new Date());
        Date now = new Date();
        refreshToken.setTimeout(new DateTime(now).plus(refreshTokenExpirationTime).toDate());
        refreshToken = SystemDaoHelper.getInstance().getRefreshTokenMongoDao().save(refreshToken);
        RefreshTokenModel refreshTokenModel =
                new RefreshTokenModel(refreshToken.getToken(), refreshToken.getId());
        return refreshTokenModel;
    }

    /**
     * remove token by refresh token id which is the session id in the token
     *
     * @param id
     */
    public void removeTokenForLogout(String id) {
        Optional<RefreshTokenMongo> refreshTokenMongoOptional =
                SystemDaoHelper.getInstance().getRefreshTokenMongoDao().findById(id);
        if (refreshTokenMongoOptional.isEmpty()) {
            return;
        }
        RefreshTokenMongo refreshTokenMongo = refreshTokenMongoOptional.get();
        // If cached in REDIS remove using sessionId.
    }

    /**
     * remove all tokens for user by userId
     *
     * @param userId
     */
    public void removeAllTokensForUser(String userId) {
        List<RefreshTokenMongo> refreshTokenMongoList =
                SystemDaoHelper.getInstance().getRefreshTokenMongoDao().findAllByUserId(userId);
        refreshTokenMongoList.forEach(
                refreshTokenMongo -> {
                    // Remove all users from REDIS.
                    SystemDaoHelper.getInstance()
                            .getRefreshTokenMongoDao()
                            .delete(refreshTokenMongo);
                });
    }

    /**
     * remove token functionality for deletion
     *
     * @param userId
     */
    public void removeTokenForDeletion(String userId) {
        List<RefreshTokenMongo> refreshTokenMongoList =
                SystemDaoHelper.getInstance().getRefreshTokenMongoDao().findAllByUserId(userId);

        refreshTokenMongoList.forEach(
                refreshTokenMongo -> {
                    SystemDaoHelper.getInstance()
                            .getRefreshTokenMongoDao()
                            .delete(refreshTokenMongo);
                    // Remove session from redis.
                });
    }

    /**
     * Save refresh token only in Mongo
     *
     * @return
     */
    public RefreshTokenModel acessTokenSaveRefreshToken(
            String userId, String phoneNumber, String deviceId, String authReqId) {
        String token = UUID.randomUUID().toString();
        String tokenHash = TokenHashUtil.getSHAString(token);
        RefreshTokenMongo refreshToken = new RefreshTokenMongo();
        refreshToken.setPhoneNumber(phoneNumber);
        refreshToken.setUserId(userId);
        refreshToken.setToken(tokenHash);
        refreshToken.setCreatedAt(new Date());
        Date now = new Date();
        refreshToken.setTimeout(new DateTime(now).plus(refreshTokenExpirationTime).toDate());
        refreshToken = SystemDaoHelper.getInstance().getRefreshTokenMongoDao().save(refreshToken);
        RefreshTokenModel refreshTokenModel =
                new RefreshTokenModel(refreshToken.getToken(), refreshToken.getId());
        return refreshTokenModel;
    }
}
