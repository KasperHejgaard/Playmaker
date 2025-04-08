package com.example.playmaker.service;

import com.example.playmaker.model.Artist;
import com.example.playmaker.model.User;
import com.example.playmaker.repository.ArtistRepository;
import com.example.playmaker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;

    public UserService(UserRepository userRepository, ArtistRepository artistRepository) {
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
    }

    public User createUser(String username, String email) {
        User user = new User();
        user.setUserName(username);
        user.setEmail(email);
        return userRepository.save(user);
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void addArtistToUser(User user, String artistName) {
        Artist artist = new Artist();
        artist.setName(artistName);
        artist.setUser(user);
        artistRepository.save(artist);
    }

    public List<Artist> getUserArtists(User user) {
        return artistRepository.findByUser(user);
    }

    public void removeArtist(Long artistId) {
        artistRepository.deleteById(artistId);
    }


}
