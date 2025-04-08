package com.example.playmaker.repository;

import com.example.playmaker.model.Artist;

import com.example.playmaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findByUser(User user);
}
