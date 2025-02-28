package com.smb116.TP2part2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText txtUname;

    private EditText txtPwd;

    private Button btnCancel;

    private Button btnLogin;

    /**
     *  Initialisation des views
     */
    private void init() {
        this.txtUname = (EditText) findViewById(R.id.txtUname);
        this.txtPwd = (EditText) findViewById(R.id.txtPwd);
        this.btnCancel = (Button) findViewById(R.id.btnCancel);
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCancelListener();
        btnLoginListener();
    }

    /**
     *  Listener du Button btnCancel
     */
    private void btnCancelListener() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResult();
                finish();
            }
        });
    }

    /**
     *  Envoie des informations renseignées par l'utilisateur
     *   a l'activité qui a déclenché cette activité
     */
    private void sendResult() {
        Intent intent = new Intent();
        intent.putExtra("name", txtUname.getText().toString());
        intent.putExtra("password", txtPwd.getText().toString());
        setResult(RESULT_OK, intent);
    }

    /**
     *  Listener du Button btnLogin
     */
    private void btnLoginListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResult();
                finish();
            }
        });

    }

    /**
     *  onCreate
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