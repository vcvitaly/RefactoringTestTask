package com.company;

public interface ProviderInterface{

    boolean scheduleAppointmentForPatientByEmail(DateTime appointmentTime, Long paitientID);

    boolean scheduleAppointmentForPatientByEmail(DateTime appointmentTime, String paitientEmail);

    boolean setAddress(String firstName, String lastName, String streetAddress,
                       String city, String zipCode, String state, String country, String phoneNumber);

    void setEligibleState(String stateCode, String stateName);
}
