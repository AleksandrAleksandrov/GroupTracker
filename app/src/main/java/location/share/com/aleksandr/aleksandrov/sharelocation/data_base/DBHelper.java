package location.share.com.aleksandr.aleksandrov.sharelocation.data_base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import location.share.com.aleksandr.aleksandrov.sharelocation.Res;

/**
 * Created by Aleksandr on 12/28/2016.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String CONTACTS_DATA_BASE_TABLE = "contacts_table";
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String FIO = "fio";
    public static final String PHONE_NUMBER = "phone_number";
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + CONTACTS_DATA_BASE_TABLE + " ("
        + ID + " integer,"
        + USERNAME + " text,"
        + FIO + " text,"
        + PHONE_NUMBER + " long"
        + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
