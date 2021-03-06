package com.quichi.blog.QuichiComments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quichi.blog.QuichiComments.controller.CommentController;
import com.quichi.blog.QuichiComments.model.Comment;
import com.quichi.blog.QuichiComments.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class CommentControllerTest {

    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    @InjectMocks
    CommentController commentControllerTest;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(commentControllerTest).build();
    }

   @Test
    public void shouldReturn201WhenCommentCreated() throws Exception {

       Mockito.when(commentService.save(ArgumentMatchers.isA(Comment.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Comment("Firt Comment", new Date(), 1)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

   @Test
    public void shouldReturnAllComments() throws Exception {
        List<Comment> comments = Arrays.asList(new Comment("Fist comment", new Date(), 1),
                new Comment("Second comment", new Date(), 1));

        Mockito.when(commentService.getAll()).thenReturn(comments);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/comments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").isNotEmpty());
    }

    @Test
    public void shouldReturnOneCommentGivenById() throws Exception {
        Comment comment = new Comment("First Comment", new Date(), 1);

        Mockito.when(commentService.findById(comment.getId())).thenReturn(comment);

        mockMvc.perform(MockMvcRequestBuilders
        .get("/comments/{id}", comment.getId())
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(0));
    }

    @Test
    public void shouldDeleteCommentById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/comments/{id}", 1))
                .andExpect(status().isAccepted());
    }

    @Test
    public void shouldUpdateCommentById() throws Exception {

        Mockito.when(commentService.update(ArgumentMatchers.isA(Comment.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Comment("First Comment update", new Date(), 1)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("First Comment update"));

    }

    @Test
    public void shouldReturnAllCommentsGivenAPostId() throws Exception {

        List<Comment> comments = Arrays.asList(
                new Comment("This is a comment one", new Date(), 1),
                new Comment("This is a comment two", new Date(), 1));
        Mockito.when(commentService.getCommentsByPostId(1)).thenReturn(comments);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/comments/blog/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").isNotEmpty());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
