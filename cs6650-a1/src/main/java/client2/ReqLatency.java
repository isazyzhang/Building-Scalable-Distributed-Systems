package client2;

public class ReqLatency {

  public enum ACTION {POST};
  private String phaseName;
  private int threadID;
  private ACTION action;
  private int statusCode;
  private boolean successful;
  private long reqStart;
  private long reqEnd;

  public ReqLatency(String phaseName, int threadID, ACTION action, int statusCode,
      boolean successful, long reqStart, long reqEnd) {
    this.phaseName = phaseName;
    this.threadID = threadID;
    this.action = action;
    this.statusCode = statusCode;
    this.successful = successful;
    this.reqStart = reqStart;
    this.reqEnd = reqEnd;
  }

  public long getReqStart() {
    return this.reqStart;
  }

  public long getReqEnd() {
    return this.reqEnd;
  }

  public String writeToCSV() {
    return action.toString() + "," + reqStart + "," + reqEnd + "," + (reqEnd - reqStart) + "," + statusCode + "\n";
  }
}
