import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import redis.clients.jedis.Jedis;

public class TwitterRedisStrategy1 implements TwitterDatabaseAPI {

  private Jedis jedis = new Jedis();

  /**
   * This method posts the given tweet to this TwitterDatabaseAPI
   *
   * @param t
   */
  @Override
  public void postTweet(Tweet t) {
    // if latest_tweet_id key does not already exist in jedis, set/initialize it to 1
    if (!(this.jedis.exists("latest_tweet_id"))) {
      this.jedis.set("latest_tweet_id", "1");
    }

    // get the latest_tweet_id from jedis
    int latest_tweet_id = Integer.parseInt(this.jedis.get("latest_tweet_id"));

    // set the key to be tweet_id, and the value to be a string of user_id, timestamp,
    // and text (separated by ;)
    this.jedis.set("tweet_"+latest_tweet_id,
        String.valueOf(t.getUser_id()) + ";" +
            this.formatDateTime(t.getTweet_ts()) + ";" +
            t.getTweet_text());

    // update this.latest_tweet_id (increment id by 1)
    jedis.incr("latest_tweet_id");
  }

  // formats the given LocalDateTime object to a String with the format: yyyy-MM-dd HH:mm:ss
  private String formatDateTime(LocalDateTime date_time) {

    DateTimeFormatter dt_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // format given date_time
    String formatted_date_time = date_time.format(dt_formatter);

    return formatted_date_time;
  }

  /**
   * This method returns the 10 most recent tweets from the given user_id's timeline (which is a
   * list of tweets posted by any user that that user follows)
   *
   * @param user_id
   */
  @Override
  public List<Tweet> getTimeline(Integer user_id) {
    List<String> user_followee_ids = this.jedis.lrange("followees_"+user_id, 0, -1);
    List<Tweet> timeline_tweets = new ArrayList<>();

    int latest_tweet_id = Integer.parseInt(this.jedis.get("latest_tweet_id"))-1;

    // loop through until ten tweets have been added to the timeline or the tweets
    // run out (the latest_tweet_id has been decremented to 0 when the minimum tweet id is 1)
    while ((timeline_tweets.size() < 10) &&
        (latest_tweet_id >= 1)) {
      if (user_followee_ids.contains(this.jedis.get("tweet_"+latest_tweet_id).split(";")[0])) {
        timeline_tweets.add(this.buildTweets(Arrays.asList(String.valueOf(latest_tweet_id))).get(0));
      }
      // decrement latest_tweet_id to look at the next most recent tweet next
      latest_tweet_id--;
    }

    return timeline_tweets;
  }

  /**
   * This method returns the followers of the given user id (who is following user_id?)
   *
   * @param user_id
   */
  public List<Integer> getFollowers(Integer user_id) {
    List<String> followers_str_ids = this.jedis.lrange("followers_"+user_id, 0, -1);

    List<Integer> followers_int_ids = new ArrayList<>();
    for (String follower_str_id : followers_str_ids) {
      followers_int_ids.add(Integer.parseInt(follower_str_id));
    }

    return followers_int_ids;
  }

  /**
   * This method returns the followees of the given user id (who is user_id following?)
   *
   * @param user_id
   */
  public List<Integer> getFollowees(Integer user_id) {
    List<String> followees_str_ids = this.jedis.lrange("followees_"+user_id, 0, -1);

    List<Integer> followees_int_ids = new ArrayList<>();
    for (String followee_str_id : followees_str_ids) {
      followees_int_ids.add(Integer.parseInt(followee_str_id));
    }

    return followees_int_ids;
  }

  /**
   * This method returns the tweets posted by the given user id
   *
   * @param user_id
   */
  public List<Tweet> getTweets(Integer user_id) {
    List<String> user_tweet_str_ids = this.jedis.lrange("user_"+user_id+"_tweets", 0, -1);
    List<Tweet> user_tweets = this.buildTweets(user_tweet_str_ids);

    return user_tweets;
  }

  // converts the given list of tweet_ids to a list of the respective Tweet objects
  private List<Tweet> buildTweets(List<String> tweet_ids) {
    List<Tweet> tweets = new ArrayList<>();

    for (String tweet_id : tweet_ids) {
      List<String> user_tweet = new ArrayList(Arrays.asList(this.jedis.get("tweet_"+tweet_id).split(";")));
      tweets.add(new Tweet(Integer.parseInt(tweet_id),
          Integer.parseInt(user_tweet.get(0)),
          this.formatStrToLDT(user_tweet.get(1)),
          user_tweet.get(2)));
    }

    return tweets;
  }

  // converts the given String (with the format: yyyy-MM-dd HH:mm:ss) to a LocalDateTime object
  private LocalDateTime formatStrToLDT(String str_date_time) {

    DateTimeFormatter dt_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // convert given string to LocalDateTime
    LocalDateTime date_time = LocalDateTime.parse(str_date_time, dt_formatter);

    return date_time;
  }

  /**
   * This method creates an instance of the DBUtils class using the given url, username, and
   * password
   *
   * @param url
   * @param user
   * @param password
   */
  @Override
  public void authenticate(String url, String user, String password) { }

  /**
   * This method closes the connection when the application finishes
   */
  @Override
  public void closeConnection() {
    // when using jedis.flushAll, we were receiving a SocketTimeoutException
    // so we left this function blank
  }
}
