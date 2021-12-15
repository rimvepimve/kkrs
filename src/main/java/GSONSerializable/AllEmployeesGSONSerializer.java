package GSONSerializable;

import com.google.gson.*;
import rs.model.Employee;

import java.lang.reflect.Type;
import java.util.List;

public class AllEmployeesGSONSerializer implements JsonSerializer<List<Employee>> {

    @Override
    public JsonElement serialize(List<Employee> employees, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Employee.class, new EmployeeGSONSerializer());
        Gson parser = gsonBuilder.create();

        for (Employee e : employees) {
            jsonArray.add(parser.toJson(e));
        }
        System.out.println(jsonArray);
        return jsonArray;
    }
}


