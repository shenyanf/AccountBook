package com.shanshan.myaccountbook.util.mail;

import com.shanshan.myaccountbook.database.MyDBHelper;
import com.shanshan.myaccountbook.util.MyLogger;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendMail {
    private static Logger myLogger = MyLogger.getMyLogger(SendMail.class.getName());

    public static Boolean send() {
        // Recipient's email ID needs to be mentioned.
        String to = "shenyan-f@163.com";

        // Sender's email ID needs to be mentioned
        String from = "shenyan-f@163.com";

        // Assuming you are sending email from localhost
        String host = "smtp.163.com";

        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);

        // Get session
        properties.put("mail.smtp.auth", "true"); // 这样才能通过验证
        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // sqlite database dir
        String dbDir = MyDBHelper.dbPath.substring(0, MyDBHelper.dbPath.lastIndexOf(File.separator));

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("shenyan-f@163.com", "ssshen14");
            }
        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());
            // Set Subject: header field
            message.setSubject("SQLite backup " + date);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText("Have a good day,buddy!");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            for (File filePath : listFile(dbDir)) {
                // Part two is attachment
                messageBodyPart = new MimeBodyPart();

                DataSource source = new FileDataSource(filePath.getAbsoluteFile());
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filePath.getAbsoluteFile().toString());
                multipart.addBodyPart(messageBodyPart);
            }
            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (Exception mex) {
            mex.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private static List<File> listFile(String path) {
        File file = new File(path);
        List<File> list = new ArrayList<File>();
        if (file.isDirectory() && file.canRead()) {
            list = Arrays.asList(file.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    File file = new File(dir, name);
                    return file.isFile() && file.canRead();
                }
            }));
        }
        for (File f : list) {
            myLogger.debug("find file:" + f.getAbsoluteFile());
        }
        return list;
    }
}