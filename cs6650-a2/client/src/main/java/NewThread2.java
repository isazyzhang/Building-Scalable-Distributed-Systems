import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class NewThread2 implements Runnable {

  public String phaseName;
  int threadID;
  int numReqsPerThread;
  int startID;
  int endID;
  int startTime;
  int endTime;
  CountDownLatch nextPhaseLatch;
  CountDownLatch curPhaseLatch;
  Queue<ReqLatency> successReqLatencies;
  Queue<ReqLatency> nonSuccessReqLatencies;
  Parameters parameters;
  private static final int NUM_OF_ALLOWED_REATTEMPTS = 5;
  public ApiClient apiClient;
  public SkiersApi skiersApi;

  public NewThread2(String phaseName, int threadID, int numReqsPerThread, int startID, int endID, int startTime, int endTime,CountDownLatch nextPhaseLatch, CountDownLatch curPhaseLatch, Queue<ReqLatency> successReqLatencies, Queue<ReqLatency> nonSuccessReqLatencies, Parameters parameters) {

    this.phaseName = phaseName;
    this.threadID = threadID;
    this.numReqsPerThread = numReqsPerThread;
    this.startID = startID;
    this.endID = endID;
    this.startTime = startTime;
    this.endTime = endTime;
    this.nextPhaseLatch = nextPhaseLatch;
    this.curPhaseLatch = curPhaseLatch;
    this.successReqLatencies = successReqLatencies;
    this.nonSuccessReqLatencies = nonSuccessReqLatencies;
    this.parameters = parameters;
    this.apiClient = new ApiClient();
    this.skiersApi = new SkiersApi(apiClient);
  }

  @Override
  public void run() {
    //apiClient.setBasePath("http://" + parameters.ip + ":" + parameters.port + "/server_war_exploded/skiers/");
    apiClient.setBasePath("http://" + parameters.ip + ":" + parameters.port + "/server_war/skiers/");
    long postReqStart;
    long postReqEnd;
    for (int i = 0; i < numReqsPerThread; i++) {
      ApiResponse<Void> res;
      LiftRide liftRide = new LiftRide();
      liftRide.setLiftID(ThreadLocalRandom.current().nextInt(1, parameters.numLifts + 1));
      liftRide.setTime(ThreadLocalRandom.current().nextInt(startTime, endTime + 1));
      int randomID = ThreadLocalRandom.current().nextInt(startID, endID);
      try {
        postReqStart = System.currentTimeMillis();
        res = skiersApi.writeNewLiftRideWithHttpInfo(liftRide, 12, "2019", "333", randomID);
        postReqEnd = System.currentTimeMillis();
        int statusCode = res.getStatusCode();
        if (statusCode == 201 || statusCode == 200) {
          successReqLatencies.offer(new ReqLatency(phaseName, threadID, ReqLatency.ACTION.POST, statusCode, true, postReqStart, postReqEnd));
        } else {
          for (int j = 0; j < NUM_OF_ALLOWED_REATTEMPTS; j++) {
            res = skiersApi.writeNewLiftRideWithHttpInfo(liftRide, 12, "2019", "333", randomID);
            if (res.getStatusCode() == 201 || res.getStatusCode() == 200) break;
          }
          nonSuccessReqLatencies.offer(new ReqLatency(phaseName, threadID, ReqLatency.ACTION.POST, statusCode, false, postReqStart, postReqEnd));
        }
      } catch (ApiException e) {
        e.printStackTrace();
      }
    }
    nextPhaseLatch.countDown();
    curPhaseLatch.countDown();
  }
}