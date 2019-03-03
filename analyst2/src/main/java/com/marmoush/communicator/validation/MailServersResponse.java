package com.marmoush.communicator.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.marmoush.communicator.crawlfile.FilePath;
import com.marmoush.communicator.crawlfile.FileUtils;
import com.marmoush.communicator.model.MailServer;

public class MailServersResponse {
	public static void main(String[] args) {
		// try {
		List<MailServer> ms = getMailServers("yellowpages.com");
		Collections.sort(ms);
		System.out.println(ms);
		// } catch (NameNotFoundException e) {
		// FileUtils.logToFile("Error: Name Not found", FilePath.LOG);
		// } catch (NamingException e) {
		// FileUtils.logToFile(e.getMessage(), FilePath.LOG);
		// }
	}

	public static List<MailServer> getMailServers(String hostName) {
		List<MailServer> mailservers = new ArrayList<MailServer>();
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.factory.initial",
				"com.sun.jndi.dns.DnsContextFactory");
		try {
			DirContext ictx = new InitialDirContext(env);
			Attributes attrs = ictx.getAttributes(hostName,
					new String[] { "MX" });
			Attribute attr = attrs.get("MX");
			MailServer ms = null;
			for (int i = 0; i < attr.size(); i++) {
				ms = toMailServer(attr.get(i));
				if (ms != null)
					mailservers.add(ms);
			}
		} catch (NamingException e) {
			//TODO
		}
		return mailservers;
	}

	private static MailServer toMailServer(Object o) {
		String s = o.toString();
		String[] arr = s.split("\\s");
		int prio = Integer.parseInt(arr[0]);
		String url = arr[1].substring(0, arr[1].length() - 1);
		if (url.length() > 5)
			return new MailServer(prio, url);
		else
			return null;
	}
}