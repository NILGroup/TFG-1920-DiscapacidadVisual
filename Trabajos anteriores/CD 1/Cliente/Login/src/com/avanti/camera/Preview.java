package com.avanti.camera;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Preview extends SurfaceView implements SurfaceHolder.Callback {
    
	private SurfaceHolder mHolder;
    private Camera mCamera;
    public static float _vision_angle;
    /**
     * Di�metro de la c�mara en mm
     */
    @SuppressWarnings("unused")
	private static double _camera_diameter = 76.2;
    
    /**
     * Distancia focal de la c�mara en mm
     */
    @SuppressWarnings("unused")
	private static double _camera_f = 71.12;
    
    public Preview(Context context) {
        super(context);
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
    	try{
    		
    		mCamera = Camera.open();   		
    		if(mCamera!=null){
	    		//double d = 2 * Math.atan(_camera_diameter/(2*_camera_f));
	    		//_vision_angle = (float) Math.toDegrees(d);
	    		_vision_angle = (float) /*50.63;*/50.87;
		        try {
		           mCamera.setPreviewDisplay(holder);
		        } catch (IOException exception) {
		            mCamera.release();
		            mCamera = null;
		        }
    		}else{
    			
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
        mCamera.stopPreview();        
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(w, h);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    
}