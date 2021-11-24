import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class Send {

  private final static String RMQ_AWS = "54.234.115.4"; // ec2-54-234-115-4.compute-1.amazonaws.com
  private final static String QUEUE_NAME = "POST_REQUESTS";

  public static void main(String[] args) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost"); // public IPV4 of ubuntu instance hosting rabbitMQ
    try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()) {
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      String message = "Hello World!";
      channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
      System.out.println(" [x] Sent '" + message + "'");
    }
  }
}
