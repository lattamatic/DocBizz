package contacts;

/**
 * Created by aravind on 10/5/15.
 */
public class ContactItem {
    String imageUrl;
    String doctorName;
    String doctorPhone;
    String doctorEmail;
    String doctorHospital;
    String doctorCity;
    String doctorSpecification;

    public ContactItem(String imageUrl, String doctorName, String doctorPhone, String doctorEmail, String doctorHospital, String doctorCity, String doctorSpecification) {
        this.imageUrl = imageUrl;
        this.doctorName = doctorName;
        this.doctorPhone = doctorPhone;
        this.doctorEmail = doctorEmail;
        this.doctorHospital = doctorHospital;
        this.doctorCity = doctorCity;
        this.doctorSpecification = doctorSpecification;
    }
}
