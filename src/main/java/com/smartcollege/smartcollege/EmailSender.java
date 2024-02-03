package com.smartcollege.smartcollege;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

public class EmailSender {
    public void sendEmail(String to, String subject, String text, String imagePath) {
        boolean flag = false;

        // Assuming you are sending email from through Gmail's SMTP
        String host = "smtp.gmail.com";
        String username = "smartcollege@gmail.com";
        String password = "sthajdthjhsyhivf"; // This should be secured!

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(text);

            // Create the attachment part
            BodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(new File(imagePath));
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(source.getName());

            // Create a multipart message and add the parts to it
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            // Set the complete message parts
            message.setContent(multipart);

            System.out.println("Sending...");
            Transport.send(message);
            System.out.println("Sent message with attachment successfully.");

            flag = true;
        } catch (MessagingException mex) {
            System.err.println("Email sending failed.");
            mex.printStackTrace();
        }

    }
}
