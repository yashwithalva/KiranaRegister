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


// TODO: Handle multiple refresh token existing for a single user.
@Component
public class RefreshTokenUtil {
    @Value("${auth.jwt.refresh.expiration.time}")
    private long refreshTokenExpirationTime;

    @Value("${auth.jwt.access.expiration.time}")
    private long accessTokenExpirationTime;

    @Value("${auth.max.login.sessions}")
    private int maxLoginSessionDevices;

    /**
     * Save refresh token only in Mongo
     *
     * @return RefreshTokenModel with token and sessionId.
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
     * remove token from mongodb using the session id.
     *
     * @param id - represents sessionId.
     */
    public void removeTokenForLogout(String id) {
        Optional<RefreshTokenMongo> refreshTokenMongoOptional =
                SystemDaoHelper.getInstance().getRefreshTokenMongoDao().findById(id);
        if (refreshTokenMongoOptional.isEmpty()) {
            return;
        }
        // TODO: Remove it from database.
        SystemDaoHelper.getInstance()
                .getRefreshTokenMongoDao()
                .delete(refreshTokenMongoOptional.get());
        // Remove userSession.
        // If cached in REDIS remove using sessionId.
    }

    /**
     * remove all tokens for user by userId
     *
     * @param userId - userId for who the refresh token was created.
     */
    public void removeAllTokensForUser(String userId) {
        List<RefreshTokenMongo> refreshTokenMongoList =
                SystemDaoHelper.getInstance().getRefreshTokenMongoDao().findAllByUserId(userId);
        refreshTokenMongoList.forEach(
                refreshTokenMongo -> {
                    // Remove all user session from REDIS.
                    SystemDaoHelper.getInstance()
                            .getRefreshTokenMongoDao()
                            .delete(refreshTokenMongo);
                });
    }

    /**
     * Do not user. Jar had implementation with appId. remove token functionality for deletion
     *
     * @param userId - userId of the user.
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
     * TODO: Redis related. Refer to JAR and implement.
     *
     * @param userId - userId of the user
     * @param sessionId - sessionId
     */
    public void removeUserSession(String userId, String sessionId) {
        //
    }

    /**
     * Save refresh token only in Mongo
     *
     * @return
     */
    public RefreshTokenModel acessTokenSaveRefreshToken(
            String userId, String phoneNumber, String email) {
        String token = UUID.randomUUID().toString();
        String tokenHash = TokenHashUtil.getSHAString(token);
        RefreshTokenMongo refreshToken = new RefreshTokenMongo();
        refreshToken.setPhoneNumber(phoneNumber);
        refreshToken.setUserId(userId);
        refreshToken.setToken(tokenHash);
        refreshToken.setCreatedAt(new Date());
        refreshToken.setEmail(email);
        Date now = new Date();
        refreshToken.setTimeout(new DateTime(now).plus(refreshTokenExpirationTime).toDate());
        refreshToken = SystemDaoHelper.getInstance().getRefreshTokenMongoDao().save(refreshToken);
        RefreshTokenModel refreshTokenModel =
                new RefreshTokenModel(refreshToken.getToken(), refreshToken.getId());
        return refreshTokenModel;
    }

    /**
     * Checks if session is valid using refreshToken time and currentTime.
     *
     * @param userId
     * @return
     */
    public boolean isSessionValid(String userId) {
        Date currentDate = new Date();
        Optional<RefreshTokenMongo> refreshTokenMongo =
                SystemDaoHelper.getInstance()
                        .getRefreshTokenMongoDao()
                        .getRefreshTokenByUserId(userId);

        if (refreshTokenMongo.isEmpty()) {
            return false;
        } else {
            if (refreshTokenMongo.get().getTimeout().after(currentDate)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
