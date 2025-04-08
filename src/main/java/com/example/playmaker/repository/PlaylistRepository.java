package com.example.playmaker.repository;

import com.example.playmaker.model.User;
import com.example.playmaker.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUser(User user);
}
