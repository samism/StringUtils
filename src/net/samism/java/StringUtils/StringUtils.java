package net.samism.java.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 7/18/14
 * Time: 9:32 PM
 */
public class StringUtils {
	private StringUtils() throws Exception {
		throw new InstantiationException("Use this class statically. No instantiation.");
	}

	/**
	 * Useful because it gets all the indices instead of the just the first or last one.
	 *
	 * @param string The string to search
	 * @param token  The token to find
	 * @return An {@see java.util.ArrayList} of all the indices found of token in str in order.
	 * If token is not found in str, returns null.
	 * @throws IllegalArgumentException If token.length() > str.length()
	 */
	public static int[] getIndicesOf(String string, String token) {
		if (string.length() < token.length()) {
			throw new IllegalArgumentException("Token's length must not exceed token string's length");
		}

		ArrayList<Integer> indices = new ArrayList<>();

		for (int i = 0; i <= string.length(); i++) {
			if ((string.length() - i) < token.length()) //prevent iobe
				break;
			if (string.substring(i, i + token.length()).equalsIgnoreCase(token)) {
				indices.add(i);
			}
		}

		if (indices.isEmpty())
			return new int[]{}; //return empty array instead of null

		//would use toArray() but it would give me an Integer[] not the primitive int array
		//manually fill up the array & return that.
		int[] arr = new int[indices.size()];
		int i = 0;
		for (Integer n : indices) { //for-each so i dont have to mess with indices too much
			arr[i++] = n; //auto un-boxed from Integer to primitive
		}

		return arr;
	}

	/**
	 * Useful to have more control than simply just getting the first and last index.
	 *
	 * @param string The string to search
	 * @param token  The token to find
	 * @param n      The index occurance to find - <b>n must > 0</b>
	 * @return The index of the nth occurance of str in token, if it exists.
	 * If n is greater than the number of occurrences existing, null will be returned.
	 */
	public static int nthIndexOf(String string, String token, int n) {
		if (n < 1 || string.isEmpty() || token.isEmpty())
			throw new IllegalArgumentException("n was non-positive, or the string and/or token was invalid.");

		n--; //minus one to work with arrays

		int[] indices = getIndicesOf(string, token);

		return n > (indices.length - 1) ? -1 : indices[n];
	}

	/**
	 * Like {@see java.lang.String#trim()} except covers entire string instead of the start and end.
	 * You could do {@see java.lang.String#replaceAll(String, String)}, but calling this method is more semantic.
	 *
	 * @param str String to strip spaces off of
	 * @return Space-free version of str
	 */
	public static String stripSpaces(String str) {
		return str.replaceAll("\\s+", "");
	}

	/**
	 * Useful for turning an unknown amount of consecutive spaces into single spaces
	 *
	 * @param str String to work with
	 * @return If str has a set of two or more consecutive sets of spaces, it returns a single spaced version
	 */
	public static String toSingleSpaced(String str) {
		Pattern p = Pattern.compile("\\s{2,}");
		Matcher m = p.matcher(str);

		return m.find() ? toSingleSpaced(str.replaceAll(p.pattern(), " ")) : str;
	}

	/**
	 * Useful if you want to take a sentence or anything delimited by spaces, and split it into
	 * an array of things you can use.
	 *
	 * @param str String to convert to a word array
	 * @return An array of strings from str
	 */
	public static String[] toWordArray(String str) {
		return toSingleSpaced(str).split(" ");
	}

	/**
	 * Finds the length of the longest element in a String array.
	 *
	 * @param str String array to analyze.
	 * @return An integer size of the longest of the elements in str.
	 */
	private static int getArrayHorizontalLimit(String[] str) {
		int longest = 0;

		for (String s : str) {
			int curLen = s.length();

			if (curLen > longest)
				longest = curLen;
		}

		return longest;
	}

	/**
	 * Useful for getting a String representation of a String array.
	 * <p>
	 * This method comes in when people run into the unexpected output of
	 * <pre>new String[]{"hi", "there"}.toString()</pre>
	 * <p>
	 * To get the excepted results of simply printing the contents of a string array, people
	 * usually go with a for loop to loop through the array and print it's contents. That takes too many
	 * lines, so this method does that for you in one line.
	 * <p>
	 * Differs to {@see Arrays#toString(Object[])} only in the fact that you can choose whether to represent
	 * the elements of the array delimited by newline characters or commas.
	 *
	 * @param str        String array to print out
	 * @param isNewLined Whether or not to separate elements with a tab or by a newline
	 * @return An index-numbered list of the string contents of str
	 */
	public static String arrayToString(String[] str, boolean isNewLined) {
		final StringBuilder str2 = new StringBuilder();
		final int longestLen = getArrayHorizontalLimit(str);

		str2.ensureCapacity(longestLen * str.length);

		for (int i = 0; i < str.length - 1; i++) {
			String me = i + ": " + str[i] + (isNewLined ? "\n" : ((i == str.length - 1) ? "" : ", "));
			str2.append(me);
		}

		return str2.toString();
	}

	/**
	 * Returns the index of where the first occurance of the given substring ends, rather than where it starts
	 * as per String#indexOf
	 *
	 * @param str    String in question
	 * @param phrase substring to find the index of
	 * @return The index of where the first occurance of the given phrase in the string ends rather than where it starts
	 * as per String#indexOf.
	 */
	public static int indexOfLastChar(String str, String phrase) {
		int idx = str.indexOf(phrase);
		int len = phrase.length();

		return idx + len;
	}

	/**
	 * Returns the index of where the nth occurance of the given substring ends, rather than where it starts
	 * as per String#indexOf
	 *
	 * @param str    String in question
	 * @param phrase substring to find the index of
	 * @param n      nth occurence of the phrase
	 * @return The index of where the first occurance of the given phrase in the string ends rather than where it starts
	 * as per String#indexOf.
	 */
	public static int nthIndexOfLastChar(String str, String phrase, int n) {
		int idx = nthIndexOf(str, phrase, n);
		int len = phrase.length();

		return idx + len;
	}

	/**
	 * Decodes a url that has been recursively encoded.
	 *
	 * @param encoded  String that is URLEncoded
	 * @param encoding One of the six standard charsets outlined in @see http://docs.oracle.com/javase/7/docs/api/java/nio/charset/Charset.html
	 * @return String that is completely URLDecoded
	 * @
	 */
	public static String decodeCompletely(String encoded, String encoding) throws UnsupportedEncodingException,
			IllegalArgumentException {
		String uno = encoded;
		String dos = uno;

		do {
			uno = dos;
			dos = URLDecoder.decode(uno, encoding);
		} while (!uno.equals(dos));

		return uno; //can return either one at this point
	}

	/**
	 * For generating relatively universal, safe filenames
	 *
	 * @param s A filename that might be unsafe for an OS
	 * @return The same string with everything except letters, numbers, ".", and "-" replaced with an underscore ("_").
	 */
	public static String normalizeForOS(String s) {
		return s.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
	}

	/**
	 * Determines if any Strings within an array are contained within another String.
	 *
	 * @param inputString The overall String
	 * @param items       The Strings to find within the overall
	 * @return True if some element of the array exists within the String
	 */

	public static boolean containsItemFromList(String inputString, String[] items) {
		for (String item : items) {
			if (inputString.contains(item)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds how many times a char is found within a String.
	 * I made this because the method countMatches(CharSequence str, CharSequence sub) from the commons lang library
	 * wasn't working...
	 *
	 * @param a The char you're looking for
	 * @param b The String in question
	 * @return An int representing how many times a was found in b.
	 */
	public static int countOccurrences(char a, String b) {
		int occ = 0;

		for (char c : b.toCharArray()) {
			if (c == a) {
				occ++;
			}
		}

		return occ;
	}

	/**
	 * A way to print the elements of a String array using the logger from slf4j.
	 *
	 * @param u     The String array to log
	 * @param log   The slf4j Logger instance to use
	 * @param label The prefix before each element for neatness.
	 */

	public static void logStringArray(String[] u, org.slf4j.Logger log, String label) {
		for (String _u : u)
			log.info(label + " " + _u);
	}

	/**
	 * A way to print the elements of a String array with {@see java.lang.System.out#println()}
	 *
	 * @param u     The String array to log
	 * @param label The prefix before each element for neatness.
	 */

	public static void printStringArray(String[] u, String label) {
		for (String _u : u)
			System.out.println(label + " " + _u);
	}

	/**
	 * Prints the contents of an array in a way I can just copy and past as valid Java code.
	 *
	 * @param u The array to parse
	 */

	public static void printAsJavaStringArray(String[] u) {
		StringBuilder s = new StringBuilder("new String[] {");

		for (int i = 0; i < u.length; i++) {
			s.append("\"" + u[i] + "\"");
			if (i == u.length - 1) {
				s.append("};");
			} else {
				s.append(", ");
			}
		}

		System.out.println(s.toString());
	}

	/**
	 * Obtains the amount of times a substring occurs in a given String
	 *
	 * @param source The String to traverse
	 * @param token  The substring to look for, can be a regex
	 * @return The number of times token occurred in source. If both source and token are empty, returns -1.
	 */
	public static int getTokenCount(String source, String token) {
		if (source.isEmpty() || token.isEmpty())
			return -1;

		int count = 0;
		Pattern p = Pattern.compile(token);
		Matcher m = p.matcher(source);

		while (m.find())
			count++;

		return count;
	}
}
