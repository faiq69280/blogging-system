package com.example.notification_service.channels;

import com.example.notification_service.constants.NotificationChannelType;
import com.example.notification_service.constants.OutcomeType;
import com.example.notification_service.model.NotificationFailureResult;
import com.example.notification_service.model.NotificationMessage;
import com.example.notification_service.model.NotificationResult;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class EmailNotificationChannel implements NotificationChannel {

    private final JavaMailSender mailSender;

    public EmailNotificationChannel(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * @param message
     * @return
     */
    @Override
    public NotificationResult sendMessage(NotificationMessage message) {
        try {
            Object messageBody = message.getMessage();

            Map<String, Object> metadataFields = message.getMessageMetaData();

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(message.getSenderAddress());
            msg.setTo(message.getRecieverAddress());
            if (metadataFields != null) {
                msg.setSubject(String.valueOf(metadataFields.get("subject")));
            }
            msg.setText(messageBody.toString());

            mailSender.send(msg);
        }
        catch (MailAuthenticationException | MailParseException  mpe) {
            return new NotificationFailureResult("Mail send failure cause %s".formatted(mpe.getMessage()),
                    OutcomeType.FAILURE, mpe);
        }
        catch (MailException me) {
            return new NotificationFailureResult("Mail send failure cause %s".formatted(me.getMessage()),
                    OutcomeType.TRANSIENT_FAILURE, me);
        }
        catch (Exception exception) {
            return new NotificationFailureResult("Unknown failure: %s".formatted(exception.getMessage()),
                    OutcomeType.FAILURE, exception);
        }

        return new NotificationResult("Successfully sent the message", OutcomeType.SUCCESS);
    }

    /**
     * @param notificationChannelType
     * @return
     */
    @Override
    public boolean supports(NotificationChannelType notificationChannelType) {
        return NotificationChannelType.EMAIL.equals(notificationChannelType);
    }

}
