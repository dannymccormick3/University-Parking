package cs4278.vupark;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText mVUnetid;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mVUnetid = findViewById(R.id.vunetid_text);
        mPassword = findViewById(R.id.password_text);
    }
}
