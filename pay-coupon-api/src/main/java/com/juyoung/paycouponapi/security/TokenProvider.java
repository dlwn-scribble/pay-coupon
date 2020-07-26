package com.juyoung.paycouponapi.security;

import com.juyoung.paycouponapi.service.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TokenProvider {
    static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.header}")
    private String header;

    @Autowired
    UserServiceImpl userService;

    private final String tokenType = "Bearer ";

    private final int VALID_MINS = 30; //30 min

    @PostConstruct
    private void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String getTokenType(){
        return this.tokenType;
    }

    //region 토큰 관리
    //토큰 생성
    public String createToken(String userId, List<String> roles){
        Claims claims = Jwts.claims().setSubject(userId);

        claims.put("roles", roles);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusMinutes(VALID_MINS);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //토큰 검증
    public boolean validateToken(String token)
    {
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    //토큰 획득
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(header);

        if(bearerToken !=null && bearerToken.startsWith(tokenType))
            return bearerToken.substring(tokenType.length(), bearerToken.length());
        return "";
    }
    //endregion

    //region 인증 처리
    public Authentication getAuthentication(String token){
        String username = this.getUserName(token);
        UserDetails userDetails = userService.loadUserByUsername(username);
        if(userDetails == null)
            throw new SecurityException();  //HTTP : 403 에러 발생
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUserName(String token){
        try{
            String username = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
            return username;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return "";
        }
    }
    //endregion
}
