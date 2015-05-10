package reports;

/**
 * Created by aravind on 10/5/15.
 */
public class ReportItem {
    String doctorName;
    int sentApproved;
    int sentDeclined;
    int receivedApproved;
    int receivedDeclined;

    public ReportItem(String doctorName, int sentApproved, int sentDeclined, int receivedApproved, int receivedDeclined) {
        this.doctorName = doctorName;
        this.sentApproved = sentApproved;
        this.sentDeclined = sentDeclined;
        this.receivedApproved = receivedApproved;
        this.receivedDeclined = receivedDeclined;
    }
}
