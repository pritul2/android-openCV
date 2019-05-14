package com.example.image_gallery;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static org.opencv.android.Utils.bitmapToMat;
import static org.opencv.android.Utils.matToBitmap;

public class MainActivity extends AppCompatActivity{

    ImageView imageView;
    Uri uri;
    Bitmap greyBitmap,imageBitmap,bitmap;
    Button btn,btn2;

 @SuppressLint("WrongViewCast")
 protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);
        btn = (Button)findViewById(R.id.btn);
        btn2 = (Button)findViewById(R.id.btn2);
        OpenCVLoader.initDebug();//always necessary to start the open cv library//
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public void openGallery(View v)
    {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent,100);//Used to get inside Gallery Activity//
        Toast.makeText(getApplicationContext(),"gone gallery",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK && data != null)
        {
            Uri data1 = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(data1);
                 bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



    public void convertToGrey(View v)
    {
        Mat rgba = new Mat();
        Mat greyMat = new Mat();

        /*BitmapFactory.Options o = new BitmapFactory.Options();
        o.inDither = false;
        o.inSampleSize = 4;8*/
        greyBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.RGB_565);
        bitmapToMat(bitmap,rgba);//converting bitmap image to matrix to perform operation//
        bitmapToMat(greyBitmap,greyMat);
        //rgba.convertTo(greyMat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(rgba,greyMat,Imgproc.COLOR_BGR2GRAY);//perform on matrix/
        matToBitmap(greyMat,greyBitmap);//matrix to bitmap//
        imageView.setImageBitmap(greyBitmap);

    }

}
