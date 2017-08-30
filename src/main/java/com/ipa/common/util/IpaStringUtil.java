package com.ipa.common.util;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;

public class IpaStringUtil {
	
	public static final String htmlTagsPattern = "<[^>]+>";
	
	public static String stripHtml(String source, boolean removeLineBreak){
		OutputSettings settings = new OutputSettings();
		settings.prettyPrint(false);
		String result = source.replaceAll("<div", "@NL@<div");
		result = result.replaceAll("<p", "@NL@<p");
		Whitelist whiteList = Whitelist.simpleText();
		result = Jsoup.clean(result, "", whiteList, settings);
		if (removeLineBreak) {
			result = result.replaceAll("(\\r\\n)[\\s^[\\r\\n]]+((\\r\\n)+)", "\r\n");
			result = result.replaceAll("(\\n)[\\s^[\\n]]*((\\n)+)", "\n");
			result = result.replaceAll("&nbsp;", " ");
			result = result.replaceAll("(@NL@)+", "\n");
			result = result.replaceAll("&gt;", ">");
			result = result.replaceAll("\\n(\\s+)", "\n");
		}else {
			result = result.replaceAll("(@NL@)+", "\n");
		}
		return result;
		//return source.replaceAll(htmlTagsPattern, "");
	}
	
	public static void main(String[] args) throws Exception{
		OutputSettings settings = new OutputSettings();
		String text = new String(Files.readAllBytes(Paths.get("c:/temp/htmlTest2.txt")),"UTF-8");
		text = stripHtml(text, true);
		System.out.println(text);
	}

}
