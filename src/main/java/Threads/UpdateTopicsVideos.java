/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Database.Database;
import YouTubeApi.Search;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author A.Y
 */
public class UpdateTopicsVideos implements Runnable {

    Search search = new Search();
    Database bs = new Database();

    /* Declaration of attributes for video info*/
    ArrayList<BigInteger> likes;
    ArrayList<BigInteger> dislikes;
    ArrayList<BigInteger> views;
    ArrayList<String> ids;
    ArrayList<String> title;
    ArrayList<String> icons;
    ArrayList<String> yearOfVideos;
    ArrayList<String> videoDuration;
    ArrayList<String> videoDescription;
    String t;

    public String topic;

    public UpdateTopicsVideos(String topic1) {

        this.topic = topic1;

        this.views = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.dislikes = new ArrayList<>();
        this.ids = new ArrayList<>();
        this.title = new ArrayList<>();
        this.videoDuration = new ArrayList<>();
        this.yearOfVideos = new ArrayList<>();
        this.icons = new ArrayList<>();
        this.videoDescription = new ArrayList<>();

    }

    @Override
    public void run() {

        int newVideos = 0;
        try {

            search.FindVideoIds("Java " + topic, "2005-01-01", "2017-01-01");
            search.videoInfo();

            ids = search.getIds();
            likes = search.getLikes();
            dislikes = search.getDislikes();
            views = search.getViews();
            title = search.getTitle();
            icons = search.getIcons();
            yearOfVideos = search.getYearOfVideos();
            videoDuration = search.getVideoDuration();
            videoDescription = search.getVideoDescription();

        } catch (IOException | ParseException | SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(UpdateTopicsVideos.class.getName()).log(Level.SEVERE, null, ex);

        }

        try {

            newVideos = bs.addVideoInfoToTable(title, views, likes, dislikes, videoDuration, yearOfVideos, icons, ids, videoDescription);

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(UpdateTopicsVideos.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(newVideos + " new videos found");
    }

    public static void main(String[] args) throws IOException, ParseException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException {

        UpdateTopicsVideos utv1 = new UpdateTopicsVideos("Strings");
        Thread t1 = new Thread(utv1);

        t1.start();
        t1.join();

        UpdateTopicsVideos utv2 = new UpdateTopicsVideos("Arrays");
        Thread t2 = new Thread(utv2);

        t2.start();
        t2.join();

        UpdateTopicsVideos utv3 = new UpdateTopicsVideos("Objects and Classes");
        Thread t3 = new Thread(utv3);

        t3.start();
        t3.join();

        UpdateTopicsVideos utv4 = new UpdateTopicsVideos("ArrayList");
        Thread t4 = new Thread(utv4);

        t4.start();
        t4.join();

        UpdateTopicsVideos utv5 = new UpdateTopicsVideos("Methods");
        Thread t5 = new Thread(utv5);

        t5.start();
        t5.join();

        UpdateTopicsVideos utv6 = new UpdateTopicsVideos("HashMap");
        Thread t6 = new Thread(utv6);

        t6.start();
        t6.join();

        UpdateTopicsVideos utv7 = new UpdateTopicsVideos("Operators");
        Thread t7 = new Thread(utv7);

        t7.start();
        t7.join();
        
        UpdateTopicsVideos utv8 = new UpdateTopicsVideos("for loop");
        Thread t8 = new Thread(utv8);

        t8.start();
        t8.join();
        
        UpdateTopicsVideos utv9 = new UpdateTopicsVideos("do while");
        Thread t9 = new Thread(utv9);

        t9.start();
        t9.join();
        
        
        UpdateTopicsVideos utv10 = new UpdateTopicsVideos("Integer");
        Thread t10 = new Thread(utv10);

        t10.start();
        t10.join();
        
        
        UpdateTopicsVideos utv11 = new UpdateTopicsVideos("Multidimensional Arrays");
        Thread t11 = new Thread(utv11);

        t11.start();
        t11.join();

    }
}
