/*
 * Ethan Gutknecht - Copyright (C) 2022
 * 10/29/2022
 * CSE486 - AI
 * 
 * NaiveBayesSpamFilter algorithm
 */

import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import com.opencsv.CSVReader;

public class SpamFilter {
	// ------ Test the Emails ------ 
	public static String[] testemails = 
		{"Hey Bill I'm reaching out to let you know that I will be out of the office tomorrow at noon. Let me know if you have any questions. - John", // Should not be spam.
		 "Click this link to win a 500 dollar amazon gift card. -Amazon", // Should be spam.
		 "Hey John, the company just finalized their spreadsheet, want to look it over before we send it into the bank. Thanks, Jim", // Should be ham.
		 "Hey there friend! I hope you're doing well.",
		 "There has been an unexpected purchase on your Apple account. Send us your account details for more information.",
		 "We are calling to reach you about your cars extended warrenty"};// Should be spam.

	
	// ------ Global Variables ------ 
	public static int numberOfSpamEmails;
	public static int numberOfHamEmails;

	
	// ------ Functions ------ 
	public static void main(String[] args) {
		// Variables For Words
		HashMap<String, Integer> spamWordsMap = new HashMap<>();
		HashMap<String, Integer> hamWordsMap = new HashMap<>();

		// Read the CSV and store the values in a map
		readEmailCSV(spamWordsMap, hamWordsMap);

		// Print stats
		System.out.println(" ------ Stats ------ ");
		System.out.println("Number of Unique Spam Words: " + spamWordsMap.size());
		System.out.println("Number of Unique Ham Words: " + hamWordsMap.size());
		System.out.println("Number of Spam Emails: " + numberOfSpamEmails);
		System.out.println("Number of Ham Emails: " + numberOfHamEmails);

		// Test E-mails
		System.out.println("\n ------ Tests ------ ");
		for (String e : testemails) {
			testEmail(e, spamWordsMap, hamWordsMap);
		}
	}


	/*
	 * Comments / notes taken
	 * 1. Get a group of emails mark them as "spam" and "ham"
	 * 
	 * 2. use the spam group to train algorithm of spam emails. Get the words
	 * from each spam email and add them to our HashSet Go through each word and
	 * add them to the HashMap if they are not already in there, if they are
	 * increment its values For every email, increment numOfSpamEmails by one
	 * 
	 * 3. use the ham group to train algorithm of a normal email. Get the
	 * words from each Ham email and add them to our HashSet Go through each word
	 * and add them to the HAM HashMap if they are not already in there, if they
	 * are increment its values For every email, increment numOfHamEmails by one
	 */		
	public static void readEmailCSV(HashMap<String, Integer> spamWordsMap, HashMap<String, Integer> hamWordsMap) {
		// Reset email count
		numberOfSpamEmails = 0;
		numberOfHamEmails = 0;
		
		HashSet<String> currentEmailWordSet = new HashSet<>();

		try {
			FileReader filereader = new FileReader("emailDataSet.csv");
			CSVReader csvReader = new CSVReader(filereader);
			String[] emailRecord;

			// Read the file filled with e-mails. One iteration = one email
			while ((emailRecord = csvReader.readNext()) != null) {
				// Extract email type and contents from CSV
				String emailType = emailRecord[1];
				String emailContents = emailRecord[2];
				
				// Creates a list of words that are in the email and clears out old list
				currentEmailWordSet = new HashSet<>();
				currentEmailWordSet.addAll(Arrays.asList(emailRecord[2].toLowerCase().split(" ")));

				// Tracks the number of emails in each group
				if (emailType.contains("spam")) {
					numberOfSpamEmails++;
				} else if (emailType.contains("ham")) {
					numberOfHamEmails++;
				}

				// Go through every word and increment its entry
				// within the map and it to the map if it does not exist.
				for (String s : currentEmailWordSet) {
					if (emailType.equals("spam")) {
						spamWordsMap.merge(s, 1, Integer::sum);
					} else if (emailType.equals("ham")) {
						hamWordsMap.merge(s, 1, Integer::sum);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testEmail(String emailToTest, HashMap<String, Integer> spamWordsMap,
			HashMap<String, Integer> hamWordsMap) {
		// Create list of distinct words from our email to test
		ArrayList<String> emailToTestWordSet = new ArrayList<>();
		emailToTestWordSet.addAll(Arrays.asList(emailToTest.toLowerCase().split(" ")));

		// Probabilities Variables
		double probGivenWordsAreSpam = 1.0;
		double probGivenWordsAreHam = 1.0;
		double probabilityOfSpamEmails = ((double) numberOfSpamEmails)
				/ ((double) (numberOfSpamEmails + numberOfHamEmails));
		double probabilityOfHamEmails = ((double) numberOfHamEmails)
				/ ((double) (numberOfSpamEmails + numberOfHamEmails));

		// Go through every word in the email and get the probability if it is spam
		for (String s : emailToTestWordSet) {
			if (!spamWordsMap.containsKey(s) || !hamWordsMap.containsKey(s))
				continue;

			// Find probability given word is spam
			probGivenWordsAreSpam = probGivenWordsAreSpam * ((spamWordsMap.get(s) + 1.0) / (numberOfSpamEmails + 2.0));

			// Find probability given word is ham
			probGivenWordsAreHam = probGivenWordsAreHam * ((hamWordsMap.get(s) + 1.0) / (numberOfHamEmails + 2.0));
		}

		// Calculate overall spam for email
		double overallProbEmailIsSpam = (probabilityOfSpamEmails * probGivenWordsAreSpam)
				/ ((probabilityOfSpamEmails * probGivenWordsAreSpam) + (probabilityOfHamEmails * probGivenWordsAreHam));

		System.out.println("There is a " + (new DecimalFormat("###.###").format(overallProbEmailIsSpam * 100.0)) + "% chance that this email is spam.");
	}

	/*
		Created a function to clean the email but decided to not use it
	
		public static String cleanEmail(String dirtyEmail) {
			String rs = "";
			for (int i = 0; i < dirtyEmail.length(); i++) {
				char currChar = dirtyEmail.charAt(i);
	
				if ((currChar >= 'a' && currChar <= 'z') || (currChar >= 'A' && currChar <= 'Z') || currChar == ' ')
					rs = rs + currChar;
			}
			return rs.toLowerCase();
		}
	*/
}
