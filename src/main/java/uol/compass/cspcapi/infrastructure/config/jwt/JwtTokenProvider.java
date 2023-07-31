package uol.compass.cspcapi.infrastructure.config.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String createToken(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expirityDate = new Date(now.getTime()  + 3600000);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(expirityDate)
                .setIssuedAt(now)
                .signWith(key)
                .compact();
    }

    public String resolveToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
            return token.substring(7);
        }

        return null;
    }

    public Boolean validateToken(String token){
        try{
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody().
                getSubject();
    }
}
