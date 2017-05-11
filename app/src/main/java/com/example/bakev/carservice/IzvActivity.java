package com.example.bakev.carservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class IzvActivity extends AppCompatActivity {

    ListView IzvList;
    List<IzvData> IzvArraylist = new ArrayList<>();
    ArrayList<String> IzvArrayString;
    ArrayAdapter<String> IzvArrayAdapter;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izv);
        IzvList = (ListView) findViewById(R.id.ServisList);
        IzvList.setEnabled(false);
        IzvArrayString = new ArrayList<>();
        IzvArrayAdapter = new ArrayAdapter<String>(this, R.layout.izvlistdata, R.id.itemtext, IzvArrayString);
        IzvList.setAdapter(IzvArrayAdapter);
        token = FirebaseInstanceId.getInstance().getToken();
        citajIzv();
    }

    public void citajIzv() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Izvestaj");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                IzvArraylist.clear();
                IzvArrayString.clear();
                IzvData izv = new IzvData();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    izv = child.getValue(IzvData.class);
                    if (izv.UserToken.contains(token)) {
                        IzvArraylist.add(izv);
                    }
                }
                IzvArrayString.add("Сервис за корисникот");
                IzvArrayString.add(" ");
                for (IzvData data : IzvArraylist) {
                    IzvArrayString.add(data.Model + " " + data.Marka + " " + data.TipServis + " " + data.DatumServis + " " + data.KilometriNaAvtomobil);
                    IzvArrayAdapter.notifyDataSetChanged();
                }
                IzvArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Intent intent = new Intent(IzvActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
