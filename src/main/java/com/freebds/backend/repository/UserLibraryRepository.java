package com.freebds.backend.repository;

import com.freebds.backend.common.web.library.resources.ILibraryResource;
import com.freebds.backend.common.web.library.resources.IUserLibraryStatusResource;
import com.freebds.backend.model.UserLibrary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLibraryRepository extends JpaRepository<UserLibrary, Long> {

    @Query("SELECT u FROM UserLibrary u WHERE u.user.id = :userId AND u.role = :role")
    List<UserLibrary> findUserLibrary(@Param("userId") Long userId, @Param("role") String role);

    Optional<UserLibrary> findFirstByUser_IdAndLibrary_Id(@Param("userId") Long userId, @Param("libraryId") Long libraryId);

    Optional<UserLibrary> findFirstByUser_IdAndIsActive(Long userId, Boolean isActive);

    @Query("SELECT " +
            "l.id as libraryId, " +
            "l.name as name, " +
            "l.description as description, " +
            "u.role as userRole, " +
            "u.isActive as isActive " +
            "FROM UserLibrary u JOIN Library l ON u.library.id = l.id " +
            "WHERE u.user.id = :userId " +
            "ORDER BY u.role, l.name ")
    List<ILibraryResource> findLibraries(@Param("userId") Long userId);


    @Query(value = "SELECT " +
            "u.id as userId, " +
            "u.firstname as firstname, " +
            "u.lastname as lastname, " +
            "'1' as status " +
            "FROM users u JOIN users_library ul ON u.id = ul.users_id " +
            "WHERE ul.library_id = :libraryId AND u.id <> :userId " +
            "UNION " +
            "SELECT " +
            "u.id as userId, " +
            "u.firstname as firstname, " +
            "u.lastname as lastname, " +
            "'0' as status " +
            "FROM users u JOIN users_library ul ON u.id = ul.users_id " +
            "WHERE ul.library_id <> :libraryId AND u.id <> :userId " +
            "AND u.id NOT IN ( " +
            "  SELECT ul2.users_id " +
            "  FROM users_library ul2 " +
            "  WHERE ul2.library_id = :libraryId ) " +
            "ORDER BY 3, 2 ",
            nativeQuery = true)
    List<IUserLibraryStatusResource> findUsersStatusOnPersonalLibrary(@Param("userId") Long userId, @Param("libraryId") Long libraryId);

    @Query("SELECT " +
            "l.id as libraryId, " +
            "l.name as name, " +
            "l.description as description, " +
            "u.role as userRole, " +
            "u.isActive as isActive " +
            "FROM UserLibrary u JOIN Library l ON u.library.id = l.id " +
            "WHERE u.user.id = :userId AND u.isActive = true ")
    List<ILibraryResource> findActiveLibrary(@Param("userId") Long userId);
}
