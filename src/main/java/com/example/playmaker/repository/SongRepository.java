package com.example.playmaker.repository;

import com.example.playmaker.model.Playlist;
import com.example.playmaker.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByPlaylist(Playlist playlist);
}
