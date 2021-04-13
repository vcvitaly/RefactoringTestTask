package com.company;

import java.util.HashSet;
import java.util.Set;

public class ProviderImpl implements Provider {

    private static final Log log = LogFactory.getLog(ProviderImpl.class);
    private final Object calendarLock = new Object();
    private final Object waitingRoomBookingLock = new Object();

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private WaitingRoomService waitingRoomService;

    @Autowired
    private PaitientService paitientService;

    private ProviderInfo providerInfo;

    private Set<String> eligibleStateCodes = new HashSet<>();

    @Override
    public boolean scheduleAppointmentForPaitientById(DateTime appointmentTime, Long patientId ){

        synchronized (waitingRoomBookingLock) {
            System.out.println("Acquired lock on waitingRoomBookingLock");
            waitingRoomService.bookRoom(providerInfo.getProviderId(), patientId, appointmentTime);

            synchronized(calendarLock) {
                System.out.println("Acquired lock on calendarLock");
                calendarService.updateCalendar(providerInfo.getProviderId(), appointmentTime);

            }
            System.out.println("Lock on calendarLock freed");
            return true;
        }
        System.out.println("Lock on waitingRoomBookingLock freed up");
        return false;
    }

    @Override
    public boolean scheduleAppointmentForPaitientByEmail(DateTime appointmentTime, String patientEmail ){
        long patientId = paitientService.getPatientId(patientEmail);

        return scheduleAppointmentForPaitientById(appointmentTime, patientId);
    }

    @Override
    public void setProviderInfo(ProviderInfo providerInfo) {
        this.providerInfo = providerInfo;
    }

    @Override
    public void setEligibleState(String stateCode) {
        eligibleStateCodes.add(stateCode);
    }
}
