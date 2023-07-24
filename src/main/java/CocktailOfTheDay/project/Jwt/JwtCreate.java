package CocktailOfTheDay.project.Jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtCreate {
    public String createJwt(String userId) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 100000000))
                .signWith(SignatureAlgorithm.HS256, SecretKey.JWT_SECRET_KEY)
                .compact();
    }
}
