package com.avanti.activities;

import android.app.Activity;

public abstract class PositionActivity extends Activity{
	
	public abstract void refresh(double x, double y, double z, int idCuadrante);
	
}
