package SkierMS;

import org.apache.commons.dbcp2.BasicDataSource;

public class DataSource {
  private static BasicDataSource dataSource;

  private static final String HOST_NAME = "liftrides.ckn7kei49nyk.us-east-1.rds.amazonaws.com";
  private static final String PORT = "3306";
  private static final String DATABASE = "lifts";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "rootpass";

  static {
    dataSource = new BasicDataSource();
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);
    dataSource.setUrl(url);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
  }
  public static BasicDataSource getDataSource() {
    return dataSource;
  }
}