package com.test;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test3 {
	public static void main(String[] args) {

		String str = "199.72.81.55 - - [01/Jul/1995:00:00:01 -0400] \"GET /history/apollo/ HTTP/1.0\" 200 6245";
		Pattern p = Pattern.compile("(?:GET|POST)\\s([^\\s]+)");

		String[] entries = str.toString().split("\r?\n");

		for (String s : entries) {

			Matcher matcher = p.matcher(s);
			if (matcher.find()) {
				System.out.println(matcher.group(1));
			}
		}
		// String value = "i am a boy";
		// StringTokenizer itr = new StringTokenizer(value.toString());
		// while (itr.hasMoreTokens()) {
		// System.out.println(itr.nextToken());
		//
		// }

	}
}
