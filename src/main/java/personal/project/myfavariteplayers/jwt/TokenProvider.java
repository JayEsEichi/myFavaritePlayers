package personal.project.myfavariteplayers.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import personal.project.myfavariteplayers.Controller.Response.ResponseDto;
import personal.project.myfavariteplayers.Controller.Response.TokenDto;
import personal.project.myfavariteplayers.Entity.RefreshToken;
import personal.project.myfavariteplayers.Entity.User;
import personal.project.myfavariteplayers.Entity.UserRoleEnum;
import personal.project.myfavariteplayers.Repository.RefreshTokenRepository;
import personal.project.myfavariteplayers.Security.UserDetailsImpl;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 10000 * 60 * 30;            //30분
    private static final long REFRESH_TOKEN_EXPRIRE_TIME = 10000 * 60 * 60 * 24 * 7;     //7일

    private final Key key;

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(User user) {
        long now = (new Date().getTime());

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .setSubject(user.getUserid())
                .claim(AUTHORITIES_KEY, UserRoleEnum.USER)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPRIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println();
        System.out.println("AccessToken 생성 확인 : " + accessToken);
        System.out.println("refreshToken 생성 확인 : " + refreshToken);
        System.out.println();

        RefreshToken refreshTokenObject = RefreshToken.builder()
                .id(user.getId())
                .user(user)
                .value(refreshToken)
                .build();

        refreshTokenRepository.save(refreshTokenObject);

        return TokenDto.builder()
                .grantType(BEARER_PREFIX)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();

    }

//  public Authentication getAuthentication(String accessToken) {
//    Claims claims = parseClaims(accessToken);
//
//    if (claims.get(AUTHORITIES_KEY) == null) {
//      throw new RuntimeException("권한 정보가 없는 토큰 입니다.");
//    }
//
//    Collection<? extends GrantedAuthority> authorities =
//        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//            .map(SimpleGrantedAuthority::new)
//            .collect(Collectors.toList());
//
//    UserDetails principal = userDetailsService.loadUserByUsername(claims.getSubject());
//
//    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//  }

    public User getUserFromAuthentication() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return null;
        }

        return ((UserDetailsImpl) authentication.getPrincipal()).getUser();
    }

    public boolean validateToken(String token) {
        try {
            System.out.println("validate 속 token : " + token);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

//  private Claims parseClaims(String accessToken) {
//    try {
//      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
//    } catch (ExpiredJwtException e) {
//      return e.getClaims();
//    }
//  }

    @Transactional(readOnly = true)
    public RefreshToken isPresentRefreshToken(User user) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUser(user);
        return optionalRefreshToken.orElse(null);
    }

    @Transactional
    public ResponseDto<?> deleteRefreshToken(User user) {
        RefreshToken refreshToken = isPresentRefreshToken(user);
        if (null == refreshToken) {
            return ResponseDto.fail("TOKEN_NOT_FOUND", "존재하지 않는 Token 입니다.");
        }

        refreshTokenRepository.delete(refreshToken);
        return ResponseDto.success("success");
    }
}
