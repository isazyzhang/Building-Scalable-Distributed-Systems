package server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Publisher {

  public final static String HOST_IP = "54.85.34.191";
  public final static String QUEUE_NAME = "messages";
  public final static int PORT = 5672;
  public final static String USERNAME = "admin";
  public final static String PASSWORD = "admin";
  private Connection conn;
  private ConnectionFactory factory;
  public MessageQueueConnection pool;

  public Publisher() {
    this.factory = new ConnectionFactory();
    factory.setHost(HOST_IP);
    factory.setPort(PORT);
    factory.setUsername(USERNAME);
    factory.setPassword(PASSWORD);
    this.conn = getConn(factory);
    this.pool = new MessageQueueConnection(conn);
  }

  private Connection getConn(ConnectionFactory factory) {
    try {
      return factory.newConnection();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public void publishToQueue(LiftRide liftRide) {
    Channel channel = null;
    try {
      channel = this.pool.getChannel();
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      channel.basicPublish("", QUEUE_NAME, null, liftRide.toString().getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}