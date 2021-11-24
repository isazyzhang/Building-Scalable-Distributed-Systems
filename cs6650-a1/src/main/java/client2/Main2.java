package client2;
import client1.Parameters;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class Main2 {

  public static void main(String[] args) throws InterruptedException {
    Parameters parameters = Parameters.parseFromCmd(args);
    Queue<ReqLatency> successReqLatencies = new ConcurrentLinkedQueue<>();
    Queue<ReqLatency> nonSuccessReqLatencies = new ConcurrentLinkedQueue<>();
    long start = System.currentTimeMillis();

    // Phase 1: startup phase; launches numThreads/4 threads
    int numThreadsPhase1 = parameters.numThreads / 4;
    int rangeOfSkierIDs1 = (int) Math.ceil(parameters.numSkiers / (double) numThreadsPhase1);
    int posReqs1 = (int) (parameters.numRuns * 0.2 * (parameters.numSkiers / (double) numThreadsPhase1));
    // Once 10% of the threads have been started in Phase 1, start phase 2:
    int phase2Trigger = (int) Math.ceil(numThreadsPhase1 / 10.0);
    CountDownLatch phase2Latch = new CountDownLatch(phase2Trigger);
    Phase2 phase1 = new Phase2("Phase One", numThreadsPhase1, rangeOfSkierIDs1, posReqs1, phase2Latch, 1, 90, successReqLatencies, nonSuccessReqLatencies, parameters);
    phase1.runPhase();

    // Phase 2: peak phase; launches "numThreads" threads
    int numThreadsPhase2 = parameters.numThreads;
    int rangeOfSkierIDs2 = (int) Math.floor(parameters.numSkiers / (double) numThreadsPhase2);
    int posReqs2 = (int) (parameters.numRuns * 0.6 * (parameters.numSkiers / (double) numThreadsPhase2));
    // Once 10% of the threads have been started in Phase 2, start phase 3:
    int phase3Trigger = (int) Math.ceil(numThreadsPhase2 / 10.0);
    CountDownLatch phase3Latch = new CountDownLatch(phase3Trigger);
    Phase2 phase2 = new Phase2("Phase Two", numThreadsPhase2, rangeOfSkierIDs2, posReqs2, phase3Latch, 91, 360, successReqLatencies, nonSuccessReqLatencies, parameters);
    phase2Latch.await();
    phase2.runPhase();

    // Phase 3: cool-down phase, starts 25% of threads
    int numThreadPhase3 = parameters.numThreads / 4;
    int rangeOfSkierIDs3 = (int) Math.ceil(parameters.numSkiers / (double) numThreadPhase3);
    int posReqs3 = (int) Math.ceil(parameters.numRuns * 0.1);
    CountDownLatch endLatch = new CountDownLatch(numThreadPhase3);
    Phase2 phase3 = new Phase2("Phase Three", numThreadPhase3, rangeOfSkierIDs3, posReqs3, endLatch, 361, 420, successReqLatencies, nonSuccessReqLatencies, parameters);
    phase3Latch.await();
    phase3.runPhase();

    phase1.waitForTermination();
    phase2.waitForTermination();
    phase3.waitForTermination();

    long end = System.currentTimeMillis();
    long wallTime = end - start;

    DataAnalyzer dataAnalyzer = new DataAnalyzer(successReqLatencies, nonSuccessReqLatencies, wallTime, parameters);
    dataAnalyzer.generate();
    dataAnalyzer.writeToFile(parameters.numThreads);
  }
}
