package applicationname.companydomain.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback {


    Camera camera;

    SurfaceView mSurfaceView;

    SurfaceHolder mSurfaceHolder;

    final int CAMERA_REQUEST_CODE = 1;

    public static CameraFragment newInstance(){
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        mSurfaceView = view.findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
        else
        {
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        return view;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        if (parameters.getMaxNumMeteringAreas() > 0){ // check that metering areas are supported
            List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();

            Rect areaRect1 = new Rect(-100, -100, 100, 100);    // specify an area in center of image
            meteringAreas.add(new Camera.Area(areaRect1, 600)); // set weight to 60%
            Rect areaRect2 = new Rect(800, -1000, 1000, -800);  // specify an area in upper right of image
            meteringAreas.add(new Camera.Area(areaRect2, 400)); // set weight to 40%
            parameters.setMeteringAreas(meteringAreas);
        }
        camera.setDisplayOrientation(90);
        parameters.setPreviewFrameRate(30);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        try {
            camera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    mSurfaceHolder.addCallback(this);
                    mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                }
            }
        }

    }
}
