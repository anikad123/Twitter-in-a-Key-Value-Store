import java.time.LocalDateTime;

// This class represents tweets that have an id, an associated user id, a date time 
// that represents when the tweet was posted, and a string representing the text of the tweet.
public class Tweet {

    private int tweet_id;
    private int user_id;
    private LocalDateTime tweet_ts;
    private String tweet_text;

    public Tweet(int user_id, String tweet_text) {
        this.tweet_id = -1;
        this.user_id = user_id;
        this.tweet_ts = LocalDateTime.now();
        this.tweet_text = tweet_text;
    }

    public Tweet(int user_id, LocalDateTime tweet_ts, String tweet_text) {
        this.tweet_id = -1;
        this.user_id = user_id;
        this.tweet_ts = tweet_ts;
        this.tweet_text = tweet_text;
    }

    public Tweet(int tweet_id, int user_id, LocalDateTime tweet_ts, String tweet_text) {
        this.tweet_id = tweet_id;
        this.user_id = user_id;
        this.tweet_ts = tweet_ts;
        this.tweet_text = tweet_text;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "tweet_id=" + tweet_id +
                ", user_id='" + user_id + '\'' +
                ", tweet_ts='" + tweet_ts + '\'' +
                ", tweet_text=" + tweet_text + '\'' +
                '}';
    }

    /**
     * This method returns the tweet id of this tweet
     * @return int return the tweet_id
     */
    public int getTweet_id() {
        return tweet_id;
    }

    /**
     * This method sets the tweet id of this tweet to the given tweet id
     * @param tweet_id the tweet_id to set
     */
    public void setTweet_id(int tweet_id) {
        this.tweet_id = tweet_id;
    }

    /**
     * This method returns the user id of this tweet
     * @return int return the user_id
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     * This method sets the user id of this tweet to the given int
     * @param user_id the user_id to set
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    /**
     * This method returns the timestamp of this tweet
     * @return LocalDateTime return the tweet_ts
     */
    public LocalDateTime getTweet_ts() {
        return tweet_ts;
    }

    /**
     * This method sets the timestamp of this tweet to the given tweet timestamp
     * @param tweet_ts the tweet_ts to set
     */
    public void setTweet_ts(LocalDateTime tweet_ts) {
        this.tweet_ts = tweet_ts;
    }

    /**
     * This method returns the text of this tweet
     * @return String return the tweet_text
     */
    public String getTweet_text() {
        return tweet_text;
    }

    /**
     * This method sets the text of this tweet to the given string of tweet text
     * @param tweet_text the tweet_text to set
     */
    public void setTweet_text(String tweet_text) {
        this.tweet_text = tweet_text;
    }

}
