package rs.util;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DBUtilTest {

  String a = new DBUtil().PASSWORD;

  @Test
  public void getConnection() {
    Connection conn = null;
    try {
      conn = new DBUtil().getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assertNotNull(conn);
    assertEquals("pass", a);
  }



}