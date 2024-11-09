package dev.lpa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class HttpExample {
  
  public static void main(String[] args) {
    
    try {
//      URL url = new URL("https://example.com/extra");
      URL url = new URL("http://localhost:8080");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST"); // "GET"
      connection.setRequestProperty("User-Agent", "Chrome");
      connection.setRequestProperty("Accept", "application/json, text/html");
      connection.setReadTimeout(30000); // 10-15s reasonable number
      
      int responseCode = connection.getResponseCode();
      System.out.printf("Response code: %d%n", responseCode);
      if (responseCode != HTTP_OK) {
        System.out.println("Error reading web page " + url);
        System.out.printf("Error: %s%n", connection.getResponseMessage());
        return;
      }
      printContents(connection.getInputStream()); // implicitly performs .connect, can use though for readability
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static void printContents(InputStream is) {
    
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
      
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
