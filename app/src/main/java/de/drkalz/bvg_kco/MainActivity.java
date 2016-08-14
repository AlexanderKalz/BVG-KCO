package de.drkalz.bvg_kco;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dependencies zum Layout herstellen
        final EditText emailEdT = (EditText) findViewById(R.id.emailEdT);
        final EditText passwordEdT = (EditText) findViewById(R.id.passwordEdT);
        EditText phoneEdT = (EditText) findViewById(R.id.phoneEdT);
        final Button loginBtn = (Button) findViewById(R.id.loginBtn);

        // Authentifikation prüfen
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    emailEdT.setText(user.getEmail().toString());
                    loginBtn.setVisibility(View.INVISIBLE);
                } else {
                    loginBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Bitte loggen Sie sich ein!", Toast.LENGTH_LONG).show();
                }
            }
        };

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailEdT.getText().toString();
                String password = passwordEdT.getText().toString();

                if (!email.equals("") && !password.equals("")) {

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Erfolgreich eingeloggt!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
