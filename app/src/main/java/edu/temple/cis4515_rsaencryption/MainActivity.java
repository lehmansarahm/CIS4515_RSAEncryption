package edu.temple.cis4515_rsaencryption;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import edu.temple.encrlib.publicprivatekeys.PPKContract;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button generateKeyPairButton = findViewById(R.id.generateButton);
        final Intent gkpIntent = new Intent(this, GenerateActivity.class);
        generateKeyPairButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(gkpIntent);
                finish();
            }
        });

        final Button encryptMessageButton = findViewById(R.id.encryptButton);
        final Intent emIntent = new Intent(this, EncryptActivity.class);
        encryptMessageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(emIntent);
                finish();
            }
        });

        final Button decryptMessageButton = findViewById(R.id.decryptButton);
        final Intent dmIntent = new Intent(this, DecryptActivity.class);
        decryptMessageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(dmIntent);
                finish();
            }
        });
    }

}