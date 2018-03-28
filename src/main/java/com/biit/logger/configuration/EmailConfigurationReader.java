package com.biit.logger.configuration;

import java.util.ArrayList;
import java.util.List;

import com.biit.logger.BiitLogger;
import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.PropertiesSourceFile;
import com.biit.utils.configuration.SystemVariablePropertiesSourceFile;
import com.biit.utils.configuration.exceptions.PropertyNotFoundException;

public class EmailConfigurationReader extends ConfigurationReader {

	private static final String DATABASE_CONFIG_FILE = "settings.conf";
	private static final String BIIT_LOG_SYSTEM_VARIABLE_CONFIG = "BIIT_LOGGER_CONFIG";

	private final String ID_EMAIL_ENABLED = "mail.enabled";
	private final String ID_EMAIL_TO = "mail.to";
	private final String ID_EMAIL_SMTP_SERVER_TAG = "mail.smtpserver";
	private final String ID_EMAIL_USERNAME = "mail.username";
	private final String ID_EMAIL_PASSWORD = "mail.password";
	private final String ID_EMAIL_SENDER = "mail.sender";

	private final String DEFAULT_EMAIL_ENABLED = "false";
	private final String DEFAULT_EMAIL_TO = null;
	private final String DEFAULT_EMAIL_SMTP_SERVER = "smtp.mail.com";
	private final String DEFAULT_EMAIL_USERNAME = "noreply@email.com";
	private final String DEFAULT_EMAIL_PASSWORD = "password";
	private final String DEFAULT_EMAIL_SENDER = "BiiT Sourcing Solutions";

	private static EmailConfigurationReader instance;

	private EmailConfigurationReader() {
		super();

		addProperty(ID_EMAIL_ENABLED, DEFAULT_EMAIL_ENABLED);
		addProperty(ID_EMAIL_TO, DEFAULT_EMAIL_TO);
		addProperty(ID_EMAIL_SMTP_SERVER_TAG, DEFAULT_EMAIL_SMTP_SERVER);
		addProperty(ID_EMAIL_USERNAME, DEFAULT_EMAIL_USERNAME);
		addProperty(ID_EMAIL_PASSWORD, DEFAULT_EMAIL_PASSWORD);
		addProperty(ID_EMAIL_SENDER, DEFAULT_EMAIL_SENDER);

		addPropertiesSource(new PropertiesSourceFile(DATABASE_CONFIG_FILE));
		addPropertiesSource(new SystemVariablePropertiesSourceFile(BIIT_LOG_SYSTEM_VARIABLE_CONFIG,
				DATABASE_CONFIG_FILE));

		readConfigurations();
	}

	public static EmailConfigurationReader getInstance() {
		if (instance == null) {
			synchronized (EmailConfigurationReader.class) {
				if (instance == null) {
					instance = new EmailConfigurationReader();
				}
			}
		}
		return instance;
	}

	private String getPropertyLogException(String propertyId) {
		try {
			return getProperty(propertyId);
		} catch (PropertyNotFoundException e) {
			BiitLogger.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}

	public synchronized String getSmtpServer() {
		return getPropertyLogException(ID_EMAIL_SMTP_SERVER_TAG);
	}

	public synchronized String getEmailUser() {
		return getPropertyLogException(ID_EMAIL_USERNAME);
	}

	public synchronized String getEmailPassword() {
		return getPropertyLogException(ID_EMAIL_PASSWORD);
	}

	public synchronized String getEmailSender() {
		return getPropertyLogException(ID_EMAIL_SENDER);
	}

	public boolean isEmailEnabled() {
		return Boolean.parseBoolean(getPropertyLogException(ID_EMAIL_ENABLED));
	}

	public List<String> getEmailToList() {
		String emailToListCommaSeparated = getPropertyLogException(ID_EMAIL_TO);
		List<String> emailToList = new ArrayList<>();
		if (emailToListCommaSeparated != null && emailToListCommaSeparated.length() > 0) {
			String[] users = emailToListCommaSeparated.split(",");
			for (String user : users) {
				emailToList.add(user.trim());
			}
		}
		return emailToList;
	}

}
