package dev.fisa.hateoas_practice.auth;

import dev.fisa.hateoas_practice.model.User;
import dev.fisa.hateoas_practice.repository.UserRepository;
import dev.fisa.hateoas_practice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    public void signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다");
        }

        User user = User.create(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()) // 비밀번호 암호화
        );

        userRepository.save(user);
    }

    // 로그인 → JWT 반환
    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        return jwtTokenProvider.generateToken(user.getId(), user.getUsername());
    }
}