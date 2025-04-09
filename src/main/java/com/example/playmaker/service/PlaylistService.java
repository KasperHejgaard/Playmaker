package com.example.playmaker.service;

import com.example.playmaker.model.Artist;
import com.example.playmaker.model.Playlist;
import com.example.playmaker.model.Song;
import com.example.playmaker.model.User;
import com.example.playmaker.repository.ArtistRepository;
import com.example.playmaker.repository.PlaylistRepository;
import com.example.playmaker.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final MistralAIService mistralAIService;

    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository, ArtistRepository artistRepository, MistralAIService mistralAIService) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
        this.mistralAIService = mistralAIService;
    }

    public Playlist generatePlaylist(User user, String playlistName, int songCount) {
        List<Artist> userArtists = artistRepository.findByUser(user);

        if (userArtists.size() > 5) {
            userArtists = userArtists.subList(0, 5);
        }

        Playlist playlist = new Playlist();
        playlist.setName(playlistName);
        playlist.setUser(user);

        playlist = playlistRepository.save(playlist);

        List<Song> generatedSongs = mistralAIService.generatePlaylist(userArtists, songCount);

        for (Song song : generatedSongs) {
            song.setPlaylist(playlist);
            songRepository.save(song);
        }

        return playlist;
    }

    public List<Playlist> getUserPlaylists(User user) {
        return playlistRepository.findByUser(user);
    }

    public Playlist getPlaylistById(Long id) {
        return playlistRepository.findById(id).orElse(null);
    }

    public void deletePlaylist(Long id) {
        playlistRepository.deleteById(id);
    }

}
