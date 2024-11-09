package dev.lpa;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import static java.net.HttpURLConnection.HTTP_OK;

public class SimpleHttpServer {
  
  public static void main(String[] args) {
    
    try {
      // lightweight decent for testing, not as performant as third party, JDK 6
      HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
      
      server.createContext("/", exchange -> {
        String requestMethod = exchange.getRequestMethod();
        System.out.println("Request Method: " + requestMethod);
        
        String response = """
          <html>
            <body>
              <H1>Hello World from My Http Server</H1>
            </body>
          </html>
          """;
        
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
}
