package co.com.bancolombia.security.jwt.provider;

import co.com.bancolombia.model.token.TokenGateway;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Set;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class JwtProvider implements TokenGateway {

    private static final Logger LOGGER = Logger.getLogger(JwtProvider.class.getName());

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expiration;

    @Override
    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
            .subject(username)
            .claim("roles", roles)
            .issuedAt(new Date())
            .expiration(new Date(new Date().getTime() + expiration))
            .signWith(getKey(secret))
            .compact();
    }

    @Override
    public String getSubject(String token) {
        return Jwts.parser()
            .verifyWith(getKey(secret))
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    @Override
    public boolean validate(String token) {
        try {
            Jwts.parser()
                .verifyWith(getKey(secret))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
            return true;
        } catch (ExpiredJwtException e) {
            LOGGER.severe("token expired");
        } catch (UnsupportedJwtException e) {
            LOGGER.severe("token unsupported");
        } catch (MalformedJwtException e) {
            LOGGER.severe("token malformed");
        } catch (SignatureException e) {
            LOGGER.severe("bad signature");
        } catch (IllegalArgumentException e) {
            LOGGER.severe("illegal args");
        }
        return false;
    }

    @Override
    public Map<String, Object> getAllClaims(String token) {
        Claims claims = parser(token);
        Map<String, Object> map = new HashMap<>();
        claims.forEach(map::put);
        return map;
    }

    @Override
    public Optional<Object> getClaim(String token, String name) {
        return Optional.ofNullable(parser(token).get(name));
    }

    @Override
    public Set<String> getRoles(String token) {
        var claims = parser(token);
        var rolesObj = claims.get("roles");
        if (rolesObj instanceof java.util.Collection<?> col) {
            return col.stream().map(String::valueOf).collect(java.util.stream.Collectors.toSet());
        }
        return rolesObj == null ? java.util.Set.of() : java.util.Set.of(String.valueOf(rolesObj));
    }

    private SecretKey getKey(String secret) {
        byte[] secretBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }

    private Claims parser(String token) {
        return Jwts.parser()
            .verifyWith(getKey(secret))
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
