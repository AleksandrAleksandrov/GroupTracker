package location.share.com.aleksandr.aleksandrov.sharelocation.data_base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import location.share.com.aleksandr.aleksandrov.sharelocation.Res;

/**
 * Created by Aleksandr on 12/28/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + Res.CONTACTS_DATA_BASE_TABLE + " ("
        + Res.ID + " integer primary key autoincrement,"
        + Res.USERNAME + " text,"
        + Res.FIO + " text,"
        + Res.PHONE_NUMBER + " long"
        + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
