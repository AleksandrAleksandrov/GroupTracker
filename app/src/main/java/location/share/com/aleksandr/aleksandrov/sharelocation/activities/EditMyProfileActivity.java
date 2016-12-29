package location.share.com.aleksandr.aleksandrov.sharelocation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import location.share.com.aleksandr.aleksandrov.sharelocation.R;
import location.share.com.aleksandr.aleksandrov.sharelocation.Res;
import location.share.com.aleksandr.aleksandrov.sharelocation.classes.MyProfileInfo;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.R.attr.bitmap;

/**
 * Created by Aleksandr on 11/23/2016.
 */

public class EditMyProfileActivity extends BaseActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    private ImageView imageView;
    private EditText user_fio, user_phone, user_email;
    private String mCurrentPhotoPath;
    private SharedPreferences sharedPreferences;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_my_profile_activity);

        user_fio = (EditText) findViewById(R.id.my_profile_fio_edit);
        user_phone = (EditText) findViewById(R.id.my_profile_phone_number_edit);
        user_email = (EditText) findViewById(R.id.my_profile_email_edit);

        sharedPreferences = getSharedPreferences(Res.PREFERENCE_KEY, MODE_PRIVATE);
        user_fio.setText(sharedPreferences.getString(Res.SHARED_PREFERENCES_FIO, ""));
        user_phone.setText(sharedPreferences.getString(Res.SHARED_PREFERENCES_PHONE_NUMBER, ""));
        user_email.setText(sharedPreferences.getString(Res.SHARED_PREFERENCES_EMAIL, ""));

        imageView = (ImageView) findViewById(R.id.my_profile_image_edit);

    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "location.share.com.aleksandr.aleksandrov.sharelocation", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
            setPic();
        }
        setPic();
    }

    private File createImageFile() throws IOException {
        // Create an imageView file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.done) {
            Observable.create(new Observable.OnSubscribe<MyProfileInfo>() {
                @Override
                public void call(final Subscriber<? super MyProfileInfo> subscriber) {
                    Communication communication = new Communication(getBaseContext());
                    MyProfileInfo data = communication.setMyInfo(user_fio.getText().toString(), user_email.getText().toString(), user_phone.getText().toString());
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<MyProfileInfo>() {
                        @Override
                        public void call(final MyProfileInfo myProfileInfo) {
                            finishWithResult(myProfileInfo);
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return true;
    }
    private void finishWithResult(MyProfileInfo myProfileInfo) {
        Bundle conData = new Bundle();
        conData.putInt("Thanks Thanks", RESULT_OK);
        Intent intent = new Intent();
        intent.putExtras(conData);
        intent.putExtra(Res.FIO, myProfileInfo.getFio());
        intent.putExtra(Res.PHONE, myProfileInfo.getPhone());
        intent.putExtra(Res.EMAIL, myProfileInfo.getEmail());
        setResult(RESULT_OK, intent);
        finish();
    }
}
