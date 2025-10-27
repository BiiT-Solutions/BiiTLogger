package com.biit.logger.configuration;

/*-
 * #%L
 * BiiT Logger
 * %%
 * Copyright (C) 2015 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.logger.BiitLogger;
import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.PropertiesSourceFile;
import com.biit.utils.configuration.SystemVariablePropertiesSourceFile;
import com.biit.utils.configuration.exceptions.PropertyNotFoundException;

import java.util.ArrayList;
import java.util.List;

public final class EmailConfigurationReader extends ConfigurationReader {

    private static final String DATABASE_CONFIG_FILE = "settings.conf";
    private static final String BIIT_LOG_SYSTEM_VARIABLE_CONFIG = "BIIT_LOGGER_CONFIG";

    private static final String ID_EMAIL_ENABLED = "mail.enabled";
    private static final String ID_EMAIL_TO = "mail.to";
    private static final String ID_EMAIL_SMTP_SERVER_TAG = "mail.smtpserver";
    private static final String ID_EMAIL_USERNAME = "mail.username";
    private static final String ID_EMAIL_PASSWORD = "mail.password";
    private static final String ID_EMAIL_SENDER = "mail.sender";

    private static final String DEFAULT_EMAIL_ENABLED = "false";
    private static final String DEFAULT_EMAIL_TO = null;
    private static final String DEFAULT_EMAIL_SMTP_SERVER = "smtp.mail.com";
    private static final String DEFAULT_EMAIL_USERNAME = "noreply@email.com";
    private static final String DEFAULT_EMAIL_PASSWORD = "password";
    private static final String DEFAULT_EMAIL_SENDER = "BiiT Sourcing Solutions";

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
