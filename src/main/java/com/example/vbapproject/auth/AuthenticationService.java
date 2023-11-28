package com.example.vbapproject.auth;


import com.example.vbapproject.configuration.JwtService;
import com.example.vbapproject.model.Account;
import com.example.vbapproject.repository.AccountRepository;
import com.example.vbapproject.token.Token;
import com.example.vbapproject.token.TokenRepository;
import com.example.vbapproject.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final AccountRepository accountRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    var account = Account.builder()
            .userName(request.getUserName())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(String.valueOf(request.getRole()))
            .build();
    var savedAccount = accountRepository.save(account);
    var jwtToken = jwtService.generateToken(savedAccount);
    var refreshToken = jwtService.generateRefreshToken(savedAccount);
    saveUserToken(savedAccount, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUserName(),
                    request.getPassword()
            )
    );
    var account = accountRepository.findByUserName(request.getUserName())
            .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
    var jwtToken = jwtService.generateToken(account);
    var refreshToken = jwtService.generateRefreshToken(account);
    revokeAllUserTokens(account);
    saveUserToken(account, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  private void saveUserToken(Account account, String jwtToken) {
    var token = Token.builder()
            .account(account)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(Account account) {
    var validUserTokens = tokenRepository.findAllValidTokenByAccount((int) account.getId());
    if (validUserTokens.isEmpty()) return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var account = this.accountRepository.findByUserName(userEmail)
              .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
      if (jwtService.isTokenValid(refreshToken, account)) {
        var accessToken = jwtService.generateToken(account);
        revokeAllUserTokens(account);
        saveUserToken(account, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
