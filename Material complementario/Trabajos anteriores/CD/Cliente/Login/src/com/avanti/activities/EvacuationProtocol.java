package com.avanti.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;

import com.avanti.camera.DrawOnTop;
import com.avanti.camera.Preview;

public class EvacuationProtocol extends Activity {
	private Preview mPreview;
//	@SuppressWarnings("unused")
//	private GLSurfaceViewActivity openGLPreview;

	protected void onCreate(Bundle savedInstanceState) {
		 MainMenu.activeActivity = this;
	super.onCreate(savedInstanceState);

	// Hide the window title.
	requestWindowFeature(Window.FEATURE_NO_TITLE);

	// Create our Preview view and set it as the content of our
	mPreview = new Preview(this);
	DrawOnTop mDraw = new DrawOnTop(this);
	setContentView(mPreview);
	addContentView(mDraw, new LayoutParams
			  (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	
	
	 
	
	}
}