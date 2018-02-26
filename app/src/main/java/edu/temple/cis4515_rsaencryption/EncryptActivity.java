package edu.temple.cis4515_rsaencryption;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import edu.temple.encrlib.codedmessages.CMContract;
import edu.temple.encrlib.encryption.RsaEncryptor;
import edu.temple.encrlib.publicprivatekeys.PPKContract;
import edu.temple.encrlib.publicprivatekeys.PPKSpinnerAdapter;
import edu.temple.encrlib.utils.Constants;

public class EncryptActivity extends BaseActivity {

    private static ContentResolver cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);

        cr = getContentResolver();
        final Spinner keySpinner = findViewById(R.id.keySpinner);
        final EditText messageEditText = findViewById(R.id.messageToEncryptEditText);
        final EditText encryptedEditText = findViewById(R.id.encryptedMessageEditText);
        final Button encryptButton = findViewById(R.id.encryptButton);

        initializeBaseLayout();
        initializeKeySpinner(keySpinner);
        initializeEncryptedMessage(encryptedEditText);

        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // retrieve selected keypair
                PPKContract.PublicPrivateKey selectedKey = (PPKContract.PublicPrivateKey) keySpinner.getSelectedItem();
                Log.i(Constants.LOG_TAG, "Encrypting message with key pair alias: " + selectedKey.getAlias());

                // retrieve provided message to encrypt
                String messageToEncrypt = messageEditText.getText().toString();
                Log.i(Constants.LOG_TAG, "Encrypting raw message: " + messageToEncrypt);

                try {
                    // perform encryption
                    byte[] encryptedMessage = RsaEncryptor.encrypt(selectedKey.getPublicKey(), messageToEncrypt);
                    String encryptedMessageString = RsaEncryptor.encodeByteArray(encryptedMessage);

                    // save encrypted message
                    ContentValues values = new ContentValues();
                    values.put(CMContract.Messages.INPUT_MESSAGE, encryptedMessageString);
                    cr.insert(CMContract.Messages.CONTENT_URI, values);

                    // display encrypted result
                    initializeEncryptedMessage(encryptedEditText);
                } catch (NoSuchAlgorithmException ex) {
                    Log.e(Constants.LOG_TAG, "Could not perform encryption with chosen algorithm.  Encountered error: "
                            + ex.getMessage() + "\n");
                    ex.printStackTrace();
                } catch (NoSuchPaddingException | BadPaddingException ex) {
                    Log.e(Constants.LOG_TAG, "Could not perform encryption with provided padding.  Encountered error: "
                            + ex.getMessage() + "\n");
                    ex.printStackTrace();
                } catch (InvalidKeyException ex) {
                    Log.e(Constants.LOG_TAG, "Could not perform encryption with provided private key.  Encountered error: "
                            + ex.getMessage() + "\n");
                    ex.printStackTrace();
                } catch (IllegalBlockSizeException ex) {
                    Log.e(Constants.LOG_TAG, "Could not perform encryption with provided block size.  Encountered error: "
                            + ex.getMessage() + "\n");
                    ex.printStackTrace();
                } catch (InvalidKeySpecException ex) {
                    Log.e(Constants.LOG_TAG, "Could not perform encryption with provided key spec.  Encountered error: "
                            + ex.getMessage() + "\n");
                    ex.printStackTrace();
                }
            }
        });
    }

    private void initializeKeySpinner(Spinner keySpinner) {
        String[] projection = PPKContract.Keys.PROJECTION_ALL;
        Cursor cursor = cr.query(PPKContract.Keys.CONTENT_URI, projection, null, null, null);
        keySpinner.setAdapter(new PPKSpinnerAdapter(this, cursor));
    }

    private void initializeEncryptedMessage(EditText encryptedMessage) {
        Cursor cursor = cr.query(CMContract.Messages.CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        int outputIndex = cursor.getColumnIndex(CMContract.Messages.OUTPUT_MESSAGE);
        encryptedMessage.setText(cursor.getString(outputIndex));
    }

}