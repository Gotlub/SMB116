package com.SMB116.tp2part1;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    // Code de demande de permission d'utilisation du téléphone
    private static final int PHONE_CODE = 100;

    // Code de résultat du login
    private static final int LOGIN_CODE = 101;

    private ImageButton phoneBtn;

    private ImageButton loginBtn;

    private ImageButton internetBtn;

    private EditText phoneInput;

    private String url;

    /**
     *  Méthode d'initialisation des boutons appelé a onCreate
     */
    private void init() {
        phoneBtn = (ImageButton) findViewById(R.id.phone);
        loginBtn = (ImageButton) findViewById(R.id.login);
        internetBtn = (ImageButton) findViewById(R.id.internet);
        phoneInput = (EditText) findViewById(R.id.editTextPhone);
        listenerPhoneBtn();
        listenerLoginBtn();
        listenerInternetBtn();
     }

    /**
     *  Méthode pour utiliser l'application téléphone
     */
    private void usePhone() {
        url = phoneInput.getText().toString();
        url = url.isEmpty() ? "tel:+01723915447" : "tel:" + url;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        startActivity(intent);
    }

    /**
     *  Méthode pour utiliser l'application login
     */
    private void useLogin() {
        // intent explicite
        Intent intent = new Intent();
        intent.addCategory( Intent.CATEGORY_DEFAULT);
        intent.setComponent(new ComponentName("com.smb116.TP2part2",
                "com.smb116.TP2part2.MainActivity"));
        startActivityForResult(intent, LOGIN_CODE);
        /* intent implicite
        Intent intent = new Intent();
        intent.setAction("login.ACTION");
        startActivityForResult(intent, LOGIN_CODE);
        */
    }

    /**
     *  Listener du ImageButton phoneBtn lancé lors de onCreate
     */
    private void listenerPhoneBtn() {
        phoneBtn.setOnClickListener(new View.OnClickListener() {
            public  void onClick(View v) {
                // si la permission call phone n'est pas valide une demande est faite à l'utilisateur
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.CALL_PHONE},PHONE_CODE);
                else
                    usePhone();
            }
        });
     }

    /**
     * Listener du ImageButton loginBtn lancé lors de onCreate
     */
    private void listenerLoginBtn() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            public  void onClick(View v) {
                // si la permission pour l'application login n'est pas valide l'utilisateur est prévenu
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        "com.smb116.TP2part2.permission.CALL_LOGIN") != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Droit insufisant", Toast.LENGTH_SHORT).show();
                }
                else
                    useLogin();
            }
        });
    }

    /**
     * Listener du ImageButton internetBtn lancé lors de onCreate
     */
    private void listenerInternetBtn() {
        internetBtn.setOnClickListener(new View.OnClickListener() {
            public  void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.hello.com"));
                startActivity(intent);
            }
        });
    }

    /**
     * Reception des demandes de permission
     * @param requestCode The request code passed in {@link requestPermissions(
     * android.app.Activity, String[], int)}
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);
        if (requestCode == PHONE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                usePhone();
            }
        }
    }

    /**
     *  Reception des results
     * @param requestCode Code of the onActivityResult request
     * @param resultCode Result of the activity
     * @param data  Data affiliated to the result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(resultCode, resultCode, data);
        if (requestCode == LOGIN_CODE) {
            if (resultCode == RESULT_OK) {
                String nameLogin = data.getStringExtra("name");
                String pwdLogin = data.getStringExtra("password");
                Toast.makeText(this, "Donnée reçue : " + nameLogin + "\n"
                        + pwdLogin, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        init();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}