package service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    // Gmail sender account
    private static final String SENDER_EMAIL =
            "dewmith.lankagps@gmail.com";

    private static final String APP_PASSWORD =
            "vvhtvxprjiswbgti";

    public static boolean sendAppointmentEmail(
            String patientEmail,
            String patientName,
            String appointmentNo,
            String pdfLink) {

        String host = "smtp.gmail.com";

        Properties properties = new Properties();

        properties.put(
                "mail.smtp.host",
                host
        );

        properties.put(
                "mail.smtp.port",
                "587"
        );

        properties.put(
                "mail.smtp.auth",
                "true"
        );

        properties.put(
                "mail.smtp.starttls.enable",
                "true"
        );

        Session session = Session.getInstance(
                properties,
                new Authenticator() {

                    @Override
                    protected PasswordAuthentication
                    getPasswordAuthentication() {

                        return new PasswordAuthentication(
                                SENDER_EMAIL,
                                APP_PASSWORD
                        );
                    }
                }
        );

        try {

            Message message =
                    new MimeMessage(session);

            message.setFrom(
                    new InternetAddress(
                            SENDER_EMAIL
                    )
            );

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(
                            patientEmail
                    )
            );

            message.setSubject(
                    "Sunrise Dental Clinic - Appointment Confirmation"
            );

            // HTML email body
            String emailBody =
                    "<html>"
                    + "<body>"
                    + "<p>Dear <b>" + patientName + "</b>,</p>"

                    + "<p>"
                    + "Your appointment has been successfully "
                    + "registered at Sunrise Dental Clinic."
                    + "</p>"

                    + "<p>"
                    + "<b>Appointment Number:</b> "
                    + appointmentNo
                    + "</p>"

                    + "<p>"
                    + "Please click the link below to view "
                    + "your appointment details:"
                    + "</p>"

                    + "<p>"
                    + "<a href=\"" + pdfLink + "\">"
                    + "Click here to view your appointment details"
                    + "</a>"
                    + "</p>"

                    + "<p>"
                    + "Regards,<br>"
                    + "<b>Sunrise Dental Clinic</b>"
                    + "</p>"

                    + "</body>"
                    + "</html>";

            // IMPORTANT:
            // Tell Gmail this is an HTML email
            message.setContent(
                    emailBody,
                    "text/html; charset=UTF-8"
            );

            Transport.send(message);

            return true;

        } catch (MessagingException e) {

            System.err.println(
                    "Email sending failed: "
                    + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }
}