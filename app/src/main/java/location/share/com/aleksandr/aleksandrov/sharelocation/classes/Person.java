package location.share.com.aleksandr.aleksandrov.sharelocation.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleksandr on 12/29/2016.
 */

public class Person implements Parcelable {
    private int id;
    private String name;
    private List<String> number = new ArrayList<String>();

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

    public void setNumber(List<String> number) {
        this.number = number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Упаковываем объект в Parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeList(number);
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {

        // Распаковываем объект из Parcel
        @Override
        public Person createFromParcel(Parcel parcel) {
            return new Person(parcel);
        }

        @Override
        public Person[] newArray(int i) {
            return new Person[0];
        }
    };

    private Person(Parcel parcel) {
        number = new ArrayList<String>();
        id = parcel.readInt();
        name = parcel.readString();
        parcel.readList(number, null);
    }
}
