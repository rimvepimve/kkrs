package rs.model;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class User implements Serializable {
  protected int userId;
  protected String userName;
  protected String password;
  protected String userType;

  public User() {}

  public User(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }

  public User(int userId, String userName, String password) {
    this.userId = userId;
    this.userName = userName;
    this.password = password;
  }

  public User(int userId, String userName, String password, String userType) {
    this.userId = userId;
    this.userName = userName;
    this.password = password;
    this.userType = userType;
  }

  public String getFullName(){
    return "";
  }
}
