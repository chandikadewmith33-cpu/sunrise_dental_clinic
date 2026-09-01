package service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    // CHANGE THESE TWO VALUES
    private static final String SENDER_EMAIL = "dewmith.lankagps@gmail.com";
    private static final String APP_PASSWORD = "qypfcujoybbrnuvu";

    public static boolean sendAppointmentEmail(
            String patientEmail,
            String patientName,
            String appointmentNo,
            String pdfLink) {

        String host = "smtp.gmail.com";

        Properties properties = new Properties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                SENDER_EMAIL,
                                APP_PASSWORD
                        );
                    }
                }
        );

        try {

            Message message = new MimeMessage(session);

            message.setFrom(
                    new InternetAddress(SENDER_EMAIL)
            );

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(patientEmail)
            );

            message.setSubject(
                    "Sunrise Dental Clinic - Appointment Confirmation"
            );

            String emailBody =
                    "Dear " + patientName + ",\n\n"
                    + "Your appointment has been successfully registered "
                    + "at Sunrise Dental Clinic.\n\n"
                    + "Appointment Number: " + appointmentNo + "\n\n"
                    + "Click here to see more details:\n"
                    + pdfLink
                    + "\n\n"
                    + "Regards,\n"
                    + "Sunrise Dental Clinic";

            message.setText(emailBody);

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