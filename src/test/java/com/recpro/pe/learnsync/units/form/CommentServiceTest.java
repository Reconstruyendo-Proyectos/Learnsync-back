package com.recpro.pe.learnsync.units.form;

import com.recpro.pe.learnsync.dtos.forum.comment.CommentDTO;
import com.recpro.pe.learnsync.dtos.forum.comment.CreateCommentDTO;
import com.recpro.pe.learnsync.exceptions.ResourceNotExistsException;
import com.recpro.pe.learnsync.models.*;
import com.recpro.pe.learnsync.models.Thread;
import com.recpro.pe.learnsync.models.enums.ERole;
import com.recpro.pe.learnsync.repos.forum.CommentRepository;
import com.recpro.pe.learnsync.services.auth.UserService;
import com.recpro.pe.learnsync.services.forum.CommentService;
import com.recpro.pe.learnsync.services.forum.ThreadService;
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

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentService commentService;
    @Mock private UserService userService;
    @Mock private ThreadService threadService;

    private List<Comment> comments;
    private Thread thread;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "jluyo", "jluyoc1@upao.edu.pe", "upao2025", true, false, null, 100, new ArrayList<>(), new ArrayList<>(), new Role(1, ERole.ADMIN, new ArrayList<>()), null);
        Topic topic = new Topic(1, "Java Basics", "Introduction to Java programming", "java-basics",  new Category(1, "Programming", "All about programming topics", new ArrayList<>()), new ArrayList<>());
        thread = new Thread(1, "Getting Started with Java", "This thread is for beginners starting with Java.", topic, user, new ArrayList<>());

        comments = List.of(
                new Comment(1, "This is a great starting point!", thread, user),
                new Comment(2, "I have a question about loops.", thread, user),
                new Comment(3, "Can anyone explain inheritance?", thread, user),
                new Comment(4, "What are the best practices for exceptions?", thread, user),
                new Comment(5, "Where can I find more resources?", thread, user)
        );
    }

    @Test
    void testListComments() {
        // Given
        Pageable pageable = PageRequest.of(0, 3);
        Page<Comment> page = new PageImpl<>(comments.subList(0, 3), pageable, comments.size());

        // When
        when(commentRepository.findAll(pageable)).thenReturn(page);

        List<CommentDTO> result = commentService.listComments(pageable);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().getIdComment()).isEqualTo(1);
        assertThat(result.get(1).getMessage()).isEqualTo("I have a question about loops.");
    }

    @Test
    void testCreateComment() {
        // Given
        CreateCommentDTO createComment = new CreateCommentDTO("New Comment", "jluyo", 1);

        // When
        when(userService.findByUser(anyString())).thenReturn(user);
        when(threadService.getThread(anyInt())).thenReturn(thread);
        CommentDTO result = commentService.createComment(createComment);

        // Then
        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(commentArgumentCaptor.capture());
        assertThat(commentArgumentCaptor.getValue().getMessage()).isEqualTo("New Comment");

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("New Comment");
    }

    @Test
    void testCreateCommentWhenUserNotExists() {
        // Given
        CreateCommentDTO createComment = new CreateCommentDTO("New Comment", "USER_NOT_EXISTS", 1);

        // When
        when(userService.findByUser(anyString())).thenThrow(new ResourceNotExistsException("El usuario "+ createComment.getUsername() + " no fue encontrado"));
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> commentService.createComment(createComment));

        // Then
        verify(userService).findByUser(anyString());
        assertThat(ex.getMessage()).isEqualTo("El usuario USER_NOT_EXISTS no fue encontrado");
    }

    @Test
    void testCreateThreadWhenTopicNotExists() {
        // Given
        CreateCommentDTO createComment = new CreateCommentDTO("New Comment", "jluyo", 99999);

        // When
        when(threadService.getThread(anyInt())).thenThrow(new ResourceNotExistsException("No existe el hilo #"+createComment.getIdThread()));
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> commentService.createComment(createComment));

        // Then
        verify(threadService).getThread(anyInt());
        assertThat(ex.getMessage()).isEqualTo("No existe el hilo #99999");
    }
}
