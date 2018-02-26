package edu.temple.cis4515_rsaencryption;

import android.content.ContentResolver;
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

public class DecryptActivity extends BaseActivity {

    private static ContentResolver cr;
    private static String encryptedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);

        cr = getContentResolver();
        final Spinner keySpinner = findViewById(R.id.keySpinner);
        final EditText encryptedEditText = findViewById(R.id.messageToDecryptEditText);
        final EditText decryptedEditText = findViewById(R.id.decryptedMessageEditText);
        final Button decryptButton = findViewById(R.id.decryptButton);

        initializeBaseLayout();
        initializeKeySpinner(keySpinner);
        initializeEncryptedMessage(encryptedEditText);

        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // retrieve selected keypair
                PPKContract.PublicPrivateKey selectedKey = (PPKContract.PublicPrivateKey) keySpinner.getSelectedItem();
                Log.i(Constants.LOG_TAG, "Decrypting message with key pair alias: " + selectedKey.getAlias());

                // retrieve provided message to encrypt
                String messageToEncrypt = encryptedEditText.getText().toString();
                Log.i(Constants.LOG_TAG, "Decrypting encoded message:\n" + messageToEncrypt);

                try {
                    // perform decryption and display result
                    byte[] encryptedArray = RsaEncryptor.decodeString(encryptedString);
                    String decryptedString = RsaEncryptor.decrypt(selectedKey.getPrivateKey(), encryptedArray);
                    decryptedEditText.setText(decryptedString);
                } catch (NoSuchAlgorithmException ex) {
                    Log.e(Constants.LOG_TAG, "Could not perform decryption with chosen algorithm.  Encountered error: "
                            + ex.getMessage() + "\n");
                    ex.printStackTrace();
                } catch (NoSuchPaddingException | BadPaddingException ex) {
                    Log.e(Constants.LOG_TAG, "Could not perform decryption with provided padding.  Encountered error: "
                            + ex.getMessage() + "\n");
                    ex.printStackTrace();
                } catch (InvalidKeyException ex) {
                    Log.e(Constants.LOG_TAG, "Could not perform decryption with provided public key.  Encountered error: "
                            + ex.getMessage() + "\n");
                    ex.printStackTrace();
                } catch (IllegalBlockSizeException ex) {
                    Log.e(Constants.LOG_TAG, "Could not perform decryption with provided block size.  Encountered error: "
                            + ex.getMessage() + "\n");
                    ex.printStackTrace();
                } catch (InvalidKeySpecException ex) {
                    Log.e(Constants.LOG_TAG, "Could not perform decryption with provided key spec.  Encountered error: "
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
        encryptedString = cursor.getString(outputIndex);
        encryptedMessage.setText(encryptedString);
    }

}