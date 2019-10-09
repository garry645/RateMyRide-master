package garry.com.ratemyride;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button swapToSignUpButton;
    EditText emailIn;
    EditText passwordIn;
    private Button logInButton;
    public static final String EMAIL_EXTRA = "garry.com.ratemyride.EMAIL_EXTRA_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        emailIn = findViewById(R.id.emailLogInET);
        passwordIn = findViewById(R.id.passwordLogInET);
        logInButton = findViewById(R.id.logInButton);

        MobileAds.initialize(this, "ca-app-pub-5571075744263497~4048463322");

        swapToSignUpButton = findViewById(R.id.swapToSignUpButton);
    }

    public void swapToSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void signIn(View view) {

        if (emailIn.getText().toString().equals("") || passwordIn.getText().toString().equals("")) {
            Toast.makeText(LoginActivity.this, "Authentication Failed",
                    Toast.LENGTH_SHORT).show();
        } else {
            logInButton.setEnabled(false);
            mAuth.signInWithEmailAndPassword(emailIn.getText().toString(), passwordIn.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Tag", "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(EMAIL_EXTRA, emailIn.getText().toString());
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Tag", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed",
                                    Toast.LENGTH_SHORT).show();
                            logInButton.setEnabled(true);
                        }

                        // ...
                    });
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}
