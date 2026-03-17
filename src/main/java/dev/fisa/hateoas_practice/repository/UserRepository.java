package dev.fisa.hateoas_practice.repository;

import dev.fisa.hateoas_practice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD + 페이지 처리를 수행하려면? -> JpaRepository 상속
public interface UserRepository extends JpaRepository<User,Long> {
}
