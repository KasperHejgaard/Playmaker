package com.example.playmaker.controller;

import com.example.playmaker.model.Artist;
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
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String email = userData.get("email");

        User user = userService.createUser(username, email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/artists")
    public ResponseEntity<?> addArtist(@PathVariable Long userId, @RequestBody Map<String, String> artistData) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        String artistName = artistData.get("name");
        userService.addArtistToUser(user, artistName);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Artist added successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/artists")
    public ResponseEntity<List<Artist>> getUserArtists(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<Artist> artists = userService.getUserArtists(user);
        return ResponseEntity.ok(artists);
    }

    @DeleteMapping("/artists/{artistId}")
    public ResponseEntity<?> removeArtist(@PathVariable Long artistId) {
        userService.removeArtist(artistId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Artist removed successfully");
        return ResponseEntity.ok(response);
    }

}
