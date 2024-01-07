package com.dbtest.yashwith.security;

import com.dbtest.yashwith.exception.TokenException;
import com.dbtest.yashwith.response.ApiResponse;
import com.dbtest.yashwith.utils.DateUtils;
import com.dbtest.yashwith.utils.SystemUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
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
import org.springframework.security.core.userdetails.UserDetails;
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
    // private final RefreshTokenUtil refreshTokenUtil;
    // private final TranslationService translationService;
    // private final HTTPRequestService httpRequestService;

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
        logger.debug("Running inside once per request AuthFilter ----> " + request.getRequestURI());
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
                        && !refreshTokenUtil.isSessionValid(
                                userId,
                                userInfo.getTokenPayload().getAppId(),
                                userInfo.getTokenPayload().getSid())) {
                    log.info("invalid token : " + jwt);
                    throw new TokenException("InvalidToken", "User forced logged out", "403");
                }
            }
        } catch (Exception e) {
            log.error(e.toString());
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
        return excludeFilter.stream().anyMatch(p -> pathMatcher.match(p, request.getRequestURI()));
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
     *     <p>public ApiResponse logout(HttpServletRequest request, HttpServletResponse response) {
     *     String bearerToken = request.getHeader("Authorization"); String currentDeviceId =
     *     request.getParameter("deviceId"); String refreshToken =
     *     request.getParameter("refreshToken"); String token = null; ApiResponse apiResponse = new
     *     ApiResponse(); if (bearerToken != null) { if (StringUtils.hasText(bearerToken) &&
     *     bearerToken.startsWith("Bearer ")) { token = bearerToken.substring(7); log.info( "token
     *     not present in cache - in logout " + request.getRequestURI() + " token " + bearerToken +
     *     "returning error code " + 200); // removiing this code as it is causing null pointer
     *     exception // UserInfo userInfo = // (UserInfo) // SecurityContextHolder.getContext() //
     *     .getAuthentication() // .getPrincipal(); String sessionId =
     *     tokenUtils.extractSessionId(token); refreshTokenUtil.removeTokenForLogout(sessionId);
     *     apiResponse.setErrorCode("200");
     *     apiResponse.setData(translationService.getTranslation("auth.log.out")); return
     *     apiResponse; } } else { log.info( "token not present in header - in logout " +
     *     request.getRequestURI() + "returning error code " + 200);
     *     apiResponse.setErrorCode("200"); // apiResponse.setSuccess(false); //
     *     apiResponse.setErrorMessage("Invalid request"); return apiResponse; }
     *     apiResponse.setErrorCode("200");
     *     apiResponse.setData(translationService.getTranslation("auth.log.out")); if
     *     (currentDeviceId == null) {
     *     apiResponse.setData(translationService.getTranslation("auth.log.out")); } else {
     *     apiResponse.setData(translationService.getTranslation("auth.log.out.devices")); } return
     *     apiResponse; }
     */
}
