package com.audition.web;

import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuditionController {

    @Autowired
    AuditionService auditionService;

    // TODO Add a query param that allows data filtering. The intent of the filter is at developers discretion.
    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(
        @RequestParam(required = false, defaultValue = "-1") final int userId) {

        // TODO Add logic that filters response data based on the query param
        List<AuditionPost> allPosts = auditionService.getPosts();

        if (userId != -1) {
            return allPosts.stream()
                .filter(post -> post.getUserId() == userId)
                .collect(Collectors.toList());
        }

        return allPosts;
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<AuditionPost> getPostsById(@PathVariable("id") final String postId) {
        final AuditionPost auditionPost = auditionService.getPostById(postId);

        // TODO Add input validation
        // check if postId is null, empty, or not a valid number
        if (postId == null || postId.isEmpty() || !postId.matches("\\d+")) {
            return new ResponseEntity<AuditionPost>(HttpStatus.BAD_REQUEST);
        }

//        // convert postId to int (assuming that post IDs are integers in your system)
//        int postIdInt = Integer.parseInt(postId);
//
//        // check if the post exists
//        final AuditionPost auditionPost = auditionService.getPostById(postIdInt);
//        if (auditionPost == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }

        return new ResponseEntity<AuditionPost>(auditionPost, HttpStatus.OK);
    }

    // TODO Add additional methods to return comments for each post. Hint: Check https://jsonplaceholder.typicode.com/
    // A post can have multiple comments made by many individuals
    // https://jsonplaceholder.typicode.com/posts/{postId}/comments
    @RequestMapping(value = "/posts/{id}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable("id") final int postId) {
        // Validate postId
        if (postId < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //Get comments for the postId
        List<Comment> comments = auditionService.getCommentsByPostId(postId);

        //Check if comments exist
        if (comments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

}
