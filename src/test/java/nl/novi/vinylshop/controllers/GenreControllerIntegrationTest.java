package nl.novi.vinylshop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.vinylshop.dtos.genre.GenreRequestDTO;
import nl.novi.vinylshop.dtos.genre.GenreResponseDTO;
import nl.novi.vinylshop.repositories.GenreRepository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest()
public class GenreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getGenreById_shouldReturnNotFound_whenGenreDoesNotExist() throws Exception {
        mockMvc.perform(get("/genres/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllGenres() throws Exception {
        mockMvc.perform(get("/genres")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createGenre() throws Exception {
        // Arrange
        GenreRequestDTO genreRequestDTO = new GenreRequestDTO();
        genreRequestDTO.setName("Blues");
        genreRequestDTO.setDescription("Blues description");

        // Act & Assert
        var response =mockMvc.perform(post("/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.name").value("Blues"))
                .andExpect(jsonPath("$.description").value("Blues description"));
    }

    @Test
    void updateGenre() throws Exception {
        // Arrange
        GenreRequestDTO genreRequestDTO = new GenreRequestDTO();
        genreRequestDTO.setName("Rock");
        genreRequestDTO.setDescription("Something Loud");

        // Act & Assert
        var response =mockMvc.perform(put("/genres/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value("Rock"))
                .andExpect(jsonPath("$.description").value("Something Loud"))
                .andReturn();

        GenreResponseDTO genreResponseDTO = objectMapper.readValue(response.getResponse().getContentAsString(), GenreResponseDTO.class);
        Assertions.assertEquals("Rock", genreResponseDTO.getName());
    }

    @Test
    void updateGenre_NotFound() throws Exception {
        // Arrange
        GenreRequestDTO genreRequestDTO = new GenreRequestDTO();
        genreRequestDTO.setName("Rock");
        genreRequestDTO.setDescription("Something Loud");

        // Act & Assert
        var response =mockMvc.perform(put("/genres/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteGenre() throws Exception {
        mockMvc.perform(delete("/genres/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}