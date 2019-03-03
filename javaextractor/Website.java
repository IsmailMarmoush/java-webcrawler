/*
 * Decompiled with CFR 0_115.
 */
package com.marmoush.jobs.website.extractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Website {
    private String website;

    public static boolean isWebsite(String email) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        String inputStr = email;
        Pattern pattern = Pattern.compile(regex, 2);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static boolean isWebsite2(String website) {
        String regex = "[www]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String inputStr = website;
        Pattern pattern = Pattern.compile(regex, 2);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public Website() {
    }

    public Website(String website) {
        this.website = website;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Website other = (Website)obj;
        if (this.website == null ? other.website != null : !this.website.equals(other.website)) {
            return false;
        }
        return true;
    }

    public String getWebsite() {
        return this.website;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.website == null ? 0 : this.website.hashCode());
        return result;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String toString() {
        return "Website [website=" + this.website + "]";
    }
}
