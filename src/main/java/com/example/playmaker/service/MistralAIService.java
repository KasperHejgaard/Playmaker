package com.example.playmaker.service;

import com.example.playmaker.model.Artist;
import com.example.playmaker.model.Playlist;
import com.example.playmaker.model.Song;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MistralAIService {

    private final PathPatternParser mvcPatternParser;

    @Value("${mistral.api.key}")
    private String mistralApiKey;

    @Value("${mistral.api.url}")
    private String mistralApiUrl;

    private final RestTemplate restTemplate;
    public MistralAIService(RestTemplate restTemplate, PathPatternParser mvcPatternParser) {
        this.restTemplate = restTemplate;
        this.mvcPatternParser = mvcPatternParser;
    }

    public List<Song> generatePlaylist(List<Artist> artists, int songCount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + mistralApiKey);

        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a playlist of ").append(songCount).append(" songs based on these artists");

        for (int i = 0; i <= artists.size(); i++) {
            if (i > 0) prompt.append(", ");
            prompt.append(artists.get(i).getName());
        }

        prompt.append(". For each song, provide the title and artist in this format: 'Title - Artist'.");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mistral-large-latest");
        requestBody.put("prompt", prompt.toString());
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        Map<String, Object> response = restTemplate.postForObject(mistralApiUrl, request, Map.class);

        String generatedText = (String) ((Map<String, Object>) ((List<?>) response.get("choices")).get(0)).get("text");

        return parseSongsFromGeneratedText(generatedText);
    }

    private List<Song> parseSongsFromGeneratedText(String generatedText) {
        List<Song> songs = new ArrayList<>();
        String[] lines = generatedText.split("\n");

        for (String line : lines) {
            line = line.trim();

            // Skip empty lines or lines without proper format
            if (line.isEmpty() || !line.contains("-")) {
                continue;
            }

            // Remove any numbering at the beginning
            if (line.matches("^\\d+\\..*")) {
                line = line.replaceFirst("^\\d+\\.\\s*", "");
            }

            String[] parts = line.split("-", 2);
            if (parts.length == 2) {
                Song song = new Song();
                song.setTitle(parts[0].trim());
                song.setArtist(parts[1].trim());
                songs.add(song);
            }
        }

        return songs;
    }

}
