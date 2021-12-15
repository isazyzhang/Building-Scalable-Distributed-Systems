package ResortMS.Model;

import com.google.gson.Gson;

public class LiftRide {
  public Integer resortID;
  public String seasonID;
  public String skierID;
  public String dayID;
  public Integer time;
  public Integer liftID;

  public LiftRide(Integer resortID, String seasonID, String skierID, String dayID, Integer time, Integer liftID) {
    this.resortID = resortID;
    this.seasonID = seasonID;
    this.skierID = skierID;
    this.dayID = dayID;
    this.time = time;
    this.liftID = liftID;
  }

  public Integer getResortID() {
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

  public Integer getTime() {
    return time;
  }

  public Integer getLiftID() {
    return liftID;
  }

  @Override
  public String toString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
