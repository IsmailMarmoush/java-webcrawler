package com.marmoush.communicator.Utils;

import java.util.Collection;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterUtils {
	public static final String[] PERSONAL_EMAIL_KEYWORDS = new String[] {
			"hotmail", "gmail", "yahoo" };

	public static final String LINK_REGEX = "\\b(([\\w-]+://?|www[.])[^\\s()<>]+(?:\\([\\w\\d]+\\)|([^[:punct:]\\s]|/?)))";
	public static final String EMAIL_Regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static TreeSet<String> filter(Collection<String> lines, String regex) {
		TreeSet<String> result = new TreeSet<String>();
		for (String line : lines) {
			if (FilterUtils.isMatching(regex, line, Pattern.CASE_INSENSITIVE)) {
				// System.out.println("Line was \'" + line + "\'");
				result.add(line);
			}
		}
		return result;
	}

	public static TreeSet<String> getDoContainAny(Collection<String> lines,
			String... keywords) {
		TreeSet<String> result = new TreeSet<String>();
		for (String line : lines) {
			for (String word : keywords) {
				if (line.contains(word))
					result.add(line);
			}
		}
		return result;
	}

	public static boolean getDoContainAny(String line, String... keywords) {
		boolean result = false;
		for (String word : keywords) {
			if (line.contains(word))
				return true;
		}
		return result;
	}

	public static TreeSet<String> getDoNotContainAny(Collection<String> lines,
			String... keywords) {
		TreeSet<String> result = new TreeSet<String>();
		for (String line : lines) {
			if (getDoNotContainAny(line, keywords))
				result.add(line);
		}
		return result;
	}

	public static boolean getDoNotContainAny(String line, String... keywords) {
		boolean result = true;
		for (String word : keywords) {
			if (line.contains(word))
				result = false;
		}
		return result;
	}

	public static boolean isMatching(String regex, String input, int flag) {
		CharSequence inputStr = input;
		Pattern pattern = Pattern.compile(regex, flag);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher.matches();
	}

	public static TreeSet<String> removeNoise(Collection<String> lines) {
		TreeSet<String> result = new TreeSet<String>();
		for (String em : lines) {
			em = em.replaceAll("[\'\",;&\\s]+", "\n");
			String[] tokens = em.split("[\\r\\n]+");
			if (tokens.length > 0) {
				for (String emToken : tokens) {
					emToken = emToken.trim();
					result.add(emToken);
				}
			}
		}
		return result;
	}

	public static TreeSet<String> removeRedundant(Collection<String> lines) {
		return StringUtils.toTreeSet(lines);
	}
}