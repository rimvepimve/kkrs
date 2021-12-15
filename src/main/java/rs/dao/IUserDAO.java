package rs.dao;

import rs.model.Comment;
import rs.model.Customer;
import rs.model.Employee;
import rs.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IUserDAO {
    User getUserByFullName (String fullName);
    String getFullNameByUserId(int id);
    User checkLogin(String userName, String password) throws SQLException;
    List<User> getAllUsers();
    List<Employee> getAllEmployees();
    void addNewEmployee(Employee employee);
    void addNewCustomer(Customer customer);
    void delete(int id);
    void addNewCommentToDB(Comment comment);
    void addReplyToComment(int parentCommentId, Comment comment);
    int getCommentIdByText(String text);
    ArrayList<Comment> getEmployeeComments(int id) throws SQLException;
    ArrayList<Comment> getCommentComments(int id) throws SQLException;
    Comment getCommentByCommentId(int id);
    void deleteComment(int commentId);
    void updateComment(String newCommentText, int id);
    void updateEmployee(String email, String fullName, String  phoneNumber,
                        String salonName, String salonAddress, String userName, String password, int userId);
    void updateCustomer(String email, String fullName, String  phoneNumber, String userName, String password, int userId);
}


