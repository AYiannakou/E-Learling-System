package GoogleSearch;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class CustomSearch {

    ArrayList<String> urlsFound;

    public CustomSearch() {

        this.urlsFound = new ArrayList<>();

    }

    public ArrayList<String> getUrlsFound() {

        return this.urlsFound;

    }

    public void findUlrs(String query) throws MalformedURLException, IOException {

        String key = "AIzaSyD9UJhuVEU6jY4t2Sdd_kNCYhXZMF_D630"; // API key 
        URL url = new URL("https://www.googleapis.com/customsearch/v1?key=" + key + "&cx=013036536707430787589:_pqjad5hr1a&q=" + query + "&alt=json");  //Given the http using our API key and query
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //create http connection
        conn.setRequestMethod("GET"); // parameter GET to receive websites

        conn.setRequestProperty("Accept", "application/json"); //set request header
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;

        while ((output = br.readLine()) != null) {

            if (output.contains("\"link\": \"")) {
                String link = output.substring(output.indexOf("\"link\": \"") + ("\"link\": \"").length(), output.indexOf("\",")); //websites found
                urlsFound.add(link);
            }
        }
        conn.disconnect();

    }

    public static void main(String[] args) throws IOException {

        CustomSearch cs = new CustomSearch();

        cs.findUlrs("javastringtutorials");

        ArrayList<String> a = cs.getUrlsFound();

        for (String b : a) {

            System.out.println(b);
        }

    }
}
