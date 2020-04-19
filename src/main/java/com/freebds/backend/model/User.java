package com.freebds.backend.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(	name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude= {"libraries"})
@ToString
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="generator_users_id_seq")
    @SequenceGenerator (name = "generator_users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "username")
    @NotBlank
    @Size(max = 20)
    private String username;

    @Column(name = "id_github")
    private String idGithub;

    @Column(name = "id_google")
    private String idGoogle;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "email")
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Column(name = "last_login_date")
    private Timestamp lastLoginDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name= "users_library",
            joinColumns = {@JoinColumn(name = "users_id", referencedColumnName= "id", foreignKey = @ForeignKey(name = "users_id") ) },
            inverseJoinColumns = { @JoinColumn(name = "library_id", referencedColumnName= "id", foreignKey = @ForeignKey(name = "library_id")) })
    private Set<Library> libraries = new HashSet<>();

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


}
