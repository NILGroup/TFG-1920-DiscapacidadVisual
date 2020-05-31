package com.avanti.GEO;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;



/**
 * Render a compass.
 */

public class CompassRenderer implements GLSurfaceView.Renderer, SensorEventListener {
        private float   mAccelerometerValues[] = new float[3];
        private float   mMagneticValues[] = new float[3];
        private float rotationMatrix[] = new float[16];
        private float remappedRotationMatrix[] = new float[16];

            private Compass mCompass;

    public CompassRenderer() {
        mCompass = new Compass();
    }

    public void onDrawFrame(GL10 gl) {
        // Get rotation matrix from the sensor
        SensorManager.getRotationMatrix(rotationMatrix, null, mAccelerometerValues, mMagneticValues);
        // As the documentation says, we are using the device as a compass in landscape mode
        SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, remappedRotationMatrix);

        // Clear color buffer
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // Load remapped matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glLoadMatrixf(remappedRotationMatrix, 0);
       
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        mCompass.draw(gl);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
         gl.glViewport(0, 0, width, height);

         /*
          * Set our projection matrix. This doesn't have to be done
          * each time we draw, but usually a new projection needs to
          * be set when the viewport is resized.
          */
         float ratio = (float) width / height;
         gl.glMatrixMode(GL10.GL_PROJECTION);
         gl.glLoadIdentity();
         gl.glFrustumf(-ratio, ratio, -1, 1, 1, 100);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        gl.glDisable(GL10.GL_DITHER);


        /*
         * Some one-time OpenGL initialization can be made here
         * probably based on features of this particular context
         */
         gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                 GL10.GL_FASTEST);

         gl.glClearColor(0,0,0,0);
    }

        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        
        public void onSensorChanged(SensorEvent event) {
               synchronized (this) {
                        switch(event.sensor.getType()) {
                        case Sensor.TYPE_ACCELEROMETER:
                                mAccelerometerValues = event.values.clone();
                            break;
                        case Sensor.TYPE_MAGNETIC_FIELD:
                                mMagneticValues = event.values.clone();
                            break;
                        default:
                                break;
                        }
               }
        }
}

