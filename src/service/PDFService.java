package service;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import model.Appointment;

public class PDFService {

    /**
     * Generates a PDF containing the selected appointment details.
     *
     * @param appointment Appointment object
     */
    public static void generateAppointmentPDF(Appointment appointment) {

        // Check whether an appointment was selected
        if (appointment == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Please select an appointment first.",
                    "PDF Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // PDF file name
        String fileName = "Appointment_"
                + appointment.getAppointmentNo()
                + ".pdf";

        // Save PDF in the project folder
        String filePath = System.getProperty("user.dir")
                + File.separator
                + fileName;

        // Create A4 document
        Document document = new Document(PageSize.A4);

        try {

            // Connect PDF writer
            PdfWriter.getInstance(
                    document,
                    new FileOutputStream(filePath)
            );

            // Open document
            document.open();

            // =================================================
            // FONTS
            // =================================================

            Font clinicNameFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    20,
                    Font.BOLD
            );

            Font headingFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    16,
                    Font.BOLD
            );

            Font labelFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    11,
                    Font.BOLD
            );

            Font valueFont = FontFactory.getFont(
                    FontFactory.HELVETICA,
                    11,
                    Font.NORMAL
            );

            Font thankYouFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    12,
                    Font.BOLD
            );

            // =================================================
            // CLINIC NAME
            // =================================================

            Paragraph clinicName = new Paragraph(
                    "SUNRISE DENTAL CLINIC",
                    clinicNameFont
            );

            clinicName.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(clinicName);

            document.add(new Paragraph(" "));

            // =================================================
            // APPOINTMENT DETAILS
            // =================================================

            Paragraph heading = new Paragraph(
                    "APPOINTMENT DETAILS",
                    headingFont
            );

            heading.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(heading);

            document.add(
                    new Paragraph("-----------------------------------------")
            );

            document.add(new Paragraph(" "));

            // =================================================
            // APPOINTMENT INFORMATION
            // =================================================

            addDetail(
                    document,
                    "Appointment No",
                    appointment.getAppointmentNo(),
                    labelFont,
                    valueFont
            );

            addDetail(
                    document,
                    "Patient Name",
                    appointment.getPatientName(),
                    labelFont,
                    valueFont
            );

            addDetail(
                    document,
                    "Address",
                    appointment.getAddress(),
                    labelFont,
                    valueFont
            );

            addDetail(
                    document,
                    "Contact",
                    appointment.getContactNumber(),
                    labelFont,
                    valueFont
            );

            addDetail(
                    document,
                    "Dentist",
                    appointment.getDentistName(),
                    labelFont,
                    valueFont
            );

            addDetail(
                    document,
                    "Treatment",
                    appointment.getTreatmentType(),
                    labelFont,
                    valueFont
            );

            // =================================================
            // DATE FORMAT
            // =================================================

            String formattedDate = "";

            if (appointment.getAppointmentDate() != null) {

                SimpleDateFormat dateFormat =
                        new SimpleDateFormat("yyyy-MM-dd");

                formattedDate =
                        dateFormat.format(
                                appointment.getAppointmentDate()
                        );
            }

            addDetail(
                    document,
                    "Date",
                    formattedDate,
                    labelFont,
                    valueFont
            );

            // =================================================
            // TIME FORMAT
            // =================================================

            String formattedTime = "";

            if (appointment.getAppointmentTime() != null) {

                SimpleDateFormat timeFormat =
                        new SimpleDateFormat("hh:mm a");

                formattedTime =
                        timeFormat.format(
                                appointment.getAppointmentTime()
                        );
            }

            addDetail(
                    document,
                    "Time",
                    formattedTime,
                    labelFont,
                    valueFont
            );

            // =================================================
            // FOOTER
            // =================================================

            document.add(new Paragraph(" "));

            document.add(
                    new Paragraph("-----------------------------------------")
            );

            document.add(new Paragraph(" "));

            Paragraph thankYou = new Paragraph(
                    "Thank you for choosing us!",
                    thankYouFont
            );

            thankYou.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(thankYou);

            document.add(new Paragraph(" "));

            document.add(
                    new Paragraph("-----------------------------------------")
            );

            // Close the document
            document.close();

            // =================================================
            // SUCCESS MESSAGE
            // =================================================

            JOptionPane.showMessageDialog(
                    null,
                    "Appointment PDF generated successfully!\n\n"
                    + "Saved as:\n"
                    + filePath,
                    "PDF Generated",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (DocumentException | IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error generating PDF:\n"
                    + e.getMessage(),
                    "PDF Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    /**
     * Adds one appointment detail to the PDF.
     */
    private static void addDetail(
            Document document,
            String label,
            String value,
            Font labelFont,
            Font valueFont
    ) throws DocumentException {

        Paragraph paragraph = new Paragraph();

        paragraph.add(
                new Chunk(
                        String.format("%-16s : ", label),
                        labelFont
                )
        );

        paragraph.add(
                new Chunk(
                        value != null ? value : "",
                        valueFont
                )
        );

        document.add(paragraph);
    }
}
