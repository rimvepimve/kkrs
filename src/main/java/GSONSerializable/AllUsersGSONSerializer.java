package GSONSerializable;

import com.google.gson.*;
import rs.model.User;

import java.lang.reflect.Type;
import java.util.List;

public class AllUsersGSONSerializer implements JsonSerializer<List<User>> {

    @Override
    public JsonElement serialize(List<User> allLibs, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new UserGSONSerializer());
        Gson parser = gsonBuilder.create();

        for (User l : allLibs) {
            jsonArray.add(parser.toJson(l));
        }
        System.out.println(jsonArray);
        return jsonArray;
    }
}
