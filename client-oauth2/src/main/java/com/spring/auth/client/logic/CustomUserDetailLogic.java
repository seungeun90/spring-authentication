package com.spring.auth.client.logic;


import com.spring.auth.domain.account.User;
import com.spring.auth.proxy.service.UserProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailLogic implements UserDetailsService {
    private final PasswordAESEncoder passwordAESEncoder;
    private final UserProxyService userProxyLogic;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findbyEmail(username);
        return User.builder()
                .name(account.getName())
                .ci(account.getCi())
                .email(account.getEmail())
                .password(account.getPassword())
                .mobile(account.getMobile())
                .role(account.getRoles())
                .build();
    }

    public boolean validateUser(Account account){
        Account user = findbyEmail(account.getEmail());
        validatePassword(account.getPassword(), user.getPassword());
        return true;
    }

    public void validatePassword(final String rawPassword, final String encodedPassword){
        String decrypted = passwordAESEncoder.decrypt(encodedPassword);
        if( !rawPassword.equals(decrypted) ) {
            throw new RuntimeException("invalid password");
        }
    }

    public Account findbyEmail(String email){
        return userProxyLogic.getUser(email);
    }





}
