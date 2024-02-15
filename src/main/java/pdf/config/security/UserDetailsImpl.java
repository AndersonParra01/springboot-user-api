package pdf.config.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pdf.models.UserModel;
import lombok.Data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Data
public class UserDetailsImpl implements UserDetails {
    public UserDetailsImpl() {

    }

    private UserModel user;

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();

        String role = this.user.getRole() == null ? "SinRol" : this.user.getRole().getAuthority();

        authorities.add(new SimpleGrantedAuthority(role));

        return authorities;
    }

    public static UserDetailsImpl fromInstanceUserRoles(UserModel _user) {
        UserDetailsImpl userImp = new UserDetailsImpl();
        userImp.setUser(_user);
        return userImp;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
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

    public Map<String, ?> claims() {

        return Map.of(
                "id", user.getId(),
                "subject", user.getUsername(),
                "role", this.getAuthorities().get(0).getAuthority());

    }

}
