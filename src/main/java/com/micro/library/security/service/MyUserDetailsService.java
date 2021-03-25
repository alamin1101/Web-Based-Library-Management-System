package com.micro.library.security.service;

import com.micro.library.entity.AppUser;
import com.micro.library.repository.AppUserRepository;
import com.micro.library.security.model.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository userLogRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = userLogRepository.findById(email).get();
        if (appUser == null)
            throw new UsernameNotFoundException("User not found");
        return new MyUserDetails(appUser);
    }
}
