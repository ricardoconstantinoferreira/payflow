package com.flow.payflow.config.security;

import com.flow.payflow.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class StoreDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return storeRepository.findByEmail(email).map(
                store -> User.builder()
                        .username(email)
                        .password(store.getPassword())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Store not found"));
    }
}
