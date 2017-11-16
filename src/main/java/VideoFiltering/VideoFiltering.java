/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VideoFiltering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author A.Y
 */
public class VideoFiltering {

    public HashMap videoViews;
    public HashMap videoLikes;

    public HashMap videoYears;

    public VideoFiltering() {

        this.videoViews = new HashMap<>();
        this.videoLikes = new HashMap<>();
        this.videoYears = new HashMap<>();

    }

    /*Used for adding videos based on user request (Filtering) and return indexes */
    public ArrayList<Integer> loadVideosToFiltering(ArrayList<Integer> result, ArrayList<String> years, ArrayList<Integer> views, String startDate, String endDate) {

        ArrayList<Integer> index;

        index = new ArrayList<>();

        int start;
        int end;

        for (int i = 0; i < years.size(); i++) { // for reading the given ArrayList

            if (startDate.equals("----") && endDate.equals("----")) {

                videoYears.put(result.get(i) + 1 , years.get(i));
                videoViews.put(result.get(i) + 1, views.get(i));
                
                index.add(result.get(i));

            } else {
                
                start = Integer.parseInt(startDate); // converts the startDate to int 
                end = Integer.parseInt(endDate); // converts the endDate to int

                String singleYear = years.get(i).split("-")[0]; //removes the rest of this content to get only the year

                int y1 = Integer.parseInt(singleYear); // convert it to int

                if (start <= y1 && end >= y1) { // Checks the given (startDate) and (endDate) to insert only the index in between them

                    videoYears.put(result.get(i) - 1, years.get(i));
                    videoViews.put(result.get(i) - 1, views.get(i));

                    index.add(result.get(i));

                }
            }

        }
        System.out.println(index);

        return index;
    }
    
    /*To check and change the index to ascending order based on views if requested*/
    public ArrayList<Integer> mostViewVideos() {

        ArrayList<Integer> index;

        index = new ArrayList<>();

        Map r = sortViews(videoViews); 

        Iterator a = r.entrySet().iterator();

        while (a.hasNext()) {

            Map.Entry pair = (Map.Entry) a.next();

            Integer i = (Integer) pair.getKey();

            index.add(i);
        }
        System.out.println("Most rated: " + index);
        return index;
    }
    
    /*Used for variation of videos in ascending order based on views ratings  */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortViews(Map<K, V> map) {
       
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Collections.reverse(list);

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
