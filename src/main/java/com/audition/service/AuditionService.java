package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class represents a service that provides methods for interacting with audition posts and comments
 */
@Service
public class AuditionService {

    @Autowired
    private AuditionIntegrationClient auditionIntegrationClient;

    /**
     * Returns a list of audition posts.
     *
     * @return List of AuditionPost objects
     */
    public List<AuditionPost> getPosts() {
        return auditionIntegrationClient.getPosts();
    }

    /**
     * Retrieves an audition post by its ID.
     *
     * @param postId The ID of the post to retrieve
     * @return The audition post with the specified ID
     */
    public AuditionPost getPostById(final String postId) {
        return auditionIntegrationClient.getPostById(postId);
    }

    /**
     * Retrieves a list of comments for a given post ID.
     *
     * @param postId The ID of the post to retrieve comments for
     * @return The list of comments for the specified post ID
     */
    public List<Comment> getCommentsByPostId(final int postId) {
        return auditionIntegrationClient.getCommentsByPostIdWithQueryParam(postId);
    }
}
