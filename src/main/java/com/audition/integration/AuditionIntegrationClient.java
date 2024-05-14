package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AuditionIntegrationClient {

    final private RestTemplate restTemplate;

    public AuditionIntegrationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<AuditionPost> getPosts() {
        // TODO make RestTemplate call to get Posts from https://jsonplaceholder.typicode.com/posts
        String url = "https://jsonplaceholder.typicode.com/posts";
        return restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<AuditionPost>>() {
            }
        ).getBody();
    }

    public AuditionPost getPostById(final String id) {
        // TODO get post by post ID call from https://jsonplaceholder.typicode.com/posts/
        try {
            String url = "https://jsonplaceholder.typicode.com/posts/{id}";
            return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AuditionPost>() {
                },
                id
            ).getBody();
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + id, "Resource Not Found",
                    404);
            } else {
                // TODO Find a better way to handle the exception so that the original error message is not lost. Feel free to change this function.
                // preserving the original exception, `e`
                throw new SystemException("An error occurred while trying to fetch the post: " + e.getMessage(), e);
                // throw new SystemException("Unknown Error message");
            }
        }
    }

    // TODO Write a method GET comments for a post from https://jsonplaceholder.typicode.com/posts/{postId}/comments -
    //  the comments must be returned as part of the post.
    public List<Comment> getCommentsByPostIdWithPathVariable(int postId) {
        String url = "https://jsonplaceholder.typicode.com/posts/{postId}/comments";
        try {
            return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Comment>>() {
                },
                postId
            ).getBody();
        } catch (final HttpClientErrorException e) {
            throw new SystemException(
                "An error occurred while trying to fetch the comments for the post with ID " + postId + ": "
                    + e.getMessage(), e);
        }
    }

    // TODO write a method. GET comments for a particular Post from https://jsonplaceholder.typicode.com/comments?postId={postId}.
    // The comments are a separate list that needs to be returned to the API consumers. Hint: this is not part of the AuditionPost pojo.
    // from the comments.json file, filter out the one post identified with postId.
    //for one postId, there are many comment objects, so a list must be returned.
    public List<Comment> getCommentsByPostIdWithQueryParam(int postId) {
        String baseUrl = "https://jsonplaceholder.typicode.com/comments";
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(baseUrl)
                .queryParam("postId", postId);

            return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Comment>>() {
                }
            ).getBody();
        } catch (final HttpClientErrorException e) {
            throw new SystemException(
                "An error occurred while trying to fetch the comments for the post with ID " + postId
                    + " using query parameters: " + e.getMessage(), e);
        }
    }
}
