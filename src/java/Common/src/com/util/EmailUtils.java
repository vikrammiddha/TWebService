/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Common.src.com.util;

import Common.src.com.Config.AppConfig;
import java.io.File;
import java.io.PrintStream;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import org.apache.log4j.Logger;
/**
 *
 * @author Vikram
 */
public class EmailUtils {
    private static Logger LOGGER = Logger.getLogger(EmailUtils.class);

    public EmailUtils()
    {
    }

    public static boolean sendEMail(final AppConfig appConfig)
    {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", appConfig.getSmtphostname());
        props.put("mail.smtp.port", appConfig.getSmtpport());
        Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(appConfig.getUserName(), appConfig.getPassword());
			}
		  });
        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(appConfig.getFromAlias()));
            message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(appConfig.getToAddressList()));
            message.setSubject(appConfig.getMailSubject());
            message.setText(appConfig.getMailBody());
            if(appConfig.getAttachmentPath() != null && appConfig.getAttachmentPath().length() > 0)
            {
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText("Invoice");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                messageBodyPart = new MimeBodyPart();
                String filename = appConfig.getAttachmentPath();
                javax.activation.DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                File file = new File(filename);
                messageBodyPart.setFileName(file.getName());
                multipart.addBodyPart(messageBodyPart);
                message.setContent(multipart);
            } else
            {
                message.setContent(appConfig.getMailBody(), "text/html");
            }
            Transport.send(message);
            System.out.println("Done");
        }
        catch(MessagingException e)
        {
            throw new RuntimeException(e);            
        }
        LOGGER.debug("Your email message was sent successfully..");
        return true;     
        
    }

    public static boolean sendEMail(AppConfig appConfig, String toAddress, String subject, String body)
    {
        LOGGER.info((new StringBuilder()).append("Sending email.....: ").append(subject).toString());
        EmailUtils email = new EmailUtils();
        appConfig.setToAddressList(toAddress);
        appConfig.setMailSubject(subject);
        appConfig.setMailBody(body);
        EmailUtils _tmp = email;
        boolean sentmail = sendEMail(appConfig);
        if(!sentmail)
        {
            LOGGER.error("sending the email failed.........");
        }
        return sentmail;
    }
}
