package com.dbtest.yashwith.security;

import com.dbtest.yashwith.response.ApiResponse;
import com.dbtest.yashwith.utils.SystemUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private final UserInfoService userInfoService;
    private final AuthFilter authFilter;
    private final TokenUtils tokenUtils;

    public SecurityConfig(
            UserInfoService userInfoService, AuthFilter authFilter, TokenUtils tokenUtils) {
        this.userInfoService = userInfoService;
        this.authFilter = authFilter;
        this.tokenUtils = tokenUtils;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userInfoService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // TODO AUTH : Configure routes and security.
        http.cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .accessDeniedHandler(
                        (request, response, e) -> {
                            String bearerToken = request.getHeader("Authorization");
                            log.error(
                                    "accessDeniedHandler "
                                            + request.getRequestURI()
                                            + " token "
                                            + bearerToken
                                            + " returning error code "
                                            + 403
                                            + " "
                                            + e.getMessage(),
                                    e);
                            ApiResponse apiResponse = new ApiResponse();
                            apiResponse.setSuccess(false);
                            apiResponse.setErrorCode("403");
                            apiResponse.setErrorMessage(e.getMessage());

                            ObjectMapper mapper = SystemUtils.getInstance().getObjectMapper();
                            response.setContentType(CONTENT_TYPE);
                            response.setStatus(403);
                            response.getWriter().write(mapper.writeValueAsString(apiResponse));
                        })
                .authenticationEntryPoint(
                        (request, res, e) -> {
                            String bearerToken = request.getHeader("Authorization");
                            log.error(
                                    "authenticationEntryPoint failed "
                                            + request.getRequestURI()
                                            + " token "
                                            + bearerToken
                                            + " returning error code "
                                            + 403
                                            + " "
                                            + e.getMessage(),
                                    e);
                            ApiResponse apiResponse = new ApiResponse();
                            apiResponse.setSuccess(false);
                            apiResponse.setErrorCode("403");
                            apiResponse.setErrorMessage(e.getMessage());

                            ObjectMapper mapper = SystemUtils.getInstance().getObjectMapper();
                            res.setContentType(CONTENT_TYPE);
                            res.setStatus(403);
                            res.getWriter().write(mapper.writeValueAsString(apiResponse));
                        })
                .and()
                .authorizeRequests()
                .anyRequest()
                .permitAll();

        // http.addFilterAfter();
        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Setting up cors
     *
     * @return CorsConfigurationSource
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                ImmutableList.of("https://*.kiranaStores.app", "http://*", "*"));
        configuration.setAllowedMethods(ImmutableList.of("*"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the
        // wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(ImmutableList.of("*"));
        configuration.setMaxAge(Long.valueOf(-1));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
