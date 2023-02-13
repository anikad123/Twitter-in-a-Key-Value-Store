import java.util.List;

// This interface represent a database of tweets (short messages posted by users) 
// similar to the web application Twitter
public interface TwitterDatabaseAPI {

    /**
     * This method posts the given tweet to this TwitterDatabaseAPI
     */
    public void postTweet(Tweet t);

    /**
     * This method returns the 10 most recent tweets from the given user_id's timeline
     * (which is a list of tweets posted by any user that that user follows)
     */
    public List<Tweet> getTimeline(Integer user_id);

    /**
     * This method returns the followers of the given user id (who is following user_id?)
     */
    public List<Integer> getFollowers(Integer user_id); 

    /**
     * This method returns the followees of the given user id (who is user_id following?)
     */
    public List<Integer> getFollowees(Integer user_id); 

    /**
     * This method returns the tweets posted by the given user id
     */
    public List<Tweet> getTweets(Integer user_id); 

    /**
     * This method creates an instance of the DBUtils class using the given url, username, and password
     */
    public void authenticate(String url, String user, String password);

    /**
     * This method closes the connection when the application finishes
     */
    public void closeConnection();
}
