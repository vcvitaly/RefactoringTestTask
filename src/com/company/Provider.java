package com.company;

public interface Provider {

    boolean scheduleAppointmentForPaitientById(DateTime appointmentTime, Long paitientID);

    boolean scheduleAppointmentForPaitientByEmail(DateTime appointmentTime, String paitientEmail);

    // TODO should it really be a part of this interface?
    void setProviderInfo(ProviderInfo providerInfo);
    // TODO should it really be a part of this interface?
    void setEligibleState(String stateCode);
}
