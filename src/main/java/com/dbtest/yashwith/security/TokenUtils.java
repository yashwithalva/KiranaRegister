package com.dbtest.yashwith.security;

import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.enums.Role;
import com.dbtest.yashwith.model.user.TokenPayload;
import com.dbtest.yashwith.utils.SystemUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenUtils {

    private static final String KIRANA_CLAIMS_KEY = "https://kirana.app/jwt/claims";

    @Value("${auth.jwt.access.expiration.time}")
    private long accessTokenExpirationTime;

    @Value("${auth.jwt.admin.expiration.time}")
    private long adminTokenExpirationTime;

    @Value("${auth.jwt.refresh.expiration.time}")
    private long refreshTokenExpirationTime;

    @Value("${auth.jwt.access.secret.old}")
    private String oldSecret;

    @Value("${auth.jwt.access.secret.new}")
    private String newSecret;

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * Extract userId from jwt.
     *
     * @param token - jwt
     * @return userId
     */
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration time of the jwt
     *
     * @param token - jwt
     * @return Date time at which token expires.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Get token issued data.
     *
     * @param token - jwt
     * @return Date format
     */
    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    /**
     * Extract session id stored in claims
     *
     * @param token - jwt
     * @return String representing sessionId.
     */
    public String extractSessionId(String token) {
        return (String) getClaims(token).get("sid");
    }

    /**
     * Template function to extract any claim TODO: Learn the method implementation
     *
     * @param token - jwt
     * @param claimsResolver - Function for resolving claim
     * @return
     * @param <T> - Claim Type
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Get all claims from TokenPayload.
     *
     * @param token - jwt
     * @return TokenPayload
     */
    public TokenPayload getUserFromTokenPayload(String token) {
        try {
            ObjectMapper mapper = SystemUtils.getInstance().getObjectMapper();
            String userId = extractUserId(token);
            Map<String, Object> claims = getClaims(token);
            TokenPayload tokenPayload = mapper.convertValue(claims, TokenPayload.class);
            tokenPayload.setUserId(userId);
            return tokenPayload;
        } catch (Exception e) {
            log.error(e.toString());
        }

        return new TokenPayload();
    }

    /**
     * Check if token is valid.
     *
     * @param token - jwt
     * @param userDetails - UserDetails
     * @return boolean True if token is valid.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userId = extractUserId(token);
        return (userId.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Is token expired
     *
     * @param token - jwt
     * @return boolean True if token expired.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Do Not Use It. Can be used for generating token with custom claims.
     *
     * @param extraClaims - Additional claim.
     * @param userDetails - User details
     * @return jwt - generated token with claims.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + adminTokenExpirationTime))
                .signWith(SignatureAlgorithm.HS256, newSecret.getBytes())
                .compact();
    }

    /**
     * Create a token for the Role User
     *
     * @param user  User information
     * @param sessionId  ObjectId of the refreshTokenMongo
     * @return jwt token with claims
     */
    public String createToken(User user, String sessionId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationTime);
        return Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(new Date())
                .setIssuer(env)
                .setExpiration(expiryDate)
                .addClaims(addMetaDataToClaim(user, Role.USER, sessionId))
                .signWith(SignatureAlgorithm.HS512, newSecret.getBytes())
                .compact();
    }

    /**
     * Create token with any role.
     *
     * @param user - User information
     * @param role - Role availed by the user.
     * @param sessionId - ObjectId of the refreshToken.
     * @return
     */
    public String createTokenWithRole(User user, Role role, String sessionId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + adminTokenExpirationTime);
        return Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(new Date())
                .setIssuer(env)
                .setExpiration(expiryDate)
                .setClaims(addMetaDataToClaim(user, role, sessionId))
                .signWith(SignatureAlgorithm.HS512, newSecret.getBytes())
                .compact();
    }

    /**
     * Additional Metadata added under the key name of KIRAN_CLAIMS reference: Hasura authorization
     * payload.
     *
     * @param user - user for whom token is being created
     * @param role - Role of the user
     * @param sessionId - ObjectId of the RefreshTokenMongo
     * @return String to Object Map.
     */
    Map<String, Object> addMetaDataToClaim(User user, Role role, String sessionId) {
        var metaData = new HashMap<String, Object>();
        TokenPayload tokenPayload = new TokenPayload();
        tokenPayload.setUserId(user.getId());
        tokenPayload.setEmail(user.getEmail());
        tokenPayload.setPhoneNumber(user.getPhoneNumber());
        tokenPayload.setRole(role);
        tokenPayload.setAllowedRoles(user.getRoles());
        tokenPayload.setSid(sessionId);
        ObjectMapper mapper = SystemUtils.getInstance().getObjectMapper();
        Map<String, Object> claims =
                mapper.convertValue(tokenPayload, new TypeReference<Map<String, Object>>() {});
        metaData.put(KIRANA_CLAIMS_KEY, claims);
        return metaData;
    }

    private Claims extractAllClaims(String token) {
        return parseAndValidate(token).getBody();
    }

    public Jws<Claims> parseAndValidate(String token) {
        try {
            return Jwts.parser().setSigningKey(newSecret.getBytes()).parseClaimsJws(token);
        } catch (SignatureException e) {
            return Jwts.parser().setSigningKey(oldSecret.getBytes()).parseClaimsJws(token);
        }
    }

    /**
     * get Claims and Kirana Claims.
     *
     * @param token - jwt
     * @return All claims in Map format
     */
    public Map<String, Object> getClaims(String token) {
        Jws<Claims> userClaims;
        var claims = new HashMap<String, Object>();
        try {
            userClaims = parseAndValidate(token);
            claims = (HashMap<String, Object>) userClaims.getBody().get(KIRANA_CLAIMS_KEY);
        } catch (ExpiredJwtException ex) {
            // can be removed later
            claims = (HashMap<String, Object>) ex.getClaims().get(KIRANA_CLAIMS_KEY);
        }
        return claims;
    }
}
