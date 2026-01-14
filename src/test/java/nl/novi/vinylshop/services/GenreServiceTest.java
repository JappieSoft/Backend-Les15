package nl.novi.vinylshop.services;

import nl.novi.vinylshop.dtos.genre.GenreRequestDTO;
import nl.novi.vinylshop.dtos.genre.GenreResponseDTO;
import nl.novi.vinylshop.entities.AlbumEntity;
import nl.novi.vinylshop.entities.GenreEntity;
import nl.novi.vinylshop.mappers.GenreDTOMapper;
import nl.novi.vinylshop.repositories.AlbumRepository;
import nl.novi.vinylshop.repositories.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    GenreRepository genreRepository;
    @Mock
    GenreDTOMapper genreDTOMapper;
    @Mock
    AlbumRepository albumRepository;
    @Mock
    AlbumEntity albumEntity;

    @InjectMocks
    GenreService genreService;

    GenreEntity mockEntity;
    GenreResponseDTO mockResponseDTO;
    GenreRequestDTO mockRequestDTO;

    @Captor
    ArgumentCaptor<AlbumEntity> albumCaptor;

    @BeforeEach
    void setUp() {
        mockEntity = new GenreEntity();
        mockEntity.setId(1L);
        mockEntity.setName("test");
        mockEntity.setDescription("testing");

        mockResponseDTO = new GenreResponseDTO();
        mockResponseDTO.setId(1L);
        mockResponseDTO.setName("test");
        mockResponseDTO.setDescription("testing");

        mockRequestDTO = new GenreRequestDTO();
        mockRequestDTO.setName("test");
        mockRequestDTO.setDescription("testing");
    }

    @Test
    @DisplayName("Return list of Genres")
    void findAllGenres() {
        //Arrange
        List<GenreEntity> entities = List.of(mockEntity);
        List<GenreResponseDTO> dtos = List.of(mockResponseDTO);
        given(genreRepository.findAll()).willReturn(entities);
        given(genreDTOMapper.mapToDto(entities)).willReturn(dtos);

        //Act
        List<GenreResponseDTO> result = genreService.findAllGenres();

        //Assert
        assertThat(result).hasSize(1);
        verify(genreRepository).findAll();
        verify(genreDTOMapper).mapToDto(anyList());
    }

    @Test
    @DisplayName("findGenreById returns a existing ID")
    void findGenreById() {
        //Arrange
        Long id = 1L;
        given(genreRepository.findById(id)).willReturn(Optional.of(mockEntity));
        given(genreDTOMapper.mapToDto(mockEntity)).willReturn(mockResponseDTO);

        //Act
        GenreResponseDTO result = genreService.findGenreById(id);

        //Assert
        assertNotNull(result);
        assertEquals(mockResponseDTO.getId(), result.getId());
        verify(genreRepository).findById(id);
        verify(genreDTOMapper).mapToDto(mockEntity);

    }

    @Test
    @DisplayName("createGenre from RequestDTO")
    void createGenre() {
        //Arrange
        given(genreDTOMapper.mapToEntity(mockRequestDTO)).willReturn(mockEntity);
        given(genreRepository.save(mockEntity)).willReturn(mockEntity);
        given(genreDTOMapper.mapToDto(mockEntity)).willReturn(mockResponseDTO);

        //Act
        GenreResponseDTO result = genreService.createGenre(mockRequestDTO);

        //Assert
        assertEquals("test", result.getName());
        verify(genreRepository).save(mockEntity);
        verify(genreDTOMapper).mapToDto(mockEntity);
    }

    @Test
    @DisplayName("updateGenre from RequestDTO")
    void updateGenre() {
        //Arrange
        Long id = 1L;

        //New updated RequestDTO
        GenreRequestDTO updateRequest = new GenreRequestDTO();
        updateRequest.setName("changedName");
        updateRequest.setDescription("changedDescription");

        //Get mockEntity
        given(genreRepository.findById(id)).willReturn(Optional.of(mockEntity));

        //New updated Entity
        GenreResponseDTO updatedResponse = new GenreResponseDTO();
        updatedResponse.setId(id);
        updatedResponse.setName("changedName");
        updatedResponse.setDescription("changedDescription");

        given(genreRepository.save(mockEntity)).willReturn(mockEntity);
        given(genreDTOMapper.mapToDto(mockEntity)).willReturn(updatedResponse);

        //Act
        GenreResponseDTO result = genreService.updateGenre(id, updateRequest);

        //Assert
        assertEquals("changedName", result.getName());
        verify(genreRepository).findById(id);
        verify(genreRepository).save(mockEntity);
        verify(genreDTOMapper).mapToDto(mockEntity);
    }

    @Test
    @DisplayName("deleteGenre by id 1")
    void deleteGenre() {
        //Arrange
        Long id = 1L;
        albumEntity.setGenre(mockEntity);
        given(albumRepository.findByGenre_Id(id)).willReturn(List.of(albumEntity));

        //Act
        genreService.deleteGenre(id);

        //Assert
        verify(albumRepository).save(albumCaptor.capture());
        assertNull(albumCaptor.getValue().getGenre());
    }
}