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

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SendEmailThread implements Runnable {
    private String smtpServer;
    private String emailUser;
    private String mailTo;
    private String emailPassword;
    private String emailSender;
    private String subject;
    private String htmlContent;

    private Set<ThreadExceptionListener> exceptionListeners;

    interface ThreadExceptionListener {
        void exceptionLaunched(MessagingException e);
    }

    public SendEmailThread() {
        exceptionListeners = new HashSet<>();
    }

    public void addExceptionListener(ThreadExceptionListener listener) {
        exceptionListeners.add(listener);
    }

    public void run() {
        List<String> to = Arrays.asList(new String[]{mailTo});
        Postman postman = new Postman(smtpServer, emailUser, emailPassword);
        try {
            postman.setSubject(subject);
            postman.addHtml(htmlContent);
            // Avoiding javax.activation.UnsupportedDataTypeException: no object DCH for MIME type multipart/mixed;
            Thread.currentThread().setContextClassLoader(SendEmail.class.getClassLoader());
            postman.sendMail(to, null, null, emailSender);
        } catch (MessagingException e) {
            // throw new EmailNotSentException(e.getMessage());
            for (ThreadExceptionListener listener : exceptionListeners) {
                listener.exceptionLaunched(e);
            }
        }
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public void setEmailSender(String emailSender) {
        this.emailSender = emailSender;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public void setExceptionListeners(Set<ThreadExceptionListener> exceptionListeners) {
        this.exceptionListeners = exceptionListeners;
    }

}
