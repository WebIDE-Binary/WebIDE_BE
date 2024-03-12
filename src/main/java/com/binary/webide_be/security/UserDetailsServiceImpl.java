package com.binary.webide_be.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    //TODO: 주석 해제 private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws IllegalArgumentException {
        log.info(email);
//        User user = userRepository.findByEmail(email)  //이메일로 커스텀이 가능하다
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

//        return new UserDetailsImpl(user, user.getEmail());
        return null;
    }

}