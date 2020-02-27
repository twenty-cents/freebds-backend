package com.freebds.freebdsbackend.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "role")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of= {"id"})
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="generator_role_id_seq")
    @SequenceGenerator (name = "generator_role_id_seq", sequenceName = "role_id_seq", allocationSize = 1)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "role")
    private Users user;
}
