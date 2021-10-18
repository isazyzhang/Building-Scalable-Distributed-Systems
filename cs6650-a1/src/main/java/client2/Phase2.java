package client2;

import client1.Parameters;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class Phase2 {

  private String phaseName;
  private int numThreads;
  private int rangeOfSkierIDs;
  private int numPostReqs;
  private CountDownLatch nextPhaseCountDown;
  private CountDownLatch curPhaseCountDown;
  private int startTime;
  private int endTime;
  private Queue<ReqLatency> successReqLatencies;
  private Queue<ReqLatency> nonSuccessReqLatencies;
  private Parameters parameters;

  public Phase2(String phaseName, int numThreads, int rangeOfSkierIDs, int numPostReqs, CountDownLatch nextPhaseCountDown, int startTime, int endTime, Queue<ReqLatency> successReqLatencies, Queue<ReqLatency> nonSuccessReqLatencies, Parameters parameters) {
    this.phaseName = phaseName;
    this.numThreads = numThreads;
    this.rangeOfSkierIDs = rangeOfSkierIDs;
    this.numPostReqs = numPostReqs;
    this.nextPhaseCountDown = nextPhaseCountDown;
    this.curPhaseCountDown = new CountDownLatch(numThreads);
    this.startTime = startTime;
    this.endTime = endTime;
    this.successReqLatencies = successReqLatencies;
    this.nonSuccessReqLatencies = nonSuccessReqLatencies;
    this.parameters = parameters;
  }

  public void runPhase() {
    for (int i = 0; i < numThreads; i++) {
      int startID = i * rangeOfSkierIDs + 1;
      int endID = (i == numThreads - 1) ? parameters.numSkiers : (i + 1 ) * rangeOfSkierIDs;
      NewThread2 t = new NewThread2(phaseName, i + 1, numPostReqs, startID, endID, startTime, endTime, nextPhaseCountDown, curPhaseCountDown, successReqLatencies, nonSuccessReqLatencies, parameters);
      Thread thread = new Thread(t);
      thread.start();
    }
  }

  public void waitForTermination() throws InterruptedException {
    curPhaseCountDown.await();
  }
}
