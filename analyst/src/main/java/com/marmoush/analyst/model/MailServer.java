package com.marmoush.analyst.model;


public class MailServer implements Comparable<MailServer> {
	public static String PRIORITY_KEY = "priority";
	private int priority;
	private String url;

	public MailServer() {
	}

	public MailServer(int priority, String url) {
		super();
		this.priority = priority;
		this.url = url;
	}

	@Override
	public int compareTo(MailServer o) {
		return Integer.compare(priority, o.getPriority());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MailServer other = (MailServer) obj;
		if (priority != other.priority)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	public int getPriority() {
		return priority;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + priority;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return priority + " " + url;
	}
}
