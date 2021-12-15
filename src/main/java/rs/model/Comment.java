package rs.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class Comment implements Serializable {
    private int commentId;
    private int authorId;
    private int userId;
    private String commentText;
    private ArrayList<Comment> comments;

    public Comment() {
        comments = new ArrayList<>();
    }

    public Comment(int author_id, int user_id, String commentText, ArrayList<Comment> comments) {
        this.authorId = author_id;
        this.userId = user_id;
        this.commentText = commentText;
        this.comments = comments;
    }

    public Comment(int commentId, int author_id, int user_id, String commentText, ArrayList<Comment> comments) {
        this.commentId = commentId;
        this.authorId = author_id;
        this.userId = user_id;
        this.commentText = commentText;
        this.comments = comments;
    }
}