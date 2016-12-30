package location.share.com.aleksandr.aleksandrov.sharelocation.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;
import location.share.com.aleksandr.aleksandrov.sharelocation.Res;
import location.share.com.aleksandr.aleksandrov.sharelocation.adapters.MyListAdapter;
import location.share.com.aleksandr.aleksandrov.sharelocation.classes.MyProfileInfo;
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

    private MyListAdapter myListAdapter;

    private DBHelper dbHelper;
    private List<Person> person;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);

        person = getAll(this);


        dbHelper = new DBHelper(this, Res.DATA_BASE_SHARE_LOCATION, null, Res.DATA_BASE_VERSION);
    }

    private static final String CONTACT_ID = ContactsContract.Contacts._ID;
    private static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    private static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

    private static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

    public static List<Person> getAll(Context context) {
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
//                                validatePhoneNumber(phones.get(id));
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
            refreshContacts(person);
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillInList(List<UserInfo> userInfoList) {

        myListAdapter = new MyListAdapter(this, userInfoList);

        ListView listView = (ListView) findViewById(R.id.list_view_contacts);
        listView.setAdapter(myListAdapter);
    }
    ProgressDialog progress;
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
                        progress.dismiss();
                        fillInList(userInfoList);
                    }
                });
    }
}
