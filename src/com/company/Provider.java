package com.company;

import java.util.HashMap;
import java.util.Map;

public class Provider implements ProviderInterface{

    private final Log log = LogFactory.getLog(getClass());
    Object calendarLock = new Object();
    Object waitingRoomBookingLock = new Object();

    @Autowired
    CalendarService calendarService;

    @Autowired
    WaitingRoomService waitingRoomService;

    @Autowired
    PaitientService paitientService;

    public Long providerId;
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String city;
    private String zipcode;
    private String state;
    private String country;
    private String phoneNumber;

    private Map<String, String> eligibleStates = new HashMap<>();

    // it schedules an appointment by patientId
    public boolean scheduleAppointmentForPaitientById(DateTime appointmentTime, Long patientId ){

        synchronized(calendarLock) {
            System.out.println("Acquired lock on calendarLock");
            calendarService.updateCalendar(providerId, appointmentTime)

            synchronized(waitingRoomBookingLock) {
                System.out.println("Acquired lock on waitingRoomBookingLock");
                waitingRoomService.bookRoom(providerId, patientId, appointmentTime);
            }
            System.out.println("Lock on waitingRoomBookingLock freed up");
            return true;
        }
        System.out.println("Lock on calendarLock freed up");
        return false;
    }

    // Same as scheduleAppointmentForPaitientById but needs paitient email instead of paitientId
    public boolean scheduleAppointmentForPatientByEmail(DateTime appointmentTime, String patientEmail ){

        boolean calendarUpdated = false;
        long patientId = paitientService.getPatientId(patientEmail);
        synchronized (waitingRoomBookingLock) {

            System.out.println("Acquired lock on waitingRoomBookingLock");
            waitingRoomService.bookRoom(providerId, patientId, appointmentTime);

            synchronized(calendarLock) {
                System.out.println("Acquired lock on calendarLock");
                calendarService.updateCalendar(providerId, appointmentTime)

            }
            System.out.println("Lock on calendarLock freed");
            return true;
        }
        System.out.println("Lock on waitingRoomBookingLock freed up");
        return false;
    }

    // set provider info
    public boolean setAddress(String firstName, String lastName, String streetAddress,
                              String city, String zipCode, String state, String country, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipcode = zipCode;
        this.state = state;
        this.country = country;
        this.phoneNumber = phoneNumber;
    }

    public void setEligibleState(String stateCode, String stateName)
    {
        if (eligibleStates.containsKey(stateCode))
            eligibleStates.replace(stateCode, stateName);
        else
            eligibleStates.put(stateCode, stateName);
    }


    /**
     * this is a convenient method to read any file. e.g patient previous visit summary.
     */
    public static String readFile(String fileName){
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            final StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
