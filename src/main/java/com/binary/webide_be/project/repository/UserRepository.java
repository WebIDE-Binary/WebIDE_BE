package com.binary.webide_be.project.repository;

import com.binary.webide_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); //옵셔널로 받아야 null을 받아도 예외가 나지 않는다. -> 핸들링으로 오류를 찾아줄거니깐 (이메일이 없을수도 있으니null이 나올수도 있다!)

    Optional<User> findById(Long id);
}
