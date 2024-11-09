package dev.lpa;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.stream.Stream;

import static java.net.HttpURLConnection.HTTP_OK;

public class HttpClientPost {
  
  public static void main(String[] args) {
    
    try {
      HttpClient client = HttpClient.newBuilder()
                            .connectTimeout(Duration.ofMinutes(1))
                            .version(HttpClient.Version.HTTP_1_1)
                            .build();
      
      HttpRequest request = HttpRequest.newBuilder()
                              .POST(HttpRequest.BodyPublishers.ofString(
                                "first=Joe&last=Smith"
                              ))
                              .uri(URI.create("http://localhost:8080"))
                              .header("Content-Type",
                                "application/x-www-form-urlencoded")
                              .build();
      
      HttpResponse<Stream<String>> response = client.send(request,
        HttpResponse.BodyHandlers.ofLines());
      if (response.statusCode() != HTTP_OK) {
        System.out.println("Error reading web page " + request.uri());
        return;
      }
//      System.out.println( response.body());
      response.body()
        .filter(s -> s.contains("<h1>"))
        .map(s -> s.replaceAll("<[^>]*>", "").strip())
        .forEach(System.out::println);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
