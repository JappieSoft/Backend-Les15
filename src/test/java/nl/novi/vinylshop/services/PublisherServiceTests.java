package nl.novi.vinylshop;

import nl.novi.vinylshop.dtos.publisher.PublisherResponseDTO;
import nl.novi.vinylshop.entities.PublisherEntity;
import nl.novi.vinylshop.mappers.PublisherDTOMapper;
import nl.novi.vinylshop.repositories.AlbumRepository;
import nl.novi.vinylshop.repositories.PublisherRepository;
import nl.novi.vinylshop.services.PublisherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTests {

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private PublisherDTOMapper publisherDTOMapper;

    @InjectMocks
    private PublisherService publisherService;

    @Test
    void findAllPublishers_shouldReturnListOfPublisherResponseDTOs() {
        // Arrange
        PublisherResponseDTO publisherdto1 = new PublisherResponseDTO();
        publisherdto1.setName("Updated Name");
        publisherdto1.setAddress("Updated Address");
        publisherdto1.setContactDetails("Updated Contact");

        PublisherResponseDTO publisherdto2 = new PublisherResponseDTO();
        publisherdto2.setName("Updated Name");
        publisherdto2.setAddress("Updated Address");
        publisherdto2.setContactDetails("Updated Contact");

        PublisherEntity publisherEntity1 = new PublisherEntity();
        publisherEntity1.setName("Old Name");
        publisherEntity1.setAddress("Old Address");
        publisherEntity1.setContactDetails("Old Contact");

        PublisherEntity publisherEntity2 = new PublisherEntity();
        publisherEntity2.setName("Old Name");
        publisherEntity2.setAddress("Old Address");
        publisherEntity2.setContactDetails("Old Contact");


        when(publisherRepository.findAll()).thenReturn(Arrays.asList(publisherEntity1, publisherEntity2));
        when(publisherDTOMapper.mapToDto(List.of(publisherEntity1, publisherEntity2))).thenReturn(List.of(publisherdto1, publisherdto2));

        // Act
        List<PublisherResponseDTO> result = publisherService.findAllPublishers();

        // Assert
        assertEquals(2, result.size());
        assertEquals(publisherdto1, result.get(0));
        assertEquals(publisherdto2, result.get(1));
        verify(publisherRepository, times(1)).findAll();
        verify(publisherDTOMapper, times(1)).mapToDto(List.of(publisherEntity1, publisherEntity2));
    }

    @Test
    void findPublisherById() {
    }

    @Test
    void createPublisher() {
    }

    @Test
    void updatePublisher() {
    }

    @Test
    void deletePublisher() {
    }
}