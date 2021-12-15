package GSONSerializable;

import com.google.gson.*;
import rs.model.Comment;

import java.lang.reflect.Type;
import java.util.List;

public class AllCommentsGSONSerializer implements JsonSerializer<List<Comment>> {
    @Override
    public JsonElement serialize(List<Comment> comments, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Comment.class, new CommentGSONSerializer());
        Gson parser = gsonBuilder.create();

        for (Comment c : comments) {
            jsonArray.add(parser.toJson(c));
        }
        System.out.println(jsonArray);
        return jsonArray;
    }
}
