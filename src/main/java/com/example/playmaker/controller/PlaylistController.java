package com.example.playmaker.controller;

import com.example.playmaker.model.Playlist;
import com.example.playmaker.model.User;
import com.example.playmaker.service.PlaylistService;
import com.example.playmaker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final UserService userService;

    public PlaylistController(PlaylistService playlistService, UserService userService) {
        this.playlistService = playlistService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Playlist> generatePlaylist(@RequestBody Map<String, Object> requestData) {
        Long userId = Long.valueOf(requestData.get("userId").toString());
        String playlistName = (String) requestData.get("playlistName");
        int songCount = Integer.parseInt(requestData.get("songCount").toString());

        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Playlist playlist = playlistService.generatePlaylist(user, playlistName, songCount);
        return ResponseEntity.ok(playlist);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Playlist>> getUserPlaylists(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<Playlist> playlists = playlistService.getUserPlaylists(user);
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylist(@PathVariable Long id) {
        Playlist playlist = playlistService.getPlaylistById(id);
        if (playlist == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(playlist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Playlist deleted successfully");
        return ResponseEntity.ok(response);
    }
}
