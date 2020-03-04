package com.avanti.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.avanti.camera.Preview;

public class CameraActivity extends Activity{
	
	private Preview mPreview;
	private ProgressDialog pd;
	
	protected void onCreate(Bundle savedInstanceState) {
		
    	super.onCreate( savedInstanceState );
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		
		pd = ProgressDialog.show(this, "", "Cargando datos...");
        mPreview = new Preview(this);        
        
        setContentView(mPreview, new LayoutParams
    			  (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));       
        
       pd.dismiss();
        
}

	public Preview getmPreview() {
		return mPreview;
	}

	public void setmPreview(Preview mPreview) {
		this.mPreview = mPreview;
	}

	public ProgressDialog getPd() {
		return pd;
	}

	public void setPd(ProgressDialog pd) {
		this.pd = pd;
	}
	
}
