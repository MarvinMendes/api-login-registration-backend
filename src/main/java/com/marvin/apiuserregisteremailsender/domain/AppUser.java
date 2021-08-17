package com.marvin.apiuserregisteremailsender.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AppUser implements UserDetails {

    @Id
    @SequenceGenerator(name = "app_user_sequence", sequenceName = "app_user_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_sequence")
    private Long id;
    private String password;
    private String userName;
    private String fullName;
    private boolean isExpired;
    private boolean nonLocked = true;
    private boolean isEnabled = false;
    @Enumerated(EnumType.STRING)
    private AppUserRole role = AppUserRole.USER;
    @OneToOne
    private Token token;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
