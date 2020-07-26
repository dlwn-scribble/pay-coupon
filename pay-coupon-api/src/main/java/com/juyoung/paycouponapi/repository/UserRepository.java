package com.juyoung.paycouponapi.repository;

import com.juyoung.paycouponapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);
    User findUserByUserIdAndPassword(String userId, String password);
}
