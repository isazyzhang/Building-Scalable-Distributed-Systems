package SkierMS;

import SkierMS.Model.LiftRide;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Consumer {

  private final static String QUEUE_NAME = "skiers";
  private final static String EXCHANGE_NAME = "exchange_queue";
  private final static String HOST = "54.85.34.191";
  private final static Integer THREADS = 10;
  public final static String USERNAME = "admin";
  public final static String USER_PASSWORD = "admin";
  public final static int PORT = 5672;

  public static void main(String[] args) throws Exception {
    Map<String, List<SkierMS.Model.LiftRide>> map = new HashMap<>();
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setVirtualHost("/");
    connectionFactory.setPort(PORT);
    connectionFactory.setHost(HOST);
    connectionFactory.setUsername(USERNAME);
    connectionFactory.setPassword(USER_PASSWORD);
    Connection connection = connectionFactory.newConnection();

    LiftDao liftDao = new LiftDao();

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        Channel channel;
        try {
          channel = connection.createChannel();
          channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
          channel.queueDeclare(QUEUE_NAME, true, false, false, null);
          channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
          channel.basicQos(1);
          DeliverCallback deliverCallback =  (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
            SkierMS.Model.LiftRide liftRide = new Gson().fromJson(msg,
                SkierMS.Model.LiftRide.class);
            liftDao.generateNewLift(liftRide);
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