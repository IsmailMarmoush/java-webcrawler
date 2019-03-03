package com.marmoush.communicator.crawlfile;

import static com.marmoush.communicator.Utils.StringUtils.println;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;

public class CrawlFileTest {
	private CrawlFile cf = new CrawlFile();

	@Before
	public void setUp() throws MalformedURLException {
		cf.readAndFilter(FilePath.RAWDATA_TEST);
	}

	@Test
	public void testGetDomains() {
		System.out.println(cf.getDomains().size());
		println(cf.getDomains(), "", "\n");
	}

	@Test
	public void testGetEmails() {
		System.out.println(cf.getEmails().size());
		println(cf.getEmails(), "", "\n");
	}

}
