package com.marmoush.analyst.validation.smtp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.marmoush.analyst.Utils.StringUtils;

public class SMTPV3 {
	public static void main(String[] args) {
		List<Boolean> resp = validate( "contact@marmoush.com",
				"a.abouelenie@yellowpages.com.eg", "contact222@marmoush.com");
		StringUtils.println(resp, "\n", "");
	}

	public static List<Boolean> validate(String... emails) {
		List<Boolean> resp = new ArrayList<Boolean>();
		for (String email : emails) {
			String address = email;
			String domain = email.split("@")[1];
			resp.add(isValid(address, domain));
		}
		return resp;
	}

	private static boolean isValid(String address, String domain) {
		boolean result=false;
		Attribute attr;
		try {
			attr = getAttribute(domain);
			List<String> mxList = getMailExchangerList(attr);
			for (String mailExchanger : mxList) {
				if (isValidIn(address, mailExchanger))
					result= true;
			}
			return true;
		} catch (NamingException e) {
			e.printStackTrace();
			result=false;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			result=false;
		} catch (IOException e) {
			e.printStackTrace();
			result=false;
		}
		return result;
	}

	private static boolean communicate(String address, BufferedReader reader,
			BufferedWriter writer) throws IOException {
		boolean result = true;
		int response;
		response = listen(reader);
		if (response != 220)
			throw new IOException("Invalid header");

		say(writer, "EHLO rgagnon.com");
		response = listen(reader);
		if (response != 250)
			throw new IOException("Not ESMTP");

		// validate the sender address
		say(writer, "MAIL FROM: <sam@orbaker.com>");
		response = listen(reader);
		if (response != 250)
			throw new IOException("Sender rejected");

		say(writer, "RCPT TO: <" + address + ">");
		response = listen(reader);

		// be polite
		say(writer, "RSET");
		listen(reader);
		say(writer, "QUIT");
		listen(reader);
		if (response != 250)
			throw new IOException("Address is not valid!");

		return result;
	}

	private static Attribute getAttribute(String domain) throws NamingException {
		// Perform a DNS lookup for MX records in the domain
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.factory.initial",
				"com.sun.jndi.dns.DnsContextFactory");
		DirContext ictx = new InitialDirContext(env);
		Attributes attrs = ictx.getAttributes(domain, new String[] { "MX" });
		Attribute attr = attrs.get("MX");

		// if we don't have an MX record, try the machine itself
		if ((attr == null) || (attr.size() == 0)) {
			attrs = ictx.getAttributes(domain, new String[] { "A" });
			attr = attrs.get("A");
			if (attr == null)
				throw new NamingException("No match for name '" + domain + "'");
		}
		return attr;
	}

	private static List<String> getMailExchangerList(Attribute attr)
			throws NamingException {
		// Huzzah! we have machines to try. Return them as an array list
		// NOTE: We SHOULD take the preference into account to be absolutely
		// correct. This is left as an exercise for anyone who cares.
		List<String> mxList = new ArrayList<String>();
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
			mxList.add(mailhost);
		}
		return mxList;
	}

	/**
	 * Now, do the SMTPV1 validation, try each mail exchanger until we get a
	 * positive acceptance. It *MAY* be possible for one MX to allow a message
	 * [store and forwarder for example] and another [like the actual mail
	 * server] to reject it. This is why we REALLY ought to take the preference
	 * into account.
	 * 
	 * @param mailExchanger
	 * @param address
	 * @return
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	private static boolean isValidIn(String address, String mailExchanger)
			throws UnknownHostException, IOException {
		boolean result = false;
		Socket skt = null;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		skt = new Socket(mailExchanger, 25);
		System.out.println(skt.getSoTimeout());
		skt.setSoTimeout(500);
		reader = new BufferedReader(new InputStreamReader(skt.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(
				skt.getOutputStream()));
		result = communicate(address, reader, writer);
		reader.close();
		writer.close();
		skt.close();
		return result;
	}

	private static int listen(BufferedReader in) throws IOException {
		String line = null;
		int response = 0;
		while ((line = in.readLine()) != null) {
			String pfx = line.substring(0, 3);
			try {
				response = Integer.parseInt(pfx);
			} catch (Exception ex) {
				response = -1;
			}
			if (line.charAt(3) != '-')
				break;
		}
		return response;
	}

	private static void say(BufferedWriter wr, String text) throws IOException {
		wr.write(text + "\r\n");
		wr.flush();
		return;
	}
}