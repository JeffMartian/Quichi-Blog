package com.quichi.blog.QuichiComments.repository;

import com.quichi.blog.QuichiComments.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentJdbcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Comment comment) {

        return jdbcTemplate.update("INSERT INTO comment (content, created_date, post_id) values(?,?,?)"
                , comment.getContent(), new Date(), comment.getPostId());
    }

    public List<Comment> getAll() {
        return jdbcTemplate.query("SELECT * FROM comment", (rs, rowNum) -> new Comment(
                rs.getLong("id"),
                rs.getString("content"),
                rs.getDate("created_date"),
                rs.getLong("post_id")
        ));
    }

    public Optional<Comment> findById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM comment where id = ? ",
                new Object[]{id} , (rs, rowNum) -> Optional.of(new Comment(
                rs.getLong("id"),
                rs.getString("content"),
                rs.getDate("created_date"),
                rs.getLong("post_id"))
        ));
    }

    public int delete(long id) {
        return jdbcTemplate.update("DELETE FROM comment WHERE id = ?", id );
    }

    public int update(Comment comment) {
        return jdbcTemplate.update("UPDATE comment SET content = ? WHERE id = ?" , comment.getContent(), comment.getId());
    }



    public List<Comment> getCommentsByPostId(long blogPostId) {
        return jdbcTemplate.query( "SELECT * FROM comment WHERE post_id = ?", new Object[]{blogPostId},
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getString("content"),
                        rs.getDate("created_date"),
                        rs.getLong("post_id")));
    }
}
