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
		throw new Exception("Use this class statically. No instantiation.");
	}

	/**
	 * Useful because it gets all the indices instead of the just the first or last one.
	 *
	 * @param str   The string to search
	 * @param token The token to find
	 * @return An {@link java.util.ArrayList} of all the indices found of token in str in order.
	 *         If token is not found in str, returns null.
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
	 * Obtains the n-th index of a given substring within a String, starting from the 0th index.
	 *
	 * @param source The String to search
	 * @param s The substring to find the n-th index of, regex can be used here
	 * @param n The particular instance of the substring that is found, >= 1
	 * @return The index of the beginning of the n-th occurrence of the substring in question; else -1, if there isn't
	 * even a single indexOf
	 */
	public static int nthIndexOf(String source, String s, int n){
		int idx = 0, instance = 0;

		if(source.isEmpty() || s.isEmpty() | !source.contains(s) || n < 1)
			return -1;

		Pattern p = Pattern.compile(s);
		Matcher m = p.matcher(source);

		while(m.find()){
			instance++;

			if(instance == n)
				idx = m.start();
		}

		return idx;
	}

	/**
	 * Obtains the amount of times a substring occurs in a given String
	 *
	 * @param source The String to traverse
	 * @param token The substring to look for, can be a regex
	 * @return The number of times token occurred in source. If both source and token are empty, returns -1.
	 */
	public static int getTokenCount(String source, String token){
		if(source.isEmpty() || token.isEmpty())
			return -1;

		int count = 0;
		Pattern p = Pattern.compile(token);
		Matcher m = p.matcher(source);

		while(m.find())
			count++;

		return count;
	}
}
