package rs.dao;

import rs.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class AppointmentDAO {

  public void addAppointment(int employeeId, int customerId, LocalDate date, String hour) {
    String sql = "insert into appointment (emp_id, cust_id, app_date, app_hour) values(?,?,?,?)";
    try (Connection conn = new DBUtil().getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, employeeId);
      pstmt.setInt(2, customerId);
      pstmt.setDate(3, Date.valueOf(date));
      pstmt.setString(4,hour);

      int rowsAffected = pstmt.executeUpdate();
      if (rowsAffected == 1) {
        System.out.println("New visit appointed.");
      } else {
        System.out.println("Error while adding new appointment.");
      }
    } catch (SQLException e) {
      DBUtil.showErrorMessage(e);
    }
  }

    public ArrayList<String> getOccupiedTimes(int employeeId, LocalDate date) {
        ArrayList<String> times = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = new DBUtil().getConnection();
            String sql = "select app_hour from appointment where emp_id  = ? and app_date = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setDate(2, Date.valueOf(date));

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                 times.add(resultSet.getString("app_hour"));
            }

            return times;

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

    public ArrayList<String> getRegistrationsForEmployeeByDate(int employeeId, LocalDate date) {
        ArrayList<String> results = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = new DBUtil().getConnection();
            String sql = "select a.app_hour, u.full_name, u.phone_number from appointment a " +
                    "inner join user u on u.user_id = a.cust_id "+
                    "where a.emp_id  = ? and a.app_date = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setDate(2, Date.valueOf(date));

            resultSet = preparedStatement.executeQuery();


            while(resultSet.next()) {
                String compositeSt = resultSet.getString("app_hour") + "  " +
                    resultSet.getString("full_name") + "  " + resultSet.getString("phone_number");
                results.add(compositeSt);
            }

            return results;

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

    public ArrayList<String> getCustomerRegistrations(int customerId) {
        ArrayList<String> results = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = new DBUtil().getConnection();
            String sql ="select a.app_date, a.app_hour, u.full_name, u.salon_address, u.phone_number  from appointment a,"
              + "user u where a.cust_id = ? and a.emp_id = u.user_id";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerId);

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                String compositeSt = resultSet.getString("app_date") + "  " +
                        resultSet.getString("app_hour") + "  " +
                        resultSet.getString("full_name") + "  " +
                        resultSet.getString("salon_address") + "  " +
                        resultSet.getString("phone_number");
                results.add(compositeSt);
            }

            return results;

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

}
