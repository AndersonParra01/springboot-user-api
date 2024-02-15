package pdf.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pdf.models.UserModel;
import pdf.repositories.UseRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired

    UseRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return UserDetailsImpl.fromInstanceUserRoles(user);

    }

}
