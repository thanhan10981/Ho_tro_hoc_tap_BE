package com.hoctap.learningsupportapi.utils.summary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubTitleExtractor {

    private static final Pattern H3 =
            Pattern.compile("^###\\s*\\*{0,2}(.+?)\\*{0,2}$", Pattern.MULTILINE);

    private static final Pattern BOLD =
            Pattern.compile("^\\*\\*(.+?)\\*\\*$", Pattern.MULTILINE);

    public static String extract(String content) {
        if (content == null) return null;

        Matcher h3 = H3.matcher(content);
        if (h3.find()) return h3.group(1).trim();

        Matcher bold = BOLD.matcher(content);
        if (bold.find()) return bold.group(1).trim();

        return null;
    }
}
