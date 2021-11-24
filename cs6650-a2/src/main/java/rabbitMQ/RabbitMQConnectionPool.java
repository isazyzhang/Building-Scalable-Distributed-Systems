package rabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;

public class RabbitMQConnectionPool {

  private Connection connection;
  private final ThreadLocal<Channel> channels;

  public RabbitMQConnectionPool(Connection connection) {
    this.connection = connection;
    channels = new ThreadLocal<>();
  }

  public Channel getChannel() throws IOException {
    Channel channel = channels.get();
    if (channel == null) {
      channel = connection.createChannel();
      channels.set(channel);
    }
    return channel;
  }
}
