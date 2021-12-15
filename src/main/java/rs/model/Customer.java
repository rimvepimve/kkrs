package rs.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Customer extends User implements Serializable {
  private String email;
  private String fullName;
  private LocalDate birthday;
  private String gender;
  private String phoneNumber;

    public Customer() {}

    public Customer(
      String userName,
      String password,
      String email,
      String fullName,
      LocalDate birthday,
      String gender,
      String phoneNumber) {
    super(userName, password);
    this.email = email;
    this.fullName = fullName;
    this.birthday = birthday;
    this.gender = gender;
    this.phoneNumber = phoneNumber;
  }

  @Override
  public String getFullName(){
    return this.fullName;
  }
}
