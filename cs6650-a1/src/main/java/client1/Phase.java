package client1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


public class Phase {

  private String phaseName;
  private int numThreads;
  private int rangeOfSkierIDs;
  private int numPostReqs;
  private CountDownLatch nextPhaseCountDown;
  private CountDownLatch curPhaseCountDown;
  private int startTime;
  private int endTime;
  private AtomicInteger success;
  private AtomicInteger nonSuccess;
  private Parameters parameters;

  public Phase(String phaseName, int numThreads, int rangeOfSkierIDs, int numPostReqs, CountDownLatch nextPhaseCountDown, int startTime, int endTime, AtomicInteger success, AtomicInteger nonSuccess, Parameters parameters) {
    this.phaseName = phaseName;
    this.numThreads = numThreads;
    this.rangeOfSkierIDs = rangeOfSkierIDs;
    this.numPostReqs = numPostReqs;
    this.nextPhaseCountDown = nextPhaseCountDown;
    this.curPhaseCountDown = new CountDownLatch(numThreads);
    this.startTime = startTime;
    this.endTime = endTime;
    this.success = success;
    this.nonSuccess = nonSuccess;
    this.parameters = parameters;
  }

  public void runPhase() {
    for (int i = 0; i < numThreads; i++) {
      int startID = i * rangeOfSkierIDs + 1;
      int endID = (i == numThreads - 1) ? parameters.numSkiers : (i + 1 ) * rangeOfSkierIDs;
      NewThread t = new NewThread(phaseName, i + 1, numPostReqs, startID, endID, startTime, endTime, nextPhaseCountDown, curPhaseCountDown, success, nonSuccess, parameters);
      Thread thread = new Thread(t);
      thread.start();
    }
  }

  public void waitForTermination() throws InterruptedException {
    curPhaseCountDown.await();
  }
}