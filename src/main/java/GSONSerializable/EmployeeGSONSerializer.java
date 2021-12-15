package GSONSerializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import rs.model.Employee;

import java.lang.reflect.Type;

public class EmployeeGSONSerializer implements JsonSerializer<Employee> {
    @Override
    public JsonElement serialize(Employee employee, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject  empJson = new JsonObject();

        empJson.addProperty("userId", employee.getUserId());
        empJson.addProperty("fullName", employee.getFullName());
        empJson.addProperty("birthday", employee.getBirthday().toString());
        empJson.addProperty("email", employee.getEmail());
        empJson.addProperty("phoneNumber", employee.getPhoneNumber());
        empJson.addProperty("salonName", employee.getSalonName());
        empJson.addProperty("salonAddress", employee.getSalonAddress());

        return empJson;
    }
}
