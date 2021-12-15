package ResortMS;

import ResortMS.Model.LiftRide;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Consumer {
  private final static String QUEUE_NAME = "resorts";
  private final static String EXCHANGE_NAME = "exchange";
  private final static String HOST = "54.85.34.191";
  public final static int PORT = 5672;
  public final static String USERNAME = "admin";
  public final static String USER_PASSWORD = "admin";
  private final static Integer THREADS = 10;

  public static void main(String[] args) throws Exception {
    Map<String, List<LiftRide>> map = new HashMap<>();
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(HOST);
    connectionFactory.setPort(PORT);
    connectionFactory.setUsername(USERNAME);
    connectionFactory.setPassword(USER_PASSWORD);
    connectionFactory.setVirtualHost("/");
    Connection connection = connectionFactory.newConnection();
    LiftDao liftDao = new LiftDao();

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        Channel channel;
        try {
          channel = connection.createChannel();
          channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
          channel.queueDeclare(QUEUE_NAME, false, true, false, null);
          channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
          channel.basicQos(1);
          DeliverCallback deliverCallback =  (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            LiftRide lift = new Gson().fromJson(message, LiftRide.class);
            liftDao.generateNewLift(lift);
          };
          channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
          });

        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    };

    for (int i = 0; i < THREADS; i++) {
      Thread thread = new Thread(runnable);
      thread.start();
    }
  }
}

