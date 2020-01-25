package com.kakaopay.housingfund.security;

import com.kakaopay.housingfund.user.model.Role;
import com.kakaopay.housingfund.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증
    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

    @Value("${jwt.token.header}")
    private String tokenHeader;

    @Value("${jwt.token.clientSecret}")
    private String secretKey;

    private final Long tokenValidTime = 1000L * 60;

    private final UserService userService;

    public JwtTokenProvider(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createToken(String email, List<Role> roles) {
        Claims claims = Jwts.claims();
        claims.setSubject(email);
        claims.put("ROLE", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT 토큰으로 인증정보를 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByUsername(this.getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveTokenHeader(HttpServletRequest request) {
        final String token = request.getHeader(tokenHeader);
        if (token != null) {
            if (logger.isDebugEnabled()) logger.debug("JWT TOKEN : " + token);
            final String[] parts = token.split(" ");
            final String scheme = parts[0];
            final String credential = parts[1];
            return BEARER.matcher(scheme).matches() ? credential : null;
        }
        return null;
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return claimsJws.getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
