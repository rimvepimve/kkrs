package rs.WebController;

import GSONSerializable.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.dao.IUserDAO;
import rs.dao.UserDAO;
import rs.model.Comment;
import rs.model.Customer;
import rs.model.Employee;
import rs.model.User;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;


@Controller
@RequestMapping("/user")
public class UserWebController {

    private  IUserDAO userDAO = new UserDAO();

    @RequestMapping(value = "/allUsers")
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String showAllUsers(){

        List<User> allUsers = userDAO.getAllUsers();

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(User.class, new UserGSONSerializer());
        Gson parser = gson.create();
        parser.toJson(allUsers.get(0));

        Type UsersList = new TypeToken<List<User>>() {
        }.getType();
        gson.registerTypeAdapter(UsersList, new AllUsersGSONSerializer());
        parser = gson.create();

        return parser.toJson(allUsers);
    }

    @RequestMapping(value = "/allEmployees")
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String showAllEmployees(){

        List<Employee> allEmployees = userDAO.getAllEmployees();

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Employee.class, new EmployeeGSONSerializer());
        Gson parser = gson.create();
        parser.toJson(allEmployees.get(0));

        Type emplyeeList = new TypeToken<List<Employee>>() {
        }.getType();
        gson.registerTypeAdapter(emplyeeList, new AllEmployeesGSONSerializer());
        parser = gson.create();

        return parser.toJson(allEmployees);
    }

    @RequestMapping(value = "/employeeComments", method = RequestMethod.POST )
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String showEmployeeComments(@RequestBody String request ) throws SQLException{

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int userId = Integer.parseInt(data.getProperty("user_id"));
        List<Comment> allComments = userDAO.getEmployeeComments(userId);

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Comment.class, new CommentGSONSerializer());
        Type commentsList = new TypeToken<List<Comment>>(){}.getType();
        gson.registerTypeAdapter(commentsList, new AllCommentsGSONSerializer());

        return parser.toJson(allComments);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String loginUser(@RequestBody String request) throws SQLException {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String userName = data.getProperty("userName");
        String password = data.getProperty("password");

        User user = userDAO.checkLogin(userName, password);
        //GsonBuilder gson = new GsonBuilder();
        //gson.registerTypeAdapter(User.class, new UserGSONSerializer());
        if(user == null){
            return "Wrong credentials";
        }
        return Integer.toString(user.getUserId());
        //return parser.toJson(user);
    }

    @RequestMapping(value = "/byFullName", method = RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String findUserByFullName(@RequestParam("fullName") String request){

        User user = userDAO.getUserByFullName(request);
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(User.class, new UserGSONSerializer());
        Gson parser = gson.create();
        return parser.toJson(user);
    }

    @RequestMapping(value = "/addEmployee", method = RequestMethod.PUT)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String addEmployee(@RequestBody String request){

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String userType = data.getProperty("user_type");
        String userName = data.getProperty("user_name");
        String password = data.getProperty("password");
        String email = data.getProperty("email");
        String fullName = data.getProperty("fullName");
        String birthday = data.getProperty("birthday");
        String gender = data.getProperty("gender");
        String phoneNumber = data.getProperty("phone_number");
        String salonName = data.getProperty("salon_name");
        String salonAddress = data.getProperty("salon_address");

        Employee emp = new Employee();
        emp.setUserType(userType);
        emp.setUserName(userName);
        emp.setPassword(password);
        emp.setEmail(email);
        emp.setFullName(fullName);
        emp.setBirthday(LocalDate.parse(birthday));
        emp.setGender(gender);
        emp.setPhoneNumber(phoneNumber);
        emp.setSalonName(salonName);
        emp.setSalonAddress(salonAddress);

        userDAO.addNewEmployee(emp);
        System.out.println("employee added");
        return "user added";
    }

    @RequestMapping(value = "/addCustomer", method = RequestMethod.PUT)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String addCustomer(@RequestBody String request){

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String userType = data.getProperty("user_type");
        String userName = data.getProperty("user_name");
        String password = data.getProperty("password");
        String email = data.getProperty("email");
        String fullName = data.getProperty("fullName");
        String birthday = data.getProperty("birthday");
        String gender = data.getProperty("gender");
        String phoneNumber = data.getProperty("phone_number");

        Customer customer = new Customer();
        customer.setUserType(userType);
        customer.setUserName(userName);
        customer.setPassword(password);
        customer.setEmail(email);
        customer.setFullName(fullName);
        customer.setBirthday(LocalDate.parse(birthday));
        customer.setGender(gender);
        customer.setPhoneNumber(phoneNumber);

        userDAO.addNewCustomer(customer);
        System.out.println("customer added");
        return "user added";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String delete(@RequestBody String req){

        Gson parser = new Gson();
        Properties data = parser.fromJson(req, Properties.class);
        String id = data.getProperty("id");
        userDAO.delete(Integer.parseInt(id));
        System.out.println("user deleted");
        return "User deleted";
    }

    @RequestMapping(value = "/addComment", method = RequestMethod.PUT)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String addComment(@RequestBody String request){

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String commentText = data.getProperty("comment_text");
        String userId = data.getProperty("user_id");
        String authorId = data.getProperty("author_id");


        Comment comment = new Comment();
        comment.setCommentText(commentText);
        comment.setUserId(Integer.parseInt(userId));
        comment.setAuthorId(Integer.parseInt(authorId));

        userDAO.addNewCommentToDB(comment);
        System.out.println("comment added");
        return "comment added";
    }

    @RequestMapping(value = "/addReply", method = RequestMethod.PUT)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String addReply(@RequestBody String request){

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String commentText = data.getProperty("comment_text");
        String authorId = data.getProperty("author_id");
        int parentCommentId = Integer.parseInt(data.getProperty("parent_id"));

        Comment comment = new Comment();
        comment.setCommentText(commentText);
        comment.setAuthorId(Integer.parseInt(authorId));

        userDAO.addReplyToComment(parentCommentId, comment);
        System.out.println("reply to comment added");
        return "reply to comment added";
    }

    @RequestMapping(value = "/CommentIdByText", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String getCommentId (@RequestBody String request){

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String commentText = data.getProperty("text");

        int commentId = userDAO.getCommentIdByText(commentText);

        return Integer.toString(commentId);
    }

    @RequestMapping(value = "/CommentById", method = RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String getCommentById(@RequestParam("commentId") String request){

        Comment comment = userDAO.getCommentByCommentId(Integer.parseInt(request));
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(User.class, new CommentGSONSerializer());
        Gson parser = gson.create();
        return parser.toJson(comment);
    }

    @RequestMapping(value = "/deleteComment", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String deleteComment(@RequestBody String req){

        Gson parser = new Gson();
        Properties data = parser.fromJson(req, Properties.class);
        String id = data.getProperty("id");
        userDAO.deleteComment(Integer.parseInt(id));
        System.out.println("user's comment deleted");
        return "User's comment deleted";
    }

    @RequestMapping(value = "/updateComment", method = RequestMethod.PUT)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String updateComment(@RequestBody String request){

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String newCommentText = data.getProperty("new_comment_text");
        int commentId = Integer.parseInt(data.getProperty("comment_id"));

        userDAO.updateComment(newCommentText, commentId);
        System.out.println("comment updated");
        return "comment updated";
    }

    @RequestMapping(value = "/fullNameById", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String showFullNameById (@RequestBody String request){

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String userId = data.getProperty("user_id");

        String fullName = userDAO.getFullNameByUserId(Integer.parseInt(userId));

        return fullName;
    }

    @RequestMapping(value = "/CommentComments", method = RequestMethod.POST )
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String showCommentComments(@RequestBody String request ) throws SQLException{

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int commentId = Integer.parseInt(data.getProperty("comment_id"));
        List<Comment> allComments = userDAO.getCommentComments(commentId);

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Comment.class, new CommentGSONSerializer());
        Type commentsList = new TypeToken<List<Comment>>(){}.getType();
        gson.registerTypeAdapter(commentsList, new AllCommentsGSONSerializer());

        return parser.toJson(allComments);
    }

}
