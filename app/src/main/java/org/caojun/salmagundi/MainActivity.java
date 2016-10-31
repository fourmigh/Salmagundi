package org.caojun.salmagundi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.caojun.salmagundi.color.ColorActivity;
import org.caojun.salmagundi.qrcode.QRCodeActivity;
import org.caojun.salmagundi.utils.LogUtils;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        this.startActivity(new Intent(this, QRCodeActivity.class));
//        this.startActivity(new Intent(this, ColorActivity.class));
        Button btnQRCode = (Button) this.findViewById(R.id.btnQRCode);
        Button btnColor = (Button) this.findViewById(R.id.btnColor);

        btnQRCode.setOnClickListener(this);
        btnColor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId())
        {
            case R.id.btnQRCode:
                intent = new Intent(this, QRCodeActivity.class);
                break;
            case R.id.btnColor:
                intent = new Intent(this, ColorActivity.class);
                break;
            default:
                return;
        }
        this.startActivity(intent);
    }
}
