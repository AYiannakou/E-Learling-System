package YouTubeApi;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.Properties;

/**
 *
 * @author A.Y
 */
public class Search {

    private static final long NUMBER_OF_VIDEOS_RETURNED = 50;

    /* Declaration of attributes for adding videos info */
    public ArrayList<BigInteger> likes;
    public ArrayList<BigInteger> dislikes;
    public ArrayList<BigInteger> views;
    public ArrayList<String> ids;
    public ArrayList<String> title;
    public ArrayList<String> icons;
    public ArrayList<String> yearOfVideos;
    public ArrayList<String> videoDuration;
    public ArrayList<String> videoDescription;

    public Search() {

        this.likes = new ArrayList<>();
        this.views = new ArrayList<>();
        this.dislikes = new ArrayList<>();
        this.ids = new ArrayList<>();
        this.title = new ArrayList<>();
        this.icons = new ArrayList<>();
        this.yearOfVideos = new ArrayList<>();
        this.videoDuration = new ArrayList<>();
        this.videoDescription = new ArrayList<>();

    }

    public ArrayList<String> getVideoDescription() {
        /* Returns video descriptions from query */
        return videoDescription;
    }

    public ArrayList<String> getIds() {
        /* Returns ids found from query */
        return ids;

    }

    public ArrayList<String> getTitle() {
        /* Returns titles found from query */
        return title;
    }

    public ArrayList<String> getIcons() {
        /* Returns video pngs found from query */
        return icons;

    }

    public ArrayList<String> getYearOfVideos() {
        /* Returns video adding year found from query */
        return yearOfVideos;
    }

    public ArrayList<String> getVideoDuration() {
        /* Returns video duration found from query */
        return videoDuration;

    }

    public ArrayList<BigInteger> getLikes() {
        /* Returns video likes found from query */
        return likes;

    }

    public ArrayList<BigInteger> getViews() {
        /* Returns video views found from query */
        return views;

    }

    public ArrayList<BigInteger> getDislikes() {
        /* Returns video dislikes found from query */
        return dislikes;

    }

    public void FindVideoIds(String query, String startDate, String endDate) throws IOException, ParseException {

        // Reads application youtube api key 
        Connections con = new Connections();

        Properties prop = con.properties();
        YouTube youtube = con.createCon();

        // Selecting which information to retrieve (id of the video)
        YouTube.Search.List search = youtube.search().list("id");

        //Puts the content of youtube api key into a String
        String apiKey = prop.getProperty("youtube.apikey");

        search.setKey(apiKey); //Set api key
        search.setQ(query); //Set user query
        search.setFields("items(id/kind,id/videoId)"); //to retrieve video ids
        search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED); //Set the maximum results that the application can receive (50)
        search.setType("video"); //To identify only video infortmation 

        if (startDate != null && endDate != null) {

            DateTime start = new DateTime(startDate + "T00:00:00Z");

            DateTime end = new DateTime(endDate + "T00:00:00Z");

            search.setPublishedAfter(start);
            search.setPublishedBefore(end);
        }

        SearchListResponse searchResponse = search.execute(); //Calls the API interface
        List<SearchResult> searchResultList = searchResponse.getItems(); // Using a "List" to insert information  

        Iterator<SearchResult> iterator = searchResultList.iterator();

        while (iterator.hasNext()) {

            SearchResult singleVideo = iterator.next(); //Iterating through the List<SearchResult>

            ResourceId rId = singleVideo.getId();     //Points to YouTube resource to get video ids

            if (rId.getKind().equals("youtube#video")) { //Checks if the results are type of video

                ids.add(rId.getVideoId()); // Embedding video ids into an ArrayList

            }
        }

    }

    public void videoInfo() throws IOException, ParseException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        int zero = 0;

        BigInteger bi = BigInteger.valueOf(zero);

        Connections con = new Connections();  //Using the object Connections to call methods

        Properties prop = con.properties(); //To read the api key and authorise the request 

        YouTube youtube = con.createCon(); //Used to request and recieve Youtube Data infromation

        String apiKey = prop.getProperty("youtube.apikey");

        YouTube.Videos.List videoRequest = youtube.videos().list("snippet,statistics,contentDetails"); //to receive all information of a video

        videoRequest.setKey(apiKey); // sets the API key

        for (String id : ids) {

            videoRequest.setId(id); //sets each id to find information

            VideoListResponse listResponse = videoRequest.execute(); //executes the statement

            List<Video> videoList = listResponse.getItems(); //receiving video information

            Video targetVideo = videoList.iterator().next();

            Thumbnail thumbnail = targetVideo.getSnippet().getThumbnails().getDefault(); // receives the http icon of the video

            /*Checks video likes,views, dislikes and desciprion to add "no description" or 0 if there is no data inside*/
            if (targetVideo.getStatistics().getLikeCount() == null) {

                likes.add(bi);

            } else {

                likes.add(targetVideo.getStatistics().getLikeCount()); // adds video likes 

            }

            if (targetVideo.getStatistics().getViewCount() == null) {

                views.add(bi);

            } else {

                views.add(targetVideo.getStatistics().getViewCount()); // adds video views 
            }

            if (targetVideo.getStatistics().getDislikeCount() == null) {

                dislikes.add(bi);

            } else {

                dislikes.add(targetVideo.getStatistics().getDislikeCount()); //adds video dislikes

            }
            if (targetVideo.getSnippet().getDescription().isEmpty()) {

                videoDescription.add("No description.");

            } else {

                videoDescription.add(targetVideo.getSnippet().getDescription());
            }

            icons.add(thumbnail.getUrl()); //adds the icon of the video
            title.add(targetVideo.getSnippet().getTitle()); // adds the title of video
            DateTime date = targetVideo.getSnippet().getPublishedAt(); // gets the date of video added

            yearOfVideos.add(date.toString().split("T")[0]); // removes the rest of the String to get only the year of the video

            videoDuration.add(targetVideo.getContentDetails().getDuration().replace("PT", "").replace("H", ":").replace("M", ":").replace("S", " ")); // add video durations
        }

    }

}
