package com.example.mycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText myCalculatorDisplay;
    EditText myCalculatorDisplayMem;

    boolean action_sumar, action_restar,action_mult,action_div;

    float value1, value2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myCalculatorDisplay = (EditText) findViewById(R.id.editText2);
        myCalculatorDisplayMem = (EditText) findViewById(R.id.editText3);
        myCalculatorDisplayMem.setText(null);
    }

    /** Called when the user clicks the . button */
    public void click_punto(View view) {
        myCalculatorDisplay.setText(myCalculatorDisplay.getText() + ".");
    }

    /** Called when the user clicks the 0 button */
    public void click_0(View view) {
        myCalculatorDisplay.setText(myCalculatorDisplay.getText() + "0");
    }

    /** Called when the user clicks the 1 button */
    public void click_1(View view) {
        myCalculatorDisplay.setText(myCalculatorDisplay.getText() + "1");
    }

    /** Called when the user clicks the 2 button */
    public void click_2(View view) {
        myCalculatorDisplay.setText(myCalculatorDisplay.getText() + "2");

    }

    /** Called when the user clicks the 3 button */
    public void click_3(View view) {
        myCalculatorDisplay.setText(myCalculatorDisplay.getText() + "3");

    }

    /** Called when the user clicks the 4 button */
    public void click_4(View view) {
        myCalculatorDisplay.setText(myCalculatorDisplay.getText() + "4");

    }

    /** Called when the user clicks the 5 button */
    public void click_5(View view) {
        myCalculatorDisplay.setText(myCalculatorDisplay.getText() + "5");

    }

    /** Called when the user clicks the 6 button */
    public void click_6(View view) {
        myCalculatorDisplay.setText(myCalculatorDisplay.getText() + "6");

    }

    /** Called when the user clicks the 7 button */
    public void click_7(View view) {
        myCalculatorDisplay.setText(myCalculatorDisplay.getText() + "7");

    }

    /** Called when the user clicks the 8 button */
    public void click_8(View view) {
        myCalculatorDisplay.setText(myCalculatorDisplay.getText() + "8");

    }

    /** Called when the user clicks the 9 button */
    public void click_9(View view) {
        myCalculatorDisplay.setText(myCalculatorDisplay.getText() + "9");

    }

    /** Called when the user clicks the borrar button */
    public void click_borrar(View view) {
        myCalculatorDisplay.setText("");
    }

    /** Called when the user clicks the = button */
    public void click_igual(View view) {
        value2 = Float.parseFloat(myCalculatorDisplay.getText() + "");
        myCalculatorDisplay.setText(null);
        float res=0;

        if(action_sumar){
            res = value1 + value2;
            myCalculatorDisplay.setText(Float.toString(res));
            action_sumar = false;
            myCalculatorDisplayMem.setText(myCalculatorDisplayMem.getText() + Float.toString(value2) + " = " + Float.toString(res) + "\n");
        }

        else if(action_restar){
            res = value1 - value2;
            myCalculatorDisplay.setText(Float.toString(res));
            action_restar = false;
            myCalculatorDisplayMem.setText(myCalculatorDisplayMem.getText() + Float.toString(value2) + " = " + Float.toString(res)+ "\n");
        }

        else if(action_mult){
            res = value1 * value2;
            myCalculatorDisplay.setText(Float.toString(res));
            action_mult = false;
            myCalculatorDisplayMem.setText(myCalculatorDisplayMem.getText() + Float.toString(value2) + " = " + Float.toString(res)+ "\n");
        }

        else if (action_div){
            if(value2 != 0) {
                res = value1 / value2;
                myCalculatorDisplay.setText(Float.toString(res));
                action_div = false;
                myCalculatorDisplayMem.setText(myCalculatorDisplayMem.getText() + Float.toString(value2) + " = " + Float.toString(res)+ "\n");
            }
            else{
                myCalculatorDisplayMem.setText(myCalculatorDisplayMem.getText() + Float.toString(value2) + " = " + "Infinito y mas alla" + "\n");
            }
        }

        myCalculatorDisplay.setText(null);
    }

    /** Called when the user clicks the + button */
    public void click_sumar(View view) {
        if (myCalculatorDisplay == null) {
            myCalculatorDisplay.setText("");
        } else {
            value1 = Float.parseFloat(myCalculatorDisplay.getText() + "");
            myCalculatorDisplayMem.setText(myCalculatorDisplayMem.getText() + Float.toString(value1) + " + ");
            action_sumar = true;
            myCalculatorDisplay.setText(null);
        }
    }

    /** Called when the user clicks the - button */
    public void click_restar(View view) {
        if (myCalculatorDisplay == null) {
            myCalculatorDisplay.setText("");
        } else {
            value1 = Float.parseFloat(myCalculatorDisplay.getText() + "");
            myCalculatorDisplayMem.setText(myCalculatorDisplayMem.getText() + Float.toString(value1) + " - ");
            action_restar = true;
            myCalculatorDisplay.setText(null);
        }
    }

    /** Called when the user clicks the x button */
    public void click_mult(View view) {
        if (myCalculatorDisplay == null) {
            myCalculatorDisplay.setText("");
        } else {
            value1 = Float.parseFloat(myCalculatorDisplay.getText() + "");
            myCalculatorDisplayMem.setText(myCalculatorDisplayMem.getText() + Float.toString(value1) + " * ");
            action_mult = true;
            myCalculatorDisplay.setText(null);
        }
    }

    /** Called when the user clicks the / button */
    public void click_div(View view) {
        if (myCalculatorDisplay == null) {
            myCalculatorDisplay.setText("");
        } else {
            value1 = Float.parseFloat(myCalculatorDisplay.getText() + "");
            myCalculatorDisplayMem.setText(myCalculatorDisplayMem.getText() + Float.toString(value1) + " / ");
            action_div = true;
            myCalculatorDisplay.setText(null);
        }
    }

}
