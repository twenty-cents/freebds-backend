package com.freebds.freebdsbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of= {"id"})
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="generator_category_id_seq")
    @SequenceGenerator(name = "generator_category_id_seq", sequenceName = "category_id_seq", allocationSize = 1)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy="categories")
    private Set<Serie> series = new HashSet<>();

}
