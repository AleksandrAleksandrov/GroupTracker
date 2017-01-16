package location.share.com.aleksandr.aleksandrov.sharelocation.friends_activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;
import location.share.com.aleksandr.aleksandrov.sharelocation.Res;
import location.share.com.aleksandr.aleksandrov.sharelocation.activities.BaseActivity;
import location.share.com.aleksandr.aleksandrov.sharelocation.activities.Communication;
import location.share.com.aleksandr.aleksandrov.sharelocation.activities.ProfileActivity;
import location.share.com.aleksandr.aleksandrov.sharelocation.classes.Person;
import location.share.com.aleksandr.aleksandrov.sharelocation.classes.UserInfo;
import location.share.com.aleksandr.aleksandrov.sharelocation.data_base.DBHelper;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Aleksandr on 11/7/2016.
 */

public class FriendsActivity extends BaseActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private MyListAdapter myListAdapter;

    private DBHelper dbHelper;
    private List<Person> person;
    private List<UserInfo> mUserInofList = null;

    private String TAG = "FriendsActivity";
    private final String SAVE_SCREEN_STATE_PHONES = "save_screen_state_phones";
    private final String SAVE_SCREEN_STATE_CONTACTS_LIST = "save_screen_state_contacts_list";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);

        if (savedInstanceState != null) {
            mUserInofList = savedInstanceState.getParcelableArrayList(SAVE_SCREEN_STATE_CONTACTS_LIST);
            if(mUserInofList != null) {
                fillInList(mUserInofList);
            } else {
                person = savedInstanceState.getParcelableArrayList(SAVE_SCREEN_STATE_PHONES);
            }
        } else {
            mUserInofList = new ArrayList<>();
            dbHelper = new DBHelper(this, Res.DATA_BASE_SHARE_LOCATION, null, Res.DATA_BASE_VERSION);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DBHelper.CONTACTS_DATA_BASE_TABLE, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int idColIndex = cursor.getColumnIndex(DBHelper.ID);
                int usernameColIndex = cursor.getColumnIndex(DBHelper.USERNAME);
                int fioColIndex = cursor.getColumnIndex(DBHelper.FIO);
                int phoneColIndex = cursor.getColumnIndex(DBHelper.PHONE_NUMBER);
                do {
                    mUserInofList.add(new UserInfo(cursor.getInt(idColIndex),
                            cursor.getString(usernameColIndex),
                            cursor.getString(fioColIndex),
                            cursor.getString(phoneColIndex)));
                } while (cursor.moveToNext());
                cursor.close();
                db.close();
//                dbHelper.close();
                fillInList(mUserInofList);
            } else {

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(FriendsActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(FriendsActivity.this,
                            Manifest.permission.READ_CONTACTS)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(FriendsActivity.this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }

            }
        }
    }

    private static final String CONTACT_ID = ContactsContract.Contacts._ID;
    private static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    private static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

    private static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

    /**
     * Get all contacts from contacts of the phone.
     * @param context
     * @return
     */
    public List<Person> getAll(Context context) {
        ContentResolver cr = context.getContentResolver();

        Cursor pCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{PHONE_NUMBER, PHONE_CONTACT_ID},
                null,
                null,
                null
        );
        if(pCur != null){
            if(pCur.getCount() > 0) {
                HashMap<Integer, ArrayList<String>> phones = new HashMap<>();
                while (pCur.moveToNext()) {
                    Integer contactId = pCur.getInt(pCur.getColumnIndex(PHONE_CONTACT_ID));
                    ArrayList<String> curPhones = new ArrayList<>();
                    if (phones.containsKey(contactId)) {
                        curPhones = phones.get(contactId);
                    }
                    curPhones.add(pCur.getString(pCur.getColumnIndex(PHONE_NUMBER)));
                    phones.put(contactId, curPhones);
                }
                Cursor cur = cr.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[]{CONTACT_ID, DISPLAY_NAME, HAS_PHONE_NUMBER},
                        HAS_PHONE_NUMBER + " > 0",
                        null,
                        DISPLAY_NAME + " ASC");
                if (cur != null) {
                    if (cur.getCount() > 0) {
                        List<Person> contacts = new ArrayList<>();
                        while (cur.moveToNext()) {
                            int id = cur.getInt(cur.getColumnIndex(CONTACT_ID));
                            if(phones.containsKey(id)) {
                                Person con = new Person();
                                con.setId(id);
                                con.setName(cur.getString(cur.getColumnIndex(DISPLAY_NAME)));
                                con.setNumber(validatePhoneNumber(phones.get(id)));
                                contacts.add(con);
                            }
                        }
                        return contacts;
                    }
                    cur.close();
                }
            }
            pCur.close();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Observable.create(new Observable.OnSubscribe<List<Person>>() {
                        @Override
                        public void call(final Subscriber<? super List<Person>> subscriber) {

                            person = getAll(FriendsActivity.this);

                            subscriber.onNext(person);
                            subscriber.onCompleted();

                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<List<Person>>() {
                                @Override
                                public void call(final List<Person> userInfoList) {
                                    refreshContacts(person);
                                }
                            });

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private static List<String> validatePhoneNumber(ArrayList<String> phoneNumber) {
        int len = 12;
        List<String> phones = new ArrayList<>();
        for (String phone : phoneNumber)
        if (phone.matches("[+\\s]\\d{3}[-\\ \\s]\\d{2}[-\\ \\s]\\d{3}[-\\ \\s]\\d{4}")) {

            char[] c = new char[12];
            phone = phone.replaceAll("[^\\d]", "");
            phone.getChars(0, 12, c, 0);
            StringBuffer stringBuffer = new StringBuffer(len);
            for (char i : c) {
                stringBuffer.append(i);
            }
            phones.add(stringBuffer.toString());
        } else if (phone.matches("\\d{3}[-\\ \\s]\\d{2}[-\\ \\s]\\d{3}[-\\ \\s]\\d{4}")) {
            char[] c = new char[12];
            phone = phone.replaceAll("[^\\d]", "");
            phone.getChars(0, 12, c, 0);
            StringBuilder stringBuffer = new StringBuilder(len);
            for (char i : c) {
                stringBuffer.append(i);
            }
            phones.add(stringBuffer.toString());
        } else if (phone.matches("\\d{12}")) {
            char[] c = new char[12];
            phone = phone.replaceAll("[^\\d]", "");
            phone.getChars(0, 12, c, 0);
            StringBuilder stringBuffer = new StringBuilder(len);
            for (char i : c) {
                stringBuffer.append(i);
            }
            phones.add(stringBuffer.toString());
        } else if (phone.matches("[+\\s]\\d{12}")) {
            char[] c = new char[12];
            phone = phone.replaceAll("[^\\d]", "");
            phone.getChars(0, 12, c, 0);
            StringBuilder stringBuffer = new StringBuilder(len);
            for (char i : c) {
                stringBuffer.append(i);
            }
            phones.add(stringBuffer.toString());
        }
        return phones;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.contacts_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.menu_item_refresh) {
            if (person != null) {
                refreshContacts(person);
            } else if (person == null) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(FriendsActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(FriendsActivity.this,
                            Manifest.permission.READ_CONTACTS)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(FriendsActivity.this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.

                }
//                person = getAll(FriendsActivity.this);
//                refreshContacts(person);
            } else {
                    Observable.create(new Observable.OnSubscribe<List<Person>>() {
                        @Override
                        public void call(final Subscriber<? super List<Person>> subscriber) {

                            person = getAll(FriendsActivity.this);

                            subscriber.onNext(person);
                            subscriber.onCompleted();

                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<List<Person>>() {
                                @Override
                                public void call(final List<Person> userInfoList) {
                                    refreshContacts(person);
                                }
                            });
                }
                }
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillInList(final List<UserInfo> userInfoList) {

        myListAdapter = new MyListAdapter(this, userInfoList);

        ListView listView = (ListView) findViewById(R.id.list_view_contacts);
        listView.setAdapter(myListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(ProfileActivity.newIntent(FriendsActivity.this, userInfoList.get(i).getFio()));
            }
        });
    }
    private ProgressDialog progress;
    private void refreshContacts(final List<Person> person) {
        progress = new ProgressDialog(FriendsActivity.this);
        progress.show();
        Observable.create(new Observable.OnSubscribe<List<UserInfo>>() {
            @Override
            public void call(final Subscriber<? super List<UserInfo>> subscriber) {
                Communication communication = new Communication(getBaseContext());
                List<UserInfo> data = communication.getUserInfoByPhone(person);
                subscriber.onNext(data);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<UserInfo>>() {
                    @Override
                    public void call(final List<UserInfo> userInfoList) {
                        ContentValues contentValues = new ContentValues();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete(DBHelper.CONTACTS_DATA_BASE_TABLE, null, null);
                        for (UserInfo userInfo : userInfoList) {
                            contentValues.put(DBHelper.ID, userInfo.getId());
                            contentValues.put(DBHelper.USERNAME, userInfo.getName());
                            contentValues.put(DBHelper.FIO, userInfo.getFio());
                            contentValues.put(DBHelper.PHONE_NUMBER, userInfo.getPhoneNumber());
                            db.insert(DBHelper.CONTACTS_DATA_BASE_TABLE, null, contentValues);
                        }
                        db.close();
                        progress.dismiss();
                        fillInList(userInfoList);
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_SCREEN_STATE_PHONES, (ArrayList<? extends Parcelable>) person);
        outState.putParcelableArrayList(SAVE_SCREEN_STATE_CONTACTS_LIST, (ArrayList<? extends Parcelable>) mUserInofList);
    }
}
