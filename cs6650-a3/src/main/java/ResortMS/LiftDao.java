package ResortMS;

import ResortMS.Model.LiftRide;
import SkierMS.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public class LiftDao {
  private static BasicDataSource dataSource;

  public LiftDao() {
    dataSource = DataSource.getDataSource();
  }

  public void generateNewLift(LiftRide lift) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String insertQueryStatement = "INSERT INTO liftrides (skierId, resortId, seasonId, dayId, time, liftId) " +
        "VALUES (?,?,?,?,?,?)";
    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(insertQueryStatement);
      preparedStatement.setString(1, lift.getSkierID());
      preparedStatement.setInt(2, lift.getResortID());
      preparedStatement.setString(3, lift.getSeasonID());
      preparedStatement.setString(4, lift.getDayID());
      preparedStatement.setInt(5, lift.getTime());
      preparedStatement.setInt(6, lift.getLiftID());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }
}
