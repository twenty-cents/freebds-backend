package com.freebds.backend.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of= {"id"})
@ToString
public class Users {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="generator_users_id_seq")
    @SequenceGenerator (name = "generator_users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "last_login_date")
    private Timestamp lastLoginDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "role_id"))
    private Role role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name= "users_library",
            joinColumns = {@JoinColumn(name = "users_id", referencedColumnName= "id", foreignKey = @ForeignKey(name = "users_id") ) },
            inverseJoinColumns = { @JoinColumn(name = "library_id", referencedColumnName= "id", foreignKey = @ForeignKey(name = "library_id")) })
    private Set<Library> libraries = new HashSet<>();
}
