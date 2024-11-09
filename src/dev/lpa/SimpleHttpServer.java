package dev.lpa;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

public class SimpleHttpServer {
  
  private static long visitorCounter = 0;
  
  public static void main(String[] args) {
    
    try {
      // lightweight decent for testing, not as performant as third party, JDK 6
      HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
      
      server.createContext("/", exchange -> {
        String requestMethod = exchange.getRequestMethod();
        System.out.println("Request Method: " + requestMethod);
        
        String data = new String(exchange.getRequestBody().readAllBytes());
        System.out.println("Body data: " + data);
        
        Map<String, String> parameters = parseParameters(data);
        System.out.println(parameters);
        
        exchange.getRequestHeaders().entrySet().forEach(System.out::println);
        if (requestMethod.equals("POST")) {
          visitorCounter++;
        }
        
        String firstName = parameters.get("first");
        String lastName = parameters.get("last");
        String response = """
          <html>
            <body>
              <h1>Hello World from My Http Server</h1>
              <p>Number of Visitors who signed up = %d<p>
              <form method="post">
                <label for="first">First name:</label>
                <input type="text" id="first" name="first" value="%s">
                <br>
                <label for="last">Last name:</label>
                <input type="text" id="last" name="last" value="%s">
                <br>
                <input type="submit" value="Submit">
              </form>
            </body>
          </html>
          """.formatted(visitorCounter,
          firstName == null ? "" : firstName,
          lastName == null ? "" : lastName);
        
        byte[] bytes = response.getBytes();
        exchange.sendResponseHeaders(HTTP_OK, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
      });
      
      server.start();
      System.out.println("Server is listening on port: " + server.getAddress().getPort());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
  
  // some Http servers already do this, this simple one doesn't
  private static Map<String, String> parseParameters(String requestBody) {
    
    Map<String, String> parameters = new HashMap<>();
    String[] pairs = requestBody.split("&");
    for (String pair : pairs) {
      String[] keyValue = pair.split("=");
      if (keyValue.length == 2) {
        parameters.put(keyValue[0], keyValue[1]);
      }
    }
    return parameters;
  }
}
