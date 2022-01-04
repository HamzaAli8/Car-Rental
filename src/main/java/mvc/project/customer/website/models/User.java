package mvc.project.customer.website.models;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Transient 
    private String confirmPassword;

    @Column
    private boolean isAccountNonExpired = true;

    @Column
    private boolean isAccountNonLocked = true;

    @Column
    private boolean isCredentialsNonExpired = true;

    @Column
    private boolean isEnabled = true;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_join_table",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> authorities = new ArrayList<>();

    public User(String username, String password, String confirmPassword, Collection<Role> authorities, Customer customer){

        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.customer = customer;
        this.confirmPassword = confirmPassword;
    }


    @OneToOne(cascade = CascadeType.PERSIST, optional = false)
    private Customer customer;

    @OneToOne(mappedBy = "user")
    private Car car;


    public void setPassword(String password) {
        this.password = password;
        checkPassword();//check
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        checkPassword();//check
    }

    private void checkPassword() {
        if(this.password == null || this.confirmPassword == null){
            return;
        }else if(!this.password.equals(confirmPassword)){
            return;
        }
    }

}
