package com.audition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AuditionApplication.class)
class AuditionApplicationTests {

    @InjectMocks
    private AuditionService service;

    @Mock
    private AuditionIntegrationClient auditionIntegrationClient;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    // TODO implement unit test. Note that an applicant should create additional unit tests as required.

    @Test
    void contextLoads() {
    }

    private AuditionPost createAuditionPost(final int userId, int id, String title, String body) {
        AuditionPost post = new AuditionPost();
        post.setUserId(userId);
        post.setId(id);
        post.setTitle(title);
        post.setBody(body);
        return post;
    }

    @Test
    public void givenAuditionIntegrationClient_whenGetPosts_thenReturnListOfPost() {
        AuditionPost post1 = createAuditionPost(1, 1,
            "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
            "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto");

        AuditionPost post2 = createAuditionPost(1, 2, "qui est esse",
            "est rerum tempore vitae\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\nqui aperiam non debitis possimus qui neque nisi nulla");

        List<AuditionPost> expectedResponse = Arrays.asList(post1, post2);

        // When
        doReturn(expectedResponse).when(auditionIntegrationClient).getPosts();
        List<AuditionPost> actualResponse = service.getPosts();

        // Then
        assertEquals(expectedResponse, actualResponse);
    }

    // An utility function to provide Streams of Arguments
    public static Stream<Arguments> provideTestParameters() {
        return Stream.of(
            Arguments.of(1, new AuditionPost(1, 1, "Test Title 1", "Test Body 1")),
            Arguments.of(2, new AuditionPost(1, 2, "Test Title 2", "Test Body 2")),
            Arguments.of(3, new AuditionPost(1, 3, "Test Title 3", "Test Body 3"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestParameters")
    void givenValidId_whenGetPostById_thenReturnPost(int postId, AuditionPost expectedPost) {

        doReturn(expectedPost).when(auditionIntegrationClient)
            .getPostById(String.valueOf(postId));

        // Execute
        AuditionPost result = service.getPostById(String.valueOf(postId));

        // Assert
        assertEquals(expectedPost, result);
        assertNotNull(result, "get result should not be null");
    }

    private Comment createComment(int postId, int id, String name, String email, String body) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setId(id);
        comment.setName(name);
        comment.setEmail(email);
        comment.setBody(body);
        return comment;
    }

    @Test
    public void givenAuditionIntegrationClient_whenGetCommentsByPostIdWithQueryParam_thenReturnListOfComments() {
        int postId = 1;
        Comment comment1 = createComment(1, 1, "id labore ex et quam laborum", "Eliseo@gardner.biz",
            "laudantium enim quasi est quidem magnam voluptate ipsam eos\n" +
                "tempora quo necessitatibus\n" +
                "dolor quam autem quasi\n" +
                "reiciendis et nam sapiente accusantium");

        Comment comment2 = createComment(1, 2, "quo vero reiciendis velit similique earum", "Jayne_Kuhic@sydney.com",
            "est natus enim nihil est dolore omnis voluptatem numquam\n" +
                "et omnis occaecati quod ullam at\n" +
                "voluptatem error expedita pariatur\n" +
                "nihil sint nostrum voluptatem reiciendis et");

        List<Comment> expectedResponse = Arrays.asList(comment1, comment2);

        doReturn(expectedResponse).when(auditionIntegrationClient).getCommentsByPostIdWithQueryParam(postId);
        List<Comment> actualResponse = service.getCommentsByPostId(postId);

        assertEquals(expectedResponse, actualResponse);
        assertEquals(expectedResponse.size(), actualResponse.size());
    }

    @Test
    public void givenAuditionIntegrationClient_whenGetPosts_thenThrowsException() {
        // Setup
        doThrow(RuntimeException.class).when(auditionIntegrationClient).getPosts();

        // Assert
        assertThrows(RuntimeException.class, () -> {
            // Execute
            service.getPosts();
        });
    }
}
