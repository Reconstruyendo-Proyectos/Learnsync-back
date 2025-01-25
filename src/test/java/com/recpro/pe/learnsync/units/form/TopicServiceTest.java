package com.recpro.pe.learnsync.units.form;

import com.recpro.pe.learnsync.dtos.forum.topic.CreateTopicDTO;
import com.recpro.pe.learnsync.dtos.forum.topic.TopicDTO;
import com.recpro.pe.learnsync.exceptions.ResourceAlreadyExistsException;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.Category;
import com.recpro.pe.learnsync.models.Topic;
import com.recpro.pe.learnsync.repos.forum.TopicRepository;
import com.recpro.pe.learnsync.services.forum.CategoryService;
import com.recpro.pe.learnsync.services.forum.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTest {

    @Mock private TopicRepository topicRepository;
    @InjectMocks private TopicService topicService;
    @Mock private CategoryService categoryService;

    private List<Topic> topics;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category(1, "General", "General discussion topics", new ArrayList<>());

        topics = List.of(
                new Topic(1, "Java Basics", "Introduction to Java programming", "java-basics", category, new ArrayList<>()),
                new Topic(2, "Advanced Java", "Deep dive into Java programming", "advanced-java", category, new ArrayList<>()),
                new Topic(3, "Spring Framework", "Introduction to Spring Framework", "spring-framework", category, new ArrayList<>()),
                new Topic(4, "Machine Learning", "Introduction to machine learning", "machine-learning", category, new ArrayList<>()),
                new Topic(5, "Deep Learning", "Understanding deep learning algorithms", "deep-learning", category, new ArrayList<>())
        );
    }

    @Test
    void testListTopics() {
        // Given
        Pageable pageable = PageRequest.of(0, 3);
        Page<Topic> page = new PageImpl<>(topics.subList(0, 3), pageable, topics.size());

        // When
        when(topicRepository.findAll(pageable)).thenReturn(page);

        List<TopicDTO> result = topicService.listTopics(pageable);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().getIdTopic()).isEqualTo(1);
        assertThat(result.get(1).getName()).isEqualTo("Advanced Java");
        assertThat(result.getLast().getDescription()).isEqualTo("Introduction to Spring Framework");
        assertThat(result.getFirst().getSlug()).isEqualTo("java-basics");
    }

    @Test
    void testCreateTopic() {
        // Given
        CreateTopicDTO createTopic = new CreateTopicDTO("New Topic", "New Description", "General");

        // When
        when(categoryService.getCategory(anyString())).thenReturn(category);
        TopicDTO result = topicService.createTopic(createTopic);

        // Then
        ArgumentCaptor<Topic> topicArgumentCaptor = ArgumentCaptor.forClass(Topic.class);
        verify(topicRepository).save(topicArgumentCaptor.capture());
        assertThat(topicArgumentCaptor.getValue().getSlug()).isEqualTo("new-topic");

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("New Topic");
        assertThat(result.getDescription()).isEqualTo("New Description");
    }

    @Test
    void testCreateTopicWhenTopicNameExists() {
        // Given
        CreateTopicDTO createTopic = new CreateTopicDTO("Java Basics", "New Description", "General");

        // When
        when(topicRepository.existsTopicByName(anyString())).thenReturn(true);
        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class, () -> topicService.createTopic(createTopic));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El tópico Java Basics ya existe");
    }

    @Test
    void testCreateTopicWhenCategoryNotExists() {
        // Given
        CreateTopicDTO createTopic = new CreateTopicDTO("New Topic", "New Description", "CATEGORY_NOT_EXISTS");

        // When
        when(categoryService.getCategory(anyString())).thenThrow(new ResourceNotExistsException("La categoria "+createTopic.getCategoryName()+" no existe"));
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> topicService.createTopic(createTopic));

        // Then
        verify(categoryService).getCategory(anyString());
        assertThat(ex.getMessage()).isEqualTo("La categoria CATEGORY_NOT_EXISTS no existe");
    }

    @Test
    void testGetTopic() {
        // Given
        String slug = "java-basics";
        Topic topic = topics.getFirst();

        // When
        when(topicRepository.findBySlug(slug)).thenReturn(Optional.of(topic));
        Topic result = topicService.getTopic(slug);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdTopic()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Java Basics");
        assertThat(result.getDescription()).isEqualTo("Introduction to Java programming");
        assertThat(result.getSlug()).isEqualTo("java-basics");
    }

    @Test
    void testGetTopicWhenTopicNotExists() {
        // Given
        String slug = "TOPIC-NOT-EXISTS";

        // When
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> topicService.getTopic(slug));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El tópico Topic Not Exists no existe");
    }
}
