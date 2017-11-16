/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package YouTubeApi;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author A.Y
 */
public class Connections {

    private static final String PROPERTIES_FILENAME = "youtube.properties";

    public Connections() {

    }

    public Properties properties() throws IOException {
           
        //To read the api key and authorise the request 
        Properties properties = new Properties();

        InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME); //reads the PROPERTIES_FILE as a stream
        properties.load(in); 

        return properties;
    }

    public YouTube createCon() {
        
        YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(),
                new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {
                    }
                }).setApplicationName("E-Learning-System").build();

        return youtube;
    }

}
