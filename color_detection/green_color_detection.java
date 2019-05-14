package com.example.video_color_detection;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.bitwise_and;
import static org.opencv.core.Core.inRange;


public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    JavaCameraView javaCameraView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        OpenCVLoader.initDebug();
        javaCameraView = (JavaCameraView) findViewById(R.id.javaCameraView);
        initialize(javaCameraView, 0);

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public void initialize(JavaCameraView javaCameraView, int index) {
        javaCameraView.setCameraIndex(index);//0 index is for rear camera and 1 is for front camera//
        javaCameraView.setCvCameraViewListener(this);
        javaCameraView.enableView();
    }

    Mat source, dest, bitwise;

    @Override
    public void onCameraViewStarted(int width, int height) {
        source = new Mat(width, height, CvType.CV_16UC4);
        dest = new Mat(width, height, CvType.CV_16UC4);
        bitwise = new Mat(width, height, CvType.CV_16UC4);

    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Imgproc.cvtColor(inputFrame.rgba(), source, Imgproc.COLOR_BGR2HSV);
        Scalar lowerb = new Scalar(58, 25, 41);//minimum range//
        Scalar upperb = new Scalar(79, 214, 255);//maximum range//

        inRange(source, lowerb, upperb, dest);//Masking//
        bitwise_and(source, source, bitwise, dest);//Applying bit wise and to get back original color//
  
        return bitwise;
    }
}

