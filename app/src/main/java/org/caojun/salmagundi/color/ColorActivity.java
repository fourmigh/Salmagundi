package org.caojun.salmagundi.color;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import org.caojun.salmagundi.R;

/**
 * 渐变色
 * Created by CaoJun on 2016/10/28.
 */

public class ColorActivity extends AppCompatActivity {

    private Button btnOK;
    private CheckBox cbHex;
    private EditText etStartRed, etStartGreen, etStartBlue, etEndRed, etEndGreen, etEndBlue;
    private final int[] ResIdNumbers = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnA, R.id.btnB, R.id.btnC, R.id.btnD, R.id.btnE, R.id.btnF};
    private Button[] btnNumbers;
    private ImageView ivColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_color);

        btnOK = (Button) this.findViewById(R.id.btnOK);
        cbHex = (CheckBox) this.findViewById(R.id.cbHex);
        etStartRed = (EditText) this.findViewById(R.id.etStartRed);
        etStartGreen = (EditText) this.findViewById(R.id.etStartGreen);
        etStartBlue = (EditText) this.findViewById(R.id.etStartBlue);
        etEndRed = (EditText) this.findViewById(R.id.etEndRed);
        etEndGreen = (EditText) this.findViewById(R.id.etEndGreen);
        etEndBlue = (EditText) this.findViewById(R.id.etEndBlue);

        ivColor = (ImageView) this.findViewById(R.id.ivColor);
        btnNumbers = new Button[ResIdNumbers.length];
        for(int i = 0;i < ResIdNumbers.length;i ++)
        {
            btnNumbers[i] = (Button) this.findViewById(ResIdNumbers[i]);
            btnNumbers[i].setOnClickListener(oclButtonNumber);
        }
    }

    private OnClickListener oclButtonNumber = new OnClickListener(){

        @Override
        public void onClick(View v) {

        }
    };

    private int formatColor(String text, boolean isHex)
    {
        return 0;
    }
}
