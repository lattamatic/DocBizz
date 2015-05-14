package referrals;


import java.util.ArrayList;


import messages.Message;


/**
 * Created by aravind on 12/5/15. 
 */
public class ReferralItem {
    String id;
    String doctorPicURL;
    public String doctorName;
    String patientName;
    String phoneNumber;
    String patientReason;
    String status;
    String referralDate;
    ArrayList<Message> messages;


    public ReferralItem(String id,String doctorPicURL, String doctorName, String patientName, String phoneNumber, String patientReason, String status, ArrayList<Message> messages, String referralDate) {
        this.id = id;
        this.doctorPicURL = doctorPicURL;
        this.doctorName = doctorName;
        this.patientName = patientName;
        this.phoneNumber = phoneNumber;
        this.patientReason = patientReason;
        this.status = status;
        this.messages = messages;
        this.referralDate = referralDate;
    }
} 