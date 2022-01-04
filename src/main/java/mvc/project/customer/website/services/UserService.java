package mvc.project.customer.website.services;

import mvc.project.customer.website.models.Customer;
import mvc.project.customer.website.models.Role;
import mvc.project.customer.website.models.User;
import mvc.project.customer.website.repositories.RoleRepository;
import mvc.project.customer.website.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
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

        user.setUserId(null);
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


    private void checkPassword(String password){

        if(password == null){

            throw new IllegalStateException("You must set a password");
        }
        if(password.length() < 6){
            throw new IllegalStateException("Password is too short must be longer than 6 characters");
        }
    }


    public List<User> getAllUsers() { return userRepository.findAll(); }


    public User getCustomer(Long id){

        return userRepository.getById(id);
    }


    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }

    public User updateUser(User user, Long userId){

        User user2 = userRepository.getById(userId);

        user2.getCustomer().setAge(user.getCustomer().getAge());
        user2.getCustomer().setFirstName(user.getCustomer().getFirstName());
        user2.getCustomer().setLastName(user.getCustomer().getLastName());
        user2.getCustomer().setEmailAddress(user.getCustomer().getEmailAddress());
        user2.getCustomer().setCity(user.getCustomer().getCity());

        return userRepository.save(user2);



    }
}
