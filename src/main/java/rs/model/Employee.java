package rs.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

@Data
public class Employee extends User implements Serializable {
  private String email;
  private String fullName;
  private LocalDate birthday;
  private String gender;
  private String phoneNumber;
  private String salonName;
  private String salonAddress;
  private ArrayList<Comment> comments;

  public Employee() {}

  public Employee(
      String userName,
      String password,
      String email,
      String fullName,
      LocalDate birthday,
      String gender,
      String phoneNumber,
      String salonName,
      String salonAddress) {
    super(userName, password);
    this.email = email;
    this.fullName = fullName;
    this.birthday = birthday;
    this.gender = gender;
    this.phoneNumber = phoneNumber;
    this.salonName = salonName;
    this.salonAddress = salonAddress;
    comments = new ArrayList<>();
  }

  @Override
  public String getFullName(){
    return this.fullName;
  }

}
