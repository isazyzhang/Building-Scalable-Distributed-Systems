package server;

import com.google.gson.Gson;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "SkiersServlet")
public class SkierServlet extends HttpServlet {

  Publisher publisher;
  Gson gson = new Gson();

  public void init() {
    try {
      publisher = new Publisher();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

   // {resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
      res.setContentType("application/json");
      String urlPath = req.getPathInfo();
      if (urlPath == null || urlPath.isEmpty()) {
          res.setStatus(HttpServletResponse.SC_NOT_FOUND);
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
        if (urlParts.length == 8) {
          liftRide.setResortID(urlParts[1]);
          liftRide.setSeasonID(urlParts[3]);
          liftRide.setDayID(urlParts[5]);
          liftRide.setSkierID(urlParts[7]);
          publisher.publishToQueue(liftRide);
        }
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write("{\n" +
            "  \"message\": \"success\"\n" +
            "}");
      }
  }

  // Checks whether the URL of a post request is valid
  private boolean isPostUrlValid(String[] urlParts) {
    // After splitting, the number of parameters needs to match
    // "", "{resortID}", "seasons", "{seasonID}",
    // "days", "{dayID}", "skiers", "{skierID}"
    boolean numParameters = urlParts != null && urlParts.length == 8;
    if (!numParameters) return false;
    // Condition for resortID
    boolean resort = isInt(urlParts[1]);
    // Condition for season & season ID
    boolean season = urlParts[2].equals("seasons") && isInt(urlParts[3]);
    // Condition for day & day ID
    boolean day = urlParts[4].equals("days") && isInt(urlParts[5])
        && Integer.valueOf(urlParts[5]) >= 1 && Integer.valueOf(urlParts[5]) <= 366;
    // Condition for skier & skier ID
    boolean skier = urlParts[6].equals("skiers") && isInt(urlParts[7]);
    // a post url is valid only if all the above conditions are satisfied
    return resort && season && day && skier;
  }

  // Verifies whether a string can be converted to an integer
  private boolean isInt(String s) {
    return s != null && s.matches("[0-9]+");
  }
}
