package client1;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class NewThread implements Runnable{

  public String phaseName;
  int threadID;
  int numReqsPerThread;
  int startID;
  int endID;
  int startTime;
  int endTime;
  CountDownLatch nextPhaseLatch;
  CountDownLatch curPhaseLatch;
  AtomicInteger successes;
  AtomicInteger nonsuccesses;
  Parameters parameters;
  private static final int NUM_OF_ALLOWED_REATTEMPTS = 5;
  public ApiClient apiClient;
  public SkiersApi skiersApi;
  int pass;
  int fail;

  public NewThread(String phaseName, int threadID, int numReqsPerThread, int startID, int endID, int startTime, int endTime,CountDownLatch nextPhaseLatch, CountDownLatch curPhaseLatch, AtomicInteger successes, AtomicInteger nonsuccesses, Parameters parameters) {

    this.phaseName = phaseName;
    this.threadID = threadID;
    this.numReqsPerThread = numReqsPerThread;
    this.startID = startID;
    this.endID = endID;
    this.startTime = startTime;
    this.endTime = endTime;
    this.nextPhaseLatch = nextPhaseLatch;
    this.curPhaseLatch = curPhaseLatch;
    this.successes = successes;
    this.nonsuccesses = nonsuccesses;
    this.parameters = parameters;
    this.apiClient = new ApiClient();
    this.skiersApi = new SkiersApi(apiClient);
    this.pass = 0;
    this.fail = 0;
  }

  @Override
  public void run() {
//    apiClient.setBasePath("http://" + parameters.ip + ":" + parameters.port + "/cs6650_a1_war_exploded/skiers/");
    apiClient.setBasePath("http://" + parameters.ip + ":" + parameters.port + "/cs6650-a1_war/skiers/");
    for (int i = 0; i < numReqsPerThread; i++) {
      ApiResponse<Void> res;
      LiftRide newliftRide = new LiftRide();
      newliftRide.setLiftID(ThreadLocalRandom.current().nextInt(1, parameters.numLifts + 1));
      newliftRide.setTime(ThreadLocalRandom.current().nextInt(startTime, endTime + 1));
      int randomID = ThreadLocalRandom.current().nextInt(startID, endID);
      try {
        res = skiersApi.writeNewLiftRideWithHttpInfo(newliftRide, 12, "2019", "333", randomID);
        if (res.getStatusCode() == 201 || res.getStatusCode() == 200) {
          pass++;
        } else {
          for (int j = 0; j < NUM_OF_ALLOWED_REATTEMPTS; j++) {
            res = skiersApi.writeNewLiftRideWithHttpInfo(newliftRide, 12, "2019", "333", randomID);
            if (res.getStatusCode() == 201 || res.getStatusCode() == 200) break;
          }
          fail++;
        }
      } catch (ApiException e) {
        e.printStackTrace();
      }
    }
    successes.addAndGet(pass);
    nonsuccesses.addAndGet(fail);
    nextPhaseLatch.countDown();
    curPhaseLatch.countDown();
  }
}
