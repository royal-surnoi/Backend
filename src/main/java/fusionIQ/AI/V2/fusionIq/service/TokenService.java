package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    private static final String SECRET_KEY = "Zxs07E2JjXAFfS8GPuCCyOvHEWDG5GujHhxCmvdrPmI";
    private static final long EXPIRATION_TIME = 86400000L; // 1 day
    private Map<String, Date> invalidatedTokens = new ConcurrentHashMap<>();
    private Map<Long, String> userActiveTokens = new ConcurrentHashMap<>(); // Track active tokens per user

    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token, User userDetails) {
        final String email = getEmailFromToken(token);
        return (email.equals(userDetails.getEmail()) && !isTokenExpired(token) && !isTokenInvalidated(token));
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
    public void invalidateToken(String token) {
        if (token != null) {
            Date expiration = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            invalidatedTokens.put(token, expiration);
        }
    }
    public boolean isTokenInvalidated(String token) {
        return invalidatedTokens.containsKey(token);
    }

    // Get active token for a user
    public String getActiveTokenForUser(Long userId) {
        return userActiveTokens.get(userId);
    }

    // Set active token for a user
    public void setActiveTokenForUser(Long userId, String token) {
        userActiveTokens.put(userId, token);
    }
    public void removeActiveTokenForUser(Long userId) {
        userActiveTokens.remove(userId);
    }
}