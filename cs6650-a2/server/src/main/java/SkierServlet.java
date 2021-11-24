import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {

  /*
    POST requests write a new lift for the skier, it follows this format:
    /{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
    Result: stores new lift ride details in the data store
   */
  Publisher publisher;
  Gson gson = new Gson();

  public void init() {
    try {
      publisher = new Publisher();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setContentType("application/json");
    String urlPath = req.getPathInfo();
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write("{\n" +
          "  \"message\": \"ERROR: no url provided\"\n" +
          "}");
      return;
    }
    String[] urlParts = urlPath.split("/");
    if (!isPostUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write("{\n" +
          "  \"message\": \"ERROR: missing or incorrect parameters\"\n" +
          "}");
    } else {
      LiftRide liftRide = gson.fromJson(req.getReader(), LiftRide.class);
      // "", "skiers", "{resortID}", "seasons", "{seasonID}",
      // "days", "{dayID}", "skiers", "{skierID}"
      liftRide.setResortID(urlParts[2]);
      liftRide.setSeasonID(urlParts[4]);
      liftRide.setDayID(urlParts[6]);
      liftRide.setSkierID(urlParts[8]);
      publisher.publishToQueue(liftRide);
      res.setStatus(HttpServletResponse.SC_OK);
      res.getWriter().write("{\n" +
          "  \"message\": \"success\"\n" +
          "}");
    }
  }

  /*
    GET requests:
    - Get the total vertical for the skier for the specified ski day
    /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
    - Get the total vertical for the skier at the specified resort. If
      no season is specified, return all seasons.
    /skiers/{skierID}/skiers{resortID}/vertical --> returns all seasons
    /skiers/{skierID}/skiers{resortID}/seasons/{seasonID}/vertical --> for
    a specific season
   */
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setContentType("application/json");
    String urlPath = req.getPathInfo();
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write("{\n" +
          "  \"message\": \"ERROR: no url provided\"\n" +
          "}");
      return;
    }
    String[] urlParts = urlPath.split("/");
    if (!isGetUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write("{\n" +
          "  \"message\": \"ERROR: missing or incorrect parameters\"\n" +
          "}");
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      res.getWriter().write("{\n" +
          "  \"message\": \"success\"\n" +
          "}");
    }
  }

  // Checks whether the URL of a post request is valid
  private boolean isPostUrlValid(String[] urlParts) {
    // After splitting, the number of parameters needs to match
    // "", "skiers", "{resortID}", "seasons", "{seasonID}",
    // "days", "{dayID}", "skiers", "{skierID}"
    boolean numParameters = urlParts != null && urlParts.length == 9;
    if (!numParameters) return false;
    // Condition for resort & resort ID
    boolean resort = urlParts[1].equals("skiers") && isInt(urlParts[2]);
    // Condition for season & season ID
    boolean season = urlParts[3].equals("seasons") && isInt(urlParts[4]);
    // Condition for day & day ID
    boolean day = urlParts[5].equals("days") && isInt(urlParts[6])
        && Integer.valueOf(urlParts[6]) >= 1 && Integer.valueOf(urlParts[6]) <= 366;
    // Condition for skier & skier ID
    boolean skier = urlParts[7].equals("skiers") && isInt(urlParts[8]);
    // a post url is valid only if all the above conditions are satisfied
    return resort && season && day && skier;
  }

  // Checks whether the URL of a get request is valid
  private boolean isGetUrlValid(String[] urlParts) {
    // There are two GET requests:
    // 1. /skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
    // After splitting, generates 9 parameters and can use the same valid method as POST request
    // 2. /skiers/{resortID}/skiers/{skierID}/vertical   OR
    //    /skiers/{resortID}/skiers/{skierID}/seasons/{seasonID}/vertical
    if (urlParts == null || (urlParts.length != 9 && urlParts.length != 6 && urlParts.length != 8)) {
      return false;
    }
    if (urlParts.length == 9) {
      return isPostUrlValid(urlParts);
      // /skiers/{resortID}/skiers/{skierID}/vertical
    } else if (urlParts.length == 6) { // Without the optional season parameter
      return (urlParts[1].equals("skiers") && isInt(urlParts[2]))
          && (urlParts[3].equals("skiers") && isInt(urlParts[4]))
          && (urlParts[5].equals("vertical"));
      // /skiers/{resortID}/skiers/{skierID}/seasons/{seasonID}/vertical
    } else { // With the optional season parameter
      return (urlParts[1].equals("skiers") && isInt(urlParts[2]))
          && (urlParts[3].equals("skiers") && isInt(urlParts[4]))
          && (urlParts[5].equals("seasons") && isInt(urlParts[6]))
          && (urlParts[7].equals("vertical"));
    }
  }

  // Verifies whether a string can be converted to an integer
  private boolean isInt(String s) {
    return s != null && s.matches("[0-9]+");
  }
}
