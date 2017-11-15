package cs4278.vupark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText mVUnetid;
    private EditText mPassword;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mVUnetid = findViewById(R.id.vunetid_text);
        mPassword = findViewById(R.id.password_text);

        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String username = mVUnetid.getText().toString();
                        String password = mPassword.getText().toString();
                        tryLogin(username, password);
                    }
                }
        );
    }

    private void tryLogin(String username, String password) {

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
