package contacts;

/**
 * Created by aravind on 10/5/15.
 */
public class ContactItem {
    public String id;
    public String imageUrl;
    public String doctorName;
    public String doctorPhone;
    public String doctorEmail;
    public String doctorHospital;
    public String doctorCity;
    public String doctorSpecification;

    public ContactItem(String id, String imageUrl, String doctorName, String doctorPhone, String doctorEmail, String doctorHospital, String doctorCity, String doctorSpecification) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.doctorName = doctorName;
        this.doctorPhone = doctorPhone;
        this.doctorEmail = doctorEmail;
        this.doctorHospital = doctorHospital;
        this.doctorCity = doctorCity;
        this.doctorSpecification = doctorSpecification;
    }
}
