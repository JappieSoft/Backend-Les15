package nl.novi.vinylshop.services;

import nl.novi.vinylshop.dtos.album.AlbumExtendedResponseDTO;
import nl.novi.vinylshop.dtos.album.AlbumRequestDTO;
import nl.novi.vinylshop.dtos.album.AlbumResponseDTO;
import nl.novi.vinylshop.entities.*;
import nl.novi.vinylshop.exceptions.RecordNotFoundException;
import nl.novi.vinylshop.mappers.AlbumDTOMapper;
import nl.novi.vinylshop.mappers.AlbumExtendedDTOMapper;
import nl.novi.vinylshop.repositories.AlbumRepository;
import nl.novi.vinylshop.repositories.ArtistRepository;
import nl.novi.vinylshop.repositories.GenreRepository;
import nl.novi.vinylshop.repositories.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock
    AlbumRepository albumRepository;

    AlbumDTOMapper albumDTOMapper = Mockito.mock(AlbumDTOMapper.class);
    AlbumExtendedDTOMapper albumExtendedDTOMapper = Mockito.mock(AlbumExtendedDTOMapper.class);

    GenreEntity mockGenre;
    PublisherEntity mockPublisher;
    AlbumEntity albumEntity;
    ArtistEntity mockArtist;

    @Mock
    GenreRepository genreRepository;
    @Mock
    PublisherRepository publisherRepository;

    @InjectMocks
    AlbumService albumService;

    AlbumEntity mockEntity;
    AlbumResponseDTO mockResponseDTO;
    AlbumExtendedResponseDTO mockExtendedDto;
    AlbumRequestDTO mockRequestDTO;

    @BeforeEach
    void setUp() {
        mockEntity = new AlbumEntity();
        mockEntity.setId(1L);
        mockEntity.setTitle("Test Album");
        mockEntity.setReleaseYear(2026);

        mockGenre = new GenreEntity();
        mockGenre.setId(1L);
        mockGenre.setName("Classical");
        mockEntity.setGenre(mockGenre);

        mockPublisher = new PublisherEntity();
        mockPublisher.setId(1L);
        mockPublisher.setName("Vinyl");
        mockEntity.setPublisher(mockPublisher);

        mockArtist = new ArtistEntity();
        Set<ArtistEntity> artists = new HashSet<>();
        mockArtist.setId(1L);
        mockArtist.setName("Bach");
        artists.add(mockArtist);
        mockEntity.setArtists(artists);

        mockResponseDTO = new AlbumResponseDTO();
        mockResponseDTO.setId(1L);
        mockResponseDTO.setTitle("Test Album");
        mockResponseDTO.setReleaseYear(2025);

/*        mockExtendedDto = new AlbumExtendedResponseDTO();
        mockExtendedDto.setId(1L);
        mockExtendedDto.setTitle("Test Album");
        mockExtendedDto.setReleaseYear(2025);*/

        mockRequestDTO = new AlbumRequestDTO();
        mockRequestDTO.setTitle("Test Album");
        mockRequestDTO.setReleaseYear(2025);

    }

    @Test
    @DisplayName("findAllAlbums")
    void findAllAlbums() {//Arrange
        List<AlbumEntity> entities = List.of(mockEntity);
        List<AlbumResponseDTO> dtos = List.of(mockResponseDTO);
        given(albumRepository.findAll()).willReturn(entities);
        given(albumDTOMapper.mapToDto(entities)).willReturn(dtos);

        //Act
        List<AlbumResponseDTO> result = albumService.findAllAlbums();

        //Assert
        assertThat(result).hasSize(1);
        verify(albumRepository).findAll();
        verify(albumDTOMapper).mapToDto(anyList());
    }

    @Test
    @DisplayName("findAlbumById returns a existing ID")
    void findAlbumById() {
        //Arrange
        Long id = 1L;
        given(albumRepository.findById(id)).willReturn(Optional.of(mockEntity));
        given(albumExtendedDTOMapper.mapToDto(mockEntity)).willReturn(mockExtendedDto);

        //Act
        AlbumExtendedResponseDTO result = albumService.findAlbumById(id);

        //Assert
        assertNotNull(result);
        assertEquals(mockResponseDTO.getId(), result.getId());
        verify(albumRepository).findById(id);
        verify(albumExtendedDTOMapper).mapToDto(mockEntity);
    }

    @Test
    @DisplayName("createAlbum with Genre & Publisher Null")
    void createAlbum_withGenreAndPublisherNull() {
        //Arrange
        mockRequestDTO.setGenreId(null);
        mockRequestDTO.setPublisherId(null);
        given(albumDTOMapper.mapToEntity(mockRequestDTO)).willReturn(mockEntity);
        given(albumRepository.save(mockEntity)).willReturn(mockEntity);
        given(albumDTOMapper.mapToDto(mockEntity)).willReturn(mockResponseDTO);

        //Act
        AlbumResponseDTO result = albumService.createAlbum(mockRequestDTO);

        //Assert
        assertEquals("Test Album", result.getTitle());
        verify(albumRepository).save(mockEntity);
        verify(albumDTOMapper).mapToDto(mockEntity);
    }

    @Test
    @DisplayName("createAlbum with Genre & Publisher id")
    void createAlbum_withGenreAndPublisherId() {
        //Arrange
        mockRequestDTO.setGenreId(1L);
        mockRequestDTO.setPublisherId(1L);

        given(albumDTOMapper.mapToEntity(mockRequestDTO)).willReturn(mockEntity);
        given(albumRepository.save(mockEntity)).willReturn(mockEntity);
        given(albumDTOMapper.mapToDto(mockEntity)).willReturn(mockResponseDTO);
        given(publisherRepository.findById(1L)).willReturn(Optional.of(mockPublisher));
        given(genreRepository.findById(1L)).willReturn(Optional.of(mockGenre));

        //Act
        AlbumResponseDTO result = albumService.createAlbum(mockRequestDTO);

        //Assert
        assertEquals("Test Album", result.getTitle());
        verify(albumRepository).save(mockEntity);
        verify(albumDTOMapper).mapToDto(mockEntity);
        verify(publisherRepository).findById(1L);
        verify(genreRepository).findById(1L);
    }


    @Test
    @DisplayName("updateAlbum from RequestDTO")
    void updateAlbum() {
        //Arrange
        Long id = 1L;

        //New updated RequestDTO
        AlbumRequestDTO updateRequest = new AlbumRequestDTO();
        updateRequest.setTitle("New Title");
        updateRequest.setReleaseYear(1919);
        updateRequest.setPublisherId(1L);
        updateRequest.setGenreId(1L);

        //Get mockEntity
        given(albumRepository.findById(id)).willReturn(Optional.of(mockEntity));

        //New updated Entity
        AlbumResponseDTO updatedResponse = new AlbumResponseDTO();
        updatedResponse.setId(id);
        updatedResponse.setTitle("New Title");
        updatedResponse.setReleaseYear(1919);

        given(albumRepository.findById(id)).willReturn(Optional.of(mockEntity));
        given(publisherRepository.findById(1L)).willReturn(Optional.of(mockPublisher));
        given(genreRepository.findById(1L)).willReturn(Optional.of(mockGenre));
        given(albumRepository.save(mockEntity)).willReturn(mockEntity);
        given(albumDTOMapper.mapToDto(mockEntity)).willReturn(updatedResponse);



        //Act
        AlbumResponseDTO result = albumService.updateAlbum(id, updateRequest);

        //Assert
        assertEquals("New Title", result.getTitle());
        verify(albumRepository).findById(id);
        verify(albumRepository).save(mockEntity);
        verify(albumDTOMapper).mapToDto(mockEntity);
    }

    @Test
    @DisplayName("deleteAlbum by id 1 = id not existing")
    void deleteAlbum_idNotExisting() {
        //Arrange
        Long id = 1L;

        //Act
        assertThrows(RecordNotFoundException.class, () -> albumService.deleteAlbum(id));

        //Assert
        verify(albumRepository).findById(id);
        verify(albumRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("deleteAlbum by id 1 = id existing")
    void deleteAlbum_idExisting() {
        //Arrange
        Long id = 1L;
        List<StockEntity> mockStockEntity = List.of();
        mockEntity.setStockItems(mockStockEntity);
        given(albumRepository.findById(id)).willReturn(Optional.of(mockEntity));
        /*given(mockStockEntity.isEmpty()).willReturn(true);*/

        //Act
        albumService.deleteAlbum(id);

        //Assert
        verify(albumRepository).findById(id);
        verify(albumRepository).deleteById(id);
    }

    @Test
    @DisplayName("deleteAlbum by id 1 = id has stock")
    void deleteAlbum_idHasStock() {
        // Arrange
        Long id = 1L;
        List<StockEntity> mockStockEntity = List.of(new StockEntity());
        mockEntity.setStockItems(mockStockEntity);
        when(albumRepository.findById(id)).thenReturn(Optional.ofNullable(mockEntity));

        // Act
        albumService.deleteAlbum(id);

        // Assert
        verify(albumRepository).findById(id);
        verify(albumRepository, never()).deleteById(id);
    }


    @Test
    void linkArtist() {
    }

    @Test
    void unlinkArtist() {
    }

    @Test
    @DisplayName("getAlbumsWithStock stock true")
    void getAlbumsWithStock_True() {
        // Arrange
        Boolean stock = true;
        List<StockEntity> mockStockEntity = List.of();
        mockStockEntity = List.of(new StockEntity());
        mockEntity.setStockItems(mockStockEntity);
        mockEntity.setStockItems(mockStockEntity);
        List<AlbumEntity> stockedAlbums = List.of(mockEntity);
        List<AlbumResponseDTO> expectedDtos = List.of(mockResponseDTO);

        given(albumRepository.findByStockItemsNotEmpty()).willReturn(stockedAlbums);
        given(albumDTOMapper.mapToDto(stockedAlbums)).willReturn(expectedDtos);

        // Act
        List<AlbumResponseDTO> result = albumService.getAlbumsWithStock(stock);

        // Assert
        assertThat(result).hasSize(1);
        verify(albumRepository).findByStockItemsNotEmpty();
        verify(albumDTOMapper).mapToDto(stockedAlbums);
        verify(albumRepository, never()).findByStockItemsEmpty();
    }

    @Test
    @DisplayName("getAlbumsWithStock stock false")
    void getAlbumsWithStock_False() {
        // Arrange
        Boolean stock = false;

        AlbumEntity emptyStockAlbum = new AlbumEntity();
        emptyStockAlbum.setId(2L);
        emptyStockAlbum.setTitle("No Stock Album");
        emptyStockAlbum.setReleaseYear(2026);
        emptyStockAlbum.setStockItems(List.of());

        List<AlbumEntity> emptyStockAlbums = List.of(emptyStockAlbum);
        List<AlbumResponseDTO> expectedDtos = List.of(mockResponseDTO);

        given(albumRepository.findByStockItemsEmpty()).willReturn(emptyStockAlbums);
        given(albumDTOMapper.mapToDto(emptyStockAlbums)).willReturn(expectedDtos);

        // Act
        List<AlbumResponseDTO> result = albumService.getAlbumsWithStock(stock);

        // Assert
        assertThat(result).hasSize(1);
        verify(albumRepository).findByStockItemsEmpty();
        verify(albumDTOMapper).mapToDto(emptyStockAlbums);
        verify(albumRepository, never()).findByStockItemsNotEmpty();
    }
}