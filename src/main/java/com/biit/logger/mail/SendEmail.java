package com.biit.logger.mail;

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
import com.biit.logger.configuration.EmailConfigurationReader;
import com.biit.logger.mail.SendEmailThread.ThreadExceptionListener;
import com.biit.logger.mail.exceptions.EmailNotSentException;
import com.biit.logger.mail.exceptions.InvalidEmailAddressException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.List;

public final class SendEmail {

    private SendEmail() {
    }

    public static void sendEmail(List<String> mailToList, String subject, String htmlContent)
            throws EmailNotSentException, InvalidEmailAddressException {
        for (String mailTo : mailToList) {
            sendEmail(mailTo, subject, htmlContent);
            BiitLogger.info(SendEmail.class.getName(), "Sending email to " + mailTo);
        }
    }

    public static void sendEmail(String mailTo, String subject, String htmlContent) throws EmailNotSentException,
            InvalidEmailAddressException {
        sendEmail(EmailConfigurationReader.getInstance().getSmtpServer(), EmailConfigurationReader.getInstance()
                .getEmailUser(), EmailConfigurationReader.getInstance().getEmailPassword(), EmailConfigurationReader
                .getInstance().getEmailSender(), mailTo, subject, htmlContent);
    }

    /**
     * Sends Mail
     *
     * @param smtpServer
     * @param emailUser
     * @param emailPassword
     * @param emailSender
     * @param mailTo
     * @param subject
     * @param htmlContent
     * @throws EmailNotSentException
     * @throws InvalidEmailAddressException
     */
    public static void sendEmail(String smtpServer, String emailUser, String emailPassword, String emailSender,
                                 String mailTo, String subject, String htmlContent) throws EmailNotSentException,
            InvalidEmailAddressException {
        if (!isValidEmailAddress(emailSender)) {
            throw new InvalidEmailAddressException("Address email '" + emailSender + "' is invalid");
        }

        try {
            SendEmailThread sendEmailThread = new SendEmailThread();
            sendEmailThread.addExceptionListener(new ThreadExceptionListener() {

                @Override
                public void exceptionLaunched(MessagingException e) {
                    BiitLogger.severe(SendEmail.class.getName(), BiitLogger.getStackTrace(e));
                    // Catch exception, convert to Runtime to take out from the thread and catch and convert again to
                    // exception.
                    throw new RuntimeException(e);
                }
            });

            sendEmailThread.setSmtpServer(smtpServer);
            sendEmailThread.setEmailUser(emailUser);
            sendEmailThread.setEmailPassword(emailPassword);
            sendEmailThread.setEmailSender(emailSender);
            sendEmailThread.setMailTo(mailTo);
            sendEmailThread.setSubject(subject);
            sendEmailThread.setHtmlContent(htmlContent);

            sendEmailThread.run();
        } catch (Throwable exc) {
            BiitLogger.errorMessage(SendEmail.class.getName(), exc);
            EmailNotSentException emailNotSentException = new EmailNotSentException(exc.getMessage());
            emailNotSentException.setStackTrace(exc.getStackTrace());
            throw emailNotSentException;
        }
    }

    /**
     * This method is not too strong, some invalid emails will pass this test. Only for very basic validation of email
     *
     * @param email
     * @return
     */
    private static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
