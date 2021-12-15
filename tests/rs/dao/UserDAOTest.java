package rs.dao;

import org.junit.Test;
import rs.model.Comment;
import rs.model.User;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserDAOTest {
  String name = "Laura Laurencija";
  int id = 20;
  UserDAO dao = new UserDAO();

  @Test
  public void getUserByFullName() {
    User u = dao.getUserByFullName(name);
    assertNotNull(u);

  }

  @Test
  public void getEmployeeComments() {
    ArrayList<Comment> comments = new ArrayList();
    try {
      ArrayList<Comment> commentarai = dao.getEmployeeComments(id);
      for (Comment c :commentarai ) {
          comments.add(c);
      }
    }catch (SQLException e) {
      e.printStackTrace();
    }
    if(comments != null)
    System.out.println(comments.get(0));

    assertFalse(comments.isEmpty());


  }
}