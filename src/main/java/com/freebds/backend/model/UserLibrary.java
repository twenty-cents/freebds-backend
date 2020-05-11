package com.freebds.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users_library")
public class UserLibrary {

    @EmbeddedId
    private UserLibraryId id = new UserLibraryId();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "users_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "users_id"))
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("libraryId")
    @JoinColumn(name = "library_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "library_id"))
    private Library library;

    @Column(name = "role", length = 50, insertable = false, updatable = false)
    private String role = "";

    @Column(name = "is_active")
    private Boolean isActive;

    public UserLibrary() {
    }

    public UserLibrary(User user, Library library) {
        this.user = user;
        this.library = library;
        this.id = new UserLibraryId(user.getId(), library.getId(), role);
    }

    public UserLibraryId getId() {
        return id;
    }

    public void setId(UserLibraryId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
        this.id.setRole(role);
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLibrary that = (UserLibrary) o;
        return id.equals(that.id) &&
                user.equals(that.user) &&
                library.equals(that.library) &&
                role.equals(that.role) &&
                isActive.equals(that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, library, role, isActive);
    }
}
