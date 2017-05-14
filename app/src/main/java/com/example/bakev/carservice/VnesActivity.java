package com.example.bakev.carservice;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.data;

public class VnesActivity extends AppCompatActivity {

    Button VnesButton;
    EditText MarkaText, ModelText, ServisText, KilometriText;
    CheckBox Servis6, Servis12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vnes);
        VnesButton = (Button) findViewById(R.id.VnesiBtn);
        MarkaText = (EditText) findViewById(R.id.MarkaTextEdit);
        ModelText = (EditText) findViewById(R.id.ModelTextEdit);
        ServisText = (EditText) findViewById(R.id.ServisTextEdit);
        KilometriText = (EditText) findViewById(R.id.KilometriEditText);
        Servis6 = (CheckBox) findViewById(R.id.SestMeseciRadio);
        Servis12 = (CheckBox) findViewById(R.id.EdnaGodinaRadio);

        VnesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IzvData izv = new IzvData();
                izv.Model = MarkaText.getText().toString();
                izv.Marka = ModelText.getText().toString();
                izv.TipServis = ServisText.getText().toString();
                izv.KilometriNaAvtomobil = KilometriText.getText().toString();
                DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
                Date date = new Date();
                izv.DatumServis = dateFormat.format(date);
                izv.UserToken = FirebaseInstanceId.getInstance().getToken();
                if (izv.Model.length() == 0 || izv.Marka.length() == 0 || izv.TipServis.length() == 0 || izv.KilometriNaAvtomobil.length() == 0) {
                    CharSequence text = "Пополни ги сите полиња";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                } else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Izvestaj");
                    myRef.push().setValue(izv);
                    if (Servis6.isChecked()) {
                        scheduleNotification(getNotification(izv.TipServis), 500, 1);
                    }//15778476000L
                    if (Servis12.isChecked()) {
                        scheduleNotification(getNotification(izv.TipServis), 1000, 2);
                    }//31556952000L
                    if (Servis6.isChecked() == false && Servis12.isChecked() == false) {
                        CharSequence text = "Не селектиравте ни една опција од наведените за добивање на исвестување";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                    CharSequence text = "Успешно го  внесевте сервисот ";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    if(MarkaText.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


                    }
                    MarkaText.setText("");
                    ModelText.setText("");
                    ServisText.setText("");
                    KilometriText.setText("");
                    Servis6.setChecked(false);
                    Servis12.setChecked(false);

                }


            }
        }
        );


    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Проверите си го вашето  возило ");
        builder.setContentText(content);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        return builder.build();
    }

    private void scheduleNotification(Notification notification, long delay, int id) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id - 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}
