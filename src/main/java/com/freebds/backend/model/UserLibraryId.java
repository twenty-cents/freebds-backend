package com.freebds.backend.model;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@ToString
public class UserLibraryId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "users_id")
    private Long userId;

    @Column(name = "library_id")
    private Long libraryId;

    private String role;

    public UserLibraryId() {
    }

    public UserLibraryId(Long userId, Long libraryId, String role) {
        super();
        this.userId = userId;
        this.libraryId = libraryId;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Long libraryId) {
        this.libraryId = libraryId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLibraryId that = (UserLibraryId) o;
        return userId.equals(that.userId) &&
                libraryId.equals(that.libraryId) &&
                role.equals(that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, libraryId, role);
    }
}
