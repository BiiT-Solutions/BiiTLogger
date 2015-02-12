package com.biit.logger.mail;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Functions to generate error report mail
 * 
 */
public class ErrorMailGeneration {
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static String localhostName;

	public static String getSubject() {
		return "Error in '" + getLocalHostName() + "'.";
	}

	public static String getHtmlMailContent(String className, Throwable error) {
		String htmlMailContent = new String();
		htmlMailContent += generateHtmlMailTitle();
		htmlMailContent += generateMailBody(className, error);

		return htmlMailContent;
	}

	private static String generateHtmlMailTitle() {
		return "<h1>Error in '" + getLocalHostName() + "' at '" + getDate() + "'.</h1>";
	}

	private static String generateMailBody(String className, Throwable throwable) {
		String body = "<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%;\"><tr><td><strong>Class: '"
				+ className + "'</strong></td></tr>";
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		body += "<tr><td>"
				+ writer.toString().replaceAll("(\r\n|\n)", "<br />").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;")
				+ "</td></tr>";
		body += "</table>";
		return body;
	}

	private static String getLocalHostName() {
		if (localhostName == null)
			try {
				localhostName = java.net.InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				localhostName = "unknown";
			}
		return localhostName;
	}

	private static String getDate() {
		Date date = new Date();
		return dateFormat.format(date);
	}
}
