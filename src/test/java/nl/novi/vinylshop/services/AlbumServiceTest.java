package nl.novi.vinylshop.services;

import nl.novi.vinylshop.dtos.album.AlbumRequestDTO;
import nl.novi.vinylshop.dtos.album.AlbumResponseDTO;
import nl.novi.vinylshop.entities.AlbumEntity;
import nl.novi.vinylshop.entities.GenreEntity;
import nl.novi.vinylshop.entities.PublisherEntity;
import nl.novi.vinylshop.mappers.AlbumDTOMapper;
import nl.novi.vinylshop.mappers.AlbumExtendedDTOMapper;
import nl.novi.vinylshop.repositories.AlbumRepository;
import nl.novi.vinylshop.repositories.ArtistRepository;
import nl.novi.vinylshop.repositories.GenreRepository;
import nl.novi.vinylshop.repositories.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AlbumDTOMapper albumDTOMapper;

    @Mock
    private AlbumExtendedDTOMapper albumExtendedDTOMapper;

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private AlbumService albumService;

    @Test
    void findAllAlbums() {



    }

    @Test
    void findAlbumById() {
    }

    @Test
    void createAlbum(){
        //arrange
        AlbumEntity albumEntity = new AlbumEntity();

        AlbumRequestDTO albumModel = new AlbumRequestDTO();
        albumModel.setGenreId(1L);
        albumModel.setPublisherId(1L);
        albumModel.setTitle("test");
        albumModel.setReleaseYear(2020);

        GenreEntity genreEntity = new GenreEntity();
        genreEntity.setId(1L);
        genreEntity.setName("test");

        PublisherEntity publisherEntity = new PublisherEntity();
        publisherEntity.setId(1L);
        publisherEntity.setName("test");

        Mockito.when(genreRepository.findById(1L)).thenReturn(java.util.Optional.of(genreEntity));
        Mockito.when(publisherRepository.findById(1L)).thenReturn(java.util.Optional.of(publisherEntity));

        Mockito.when(albumRepository.save(any())).thenReturn(albumEntity);

        //act

        AlbumResponseDTO result = albumService.createAlbum(albumModel);

        //assert

        assert(result != null);

    }

    @Test
    void updateAlbum() {
    }

    @Test
    void deleteAlbum() {
    }

    @Test
    void linkArtist() {
    }

    @Test
    void unlinkArtist() {
    }

    @Test
    void getAlbumsWithStock() {
    }
}