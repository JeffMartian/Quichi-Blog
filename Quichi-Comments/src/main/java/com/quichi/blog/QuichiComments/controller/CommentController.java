package com.quichi.blog.QuichiComments.controller;

import com.quichi.blog.QuichiComments.model.Comment;
import com.quichi.blog.QuichiComments.model.CommentDataException;
import com.quichi.blog.QuichiComments.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment create(@RequestBody @Valid final Comment comment) throws CommentDataException{
        final int rowsInserted = commentService.save(comment);

        if(rowsInserted > 0)
            return comment;
        else
            throw new CommentDataException("The message could not be created");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Comment> getAllComments() {
        return commentService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Comment getComment(@PathVariable("id") long id) throws ClassNotFoundException {
        return commentService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteComment(@PathVariable("id") long id) {
        commentService.delete(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Comment updateComment(@RequestBody @Valid Comment comment) throws CommentDataException {
        int rowUpdated = commentService.update(comment);

        if (rowUpdated > 0) {
            return comment;
        } else {
            throw new CommentDataException("The message could not be updated");
        }
    }

    @GetMapping("/blog/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Comment> getAllComments(@PathVariable("id") long postId){
        return commentService.getCommentsByPostId(postId);

    }
}
