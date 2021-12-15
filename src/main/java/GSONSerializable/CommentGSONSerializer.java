package GSONSerializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import rs.model.Comment;

import java.lang.reflect.Type;

public class CommentGSONSerializer implements JsonSerializer<Comment> {
    @Override
    public JsonElement serialize(Comment comment, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject commentJson = new JsonObject();

        commentJson.addProperty("commentId", comment.getCommentId());
        commentJson.addProperty("authorId", comment.getAuthorId());
        commentJson.addProperty("userId", comment.getUserId());
        commentJson.addProperty("commentText", comment.getCommentText());

         return commentJson;
    }
}
