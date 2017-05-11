package com.example.bakev.carservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    Button VnesButton, IzvButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VnesButton = (Button) findViewById(R.id.VnesServisButton);
        IzvButton = (Button) findViewById(R.id.IzvServis);
        VnesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent VnesIntent = new Intent(MainActivity.this, VnesActivity.class);
                startActivity(VnesIntent);
            }
        });
        IzvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IzvIntent = new Intent(MainActivity.this, IzvActivity.class);
                startActivity(IzvIntent);
            }
        });
    }
}
