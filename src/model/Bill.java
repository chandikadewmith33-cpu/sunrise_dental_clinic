package model;

/**
 * Represents a calculated bill/receipt for one appointment.
 */
public class Bill {
    private String appointmentNo;
    private String patientName;
    private String treatmentType;
    private double consultationFee;
    private double treatmentCost;
    private double totalAmount;

    public Bill() {
    }

    public Bill(String appointmentNo, String patientName, String treatmentType,
                double consultationFee, double treatmentCost) {
        this.appointmentNo = appointmentNo;
        this.patientName = patientName;
        this.treatmentType = treatmentType;
        this.consultationFee = consultationFee;
        this.treatmentCost = treatmentCost;
        this.totalAmount = consultationFee + treatmentCost;
    }

    public String getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(String appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
        recalculateTotal();
    }

    public double getTreatmentCost() {
        return treatmentCost;
    }

    public void setTreatmentCost(double treatmentCost) {
        this.treatmentCost = treatmentCost;
        recalculateTotal();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    private void recalculateTotal() {
        this.totalAmount = this.consultationFee + this.treatmentCost;
    }
}
