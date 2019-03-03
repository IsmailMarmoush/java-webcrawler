package com.marmoush.communicator.validation.smtp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.marmoush.communicator.crawlfile.FilePath;
import com.marmoush.communicator.crawlfile.FileUtils;

public class SMTPV2 {

	public static boolean isAddressValid(String address) {
		// Find the separator for the domain name
		int pos = address.indexOf('@');

		// If the address does not contain an '@', it's not valid
		if (pos == -1)
			return false;

		// Isolate the domain/machine name and get a list of mail exchangers
		String domain = address.substring(++pos);
		ArrayList<String> mxList = null;
		try {
			mxList = getMX(domain);
		} catch (NamingException ex) {
			return false;
		}

		// Just because we can send mail to the domain, doesn't mean that the
		// address is valid, but if we can't, it's a sure sign that it isn't
		if (mxList.size() == 0)
			return false;
		else
			FileUtils.logToFile("Domain: " + domain + "has " + mxList.size()
					+ " Mail servers", FilePath.LOG);

		// Now, do the SMTPV1 validation, try each mail exchanger until we get
		// a positive acceptance. It *MAY* be possible for one MX to allow
		// a message [store and forwarder for example] and another [like
		// the actual mail server] to reject it. This is why we REALLY ought
		// to take the preference into account.
		for (int mx = 0; mx < mxList.size(); mx++) {
			String theMX = mxList.get(mx);
			boolean valid = false;
			try {
				FileUtils.logToFile("Processing MX:" + theMX, FilePath.LOG);

				int res;
				Socket skt = new Socket(mxList.get(mx), 25);
				BufferedReader rdr = new BufferedReader(new InputStreamReader(
						skt.getInputStream()));
				BufferedWriter wtr = new BufferedWriter(new OutputStreamWriter(
						skt.getOutputStream()));

				res = hear(rdr);
				if (res != 220)
					throw new Exception("Invalid header");
				say(wtr, "EHLO rgagnon.com");

				res = hear(rdr);
				if (res != 250)
					throw new Exception("Not ESMTP");

				// validate the sender address
				say(wtr, "MAIL FROM: <sam@orbaker.com>");
				res = hear(rdr);
				if (res != 250)
					throw new Exception("Sender rejected");

				say(wtr, "RCPT TO: <" + address + ">");
				res = hear(rdr);

				// be polite
				say(wtr, "RSET");
				hear(rdr);
				say(wtr, "QUIT");
				hear(rdr);
				if (res != 250)
					throw new Exception("Address is not valid!");

				valid = true;
				rdr.close();
				wtr.close();
				skt.close();
			} catch (Exception ex) {
				// Do nothing but try next host
				// ex.printStackTrace();
				FileUtils.logToFile(ex.getMessage(), FilePath.LOG);
			} finally {
				if (valid)
					return true;
			}
		}
		return false;
	}

	public static void main(String args[]) {
		String testData[] = { "contact@marmoush.com",
				"a.abouelenie@yellowpages.com.eg", "contact222@marmoush.com"

		// "fail.me@nowhere.spam", // Invalid domain name
		// "arkham@bigmeanogre.net", // Invalid address
		// "nosuchaddress@yahoo.com" // Failure of this method
		// "marmoushismail@gmail.com"
		};

		for (int ctr = 0; ctr < testData.length; ctr++) {
			System.out.println(testData[ctr] + " is valid? "
					+ isAddressValid(testData[ctr]));
		}
		return;
	}

	private static ArrayList<String> getMX(String hostName)
			throws NamingException {
		// Perform a DNS lookup for MX records in the domain
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.factory.initial",
				"com.sun.jndi.dns.DnsContextFactory");
		DirContext ictx = new InitialDirContext(env);
		Attributes attrs = ictx.getAttributes(hostName, new String[] { "MX" });
		Attribute attr = attrs.get("MX");

		// if we don't have an MX record, try the machine itself
		if ((attr == null) || (attr.size() == 0)) {
			attrs = ictx.getAttributes(hostName, new String[] { "A" });
			attr = attrs.get("A");
			if (attr == null)
				throw new NamingException("No match for name '" + hostName
						+ "'");
		}

		// Huzzah! we have machines to try. Return them as an array list
		// NOTE: We SHOULD take the preference into account to be absolutely
		// correct. This is left as an exercise for anyone who cares.
		ArrayList<String> res = new ArrayList<String>();
		NamingEnumeration<?> en = attr.getAll();

		while (en.hasMore()) {
			String mailhost;
			String x = (String) en.next();
			String f[] = x.split(" ");
			// THE fix *************
			if (f.length == 1)
				mailhost = f[0];
			else if (f[1].endsWith("."))
				mailhost = f[1].substring(0, (f[1].length() - 1));
			else
				mailhost = f[1];
			// THE fix *************
			res.add(mailhost);
		}
		return res;
	}

	private static int hear(BufferedReader in) throws IOException {
		String line = null;
		int res = 0;

		while ((line = in.readLine()) != null) {
			String pfx = line.substring(0, 3);
			try {
				res = Integer.parseInt(pfx);
			} catch (Exception ex) {
				res = -1;
			}
			if (line.charAt(3) != '-')
				break;
		}

		return res;
	}

	private static void say(BufferedWriter wr, String text) throws IOException {
		wr.write(text + "\r\n");
		wr.flush();

		return;
	}
}