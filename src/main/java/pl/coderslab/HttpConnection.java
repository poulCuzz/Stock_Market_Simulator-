package pl.coderslab;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpConnection {

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=META&interval=60min&outputsize=full&apikey=2LK6Y77G2WFNSZW7")).build();
        client.sendAsync(request,HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();

    }

    public static String parse(String responseBody) {
        JSONArray metaStocks = new JSONArray(responseBody);
        for (int i = 0; i < metaStocks.length(); i++) {
            JSONObject metaStock = metaStocks.getJSONObject(i);
            String dateAndTime = metaStock.getString("Time Series (60min)");
            String open = metaStock.getString("4. close");
            System.out.println(dateAndTime + " " + open);
        }
        return null;
    }
}
