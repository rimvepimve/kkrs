package GSONSerializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import rs.model.User;

import java.lang.reflect.Type;


public class UserGSONSerializer implements JsonSerializer<User> {
    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject userJson = new JsonObject();

        userJson.addProperty("userId", user.getUserId());
        userJson.addProperty("userName", user.getUserName());

        return userJson;
    }
}
