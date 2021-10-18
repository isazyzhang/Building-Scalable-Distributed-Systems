package server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ResortServlet")
public class ResortServlet extends javax.servlet.http.HttpServlet {

    /*
    POST request adds a new season for a resort, it follows this format:
    /resorts/{resortID}/seasons
   */
  protected void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {
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
          "  \"message\": \"ERROR: missing parameters\"\n" +
          "}");
      return;
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      res.getWriter().write("{\n" +
          "  \"message\": \"success\"\n" +
          "}");
      return;
    }
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    // There are two GET requests:
    // 1. /resorts --> get a list of ski resorts in the database
    // 2. /resorts/{resortID}/seasons --> get a list of seasons for the specified resort
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
          "  \"message\": \"ERROR: missing parameters\"\n" +
          "}");
      return;
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      res.getWriter().write("{\n" +
          "  \"message\": \"success\"\n" +
          "}");
      return;
    }
  }

  // Checks whether the URL of a post request is valid
  private boolean isPostUrlValid(String[] urlParts) {
    // After splitting, the number of parameters needs to match
    // "", "resorts", "{resortID}", "seasons"
    boolean numParameters = urlParts != null && urlParts.length == 4;
    if (!numParameters) return false;
    // Condition for resort & resort ID
    boolean resort = urlParts[1].equals("resorts") && isInt(urlParts[2]);
    // Condition for season
    boolean season = isInt(urlParts[3]) && urlParts.length == 4;
    // a post url is valid only if all the above conditions are satisfied
    return resort && season;
  }

  // Checks whether the URL of a get request is valid
  private boolean isGetUrlValid(String[] urlParts) {
    // 1. /resorts --> length = 2
    // 2. /resorts/{resortID}/seasons --> length = 4
    boolean numParameters = urlParts != null && (urlParts.length == 2 || urlParts.length == 4);
    if (!numParameters) return false;
    if (urlParts.length == 2) {
      return urlParts[1].equals("resorts");
    }
    return isPostUrlValid(urlParts);
  }

  // Verifies whether a string can be converted to an integer
  private boolean isInt(String s) {
    return s != null && s.matches("[0-9]+");
  }
}
