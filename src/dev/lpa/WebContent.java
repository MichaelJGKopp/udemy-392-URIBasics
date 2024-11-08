package dev.lpa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class WebContent {
  
  public static void main(String[] args) {
    
    try {
      URL url = new URL("http://jsonplaceholder.typicode.com/todos?id=5");
      printContents(url.openStream());
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
