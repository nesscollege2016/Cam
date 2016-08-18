package ness.tomerbu.edu.cam;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_CAMERA = 10;
    ImageView ivImageCapture;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ivImageCapture = (ImageView) findViewById(R.id.ivImageCapture);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            takePicture();
            }


        });
    }

    //OnClick On Fab...
    private void takePicture() {

        if (checkCallingOrSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CAMERA);
            return;
        }

        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);

        File folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            imageFile = File.createTempFile("temp", "jpg", folder);
            //String path = imageFile.getAbsolutePath();
            Uri uri = FileProvider.getUriForFile(this,
                    "ness.tomerbu.edu.cam.fileprovider",
                    imageFile);


            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQUEST_CAMERA);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    //Response of permission
    onRequestPermissionResult(){
        if (requestCode == REQUEST_CAMERA &&
                granted[0]==PackageManager.PERMISSION_GRANTED){

            takePicture();
        }
        else {
            Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);

        //Get the thumbnail:
        //Bitmap thumbnail = resultIntent.getParcelableExtra("data");
        //ivImageCapture.setImageBitmap(thumbnail);

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        ivImageCapture.setImageBitmap(bitmap);
    }












    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
