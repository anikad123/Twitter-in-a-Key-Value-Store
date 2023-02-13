import java.io.File;
import java.util.Scanner;
import redis.clients.jedis.Jedis;

/**
 * Class to programmatically upload the followers data from a follows.csv file to buckets (lists)
 * in Redis through Jedis
 */
public class UploadFollowersData {

  public static void main(String[] args) throws Exception {

    Jedis jedis = new Jedis();

    Scanner sc = new Scanner(new File("/Users/anikadas/Downloads/hw1_data/follows.csv"));
    sc.useDelimiter("\n");
    sc.next();

    String follows_data = "";
    String follows_data_user_id = "";
    String follows_data_follows_id = "";

    while (sc.hasNext()) {
      follows_data = sc.next();
      follows_data_user_id = follows_data.split(",")[0];
      follows_data_follows_id = follows_data.split(",")[1];

      // add follower data for follows_id
      jedis.lpush("followers_"+follows_data_follows_id, follows_data_user_id);

      // add followee data for user_id
      jedis.lpush("followees_"+follows_data_user_id, follows_data_follows_id);
    }

    // close the scanner
    sc.close();

  }
}
