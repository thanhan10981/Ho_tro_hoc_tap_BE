package com.hoctap.learningsupportapi.utils;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);

            // ✅ LẤY THÔNG TIN TỪ TOKEN
            Integer userId = JwtUtil.getUserId(token);
            String email = JwtUtil.getEmailFromToken(token);

            // ✅ SET ATTRIBUTE (GIỮ NGUYÊN LOGIC CŨ)
            request.setAttribute("currentUserId", userId);

            // ✅ SET AUTHENTICATION (FIX LỖI)
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,   // ⭐ CỰC KỲ QUAN TRỌNG
                            null,
                            List.of()
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            // token sai → bỏ qua
        }

        filterChain.doFilter(request, response);
    }
}
