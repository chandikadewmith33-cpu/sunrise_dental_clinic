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
        String sql = "INSERT INTO appointments (appointment_no, patient_name, address, contact_number, "
                + "dentist_name, treatment_type, appointment_date, appointment_time) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, appointment.getAppointmentNo());
            ps.setString(2, appointment.getPatientName());
            ps.setString(3, appointment.getAddress());
            ps.setString(4, appointment.getContactNumber());
            ps.setString(5, appointment.getDentistName());
            ps.setString(6, appointment.getTreatmentType());
            ps.setDate(7, appointment.getAppointmentDate());
            ps.setTime(8, appointment.getAppointmentTime());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting appointment: " + e.getMessage());
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
}
