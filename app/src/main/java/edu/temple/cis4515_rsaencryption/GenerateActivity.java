package edu.temple.cis4515_rsaencryption;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import edu.temple.encrlib.publicprivatekeys.PPKContract;
import edu.temple.encrlib.publicprivatekeys.PPKCursorAdapter;
import edu.temple.encrlib.utils.Constants;

public class GenerateActivity extends BaseActivity {

    public static final Uri PPK_CONTENT_URI = PPKContract.Keys.CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        initializeBaseLayout();

        final ContentResolver cr  = getContentResolver();
        refreshListView(cr);

        final EditText alias = findViewById(R.id.alias);
        final Button generateButton = findViewById(R.id.generateButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (alias.getText().toString().isEmpty()) {
                    Log.d(Constants.LOG_TAG, "Can't create new key-pair without an alias!");
                    Toast.makeText(GenerateActivity.this,
                            "Please provide an alias for the new key-pair.", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put(PPKContract.Keys.ALIAS, alias.getText().toString());
                    cr.insert(PPK_CONTENT_URI, values);
                    refreshListView(cr);
                }
            }
        });

        final Button emptyTableButton = findViewById(R.id.clearDbButton);
        emptyTableButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ContentResolver cr  = getContentResolver();
                cr.delete(PPKContract.CONTENT_URI, null, null);
                refreshListView(cr);
            }
        });
    }

    private void refreshListView(ContentResolver cr) {
        ListView ppkLv = findViewById(R.id.ppkListView);
        View emptyView = findViewById(R.id.emptyView);

        String[] projection = PPKContract.Keys.PROJECTION_ALL;
        Cursor cursor = cr.query(PPK_CONTENT_URI, projection, null, null, null);

        Log.d(Constants.LOG_TAG, "PPK query complete.  Records returned: " + cursor.getCount());
        if (cursor.getCount() == 0) ppkLv.setEmptyView(emptyView);
        else emptyView.setVisibility(View.INVISIBLE);

        PPKCursorAdapter adapter = new PPKCursorAdapter(this, cursor);
        ppkLv.setAdapter(adapter);
    }

}