import database.DBUtils;

import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;  
import java.time.format.DateTimeFormatter;


public class TwitterDatabaseMySQL implements TwitterDatabaseAPI {

    DBUtils dbu;

    /**
     * This method posts the given tweet to this TwitterDatabaseAPI
     */
    public void postTweet(Tweet t) {
        String sql = "INSERT INTO Tweet (user_id,tweet_text) VALUES" +
                    "('"+t.getUser_id()+"','"+t.getTweet_text()+"')";
        dbu.insertOneRecord(sql);
    }

    /**
     * This method returns the 10 most recent tweets from the given user_id's timeline
     * (which is a list of tweets posted by any user that that user follows)
     */
    public List<Tweet> getTimeline(Integer user_id) {
        String sql = "SELECT * " + 
        		"FROM Tweet " + 
        		"WHERE user_id in " + 
        		"(SELECT follows_id " + 
        		"FROM Follows " + 
        		"WHERE user_id = 1) " + 
        		"ORDER BY tweet_ts DESC " + 
        		"LIMIT 10";

        // initialize list of tweets
        List<Tweet> timelineTweetsList = new ArrayList<Tweet>();

        try {
            // initialize statement        
            Statement stmt = this.dbu.getCon().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
            while (rs.next() != false) {
                timelineTweetsList.add(new Tweet(rs.getInt("tweet_id"), rs.getInt("user_id"), LocalDateTime.parse(rs.getString("tweet_ts"), dtFormatter),
                        rs.getString("tweet_text")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return timelineTweetsList;
    }

    /**
     * This method returns the followers of the given user id (who is following user_id?)
     */
    public List<Integer> getFollowers(Integer user_id) {  // who is following user_id
        String sql = "SELECT user_id FROM Follows WHERE follows_id = " + user_id;

        // initialize list of followers
        List<Integer> followersList = new ArrayList<Integer>();

        try {
            // initialize statement       	
        	Statement stmt = this.dbu.getCon().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next() != false)
                followersList.add(rs.getInt("user_id"));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return followersList;
    }

    /**
     * This method returns the followees of the given user id (who is user_id following?)
     */
    public List<Integer> getFollowees(Integer user_id) { // who is user_id following?
        String sql = "SELECT follows_id FROM Follows WHERE user_id = " + user_id;

        // initialize list of followees
        List<Integer> followeesList = new ArrayList<Integer>();

        try {
            // initialize statement
        	Statement stmt = this.dbu.getCon().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next() != false)
                followeesList.add(rs.getInt("follows_id"));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return followeesList;
    } 

    /**
     * This method returns the tweets posted by the given user id
     */
    public List<Tweet> getTweets(Integer user_id) { // tweets posted by user_id
        String sql = "SELECT * FROM Tweet WHERE user_id = " + user_id;

        // initialize list of Tweets
        List<Tweet> tweets = new ArrayList<Tweet>();

        try {
            // initialize statement     	
        	Statement stmt = this.dbu.getCon().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            while (rs.next() != false)
                tweets.add(new Tweet(rs.getInt("tweet_id"), rs.getInt("user_id"), LocalDateTime.parse(rs.getString("tweet_ts"), dtFormatter),
                        rs.getString("tweet_text")));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return tweets;
    }


    /**
     * This method creates an instance of the DBUtils class using the given url, username, and password
     */
    public void authenticate(String url, String user, String password) {
        dbu = new DBUtils(url, user, password);
    }


    /**
     * This method closes the connection when the application finishes
     */
    public void closeConnection() { 
        dbu.closeConnection(); 
    }

}
