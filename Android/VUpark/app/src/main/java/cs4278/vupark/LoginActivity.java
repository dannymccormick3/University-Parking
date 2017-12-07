package cs4278.vupark;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText mVUnetid;
    private EditText mPassword;
    private Button mLoginButton;
    private HashMap<String, String> userMap = new HashMap<>();
    private HashMap<String, Object> lotMap = new HashMap<>();
    private FirebaseDatabase database;


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
        database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userMap = (HashMap)dataSnapshot.getValue();
                String username = "dmccormick";
                Log.d(username, userMap.get(username));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to load user info from database",
                        Toast.LENGTH_LONG).show();
            }
        });
        DatabaseReference lotRef = database.getReference("lots");
        lotRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lotMap = (HashMap)dataSnapshot.getValue();
                Log.d("LOT TEST", lotMap.get("lotid1").toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to load lot info from database",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void tryLogin(String username, String password) {
        final Intent intent = new Intent(this, MapsActivity.class);
        Log.d(username, userMap.get(username));
        if (userMap.containsKey(username) && userMap.get(username).equals(password)) {
            intent.putExtra("username", username);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Invalid username or password",
                    Toast.LENGTH_LONG).show();
        }


    }
}
