package net.samism.java.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 7/18/14
 * Time: 9:32 PM
 */
public class StringUtils {

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
}
