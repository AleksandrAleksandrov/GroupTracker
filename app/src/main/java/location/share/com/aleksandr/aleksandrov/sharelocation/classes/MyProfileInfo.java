package location.share.com.aleksandr.aleksandrov.sharelocation.classes;

/**
 * Created by Aleksandr on 12/14/2016.
 */

public class MyProfileInfo {
    private int id;
    private String fio;
    private String email;
    private String phone;

    public MyProfileInfo() {

    }

    public MyProfileInfo(int id, String fio, String email, String phone) {
        this.id = id;
        this.fio = fio;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getFio() {
        return fio;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
