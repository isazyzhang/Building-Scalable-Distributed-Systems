package client;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class DataAnalyzer {

  private long wallTime;
  private Parameters parameters;
  private Queue<ReqLatency> successReqLatencies;
  private Queue<ReqLatency> nonSuccessReqLatencies;

  public DataAnalyzer(Queue<ReqLatency> successReqLatencies, Queue<ReqLatency> nonSuccessReqLatencies, long wallTime, Parameters parameters) {
    this.successReqLatencies = successReqLatencies;
    this.nonSuccessReqLatencies = nonSuccessReqLatencies;
    this.wallTime = wallTime;
    this.parameters = parameters;
  }

  public void generate() {
    System.out.println("Number of threads: " + parameters.numThreads);
    System.out.println("Number of skiers: " + parameters.numSkiers);
    System.out.println("Number of lifts: " + parameters.numLifts);
    System.out.println("Number of runs per skier per day: " + parameters.numRuns);
    System.out.println("IP: " + parameters.ip);
    System.out.println("Port: " + parameters.port);
    System.out.println("=========================================================");
    System.out.println("# of successful requests :" + successReqLatencies.size());
    System.out.println("# of failed requests :" + nonSuccessReqLatencies.size());
    System.out.println("wall time: " + wallTime);
    System.out.println( "throughput per second: " + (successReqLatencies.size() + nonSuccessReqLatencies.size()) / (double) (wallTime / 1000) + " post requests per second");
    System.out.println("=========================================================");
    analyze("POST", successReqLatencies, nonSuccessReqLatencies);
  }

  private void analyze(String action, Queue<ReqLatency> successReqLatencies, Queue<ReqLatency> nonSuccessReqLatencies) {
    List<Long> successLatenciesAscending = sortLatencies(successReqLatencies);
    List<Long> nonSuccessLatenciesAscending = sortLatencies(nonSuccessReqLatencies);
    List<Long> totalAscending = sortLatencies(successLatenciesAscending, nonSuccessLatenciesAscending);

    long meanResponseTime = mean(totalAscending);
    long medianResponseTime = median(totalAscending);
    long ninetyNinePercentile = percentile(totalAscending, 99);
    long maxResponseTime = findMax(totalAscending);

    System.out.println("Mean response time (ms): " + meanResponseTime);
    System.out.println("Median response time (ms): " + medianResponseTime);
    System.out.println("99th percentile response time (ms): " + ninetyNinePercentile);
    System.out.println("Max response time (ms): " + maxResponseTime);
  }

  private List<Long> sortLatencies(List<Long> successLatenciesAscending, List<Long> nonSuccessLatenciesAscending) {
    List<Long> res = new ArrayList<>();
    res.addAll(successLatenciesAscending);
    res.addAll(nonSuccessLatenciesAscending);
    Collections.sort(res);
    return res;
  }

  private List<Long> sortLatencies(Queue<ReqLatency> reqLatencies) {
    List<Long> res = new ArrayList<>();
    for (ReqLatency info : reqLatencies) {
      res.add(info.getReqEnd() - info.getReqStart());
    }
    Collections.sort(res);
    return res;
  }

  private long mean(List<Long> latencies) {
    long sum = 0;
    for (long latency : latencies) {
      sum += latency;
    }
    return sum / latencies.size();
  }

  private long median(List<Long> latencies) {
    return latencies.get(latencies.size() / 2);
  }

  private long percentile(List<Long> latencies, int percentage) {
    int percentIndex = (int)Math.round((double)latencies.size() * (double)percentage / 100.0) - 1;
    return latencies.get(percentIndex);
  }

  private long findMax(List<Long> latencies) {
    return latencies.get(latencies.size() - 1);
  }

  public void writeToFile(int numThreads) {
    writeToCSV("success_record_" + numThreads, successReqLatencies);
    writeToCSV("nonsuccess_record_" + numThreads, nonSuccessReqLatencies);
  }

  private void writeToCSV(String fileName, Queue<ReqLatency> reqLatencies) {
    try {
      FileWriter csvWriter = new FileWriter(fileName + ".csv");
      csvWriter.append("action,startTime,endTime,latency,statusCode\n");
      for (ReqLatency latency : reqLatencies) {
        csvWriter.append(latency.writeToCSV());
      }
      csvWriter.flush();
      csvWriter.close();
    }
    catch (IOException e) {
    }
  }
}