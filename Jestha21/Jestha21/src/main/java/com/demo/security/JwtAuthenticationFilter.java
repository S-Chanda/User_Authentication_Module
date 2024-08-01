package com.demo.security;


import com.demo.service.implementation.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    CustomUserDetailService userDetailService;

    private static final List<String> EXCLUDED_URLs= Arrays.asList("/auth/login", "/auth/verifyOtp", "/auth/generateOtp/**", "/user/addUser", "user/forgot-password", "user/reset-password**");




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestedURI= request.getRequestURI();

        if (EXCLUDED_URLs.contains(requestedURI)) {
            filterChain.doFilter(request, response); // Skip authentication filter
            return;
        }

            final String requestHeader = request.getHeader("Authorization");

            logger.info("Header: " + requestHeader);
            String username = null;
            String token = null;

            //get token
            if(requestHeader!= null && requestHeader.startsWith("Bearer ")){
                token = requestHeader.substring(7);
                try {
                    username = jwtHelper.getUsernameFromToken(token);
                } catch (Exception e) {
                    logger.error("Unable to get Token, or token has expired", e);
                }
            }else {
                logger.info("Invalid Authorization value");
            }


            //validate user
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                logger.info("User details: {}", userDetails);
                boolean validateToken = jwtHelper.validateToken(token,userDetails);
                if(validateToken){
                    //set authentication
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities() );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                else{
                    logger.info("Validation fails");
                }

            }
        //if authentication success, then we forward the request
        filterChain.doFilter(request, response);





    }
}
