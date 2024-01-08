package com.dbtest.yashwith.security;

import com.dbtest.yashwith.exception.TokenException;
import com.dbtest.yashwith.response.ApiResponse;
import com.dbtest.yashwith.utils.DateUtils;
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

                System.out.println("Still Running 3!");
                // User details must be set into web context.
                UserDetails userDetails = userInfoService.loadUserByUsername(jwt);
                UserInfo userInfo = (UserInfo) userDetails;
                userInfo.setAppVer(httpServletRequest.getHeader("appVer"));

                Date sessionCheckDate =
                        new Date(
                                DateUtils.getDateFromString(authSessionIdDate, "dd-MM-yyyy HH:mm"));

                boolean eligibleForSessionCheck =
                        tokenUtils.getIssuedAt(jwt).after(sessionCheckDate);

                if (eligibleForSessionCheck
                        && !refreshTokenUtil.isSessionValid(userInfo.getUserId())) {
                    log.info("invalid token : " + jwt);
                    throw new TokenException("InvalidToken", "User forced logged out", "403");
                }

                // Adding authentication to security context.
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Completed running systems.");
                filterChain.doFilter(request, response);
            } else {
                throw new TokenException("Token is not", "Token is not present", "401");
            }
        } catch (TokenException e) {
            log.error(
                    "Token exception "
                            + request.getRequestURI()
                            + " returning error code "
                            + 403
                            + e.getMessage());
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
        String path = request.getRequestURI();
        boolean isCreate = path.equals("/api/v1/auth/create");
        boolean isLogin = path.equals("/api/v1/auth/login");
        return isCreate || isLogin;
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
     * TODO : Not implemented yet Logs out a token also accepts device id - for giving the ability
     * to user to logout from other device removes access token & refresh token from redis
     *
     * @param request - request
     * @param response - response
     * @return response
     */
}
