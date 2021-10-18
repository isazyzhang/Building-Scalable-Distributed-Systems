package client1;

public class DataAnalyzer {

  private int successes;
  private int nonsuccesses;
  private long wallTimeInMs;
  private Parameters parameters;

  public DataAnalyzer(int successes, int nonsuccesses, long wallTimeInMs, Parameters parameters) {
    this.successes = successes;
    this.nonsuccesses = nonsuccesses;
    this.wallTimeInMs = wallTimeInMs;
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
    System.out.println("# of successful requests :" + successes);
    System.out.println("# of failed requests :" + nonsuccesses);
    System.out.println("wall time: " + wallTimeInMs);
    System.out.println( "throughput per second: " + (successes + nonsuccesses) / (double) (wallTimeInMs / 1000) + " post requests per second");
  }
}
