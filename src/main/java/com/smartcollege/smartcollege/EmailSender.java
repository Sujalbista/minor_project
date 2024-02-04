package com.smartcollege.smartcollege;

import Encryption.Encryption;

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
    private static final String USERNAME = "smartcollege004@gmail.com";
    private static final String PASSWORD = "hwfv bhqk uife xffr"; // This is secured!

    private static Session session;
    // Initialize the session once
    static {
        initialize();
    }

    public static void initialize() {
        if (session == null) {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", HOST);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    try {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            session.setDebug(true);
        }
    }

    public static String sendEmail(Address[] toAddresses, String subject, String text) {
        String feedback;
        if(session == null){
            initialize();
            sendEmail(toAddresses,subject,text,null);
            feedback ="Done";
        }else{
            sendEmail(toAddresses,subject,text,null);
            feedback ="Done";
        }
        return  feedback;
    }

    private  static class SendEmailThread implements Runnable{
        Address[] toAddresses;
        String subject;
        String text;
        String imagePath;

        SendEmailThread(Address[] toAddresses, String subject, String text, String imagePath){
            this.toAddresses = toAddresses;
            this.imagePath = imagePath;
            this.subject = subject;
            this.text = text;
        }
        public void run(){
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
    public static void sendMarksheet(Address[] toAddresses, String subject, String htmlContent) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, toAddresses);
            message.setSubject(subject);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlContent, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            System.out.println("Sending...");
            Transport.send(message);
            System.out.println("Sent message successfully.");
        } catch (MessagingException mex) {
            System.err.println("Email sending failed.");
            mex.printStackTrace();
        }
    }

}
