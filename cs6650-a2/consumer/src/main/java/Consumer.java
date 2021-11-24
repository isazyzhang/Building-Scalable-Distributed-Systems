import com.google.gson.Gson;
import java.util.ArrayList;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer {

  private final static String QUEUE_NAME = "messages";
  private final static Integer THREADS = 10;
  private final static String HOST_IP = "54.85.34.191";
  public final static int PORT = 5672;
  public final static String USER_NAME = "admin";
  public final static String USER_PASSWORD = "admin";


  public static void main(String[] args) throws Exception {
    Map<String, List<LiftRide>> map = new HashMap<>();
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(HOST_IP);
    connectionFactory.setPort(PORT);
    connectionFactory.setUsername(USER_NAME);
    connectionFactory.setPassword(USER_PASSWORD);
    connectionFactory.setVirtualHost("/");
    Connection connection = connectionFactory.newConnection();
    AtomicInteger count = new AtomicInteger();

    Runnable runnable = () -> {
      Channel channel;
      try {
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicQos(1);
        DeliverCallback deliverCallback =  (tag, delivery) -> {
          String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
          LiftRide liftRide = new Gson().fromJson(message, LiftRide.class);
          String skierId = liftRide.skierID;
          count.getAndIncrement();

          if (!map.containsKey(skierId)) {
            System.out.println(skierId);
            map.put(skierId, new ArrayList<>());
          }

          map.get(skierId).add(liftRide);
          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, tag -> {
        });

      } catch (IOException e) {
        e.printStackTrace();
      }
    };

    for (int i = 0; i < THREADS; i++) {
      Thread thread = new Thread(runnable);
      thread.start();
    }
  }
}

