/*
 * Decompiled with CFR 0_115.
 */
package com.marmoush.jobs.email.extractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Email
implements Comparable<Email> {
    String email;

    public static boolean isEmail(String email) {
        String expression = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String inputStr = email;
        Pattern pattern = Pattern.compile(expression, 2);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static String toWebsite(String email) {
        return "http://www." + email.split("@")[1];
    }

    public Email() {
    }

    public Email(String email) {
        this.setEmail(email);
    }

    @Override
    public int compareTo(Email otherEmail) {
        return this.email.compareTo(otherEmail.getFullEmail());
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
        Email other = (Email)obj;
        if (this.email == null ? other.email != null : !this.email.equals(other.email)) {
            return false;
        }
        return true;
    }

    public String getCompanyName() {
        return this.email.split("@")[1].split("\\.")[0];
    }

    public String getFullEmail() {
        return this.email;
    }

    public String getName() {
        return this.email.split("@")[0];
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.email == null ? 0 : this.email.hashCode());
        return result;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
