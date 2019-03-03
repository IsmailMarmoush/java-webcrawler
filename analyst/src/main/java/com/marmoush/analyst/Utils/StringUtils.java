package com.marmoush.analyst.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import com.marmoush.analyst.model.MailServer;

public class StringUtils {
	public static String emailToDomain(String email) {
		return email.split("@")[1];
	}

	public static List<String> emailToDomainList(Collection<String> emails) {
		List<String> domains = new ArrayList<String>(emails.size());
		for (String email : emails) {
			domains.add(emailToDomain(email));
		}
		return domains;
	}

	public static TreeSet<String> emailToDomainsTree(Collection<String> emails) {
		TreeSet<String> domains = new TreeSet<String>();
		for (String email : emails) {
			domains.add(emailToDomain(email));
		}
		return domains;
	}

	public static String emailToLink(String email) {
		return "http://" + email.split("@")[1];
	}

	public static URL emailToURL(String email) throws MalformedURLException {
		return new URL("http://" + email.split("@")[1]);
	}

	public static List<List<String>> frequencyPair(Collection<String> collection) {
		List<Integer> freq = frequencyRdn(collection);
		TreeSet<String> tree = new TreeSet<String>();
		int i = 0;
		for (String str : collection) {
			tree.add(str + "#" + freq.get(i));
			i++;
		}
		List<List<String>> result = new ArrayList<List<String>>();
		for (String str : tree) {
			String[] arr = str.split("#");
			result.add(Arrays.asList(arr[1], arr[0]));
		}
		return result;
	}

	public static String[][] frequencyPairRdn(Collection<String> collection) {
		List<Integer> freq = frequencyRdn(collection);
		String[][] pair = new String[collection.size()][2];
		int i = 0;
		for (String str : collection) {
			pair[i][0] = freq.get(i) + "";
			pair[i][1] = str;
			i++;
		}
		return pair;
	}

	public static List<Integer> frequencyRdn(Collection<String> collection) {
		List<Integer> sum = new ArrayList<Integer>(collection.size());
		for (String str : collection) {
			int occurrences = Collections.frequency(collection, str);
			sum.add(occurrences);
		}
		return sum;
	}

	public static String linkToDomain(String link) throws MalformedURLException {
		URL url = null;
		try {
			url = new URL(link);
		} catch (MalformedURLException e) {
			throw new MalformedURLException("The link is" + link);
		}
		return url.getHost();
	}

	public static List<String> linkToDomainList(Collection<String> links)
			throws MalformedURLException {
		List<String> domains = new ArrayList<String>();
		for (String link : links) {
			domains.add(linkToDomain(link));
		}
		return domains;
	}

	public static TreeSet<String> linkToDomainTree(Collection<String> links)
			throws MalformedURLException {
		TreeSet<String> domains = new TreeSet<String>();
		for (String link : links) {
			domains.add(linkToDomain(link));
		}
		return domains;
	}

	public static void println(Collection<? extends Object> lines,
			String suffix, String prefix) {
		System.out.println(toString(lines, suffix, prefix));
	}

	public static TreeSet<String> toHtmlLink(Collection<String> lines) {
		TreeSet<String> result = new TreeSet<String>();
		for (String line : lines) {
			result.add(toHtmlLink(line));
		}
		return result;
	}

	public static String toHtmlLink(String line) {
		return "<a href=\"" + line + "\" target=\"_blank\"> " + line + "</a>";
	}

	public static List<String> toList(Collection<String> lines) {
		List<String> list = new ArrayList<String>(lines.size());
		for (String line : lines) {
			list.add(line);
		}
		return list;
	}

	public static TreeSet<String> toLower(Collection<String> lines) {
		TreeSet<String> result = new TreeSet<String>();
		for (String line : lines) {
			result.add(line.toLowerCase());
		}
		return result;
	}

	public static String toString(Collection<? extends Object> collection,
			String prefix, String suffix) {
		StringBuilder sb = new StringBuilder();
		for (Object line : collection) {
			sb.append(prefix);
			sb.append(line.toString());
			sb.append(suffix);
		}
		return sb.toString();
	}

	public static List<String> mailServersToURL(Collection<MailServer> mailservers) {
		List<String> ms = new ArrayList<String>(mailservers.size());
		for (MailServer mailServer : mailservers) {
			ms.add(mailServer.getUrl());
		}
		return ms;
	}

	public static List<Integer> mailServersToPriority(
			Collection<MailServer> mailservers) {
		List<Integer> ms = new ArrayList<Integer>(mailservers.size());
		for (MailServer mailServer : mailservers) {
			ms.add(mailServer.getPriority());
		}
		return ms;
	}

	public static String toString(List<List<String>> collection,
			String betweenElements, String betweenRows) {
		StringBuilder sb = new StringBuilder();
		for (List<String> row : collection) {
			sb.append(row.get(0));
			sb.append(betweenElements);
			sb.append(row.get(1));
			sb.append(betweenRows);
		}
		return sb.toString();
	}

	public static TreeSet<String> toTreeSet(Collection<String> lines) {
		TreeSet<String> tree = new TreeSet<String>();
		for (String em : lines) {
			tree.add(em);
		}
		return tree;
	}

	public static String websiteToDomainName(String website)
			throws MalformedURLException {
		String result = new URL(website).getHost().trim().toLowerCase();
		if (result.startsWith("www.")) {
			return result.substring(4);
		}
		return result;
	}
}
