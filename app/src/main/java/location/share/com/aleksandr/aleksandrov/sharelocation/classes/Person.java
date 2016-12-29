package location.share.com.aleksandr.aleksandrov.sharelocation.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleksandr on 12/29/2016.
 */

public class Person {
    private int id;
    private String name;
    private List<String> number = new ArrayList<>();

    public Person (String name, String number) {
        this.name = name;
        this.number.add(number);
    }

    public Person() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNumber() {
        return number;
    }

    public void setNumber(ArrayList<String> number) {
        this.number = number;
//        for (int i = 0; i < number.length; i++) {
//            this.number.add(number[i]);
//        }

    }
}
