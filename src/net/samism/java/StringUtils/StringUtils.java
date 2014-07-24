package net.samism.java.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
		throw new Exception("Use this class statically. No instantiation.");
	}

	/**
	 * Useful because it gets all the indices instead of the just the first or last one.
	 *
	 * @param str   The string to search
	 * @param token The token to find
	 * @return An {@see java.util.ArrayList} of all the indices found of token in str in order.
	 * If token is not found in str, returns null.
	 * @throws IllegalArgumentException If token.length() > str.length()
	 */
	public static int[] getIndicesOf(String str, String token) {
		if (str.length() < token.length()) {
			throw new IllegalArgumentException("token's length must not exceed searched string's length");
		}

		ArrayList<Integer> indices = new ArrayList<Integer>();

		for (int i = 0; i <= str.length(); i++) {
			if ((str.length() - i) < token.length()) //prevent iobe
				break;
			if (str.substring(i, i + token.length()).equalsIgnoreCase(token)) {
				indices.add(i);
			}
		}

		int[] indiceArray = new int[indices.size()];

		for (int i = 0; i < indices.size(); i++) {
			indiceArray[i] = indices.get(i);
		}

		return indices.size() == 0 ? null : indiceArray;
	}

	/**
	 * Useful to have more control than simply just getting the first and last index.
	 *
	 * @param str   The string to search
	 * @param token The token to find
	 * @param n     The index occurance to find - <b>n must > 0</b>
	 * @return The index of the nth occurance of str in token, if it exists.
	 * If n is greater than the number of occurrences existing, null will be returned.
	 */
	public static int nthIndexOf(String str, String token, int n) {
		if (n < 1) n = 1;
		int[] indices = getIndicesOf(str, token);

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
		return str.contains("  ") ? toSingleSpaced(str.replace("  ", " ")) : str;
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
	 * Gets the length of the longest element in a String array.
	 *
	 * @param str String array to analyze.
	 * @return An integer size of the longest of the elements in str.
	 */
	private static int getLongestElementLength(String[] str) {
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
	 * <p/>
	 * This method comes in when people run into the unexpected output of
	 * <pre>new String[]{"hi", "there"}.toString()</pre>
	 * <p/>
	 * To get the excepted results of simply printing the contents of a string array, people
	 * usually go with a for loop to loop through the array and print it's contents. That takes too many
	 * lines, so this method does that for you in one line.
	 * <p/>
	 * Differs to {@see Arrays#toString(Object[])} only in the fact that you can choose whether to represent
	 * the elements of the array delimited by newline characters or commas.
	 *
	 * @param str        String array to print out
	 * @param isNewLined Whether or not to separate elements with a tab or by a newline
	 * @return An index-numbered list of the string contents of str
	 */
	public static String arrayToString(String[] str, boolean isNewLined) {
		final StringBuilder str2 = new StringBuilder();
		final int longestLen = getLongestElementLength(str);

		str2.ensureCapacity(longestLen * str.length);

		for (int i = 0; i < str.length - 1; i++) {
			String me = i + ": " + str[i] + (isNewLined ? "\n" : ((i == str.length - 1) ? "" : ", "));
			str2.append(me);
		}

		return str2.toString();
	}

	/**
	 * Converts a String array to the specified type of collection.
	 *
	 * <b>The specified type of collection must be a subclass of {@see java.util.AbstractList}.</b>
	 *
	 * @param str   The array of Strings to convert from
	 * @param class_ The collection's class to convert to
	 * @return A Collection specified by clazz, loaded with the elements of str
	 * @deprecated Use {@see java.util.Arrays#asList(Object[])}; it's shorter.
	 * @throws IllegalAccessException &nbsp;
	 * @throws InstantiationException &nbsp;
	 */
	public static List<String> arrayAsList(String[] str, Class<? extends List> class_)
			throws IllegalAccessException, InstantiationException {
		final List collection;
		return Collections.addAll((collection = class_.newInstance()), str) ? collection : null;
	}

	/**
	 * Returns the index of where the given substring ends, rather than where it starts
	 * as per String#indexOf
	 *
	 * @param str    String in question
	 * @param phrase substring to find the index of
	 * @return The index of where the given string ends rather than where it starts
	 * as per String#indexOf
	 */
	public static int indexOfLastChar(String str, String phrase) {
		int idx = str.indexOf(phrase);
		int len = phrase.length();

		return idx + len;
	}

	/**
	 * Decodes a url that has been recursively encoded.
	 *
	 * @param encoded String that is URLEncoded
	 * @return String that is completely URLDecoded
	 */
	public static String decodeCompletely(String encoded) {
		String uno = encoded;
		String dos = uno;

		do {
			uno = dos;
			try {
				dos = URLDecoder.decode(uno, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} while (!uno.equals(dos));

		return uno; //can return either one at this point
	}

	/**
	 * For generating relatively safe filenames
	 *
	 * @param s A filename that might be unsafe for an OS
	 * @return The same string with everything except letters, numbers, ".", and "-" replaced with an underscore ("_").
	 */
	public static String normalizeForOS(String s) {
		return s.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
	}

	public static boolean containsItemFromList(String inputString, String[] items) {
		for (int i = 0; i < items.length; i++) {
			if (inputString.contains(items[i])) {
				return true;
			}
		}
		return false;
	}

	public static void logStringArray(String[] u, org.slf4j.Logger log) {
		for (String _u : u)
			log.info("url: " + _u);
	}

	public static void printStringArray(String[] u, String label) {
		for (String _u : u)
			System.out.println(label + _u);
	}

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
