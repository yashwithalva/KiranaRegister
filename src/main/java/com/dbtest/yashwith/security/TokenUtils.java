package com.dbtest.yashwith.security;

import com.dbtest.yashwith.model.user.TokenPayload;
import com.dbtest.yashwith.utils.SystemUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenUtils {

    private static final String KIRANA_CLAIMS_KEY = "https://kirana.app/jwt/claims";

    @Value("${auth.jwt.access.expiration.time}")
    private long accessTokenExpirationTime;

    @Value("${auth.jwt.admin.expiration.time}")
    private long adminTokenExpirationTime;

    @Value("${auth.jwt.refresh.expiration.time}")
    private long refreshTokenExpirationTime;

    @Value("${auth.jwt.switch.user.expiration.time}")
    private long switchUserExpirationTime;

    @Value("${auth.jwt.access.secret.old}")
    private String oldSecret;

    @Value("${auth.jwt.access.secret.new}")
    private String newSecret;

    @Value("${spring.profiles.active}")
    private String env;

    @Value("${auth.jwt.hasura.version}")
    private String jwtHasuraVersion;

    @Value("${auth.jwt.version}")
    private String jwtVersion;

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
    public Date getIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
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
            e.printStackTrace();
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
     * Generate Token with additional claims
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

    // TODO AUTH 1: generateAdminToken()
    // TODO AUTH 2: generateUserToken()

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

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
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
