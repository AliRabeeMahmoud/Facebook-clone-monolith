package com.example.facebook.controller;

import com.example.facebook.dto.TagDto;
import com.example.facebook.entity.Comment;
import com.example.facebook.entity.Post;
import com.example.facebook.entity.Tag;
import com.example.facebook.entity.User;
import com.example.facebook.exception.EmptyPostException;
import com.example.facebook.response.CommentResponse;
import com.example.facebook.response.PostResponse;
import com.example.facebook.service.CommentService;
import com.example.facebook.service.PostService;
import com.example.facebook.service.TagService;
import com.example.facebook.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;
    private final TagService tagService;

    @PostMapping("/posts/create")
    public ResponseEntity<Post> createNewPost(@RequestParam(value = "content", required = false) Optional<String> content,
                                           @RequestParam(name = "postPhoto", required = false) Optional<MultipartFile> postPhoto,
                                           @RequestParam(name = "postTags", required = false) Optional<String> postTags) throws JsonProcessingException {
        if ((content.isEmpty() || content.get().length() <= 0) &&
                (postPhoto.isEmpty() || postPhoto.get().getSize() <= 0)) {
            throw new EmptyPostException();
        }

        ObjectMapper mapper = new ObjectMapper();

        String contentToAdd = content.isEmpty() ? null : content.get();
        MultipartFile postPhotoToAdd = postPhoto.isEmpty() ? null : postPhoto.get();
        List<TagDto> postTagsToAdd = postTags.isEmpty() ? null :
                mapper.readValue(postTags.get(), new TypeReference<>() {});
        log.info("json parse is safe");

        Post createdPost = postService.createNewPost(contentToAdd, postPhotoToAdd, postTagsToAdd);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @PostMapping("/posts/{postId}/update")
    public ResponseEntity<Post> updatePost(@PathVariable("postId") Long postId,
                                        @RequestParam(value = "content", required = false) Optional<String> content,
                                        @RequestParam(name = "postPhoto", required = false) Optional<MultipartFile> postPhoto,
                                        @RequestParam(name = "postTags", required = false) Optional<String> postTags) throws JsonProcessingException {
        if ((content.isEmpty() || content.get().length() <= 0) &&
                (postPhoto.isEmpty() || postPhoto.get().getSize() <= 0)) {
            throw new EmptyPostException();
        }

        ObjectMapper mapper = new ObjectMapper();

        String contentToAdd = content.isEmpty() ? null : content.get();
        MultipartFile postImageToAdd = postPhoto.isEmpty() ? null : postPhoto.get();
        List<TagDto> postTagsToAdd = postTags.isEmpty() ? null :
                mapper.readValue(postTags.get(), new TypeReference<>() {});

        Post updatePost = postService.updatePost(postId, contentToAdd, postImageToAdd, postTagsToAdd);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
    }

    @DeleteMapping("/posts/{postId}/photo/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deletePostPhoto(@PathVariable("postId") Long postId) {
        postService.deletePostPhoto(postId);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable("postId") Long postId) {
        PostResponse foundPostResponse = postService.getPostResponseById(postId);
        return new ResponseEntity<>(foundPostResponse, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<List<User>> getPostLikes(@PathVariable("postId") Long postId,
                                          @RequestParam("page") Integer page,
                                          @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Post targetPost = postService.getPostById(postId);
        List<User> postLikerList = userService.getLikesByPostPaginate(targetPost, page, size);
        return new ResponseEntity<>(postLikerList, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/shares")
    public ResponseEntity<List<PostResponse>> getPostShares(@PathVariable("postId") Long postId,
                                           @RequestParam("page") Integer page,
                                           @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Post sharedPost = postService.getPostById(postId);
        List<PostResponse> foundPostShares = postService.getPostSharesPaginate(sharedPost, page, size);
        return new ResponseEntity<>(foundPostShares, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/like")
    @ResponseStatus(HttpStatus.OK)
    public void likePost(@PathVariable("postId") Long postId) {
        postService.likePost(postId);
    }

    @PostMapping("/posts/{postId}/unlike")
    @ResponseStatus(HttpStatus.OK)
    public void unlikePost(@PathVariable("postId") Long postId) {
        postService.unlikePost(postId);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getPostComments(@PathVariable("postId") Long postId,
                                             @RequestParam("page") Integer page,
                                             @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Post targetPost = postService.getPostById(postId);
        List<CommentResponse> postCommentResponseList = commentService.getPostCommentsPaginate(targetPost, page, size);
        return new ResponseEntity<>(postCommentResponseList, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/comments/create")
    public ResponseEntity<CommentResponse> createPostComment(@PathVariable("postId") Long postId,
                                               @RequestParam(value = "content") String content) {
        Comment savedComment = postService.createPostComment(postId, content);
        CommentResponse commentResponse = CommentResponse.builder()
                .comment(savedComment)
                .likedByAuthUser(false)
                .build();
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}/update")
    public ResponseEntity<Comment> updatePostComment(@PathVariable("commentId") Long commentId,
                                               @PathVariable("postId") Long postId,
                                               @RequestParam(value = "content") String content) {
        Comment savedComment = postService.updatePostComment(commentId, postId, content);
        return new ResponseEntity<>(savedComment, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deletePostComment(@PathVariable("commentId") Long commentId,
                                               @PathVariable("postId") Long postId) {
        postService.deletePostComment(commentId, postId);
    }

    @PostMapping("/posts/comments/{commentId}/like")
    @ResponseStatus(HttpStatus.OK)
    public void likePostComment(@PathVariable("commentId") Long commentId) {
        commentService.likeComment(commentId);
    }

    @PostMapping("/posts/comments/{commentId}/unlike")
    @ResponseStatus(HttpStatus.OK)
    public void unlikePostComment(@PathVariable("commentId") Long commentId) {
        commentService.unlikeComment(commentId);
    }

    @GetMapping("/posts/comments/{commentId}/likes")
    public ResponseEntity<List<User>> getCommentLikeList(@PathVariable("commentId") Long commentId,
                                                @RequestParam("page") Integer page,
                                                @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Comment targetComment = commentService.getCommentById(commentId);
        List<User> commentLikes = userService.getLikesByCommentPaginate(targetComment, page, size);
        return new ResponseEntity<>(commentLikes, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/share/create")
    public ResponseEntity<Post> createPostShare(@PathVariable("postId") Long postId,
                                             @RequestParam(value = "content", required = false) Optional<String> content) {
        String contentToAdd = content.isEmpty() ? null : content.get();
        Post postShare = postService.createPostShare(contentToAdd, postId);
        return new ResponseEntity<>(postShare, HttpStatus.OK);
    }

    @PutMapping("/posts/{postShareId}/share/update")
    public ResponseEntity<Post> updatePostShare(@PathVariable("postShareId") Long postShareId,
                                             @RequestParam(value = "content", required = false) Optional<String> content) {
        String contentToAdd = content.isEmpty() ? null : content.get();
        Post updatedPostShare = postService.updatePostShare(contentToAdd, postShareId);
        return new ResponseEntity<>(updatedPostShare, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postShareId}/share/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deletePostShare(@PathVariable("postShareId") Long postShareId) {
        postService.deletePostShare(postShareId);
    }

    @GetMapping("/posts/tags/{tagName}")
    public ResponseEntity<List<PostResponse>> getPostsByTag(@PathVariable("tagName") String tagName,
                                          @RequestParam("page") Integer page,
                                          @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Tag targetTag = tagService.getTagByName(tagName);
        List<PostResponse> taggedPosts = postService.getPostByTagPaginate(targetTag, page, size);
        return new ResponseEntity<>(taggedPosts, HttpStatus.OK);
    }
}
