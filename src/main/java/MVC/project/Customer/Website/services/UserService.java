package MVC.project.Customer.Website.services;

import MVC.project.Customer.Website.models.Role;
import MVC.project.Customer.Website.models.User;
import MVC.project.Customer.Website.repositories.RoleRepository;
import MVC.project.Customer.Website.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if(user == null){

            throw new UsernameNotFoundException(username + " is not a valid username! Check for typos and try again.");
        }
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId) throws EntityNotFoundException{

        Optional<User> user = userRepository.findById(userId);

        return (User) Hibernate.unproxy(user);
    }

    public User createNewUser (User user){

        user.setId(null);
        user.getAuthorities().forEach(a -> a.setId(null));

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setAuthorities(Collections.singletonList(roleRepository.getById(1L)));

        checkPassword(user.getPassword());
        user.setPassword((encoder.encode(user.getPassword())));

        try{
            return userRepository.save(user);
        } catch (Exception e) {

            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
    }

    public User createNewUserAdmin (User userAdmin){

        userAdmin.setId(null);
        userAdmin.getAuthorities().forEach(a -> a.setId(null));


        userAdmin.setAccountNonExpired(true);
        userAdmin.setAccountNonLocked(true);
        userAdmin.setCredentialsNonExpired(true);
        userAdmin.setEnabled(true);
        userAdmin.setAuthorities(Collections.singletonList(new Role(Role.Roles.ROLE_ADMIN)));

        checkPassword(userAdmin.getPassword());
        userAdmin.setPassword((encoder.encode(userAdmin.getPassword())));

        try{
            return userRepository.save(userAdmin);
        } catch (Exception e) {

            throw new IllegalStateException(e.getMessage(), e.getCause());
        }
    }

    private void checkPassword(String password){

        if(password == null){

            throw new IllegalStateException("You must set a password");
        }
        if(password.length() < 6){
            throw new IllegalStateException("Password is too short must be longer than 6 characters");
        }
    }


}
