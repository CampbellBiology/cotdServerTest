package CocktailOfTheDay.project.Jwt;

import io.jsonwebtoken.*;

public class JwtValidate {
    public static int validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecretKey.JWT_SECRET_KEY).parseClaimsJws(token);
            return 1; // 검증통과
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature");
            return 0; // 유효하지 않은 시그니처
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token");
            return -1; // 유효하지 않은 토큰
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token");
            return -2; // 만료된 토큰
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token");
            return -3; // 지원되지 않는 토큰
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty.");
            return -4; // jwt를 증명하는 문자열이 비어있음
        }
    }
}
