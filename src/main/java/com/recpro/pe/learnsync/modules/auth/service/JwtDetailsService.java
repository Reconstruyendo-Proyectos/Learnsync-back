package com.recpro.pe.learnsync.modules.auth.service;

import com.recpro.pe.learnsync.modules.auth.model.User;
import com.recpro.pe.learnsync.modules.auth.repository.UserRepository;
import com.recpro.pe.learnsync.shared.security.UserSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class JwtDetailsService implements UserDetailsService {    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario "+username + " no fue encontrado"));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+user.getRole().getRoleName().name());
        return new UserSecurity(user.getUsername(), user.getPassword(), Collections.singletonList(authority), user);
    }
}