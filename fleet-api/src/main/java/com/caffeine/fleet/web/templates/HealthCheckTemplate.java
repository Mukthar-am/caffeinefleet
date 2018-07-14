package com.caffeine.fleet.web.templates;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple health check html page enhancer
 */
public class HealthCheckTemplate {


    public static String getHealthTemplate() {
        StringBuilder healthCheckHtml = new StringBuilder();
        healthCheckHtml.append("<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <title>Wallfeed health.</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "<body bgcolor=\"#E6E6FA\">\n" +
                "    <h4>Wallfeed is healthier as every before to serve you on data picking.</h4>\n" +
                "  </body>\n" +
                "</html>");

        return healthCheckHtml.toString();
    }


    public static void main(String[] args) {
        String dateStr = "2017-11-05T01:38:03+05:30";
        String dateRegex = "(\\d\\d\\d\\d-(0[1-9]|1[012])-(0[1-9]|1[0-9]|2[0-9]|3[01])T(\\d\\d:\\d\\d:\\d\\d)\\+05:30)";


        Pattern pattern = Pattern.compile(dateRegex);
        Matcher matcher = pattern.matcher(dateStr);

        if (matcher.find()) {
            System.out.println(matcher.group(1));
        }

    }

    public static boolean findRegexPattern(String sourceString, String textToSearch) {
        boolean found = false;

        Pattern word = Pattern.compile(textToSearch);
        Matcher match = word.matcher(sourceString);

        System.out.println(match.group());

        while (match.find()) {
            found = true;
        }

        return found;
    }
}
