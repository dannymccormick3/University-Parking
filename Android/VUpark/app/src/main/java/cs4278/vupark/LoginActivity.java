package cs4278.vupark;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText mVUnetid;
    private EditText mPassword;
    private Button mLoginButton;
    private DBConnection mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mVUnetid = findViewById(R.id.vunetid_text);
        mPassword = findViewById(R.id.password_text);

        mConnection = new DBConnection();

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

    private void tryLogin(String mUsername, String mPassword) {
        final String username = mUsername;
        final String password = mPassword;
        final Intent intent = new Intent(this, MapsActivity.class);
        new AsyncTask() {
            @Override
            protected Boolean doInBackground(Object[] objects) {
                boolean valid = mConnection.validateCredentials(username, password);
                if (valid) {
                    ArrayList<ParkingLot> availableLots = mConnection.getAvailableLots();
                    ArrayList<String> names = new ArrayList<>();
                    ArrayList<PolygonOptions> polys = new ArrayList<>();
                    for (ParkingLot p: availableLots){
                        names.add(p.getName());
                        polys.add(p.getPolyOps());
                    }
                    intent.putExtra("names", names);
                    intent.putExtra("polys", polys);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                return valid;
            }

            @Override
            protected void onPostExecute(Object valid){
                if (!(boolean)valid){
                    Toast.makeText(getApplicationContext(), "Invalid username or password",
                            Toast.LENGTH_LONG).show();
                }
            }
        }.execute();

    }
}
