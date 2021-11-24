import com.google.gson.Gson;

public class LiftRide {
  public String resortID;
  public String seasonID;
  public String skierID;
  public String dayID;
  public String time;
  public String liftID;

  public LiftRide(String resortID, String seasonID, String skierID, String dayID, String time, String liftID) {
    this.resortID = resortID;
    this.seasonID = seasonID;
    this.skierID = skierID;
    this.dayID = dayID;
    this.time = time;
    this.liftID = liftID;
  }

  public void setResortID(String resortID) {
    this.resortID = resortID;
  }

  public void setSeasonID(String seasonID) {
    this.seasonID = seasonID;
  }

  public void setSkierID(String skierID) {
    this.skierID = skierID;
  }

  public void setDayID(String dayID) {
    this.dayID = dayID;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public void setLiftID(String liftID) {
    this.liftID = liftID;
  }

  public String getResortID() {
    return resortID;
  }

  public String getSeasonID() {
    return seasonID;
  }

  public String getSkierID() {
    return skierID;
  }

  public String getDayID() {
    return dayID;
  }

  public String getTime() {
    return time;
  }

  public String getLiftID() {
    return liftID;
  }

  @Override
  public String toString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
