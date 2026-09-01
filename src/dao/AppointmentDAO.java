package dao;

import db.DBConnection;
import model.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for appointments, treatment costs and bills.
 */
public class AppointmentDAO {
    public boolean deleteByAppointmentNo(String appointmentNo) {

    String deleteBillSQL = "DELETE FROM bills WHERE appointment_no = ?";
    String deleteAppointmentSQL = "DELETE FROM appointments WHERE appointment_no = ?";

    Connection con = null;

    try {
        con = DBConnection.getConnection();

        // Start transaction
        con.setAutoCommit(false);

        // First delete any bill connected to this appointment.
        try (PreparedStatement psBill = con.prepareStatement(deleteBillSQL)) {

            psBill.setString(1, appointmentNo);
            psBill.executeUpdate();
        }

        // Then delete the appointment.
        int rowsAffected;

        try (PreparedStatement psAppointment =
                     con.prepareStatement(deleteAppointmentSQL)) {

            psAppointment.setString(1, appointmentNo);
            rowsAffected = psAppointment.executeUpdate();
        }

        // If appointment was deleted successfully, commit.
        if (rowsAffected > 0) {

            con.commit();
            return true;

        } else {

            // Appointment was not found, so undo any bill deletion.
            con.rollback();
            return false;
        }

    } catch (SQLException e) {

        // Undo changes if something goes wrong.
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException rollbackException) {
                System.err.println(
                    "Rollback error: " + rollbackException.getMessage()
                );
            }
        }

        System.err.println(
            "Delete appointment error: " + e.getMessage()
        );

        e.printStackTrace();

        return false;

    } finally {

        if (con != null) {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                System.err.println(
                    "Connection closing error: " + e.getMessage()
                );
            }
        }
    }
}



    /**
     * Generates the next appointment number in the format APT0001, APT0002, ...
     */
    public String generateNextAppointmentNo() {
        String sql = "SELECT appointment_no FROM appointments ORDER BY appointment_no DESC LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                String last = rs.getString("appointment_no");
                int num = Integer.parseInt(last.replaceAll("[^0-9]", ""));
                num++;
                return String.format("APT%04d", num);
            } else {
                return "APT0001";
            }
        } catch (SQLException | NumberFormatException e) {
    System.err.println("Error generating appointment number: " + e.getMessage());
    return "APT0001";
 }
    }

    public boolean insertAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments "
        + "(appointment_no, patient_name, address, contact_number, email, "
        + "dentist_name, treatment_type, appointment_date, appointment_time) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, appointment.getAppointmentNo());
            ps.setString(2, appointment.getPatientName());
            ps.setString(3, appointment.getAddress());
            ps.setString(4, appointment.getContactNumber());
            ps.setString(5, appointment.getEmail());
            ps.setString(6, appointment.getDentistName());
            ps.setString(7, appointment.getTreatmentType());
            ps.setDate(8, appointment.getAppointmentDate());
            ps.setTime(9, appointment.getAppointmentTime());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
    System.err.println(
            "Error inserting appointment " 
            + appointment.getAppointmentNo() 
            + ": " 
            + e.getMessage()
    );
    return false;
}
    }

    public Appointment findByAppointmentNo(String appointmentNo) {
        String sql = "SELECT * FROM appointments WHERE appointment_no = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, appointmentNo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Appointment(
                        rs.getString("appointment_no"),
                        rs.getString("patient_name"),
                        rs.getString("address"),
                        rs.getString("contact_number"),
                        rs.getString("email"),
                        rs.getString("dentist_name"),
                        rs.getString("treatment_type"),
                        rs.getDate("appointment_date"),
                        rs.getTime("appointment_time")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding appointment: " + e.getMessage());
        }
        return null;
    }

    /**
     * Looks up the treatment cost for the given treatment type from the treatments table.
     */
    public double getTreatmentCost(String treatmentType) {
        String sql = "SELECT cost FROM treatments WHERE treatment_type = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, treatmentType);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("cost");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting treatment cost: " + e.getMessage());
        }
        return 0.0;
    }

    public String[] getAllTreatmentTypes() {
        String sql = "SELECT treatment_type FROM treatments ORDER BY treatment_type";
        List<String> types = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                types.add(rs.getString("treatment_type"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching treatment types: " + e.getMessage());
        }
        return types.toArray(new String[0]);
    }

    public boolean saveBill(String appointmentNo, double consultationFee, double treatmentCost, double total) {
        String sql = "INSERT INTO bills (appointment_no, consultation_fee, treatment_cost, total_amount) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, appointmentNo);
            ps.setDouble(2, consultationFee);
            ps.setDouble(3, treatmentCost);
            ps.setDouble(4, total);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving bill: " + e.getMessage());
            return false;
        }
    }
    /**
 * Get doctors who are not already booked for the selected date and time.
 */
public String[] getAvailableDoctors(String date, String time) {

    List<String> doctors = new ArrayList<>();

    String sql = "SELECT full_name "
            + "FROM users "
            + "WHERE role = 'DOCTOR' "
            + "AND full_name NOT IN ("
            + "    SELECT dentist_name "
            + "    FROM appointments "
            + "    WHERE appointment_date = ? "
            + "    AND appointment_time = ?"
            + ") "
            + "ORDER BY full_name";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setDate(1, java.sql.Date.valueOf(date));
        ps.setTime(2, java.sql.Time.valueOf(time + ":00"));

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                doctors.add(
                        rs.getString("full_name")
                );
            }
        }

    } catch (SQLException | IllegalArgumentException e) {

        System.err.println(
                "Error loading available doctors: "
                + e.getMessage()
        );
    }

    return doctors.toArray(new String[0]);
}
}
