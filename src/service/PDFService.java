package service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import model.Appointment;

public class PDFService {

    public static String generateAppointmentPDF(Appointment appointment) {

        if (appointment == null) {

            JOptionPane.showMessageDialog(
                    null,
                    "Please select an appointment first.",
                    "PDF Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return null;
        }

        // =====================================================
        // FILE NAME
        // =====================================================

        String fileName = "Appointment_"
                + appointment.getAppointmentNo()
                + ".pdf";

        String filePath = System.getProperty("user.dir")
                + File.separator
                + fileName;

        Document document = new Document(
                PageSize.A4,
                50,
                50,
                50,
                50
        );

        try {

            PdfWriter.getInstance(
                    document,
                    new FileOutputStream(filePath)
            );

            document.open();

            // =====================================================
            // COLORS
            // =====================================================

            Color darkBlue = new Color(20, 91, 125);
            Color lightBlue = new Color(225, 243, 250);
            Color borderBlue = new Color(150, 205, 225);
            Color darkGray = new Color(55, 55, 55);
            Color white = Color.WHITE;

            // =====================================================
            // FONTS
            // =====================================================

            Font clinicFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    24,
                    Font.BOLD,
                    white
            );

            Font subtitleFont = FontFactory.getFont(
                    FontFactory.HELVETICA,
                    11,
                    Font.NORMAL,
                    white
            );

            Font headingFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    18,
                    Font.BOLD,
                    darkBlue
            );

            Font labelFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    11,
                    Font.BOLD,
                    darkBlue
            );

            Font valueFont = FontFactory.getFont(
                    FontFactory.HELVETICA,
                    11,
                    Font.NORMAL,
                    darkGray
            );

            Font footerFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    12,
                    Font.BOLD,
                    darkBlue
            );

            Font smallFont = FontFactory.getFont(
                    FontFactory.HELVETICA,
                    9,
                    Font.NORMAL,
                    darkGray
            );

            // =====================================================
            // HEADER
            // =====================================================

            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);

            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(darkBlue);
            headerCell.setPaddingTop(20);
            headerCell.setPaddingBottom(20);
            headerCell.setBorder(0);
            headerCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            Paragraph clinicName = new Paragraph(
                    "SUNRISE DENTAL CLINIC",
                    clinicFont
            );

            clinicName.setAlignment(Paragraph.ALIGN_CENTER);

            Paragraph subtitle = new Paragraph(
                    "Dental Care • Healthy Smiles • Better Life",
                    subtitleFont
            );

            subtitle.setAlignment(Paragraph.ALIGN_CENTER);

            headerCell.addElement(clinicName);
            headerCell.addElement(subtitle);

            headerTable.addCell(headerCell);

            document.add(headerTable);

            document.add(new Paragraph(" "));

            // =====================================================
            // APPOINTMENT TITLE
            // =====================================================

            Paragraph title = new Paragraph(
                    "APPOINTMENT CONFIRMATION",
                    headingFont
            );

            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(5);

            document.add(title);

            Paragraph line = new Paragraph(
                    "Your appointment has been successfully registered.",
                    smallFont
            );

            line.setAlignment(Paragraph.ALIGN_CENTER);
            line.setSpacingAfter(20);

            document.add(line);

            // =====================================================
            // APPOINTMENT NUMBER BOX
            // =====================================================

            PdfPTable numberTable = new PdfPTable(1);
            numberTable.setWidthPercentage(100);

            PdfPCell numberCell = new PdfPCell();

            numberCell.setBackgroundColor(lightBlue);
            numberCell.setBorderColor(borderBlue);
            numberCell.setBorderWidth(1);
            numberCell.setPadding(12);
            numberCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            Paragraph appointmentNumber = new Paragraph(
                    "APPOINTMENT NO: "
                    + appointment.getAppointmentNo(),
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            14,
                            Font.BOLD,
                            darkBlue
                    )
            );

            appointmentNumber.setAlignment(
                    Paragraph.ALIGN_CENTER
            );

            numberCell.addElement(appointmentNumber);
            numberTable.addCell(numberCell);

            document.add(numberTable);

            document.add(new Paragraph(" "));

            // =====================================================
            // APPOINTMENT DETAILS TABLE
            // =====================================================

            PdfPTable detailsTable = new PdfPTable(2);

            detailsTable.setWidthPercentage(100);

            detailsTable.setWidths(new float[]{35, 65});

            addDetailRow(
                    detailsTable,
                    "Patient Name",
                    appointment.getPatientName(),
                    labelFont,
                    valueFont,
                    lightBlue,
                    borderBlue
            );

            addDetailRow(
                    detailsTable,
                    "Address",
                    appointment.getAddress(),
                    labelFont,
                    valueFont,
                    white,
                    borderBlue
            );

            addDetailRow(
                    detailsTable,
                    "Contact Number",
                    appointment.getContactNumber(),
                    labelFont,
                    valueFont,
                    lightBlue,
                    borderBlue
            );

            addDetailRow(
                    detailsTable,
                    "Email",
                    appointment.getEmail(),
                    labelFont,
                    valueFont,
                    white,
                    borderBlue
            );

            addDetailRow(
                    detailsTable,
                    "Dentist",
                    appointment.getDentistName(),
                    labelFont,
                    valueFont,
                    lightBlue,
                    borderBlue
            );

            addDetailRow(
                    detailsTable,
                    "Treatment",
                    appointment.getTreatmentType(),
                    labelFont,
                    valueFont,
                    white,
                    borderBlue
            );

            // =====================================================
            // DATE
            // =====================================================

            String formattedDate = "";

            if (appointment.getAppointmentDate() != null) {

                SimpleDateFormat dateFormat =
                        new SimpleDateFormat("dd MMMM yyyy");

                formattedDate =
                        dateFormat.format(
                                appointment.getAppointmentDate()
                        );
            }

            addDetailRow(
                    detailsTable,
                    "Appointment Date",
                    formattedDate,
                    labelFont,
                    valueFont,
                    lightBlue,
                    borderBlue
            );

            // =====================================================
            // TIME
            // =====================================================

            String formattedTime = "";

            if (appointment.getAppointmentTime() != null) {

                SimpleDateFormat timeFormat =
                        new SimpleDateFormat("hh:mm a");

                formattedTime =
                        timeFormat.format(
                                appointment.getAppointmentTime()
                        );
            }

            addDetailRow(
                    detailsTable,
                    "Appointment Time",
                    formattedTime,
                    labelFont,
                    valueFont,
                    white,
                    borderBlue
            );

            document.add(detailsTable);

            document.add(new Paragraph(" "));

            // =====================================================
            // IMPORTANT MESSAGE
            // =====================================================

            PdfPTable messageTable = new PdfPTable(1);
            messageTable.setWidthPercentage(100);

            PdfPCell messageCell = new PdfPCell();

            messageCell.setBackgroundColor(
                    new Color(245, 250, 252)
            );

            messageCell.setBorderColor(borderBlue);
            messageCell.setPadding(15);

            Paragraph messageTitle = new Paragraph(
                    "Please remember",
                    labelFont
            );

            messageTitle.setAlignment(
                    Paragraph.ALIGN_CENTER
            );

            Paragraph message = new Paragraph(
                    "Please arrive a few minutes before your "
                    + "scheduled appointment time. "
                    + "If you need to change your appointment, "
                    + "please contact Sunrise Dental Clinic.",
                    smallFont
            );

            message.setAlignment(
                    Paragraph.ALIGN_CENTER
            );

            messageCell.addElement(messageTitle);
            messageCell.addElement(message);

            messageTable.addCell(messageCell);

            document.add(messageTable);

            document.add(new Paragraph(" "));

            // =====================================================
            // FOOTER
            // =====================================================

            Paragraph thankYou = new Paragraph(
                    "Thank you for choosing Sunrise Dental Clinic!",
                    footerFont
            );

            thankYou.setAlignment(
                    Paragraph.ALIGN_CENTER
            );

            document.add(thankYou);

            Paragraph smile = new Paragraph(
                    "Keep smiling — your smile matters to us.",
                    smallFont
            );

            smile.setAlignment(
                    Paragraph.ALIGN_CENTER
            );

            document.add(smile);

            document.add(new Paragraph(" "));

            Paragraph footer = new Paragraph(
                    "SUNRISE DENTAL CLINIC  |  APPOINTMENT CONFIRMATION",
                    smallFont
            );

            footer.setAlignment(
                    Paragraph.ALIGN_CENTER
            );

            document.add(footer);

            // =====================================================
            // CLOSE PDF
            // =====================================================

            document.close();

            System.out.println(
                    "PDF generated successfully: "
                    + filePath
            );

            return filePath;

        } catch (DocumentException | IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error generating PDF:\n"
                    + e.getMessage(),
                    "PDF Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // ADD DETAIL ROW
    // =========================================================

    private static void addDetailRow(
            PdfPTable table,
            String label,
            String value,
            Font labelFont,
            Font valueFont,
            Color background,
            Color border
    ) {

        PdfPCell labelCell = new PdfPCell(
                new Phrase(label, labelFont)
        );

        labelCell.setBackgroundColor(background);
        labelCell.setBorderColor(border);
        labelCell.setPadding(10);

        PdfPCell valueCell = new PdfPCell(
                new Phrase(
                        value != null ? value : "",
                        valueFont
                )
        );

        valueCell.setBackgroundColor(background);
        valueCell.setBorderColor(border);
        valueCell.setPadding(10);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}