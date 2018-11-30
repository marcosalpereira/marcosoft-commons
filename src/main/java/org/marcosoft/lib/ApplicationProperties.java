 package org.marcosoft.lib;


 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.util.Map;
 import java.util.Properties;


 public class ApplicationProperties
 {
	 private final Properties properties = new Properties();
	 private final File fileProperties;
	 private final Map<String, Object> defaults = new java.util.HashMap();

	
	 public ApplicationProperties(String applicationName) {
		 String appHome = System.getProperty("user.home") + File.separator + "." + applicationName;
		 File appHomeFile = new File(appHome);
		 if (!appHomeFile.exists()) {
			 appHomeFile.mkdirs();
			 }
		
		 String fileName = appHome + File.separator + "application.properties";
		 this.fileProperties = new File(fileName);
		 try
		 {
			 if (this.fileProperties.exists()) {
				 this.properties.load(new FileInputStream(this.fileProperties));
				 } else {
				 this.properties.store(new FileOutputStream(this.fileProperties), null);
				 }
			 } catch (IOException e) {
			 throw new RuntimeException(e);
			 }
		 }

	
	 public void setDefault(String property, String value) {
		 this.defaults.put(property, value);
		 }

	
	 public String getProperty(String key) {
		 String property = this.properties.getProperty(key);
		 if (property != null)
			return property;
		 return (String) this.defaults.get(key);
		 }

	
	 public String getProperty(String key, String notFoundValue) {
		 String property = getProperty(key);
		 if (property != null)
			return property;
		 return notFoundValue;
		 }

	
	 public int getIntProperty(String key) {
		 return Integer.parseInt(getProperty(key));
		 }

	
	 public void setProperty(String key, String value) {
		 this.properties.setProperty(key, value);
		 try {
			 this.properties.store(new FileOutputStream(this.fileProperties), null);
			 } catch (IOException e) {
			 throw new RuntimeException(e);
			 }
		 }
	 }

/*
 * Location: /home/54706424372/bin/java/alarm-0.11.jar!/org/marcosoft/lib/
 * ApplicationProperties.class Java compiler version: 5 (49.0) JD-Core Version:
 * 0.7.1
 */