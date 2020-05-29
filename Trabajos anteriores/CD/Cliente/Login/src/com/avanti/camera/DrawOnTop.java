package com.avanti.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;


public class DrawOnTop extends View {
		
	private Point persona;
	
        public DrawOnTop(Context context) {
                super(context);
                
                // TODO Auto-generated constructor stub
        }
        
        public DrawOnTop(Context context, String texto) {
            super(context);
           
            
            // TODO Auto-generated constructor stub
    }

        @Override
        protected void onDraw( Canvas canvas ) {
                // TODO Auto-generated method stub
        		//drawNet(canvas);
//                drawCircle(canvas);
             //   super.onDraw(canvas);
        	drawPerson(canvas);
        }
        
        protected void drawCircle(Canvas canvas) {
        	 Paint paint = new Paint();
             paint.setStyle(Paint.Style.FILL);
             paint.setColor(Color.BLACK);
             
             canvas.drawCircle(40, 40, 40, paint);
             //canvas.drawText("Test Text", 10, 10, paint);
        }
        
        protected void drawText(Canvas canvas, String texto) {
       	 Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            canvas.drawText(texto, 10, 10, paint);
       }
        
        protected void drawNet(Canvas canvas) {
       	 Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            
            paint.setColor(Color.BLACK);
            float array[] = {10,300,30,150, 20, 300,40,150};
            canvas.drawLines(array, paint) ;
            //canvas.drawText("Test Text", 10, 10, paint);
       }

		public void setPersona(Point persona) {
			this.persona = persona;
		}

		public Point getPersona() {
			return persona;
		}
        
		public void drawPerson(Canvas canvas) {
			 Paint paint = new Paint();
			canvas.drawCircle(this.persona.x, this.persona.y, 10, paint);
			
		}
       
        

} 