package edu.temple.encrlib.publicprivatekeys;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import edu.temple.encrlib.R;

public class PPKCursorAdapter extends CursorAdapter {

    private static final int KEY_CHAR_LIMIT = 50;

    public PPKCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.ppk_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView aliasView = view.findViewById(R.id.alias);
        TextView publicKeyView = view.findViewById(R.id.publicKey);
        TextView privateKeyView = view.findViewById(R.id.privateKey);

        String alias = cursor.getString(cursor.getColumnIndexOrThrow(PPKContract.Keys.ALIAS));
        String publicKey = cursor.getString(cursor.getColumnIndexOrThrow(PPKContract.Keys.PUBLIC));
        String privateKey = cursor.getString(cursor.getColumnIndexOrThrow(PPKContract.Keys.PRIVATE));

        aliasView.setText(alias);
        publicKeyView.setText(publicKey.substring(0, KEY_CHAR_LIMIT) + "...");
        privateKeyView.setText(privateKey.substring(0, KEY_CHAR_LIMIT) + "...");
    }

}