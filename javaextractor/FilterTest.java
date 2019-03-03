/*
 * Decompiled with CFR 0_115.
 */
package com.marmoush.jobs.email.extractor;

import com.marmoush.jobs.email.extractor.EmailFilter;
import java.util.List;
import java.util.TreeSet;

public class FilterTest {
    public static void main(String[] args) {
        List<String> srcEmails = EmailFilter.readFileToList("rowWebsites.txt");
        srcEmails = EmailFilter.removeNoise(srcEmails);
        srcEmails = EmailFilter.filterNonEmails(srcEmails);
        srcEmails = EmailFilter.filterFishyEmails(srcEmails);
        TreeSet<String> emailTree = EmailFilter.toTreeSet(srcEmails);
        EmailFilter.writeTreeToFile(emailTree, "EmailsFiltered2.txt");
    }
}
