package com.recpro.pe.learnsync.units.form;

import com.recpro.pe.learnsync.dtos.forum.thread.CreateThreadDTO;
import com.recpro.pe.learnsync.dtos.forum.thread.ThreadDTO;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.*;
import com.recpro.pe.learnsync.models.Thread;
import com.recpro.pe.learnsync.models.enums.ERole;
import com.recpro.pe.learnsync.repos.forum.ThreadRepository;
import com.recpro.pe.learnsync.services.auth.UserService;
import com.recpro.pe.learnsync.services.forum.ThreadService;
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
public class ThreadServiceTest {

    @Mock private ThreadRepository threadRepository;
    @InjectMocks private ThreadService threadService;
    @Mock private UserService userService;
    @Mock private TopicService topicService;

    private List<Thread> threads;
    private Topic topic;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "jluyo", "jluyoc1@upao.edu.pe", "upao2025", true, false, null, 100, new ArrayList<>(), new ArrayList<>(), new Role(1, ERole.ADMIN, new ArrayList<>()), null);
        topic = new Topic(1, "Java Basics", "Introduction to Java programming", "java-basics", new Category(1, "Programming", "All about programming topics", new ArrayList<>()), new ArrayList<>());
        threads = List.of(
                new Thread(1, "Getting Started with Java", "This thread is for beginners starting with Java.", topic, user, new ArrayList<>()),
                new Thread(2, "Java Collections", "Discussion on Java Collections Framework.", topic, user, new ArrayList<>()),
                new Thread(3, "Java Concurrency", "Let's talk about concurrency and multithreading in Java.", topic, user, new ArrayList<>()),
                new Thread(4, "Java Streams API", "Exploring the Java Streams API.", topic, user, new ArrayList<>()),
                new Thread(5, "Java Best Practices", "Share your best practices for writing Java code.", topic, user, new ArrayList<>())
        );
    }

    @Test
    void testListThreads() {
        // Given
        Pageable pageable = PageRequest.of(0, 3);
        Page<Thread> page = new PageImpl<>(threads.subList(0, 3), pageable, threads.size());

        // When
        when(threadRepository.findAll(pageable)).thenReturn(page);

        List<ThreadDTO> result = threadService.listThreads(pageable);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().getIdThread()).isEqualTo(1);
        assertThat(result.get(1).getTitle()).isEqualTo("Java Collections");
        assertThat(result.getLast().getMessage()).isEqualTo("Let's talk about concurrency and multithreading in Java.");
    }

    @Test
    void testCreateThread() {
        // Given
        CreateThreadDTO createThread = new CreateThreadDTO("New Thread", "New Description", "jluyo", "Java Basics");

        // When
        when(userService.findByUser(anyString())).thenReturn(user);
        when(topicService.getTopic(anyString())).thenReturn(topic);
        ThreadDTO result = threadService.createThread(createThread);

        // Then
        ArgumentCaptor<Thread> threadArgumentCaptor = ArgumentCaptor.forClass(Thread.class);
        verify(threadRepository).save(threadArgumentCaptor.capture());
        assertThat(threadArgumentCaptor.getValue().getTitle()).isEqualTo("New Thread");

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("New Thread");
        assertThat(result.getMessage()).isEqualTo("New Description");
    }

    @Test
    void testCreateThreadWhenUserNotExists() {
        // Given
        CreateThreadDTO createThread = new CreateThreadDTO("New Thread", "New Description", "USER_NOT_EXISTS", "Java Basics");

        // When
        when(userService.findByUser(anyString())).thenThrow(new ResourceNotExistsException("El usuario "+ createThread.getUsername() + " no fue encontrado"));
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> threadService.createThread(createThread));

        // Then
        verify(userService).findByUser(anyString());
        assertThat(ex.getMessage()).isEqualTo("El usuario USER_NOT_EXISTS no fue encontrado");
    }

    @Test
    void testCreateThreadWhenTopicNotExists() {
        // Given
        CreateThreadDTO createThread = new CreateThreadDTO("New Thread", "New Description", "jluyo", "TOPIC-NOT-EXISTS");

        // When
        when(topicService.getTopic(anyString())).thenThrow(new ResourceNotExistsException("El tópico " + Topic.transformName(createThread.getSlug().replaceAll("-", " ")) + " no existe"));
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> threadService.createThread(createThread));

        // Then
        verify(topicService).getTopic(anyString());
        assertThat(ex.getMessage()).isEqualTo("El tópico Topic Not Exists no existe");
    }

    @Test
    void testGetThread() {
        // Given
        Integer idThread = 1;
        Thread thread = threads.getFirst();

        // When
        when(threadRepository.findById(idThread)).thenReturn(Optional.of(thread));
        Thread result = threadService.getThread(idThread);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdThread()).isEqualTo(1);
        assertThat(result.getTitle()).isEqualTo("Getting Started with Java");
        assertThat(result.getMessage()).isEqualTo("This thread is for beginners starting with Java.");
    }

    @Test
    void testGetTopicWhenTopicNotExists() {
        // Given
        Integer idThread = 99999;

        // When
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> threadService.getThread(idThread));

        // Then
        assertThat(ex.getMessage()).isEqualTo("No existe el hilo #99999");
    }

}
