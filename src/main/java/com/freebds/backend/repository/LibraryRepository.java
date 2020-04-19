package com.freebds.backend.repository;

import com.freebds.backend.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {

    @Query("SELECT l FROM Library l " +
            "  JOIN UserLibrary ul ON l.id = ul.library.id " +
            "WHERE ul.user.id = :userId AND ul.library.id = :libraryId")
    Optional<Library> findLibraryByUser(@Param("libraryId") Long libraryId, @Param("userId") Long userId);

    @Query("SELECT ul.role FROM Library l " +
            "  JOIN UserLibrary ul ON l.id = ul.library.id " +
            "WHERE ul.user.id = :userId AND ul.library.id = :libraryId")
    Optional<String> findUserRole(@Param("libraryId") Long libraryId, @Param("userId") Long userId);

    @Query("SELECT l FROM Library l " +
            "  JOIN UserLibrary ul ON l.id = ul.library.id " +
            "WHERE ul.user.id = :userId ORDER BY l.name")
    List<Library> findLibrariesByUser(@Param( "userId") Long userId);
}
