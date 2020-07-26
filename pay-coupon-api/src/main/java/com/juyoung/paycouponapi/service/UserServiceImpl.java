package com.juyoung.paycouponapi.service;

import com.juyoung.paycouponapi.exception.BusinessErrorCode;
import com.juyoung.paycouponapi.exception.BusinessException;
import com.juyoung.paycouponapi.model.entity.User;
import com.juyoung.paycouponapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    @Override
    public User createUser(String userId, String password) {
        User user = userRepository.findByUserId(userId);

        if(user!=null)
            throw new BusinessException(BusinessErrorCode.USER_ID_EXIST);

        return userRepository.save(new User(userId, passwordEncoder().encode(password)));
    }

    @Override
    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public User getUserByUserIdAndPassword(String userId, String password) {
        User user = userRepository.findByUserId(userId);
        if(user != null && this.passwordEncoder().matches(password, user.getPassword()))
            return user;
        return null;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserId(username);
    }

    @Override
    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }
}
