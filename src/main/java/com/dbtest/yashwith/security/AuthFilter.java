package com.dbtest.yashwith.security;

import com.dbtest.yashwith.exception.TokenException;
import com.dbtest.yashwith.response.ApiResponse;
import com.dbtest.yashwith.utils.DateUtil;
import com.dbtest.yashwith.utils.RefreshTokenUtil;
import com.dbtest.yashwith.utils.SystemUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;
    private final UserInfoService userInfoService;
    private final HttpServletRequest httpServletRequest;
    private final RefreshTokenUtil refreshTokenUtil;

    @Value("#{'${urls.exclude.filter}'.split(',')}")
    List<String> excludeFilter;

    @Value("${auth.session.id.date}")
    private String authSessionIdDate;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * Auth filter
     *
     * @see <a
     *     href="https://stackoverflow.com/questions/67424274/what-is-springs-securitycontext-behavior-in-terms-of-threads-or-different-reque">ThreadContext</a>
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = null;
        logger.debug("Running inside once per request AuthFilter : " + request.getRequestURI());
        try {
            jwt = getJwtFromHeader(request);
            if (StringUtils.hasText(jwt)) {
                if (tokenUtils.isTokenExpired(jwt)) {
                    log.info("invalid token : " + jwt);
                    throw new TokenException("InvalidToken", "Invalid token submitted", "403");
                }

                String userId = tokenUtils.extractUserId(jwt);
                ThreadContext.put("userId", userId);
                if (userId == null) {
                    log.info("invalid token : " + jwt);
                    throw new TokenException("InvalidToken", "Invalid userId in token", "403");
                }

                UserDetails userDetails = userInfoService.loadUserByUsername(jwt);
                UserInfo userInfo = (UserInfo) userDetails;

                // Can set addition details such as appVer, os

                Date sessionCheckDate =
                        new Date(DateUtil.getDateFromString(authSessionIdDate, "dd-MM-yyyy HH:mm"));

                // Eligible if extracted Jwt is > sessionCheckDate(3rd May 2023)
                // It is always true.
                boolean eligibleForSessionCheck =
                        tokenUtils.extractIssuedAt(jwt).after(sessionCheckDate);


                // TODO : Check refresh token existence once REDIS connected.
                if (eligibleForSessionCheck
                        && !refreshTokenUtil.isSessionValid(userInfo.getUserId())) {
                    log.info("invalid token : " + jwt);
                    throw new TokenException("InvalidToken", "User forced logged out", "403");
                }

                // Adding authentication to security context.
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                // Convert HttpServletRequest to WebAuthenticationDetails class.
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // Stores principal, credentials and authorities.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);

            ThreadContext.remove("userId");
            ThreadContext.clearAll();

        } catch (TokenException e) {
            log.error(
                    "$ Token exception "
                            + " " + e.getApiResponse().getError() + " "
                            + request.getRequestURI()
                            + " returning error code "
                            + 403
                            + " " + e.getMessage());
            var apiResponse = e.getApiResponse();
            createErrorResponse(apiResponse.getErrorCode(), apiResponse.getError(), null, response);
        } catch (JwtException e) {
            String message;
            String errorCode = "403";
            log.error("JwtException " + e.getMessage());
            if (e instanceof ExpiredJwtException) {
                message = "User access token expired";
                errorCode = "401";
                log.error(
                        "ExpiredJwtException "
                                + request.getRequestURI()
                                + " returning error code "
                                + 401
                                + " "
                                + e.getMessage());
            } else if (jwt == null) {
                message = "No JWT token submitted";
                log.error(
                        "jwt is null "
                                + request.getRequestURI()
                                + " returning error code "
                                + 403
                                + " "
                                + e.getMessage());
            } else {
                log.error(
                        "jwt is not null & not expired"
                                + request.getRequestURI()
                                + "returning error code "
                                + 403
                                + " "
                                + e.getMessage());
                message = "Invalid JWT token submitted";
            }
            createErrorResponse(errorCode, message, message, response);
        } catch (Exception e) {
            log.error(
                    "Token exception "
                            + request.getRequestURI()
                            + " returning error code"
                            + 403
                            + e.getMessage());
            var apiResponse = new ApiResponse();
            createErrorResponse(
                    apiResponse.getErrorCode(), "Some invalid exception", null, response);
        }
    }

    /**
     * Excludes filter on urls. Can change under application.properties>urls.exclude.filter.
     *
     * @param request All api requests.
     * @return True if successfullt applied.
     * @throws ServletException
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // TODO : Needs refactoring using antPathMatcher

        String path = request.getRequestURI();
        boolean isCreate = path.equals("/api/v1/auth/create");
        boolean isLogin = path.equals("/api/v1/auth/login");
        boolean isGetAccess = path.equals("/api/v1/refresh/getAccessToken");
        return isCreate || isLogin || isGetAccess;
    }

    /**
     * Get JWT token from the html header.
     *
     * @param request - api request
     * @return JWT token(if available else empty string)
     */
    private String getJwtFromHeader(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            return "";
        }
        return authHeader.substring(7);
    }

    private void createErrorApiResponse(ApiResponse apiResponse, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            response.setStatus(Integer.parseInt(apiResponse.getErrorCode()));
        } catch (Exception e) {
            log.error("exception in converting error code to status returning error code " + 403);
            response.setStatus(403);
        }
        ObjectMapper mapper = SystemUtils.getInstance().getObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiResponse));
    }

    private void createErrorResponse(
            String errorCode, String errorMessage, String message, HttpServletResponse response)
            throws IOException {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(false);
        apiResponse.setErrorCode(errorCode);
        apiResponse.setErrorMessage(errorMessage);
        apiResponse.setData(message);
        response.setContentType("application/json;charset=UTF-8");
        createErrorApiResponse(apiResponse, response);
    }

    /**
     * Logout of the app. Use jwt token to get SessionId and delete from mongo repository.
     *
     * @param request - request
     * @param response - response
     * @return response
     */
    public ApiResponse logout(HttpServletRequest request, HttpServletResponse response) {
        String bearerToken = request.getHeader("Authorization");
        String refreshToken = request.getParameter("refreshToken");
        String token = null;
        ApiResponse apiResponse = new ApiResponse();
        if (bearerToken != null) {
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                token = bearerToken.substring(7);
                log.info(
                        "token not present in cache - in logout "
                                + request.getRequestURI()
                                + " token "
                                + bearerToken
                                + "returning error code "
                                + 200);
                // Can't borrow sercurity Context holder. It gives an error.
                String sessionId = tokenUtils.extractSessionId(token);
                refreshTokenUtil.removeTokenForLogout(sessionId);
                apiResponse.setErrorCode("200");
                return apiResponse;
            }
        } else {
            log.info(
                    "token not present in header - in logout "
                            + request.getRequestURI()
                            + "returning error code "
                            + 200);
            apiResponse.setErrorCode("200");
            return apiResponse;
        }
        apiResponse.setErrorCode("200");
        return apiResponse;
    }
}
