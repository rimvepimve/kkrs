package rs.dao;

import rs.model.Comment;
import rs.model.Customer;
import rs.model.Employee;
import rs.model.User;
import rs.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

  private List<User> users;

  public UserDAO() {
    users = getAllUsers();
  }

  @Override
  public List<Employee> getAllEmployees() {
    List<Employee> allEmployees = new ArrayList<>();
    try(
            Connection conn = new DBUtil().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from user where user_type = 'e'")
            ) {
      while (rs.next()) {
        Employee emp = extractEmployee(rs);
        allEmployees.add(emp);
      }
    } catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }
    return allEmployees;
  }

  @Override
  public User getUserByFullName (String fullName) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      conn = new DBUtil().getConnection();
      String sql = "Select * from user where full_name = ?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,fullName);
      rs = pstmt.executeQuery();
      if(rs.next()) {
        if("e".equals(rs.getString("user_type"))){
          return extractEmployee(rs);
        } else{
          return extractCustomer(rs);
        }
      }
    } catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    } finally{
      try{
        if (conn != null) conn.close();
        if (pstmt != null) pstmt.close();
        if (rs != null) rs.close();
      } catch (SQLException e) {
        DBUtil.showErrorMessage(e);
      }
    }
    return null;
  }

  @Override
  public List<User> getAllUsers() {
    List<User> users = new ArrayList<>();
    try(
            Connection conn = new DBUtil().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from user")
    ) {
      while (rs.next()) {
        if ("e".equals(rs.getString("user_type"))) {
          Employee emp = extractEmployee(rs);
          users.add(emp);
        } else {
          Customer customer = extractCustomer(rs);
          users.add(customer);
        }
      }
    } catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }

    return users;
  }

  private Customer extractCustomer(ResultSet rs) throws SQLException{
    Customer customer = new Customer();
    customer.setUserType(rs.getString("user_type"));
    customer.setUserId(rs.getInt("user_id"));
    customer.setUserName(rs.getString("user_name"));
    customer.setPassword(rs.getString("password"));
    customer.setEmail(rs.getString("email"));
    customer.setFullName(rs.getString("full_name"));
    customer.setBirthday(rs.getDate("birthday").toLocalDate());
    customer.setGender(rs.getString("gender"));
    customer.setPhoneNumber(rs.getString("phone_number"));
    return customer;
  }

  private Employee extractEmployee(ResultSet rs) throws SQLException{
    Employee emp = new Employee();
    emp.setUserType(rs.getString("user_type"));
    emp.setUserId(rs.getInt("user_id"));
    emp.setUserName(rs.getString("user_name"));
    emp.setPassword(rs.getString("password"));
    emp.setEmail(rs.getString("email"));
    emp.setFullName(rs.getString("full_name"));
    emp.setBirthday(rs.getDate("birthday").toLocalDate());
    emp.setGender(rs.getString("gender"));
    emp.setPhoneNumber(rs.getString("phone_number"));
    emp.setSalonName(rs.getString("salon_name"));
    emp.setSalonAddress(rs.getString("salon_address"));
    emp.setComments(getEmployeeComments(emp.getUserId()));
    return emp;
  }

@Override
  public ArrayList<Comment> getEmployeeComments(int id) throws SQLException{
    ArrayList<Comment> allComments = new ArrayList<>();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try{
      connection = new DBUtil().getConnection();
      String sql = "select * from comment where user_id = ?";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()){
        Comment comment = new Comment();
        comment.setUserId(id);
        comment.setAuthorId(resultSet.getInt("author_id"));
        comment.setCommentId(resultSet.getInt("comment_id"));
        comment.setCommentText(resultSet.getString("comment_text"));
        comment.setComments(fillSubCommentsList(resultSet.getInt("comment_id")));
        ArrayList<Comment> subComments = fillSubCommentsList(comment.getCommentId());
        if(subComments != null)
          comment.setComments(subComments);
          allComments.add(comment);
      }
      return allComments;
    }catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }
    finally{
      if (connection != null) connection.close();
      if (preparedStatement != null) preparedStatement.close();
      if (resultSet != null) resultSet.close();
    }
    return null;
  }

  @Override
  public Comment getCommentByCommentId(int id) {
    Comment comment = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try{
      connection = new DBUtil().getConnection();
      String sql = "select * from comment where comment_id = ?";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);

      resultSet = preparedStatement.executeQuery();

      comment = extractComment(resultSet);

    }catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }
    return comment;
  }

  private ArrayList<Comment> fillSubCommentsList(int commentId) throws SQLException {
    ArrayList<Comment> commentsList = new ArrayList<>();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try{
      connection = new DBUtil().getConnection();
      String sql = "select comments_id from comment_comment where comment_id = ?";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, commentId);
      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()){
        Comment comment = getCommentByCommentId(resultSet.getInt("comments_id"));
        commentsList.add(comment);
      }
      return commentsList;
    }catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }
    finally{
      if (connection != null) connection.close();
      if (preparedStatement != null) preparedStatement.close();
      if (resultSet != null) resultSet.close();
    }
    return null;
  }


  private Comment extractComment(ResultSet rs) throws SQLException {
    Comment comment = null;
    if(rs.next()) {
      comment = new Comment();
      comment.setUserId(rs.getInt("user_id"));
      comment.setAuthorId(rs.getInt("author_id"));
      comment.setCommentText(rs.getString("comment_text"));
      comment.setCommentId(rs.getInt("comment_id"));
      ArrayList<Comment> subComments = fillSubCommentsList(comment.getCommentId());
      if(subComments != null)
        comment.setComments(subComments);
    }
    return comment;
  }

  @Override
  public void addNewEmployee(Employee employee) {
    users.add(employee);
    String sql = "insert into user (user_type, user_name, password, email, full_name, birthday, gender," +
            " phone_number, salon_name, salon_address) values (?,?,?,?,?,?,?,?,?,?)";
    try(
            Connection conn = new DBUtil().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
      pstmt.setString(1, "e");
      pstmt.setString(2, employee.getUserName());
      pstmt.setString(3, employee.getPassword());
      pstmt.setString(4, employee.getEmail());
      pstmt.setString(5, employee.getFullName());
      Date sqlDate = Date.valueOf(employee.getBirthday());
      pstmt.setDate(6, sqlDate);
      pstmt.setString(7, employee.getGender());
      pstmt.setString(8, employee.getPhoneNumber());
      pstmt.setString(9, employee.getSalonName());
      pstmt.setString(10, employee.getSalonAddress());

      int rowsAffected = pstmt.executeUpdate();
      System.out.println("Inserted rows: " + rowsAffected);
      users = getAllUsers();

    }catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }
    //save();
  }

  @Override
  public void addNewCustomer(Customer customer) {
    users.add(customer);
    String sql = "insert into user (user_type, user_name, password, email, full_name, birthday, gender," +
            " phone_number) values (?,?,?,?,?,?,?,?)";
    try(
            Connection conn = new DBUtil().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
    ){
      pstmt.setString(1, "c");
      pstmt.setString(2, customer.getUserName());
      pstmt.setString(3, customer.getPassword());
      pstmt.setString(4, customer.getEmail());
      pstmt.setString(5, customer.getFullName());
      Date sqlDate = Date.valueOf(customer.getBirthday());
      pstmt.setDate(6, sqlDate);
      pstmt.setString(7, customer.getGender());
      pstmt.setString(8, customer.getPhoneNumber());

      int rowsAffected = pstmt.executeUpdate();
      System.out.println("Inserted rows: " + rowsAffected);
      users = getAllUsers();

    }catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }
  }

  @Override
  public void addNewCommentToDB(Comment comment) {
    String sql = "insert into comment (author_id, user_id, comment_text) values(?,?,?)";
    try (Connection conn = new DBUtil().getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, comment.getAuthorId());
      pstmt.setInt(2, comment.getUserId());
      pstmt.setString(3, comment.getCommentText());

      int rowsAffected = pstmt.executeUpdate();
      if (rowsAffected == 1) {
        System.out.println("New comment added.");
        users = getAllUsers();
      } else {
        System.out.println("Error while adding new comment.");
      }
    } catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }
  }

  @Override
  public void addReplyToComment(int parentCommentId, Comment comment) {
    String sql = "insert into comment (author_id, comment_text) values(?,?)";
    try (Connection conn = new DBUtil().getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, comment.getAuthorId());
      pstmt.setString(2, comment.getCommentText());

      int rowsAffected = pstmt.executeUpdate();
      if (rowsAffected == 1) {
        System.out.println("New SubComment added.");
        mapComments(parentCommentId, comment.getCommentText());
        users = getAllUsers();
      } else {
        System.out.println("Error while adding new SubComment.");
      }
    } catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }
  }

  private void mapComments(int parentCommentId, String commentText) {
    int currentCommentId = getCommentIdByText(commentText);
    String sql = "insert into comment_comment (comment_id, comments_id ) values (?,?)";
    try (Connection conn = new DBUtil().getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, parentCommentId);
      pstmt.setInt(2, currentCommentId);
      int rowsAffected = pstmt.executeUpdate();
      if (rowsAffected == 1) {
        System.out.println("Comments maped");
      } else {
        System.out.println("Error while mapping comments");
      }
    } catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }
  }

  @Override
  public int getCommentIdByText(String text){
    Connection conn = null;
    String sql = "select comment_id from comment where comment_text = ?";
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try{
      conn = new DBUtil().getConnection();
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,text);
      rs = pstmt.executeQuery();
      if(rs.next()) {
        return rs.getInt("comment_id");
      }
    }catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }
    return 0;
  }

  @Override
  public void delete(int id) {
    String sql = "delete from user where user_id = ?";
    try(Connection conn = new DBUtil().getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
      pstmt.setInt(1,id);

      int rowsDeleted = pstmt.executeUpdate();
      if(rowsDeleted == 1) {
        System.out.println("User deleted successfully.");
      } else {
        System.out.println("Error while deleting user.");
      }
    }
    catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }
  }

  @Override
  public void deleteComment(int commentId) {
    String sql = "delete from comment where comment_id = ?";
    try(Connection conn = new DBUtil().getConnection();
    PreparedStatement pstmt = conn.prepareStatement(sql)){
      pstmt.setInt(1, commentId);
      int rowsDeleted = pstmt.executeUpdate();
      if(rowsDeleted == 1) {
        System.out.println("Comment deleted successfully.");
        users = getAllUsers();
      } else {
        System.out.println("Error while deleting comment.");
      }
    }catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }
  }

  @Override
  public void updateComment(String newCommentText, int commentId){
    String sql = "update comment set comment_text = ? where comment_id =?";
    try(Connection conn = new DBUtil().getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
      pstmt.setString(1,newCommentText);
      pstmt.setInt(2,commentId);
      int updatedRows = pstmt.executeUpdate();
    }catch (SQLException e){
      DBUtil.showErrorMessage(e);
    }
  }

  @Override
  public User checkLogin(String userName, String password) throws SQLException {
    String sql = "select * from user where user_name = ? and password = ?";
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      conn = new DBUtil().getConnection();
      pstmt = conn.prepareStatement(sql);

      pstmt.setString(1,userName);
      pstmt.setString(2, password);

      rs = pstmt.executeQuery();

      User user = null;
      if(!rs.next())
        return user;
      if("e".equals(rs.getString("user_type"))){
        user = extractEmployee(rs);
      } else {
        user = extractCustomer(rs);
      }
      return user;
    }
    catch (SQLException e){
      DBUtil.showErrorMessage(e);
    }
    finally{
      if(conn != null) conn.close();
      if(pstmt != null) pstmt.close();
      if(rs != null) rs.close();
    }
    return null;
  }


  @Override
  public String getFullNameByUserId(int id) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try{
      connection = new DBUtil().getConnection();
      String sql = "select full_name from user where user_id = ?";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);

      resultSet = preparedStatement.executeQuery();

      if(resultSet.next()) {
        return resultSet.getString("full_name");
      }

    }catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    } finally {
      try {
        if (connection != null) connection.close();
        if (preparedStatement != null) preparedStatement.close();
        if (resultSet != null) resultSet.close();
      } catch (SQLException e){
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override
  public void updateEmployee(String email, String fullName, String  phoneNumber, String salonName, String salonAddress, String userName, String password, int userId){
    String sql = "update user set user_name = ?, password = ?, email = ?, full_name = ?, phone_number = ?, salon_name = ?, salon_address = ? where user_id =?";
    try(Connection conn = new DBUtil().getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
      pstmt.setString(1,userName);
      pstmt.setString(2,password);
      pstmt.setString(3,email);
      pstmt.setString(4,fullName);
      pstmt.setString(5,phoneNumber);
      pstmt.setString(6,salonName);
      pstmt.setString(7,salonAddress);
      pstmt.setInt(8,userId);
      int updatedRows = pstmt.executeUpdate();
    }catch (SQLException e){
      DBUtil.showErrorMessage(e);
    }
  }

  @Override
  public void updateCustomer(String email, String fullName, String  phoneNumber, String userName, String password, int userId){
    String sql = "update user set user_name = ?, password = ?, email = ?, full_name = ?, phone_number = ? where user_id = ?";
    try(Connection conn = new DBUtil().getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
      pstmt.setString(1,userName);
      pstmt.setString(2,password);
      pstmt.setString(3,email);
      pstmt.setString(4,fullName);
      pstmt.setString(5,phoneNumber);
      pstmt.setInt(6,userId);
      int updatedRows = pstmt.executeUpdate();
    }catch (SQLException e){
      DBUtil.showErrorMessage(e);
    }
  }

  @Override
  public ArrayList<Comment> getCommentComments(int id) throws SQLException{
    ArrayList<Comment> allComments = new ArrayList<>();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try{
      connection = new DBUtil().getConnection();
      String sql = "select * from comment where comment_id in (select comments_id from comment_comment WHERE comment_id = ?)";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()){
        Comment comment = new Comment();
        comment.setUserId(id);
        comment.setAuthorId(resultSet.getInt("author_id"));
        comment.setCommentId(resultSet.getInt("comment_id"));
        comment.setCommentText(resultSet.getString("comment_text"));
        comment.setComments(fillSubCommentsList(resultSet.getInt("comment_id")));
        ArrayList<Comment> subComments = fillSubCommentsList(comment.getCommentId());
        if(subComments != null)
          comment.setComments(subComments);
        allComments.add(comment);
      }
      return allComments;
    }catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }
    finally{
      if (connection != null) connection.close();
      if (preparedStatement != null) preparedStatement.close();
      if (resultSet != null) resultSet.close();
    }
    return null;
  }

}
