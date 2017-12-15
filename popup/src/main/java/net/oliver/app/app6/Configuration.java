package net.oliver.app.app6;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

	public static String ticketHref;
	public static String companyname = "evolveit";// "training";//evolveit
	public static String public_key = "g9cxUUYwepM0Kf43";// "y6sR3iL1ora8fTOs";
	public static String private_key = "2Eo7xySgt8fSb85I";
	public static String rest_basepath = "2Eo7xySgt8fSb85I";// "moF7NvBATrfkBtXk";

	private static Properties prop = new Properties();
	static {

		try {
			prop.load(new FileInputStream(new File("./config/config.properties")));
			ticketHref = prop.getProperty("ticketHref");
			companyname = prop.getProperty("rest_companyname");
			public_key = prop.getProperty("rest_publickey");
			private_key = prop.getProperty("rest_privatekey");
			rest_basepath = prop.getProperty("rest_basepath");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String getValue(String key) {
		return prop.getProperty(key);
	}
}
