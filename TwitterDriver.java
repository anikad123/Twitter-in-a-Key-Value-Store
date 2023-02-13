import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This program exercises the TwitterDatabaseAPI (MySQL implementation)
 * (where nothing other than the instantiation of the API shows that
 * the underlying database is Relational, or MySQL).
 */
public class TwitterDriver {

	private static TwitterDatabaseAPI api = new TwitterRedisStrategy1();

	public static void main(String[] args) throws Exception {

		///////////////// Part 1: program to simulate users posting tweets
		Scanner sc = new Scanner(new File("/Users/anikadas/Downloads/hw1_data/tweet.csv"));
		sc.useDelimiter("\n");
		String tweet1_data = "";
		int tweet1_userId = -1;
		String tweet1_text = "";

		// get start time
		long startTime_p1 = System.nanoTime();

		sc.next();

		while (sc.hasNext()) {
			tweet1_data = sc.next();
			tweet1_userId = Integer.parseInt(tweet1_data.split(",")[0]);
			tweet1_text = tweet1_data.split(",")[1];

			// create Tweet object for scanned file values
			Tweet tweet1 = new Tweet(tweet1_userId, tweet1_text);

			// insert Tweet
			api.postTweet(tweet1);
		}

		// get end time
		long endTime_p1 = System.nanoTime();

		// calculate duration of process
		long duration_p1 = (endTime_p1 - startTime_p1);

		// convert nano seconds to seconds
		// 1 second = 1,000,000,000 nano seconds
		double durationInSeconds_p1 = (double) duration_p1 / 1000000000;

		System.out.println("Duration in Seconds for Part 1 (program to simulate users posting tweets): " + durationInSeconds_p1);

		// close the scanner
		sc.close();

		///////////////// Part 2: program to simulate users opening the twitter app on their
		///////////////// smartphones and refreshing the home timeline to see new posts

		// get start time
		long startTime_p2 = System.nanoTime();

		// repeatedly (10 times) pick a random user and return the 10 most recent tweets
		// from their timeline
		for (int i = 0; i < 10; i++) {
			// get a random user id (rand_user_id) in the range of 1 to 9999
			int max_user_id = 9999;
			int rand_user_id = ThreadLocalRandom.current().nextInt(1, max_user_id + 1);

			// get the home timeline of the random user
			List<Tweet> rand_user_timeline = api.getTimeline(rand_user_id);
		}

		// get end time
		long endTime_p2 = System.nanoTime();

		// calculate duration of process
		long duration_p2 = (endTime_p2 - startTime_p2);

		// convert nano seconds to seconds
		// 1 second = 1,000,000,000 nano seconds
		double durationInSeconds_p2 = (double) duration_p2 / 1000000000;

		System.out.println("Duration in Seconds for Part 2 (program to simulate users opening \n" +
				"the twitter app on their smartphones and refreshing the home timeline to see new posts): " + durationInSeconds_p2);

		api.closeConnection();
	}
}