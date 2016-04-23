package fi.aalto.mobilesystems.ledcontrol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;


/**
 * Created by Zhengwu on 21/04/2016.
 */
public class Broadcast extends AppCompatActivity {

    private static final String TAG = "Broadcast Activity";
    String msg = "Trolololol";
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        Log.d(msg,"The onCreate() event");


        final ImageButton mailButton = (ImageButton)findViewById(R.id.mail);
        mailButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LedControl.getContext(),mailColorActivity.class);
                startActivity(intent);
            }
        });
    }



}
