package model;

import java.sql.Date;
import java.sql.Time;

/**
 * Represents one patient appointment / visit record.
 */
public class Appointment {
    private String appointmentNo;
    private String patientName;
    private String address;
    private String contactNumber;
    private String email;
    private String dentistName;
    private String treatmentType;
    private Date appointmentDate;
    private Time appointmentTime;

    public Appointment() {
    }

    public Appointment(String appointmentNo, String patientName, String address,
                    String contactNumber, String email,
                    String dentistName, String treatmentType,
                    Date appointmentDate, Time appointmentTime) {

        this.appointmentNo = appointmentNo;
        this.patientName = patientName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
        this.dentistName = dentistName;
        this.treatmentType = treatmentType;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
    this.email = email;
}

    public String getDentistName() {
        return dentistName;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }

    public String getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
