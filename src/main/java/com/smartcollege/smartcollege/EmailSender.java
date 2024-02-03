package com.smartcollege.smartcollege;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender {
    private static final String HOST = "smtp.gmail.com";
    private static final String USERNAME = "smartcollege@gmail.com";
    private static final String PASSWORD = "sthajdthjhsyhivf"; // This is secured!

    private static Session session;

    // Initialize the session once
    static {
        initialize();
    }

    private static void initialize() {
        if (session == null) {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", HOST);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            session.setDebug(true);
        }
    }

    public static void sendEmail(Address[] toAddresses, String subject, String text) {
        sendEmail(toAddresses, subject, text, null);
    }

    public static void sendEmail(Address[] toAddresses, String subject, String text, String imagePath) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, toAddresses);
            message.setSubject(subject);

            if (imagePath == null) {
                message.setText(text);
            } else {
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(text);

                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(new File(imagePath));
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                attachmentBodyPart.setFileName(source.getName());

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                multipart.addBodyPart(attachmentBodyPart);

                message.setContent(multipart);
            }

            System.out.println("Sending...");
            Transport.send(message);
            System.out.println("Sent message successfully.");
        } catch (MessagingException mex) {
            System.err.println("Email sending failed.");
            mex.printStackTrace();
        }
    }
}
