public class Parameters {

  public static final String NUM_THREADS = "numThreads";
  public static final String NUM_SKIERS = "numSkiers";
  public static final String NUM_LIFTS = "numLifts";
  public static final String NUM_LIFTS_PER_SKIER_PER_DAY = "numRuns";
  public static final String IP = "ip";
  public static final String PORT = "port";

  public int numThreads;
  public int numSkiers;
  public int numLifts;
  public int numRuns;
  public String ip;
  public String port;

  public Parameters(int numThreads, int numSkiers, int numLifts, int numRuns, String ip, String port) {
    this.numThreads = numThreads;
    this.numSkiers = numSkiers;
    this.numLifts = numLifts;
    this.numRuns = numRuns;
    this.ip = ip;
    this.port = port;
  }

  public static Parameters parseFromCmd(String[] args) {

    int numThreads = 0;
    int numSkiers = 0;
    int numLifts = 40;
    int numRuns = 10;
    String ip = null;
    String port = null;

    for (int i = 0; i < args.length; i++) {
      String parameter = args[i];
      String value = args[++i];
      if (parameter != null && parameter.equals(NUM_THREADS)) {
        numThreads = Integer.valueOf(value);
      }
      if (parameter != null && parameter.equals(NUM_SKIERS)) {
        numSkiers = Integer.valueOf(value);
      }
      if (parameter != null && parameter.equals(NUM_LIFTS)) {
        numLifts = Integer.valueOf(value);
      }
      if (parameter != null && parameter.equals(NUM_LIFTS_PER_SKIER_PER_DAY)) {
        numRuns = Integer.valueOf(value);
      }
      if (parameter != null && parameter.equals(IP)) {
        ip = value;
      }
      if (parameter != null && parameter.equals(PORT)) {
        port = value;
      }
    }
    return new Parameters(numThreads, numSkiers, numLifts, numRuns, ip, port);
  }
}
