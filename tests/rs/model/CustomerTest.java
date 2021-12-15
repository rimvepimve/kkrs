package rs.model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerTest {

  @Test
  public void getEmail_IsNotNull() {
    //arrange
    Customer c = new Customer();
    //act
    String email = c.getEmail();
    //assert
    Assert.assertNotNull(email);
  }

  @Test
  public void getEmail_ContainsEta(){
    //arrange
    Customer cust = new Customer("Vardas", "pw", "mailg@mail.com", "Full Name", null, "m", "860145287" );
    //act
    Boolean eta = cust.getEmail().contains("@");
    //assert
    Assert.assertTrue(eta);
  }
}