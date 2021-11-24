import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;

public class MessageQueueConnection {

  private Connection conn;
  private final ThreadLocal<Channel> channels;

  public MessageQueueConnection(Connection conn) {
    this.conn = conn;
    this.channels = new ThreadLocal<>();
  }

  public Channel getChannel() throws IOException {
    Channel channel = channels.get();
    if (channel == null) {
      channel = conn.createChannel();
      channels.set(channel);
    }
    return channel;
  }
}