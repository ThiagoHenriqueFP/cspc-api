package uol.compass.cspcapi.infrastructure.config.userDetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.user.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private String username;
    private String password;

    private List<Role> roles = new ArrayList<>();

    public UserDetailsImpl(String username, String password, List<Role> roles) {
        this.username = username;
        this.password = password;
        if(roles == null || roles.size() == 0)
            this.roles = Collections.emptyList();
        else
            this.roles.addAll(roles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if(roles == null)
            return null;

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
