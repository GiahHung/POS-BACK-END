package com.example.pos.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

@Component
public class JwtFilter extends OncePerRequestFilter {

    // Bean dùng để kiểm tra và xử lý token (bạn cần tự hiện thực lớp này)
    @Autowired
    private JwtUtil jwtUtil;

    // Dùng để load thông tin người dùng từ username trích xuất được từ token
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
            throws jakarta.servlet.ServletException, IOException {
        // B1: Lấy header "Authorization" từ request
        String authHeader = request.getHeader("Authorization");
        String token = null;

        // B2: Kiểm tra xem header có tồn tại và bắt đầu bằng "Bearer " hay không
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Lấy token bằng cách loại bỏ phần "Bearer " (7 ký tự đầu)
            token = authHeader.substring(7);
        }

        // B3: Nếu có token, kiểm tra tính hợp lệ của token đó
        if (token != null && jwtUtil.validateToken(token)) {
            // B4: Lấy username từ token
            Claims claims = jwtUtil.extractAllClaims(token);
            String username = claims.getSubject();
            String roleId = claims.get("roleId", String.class);
            if (roleId == null) {
                System.out.println("Role ID is missing from JWT!");
            } else {
                System.out.println("Extracted Role ID: " + roleId);
            }
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(roleId));

            // B5: Tải thông tin người dùng từ username
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // B6: Tạo đối tượng Authentication dựa trên thông tin người dùng
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, authorities);

            // Gán thêm thông tin chi tiết từ request (tùy chọn)
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // B7: Thiết lập đối tượng Authentication vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Tiếp tục cho các bộ lọc phía sau
        filterChain.doFilter(request, response);
    }

}