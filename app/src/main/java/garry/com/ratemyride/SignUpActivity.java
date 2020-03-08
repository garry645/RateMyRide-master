package garry.com.ratemyride;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignUpActivity extends AppCompatActivity {
    public static final String EMAIL_EXTRA = "garry.com.ratemyride.EMAIL_EXTRA_TEXT";

    //LinearLayout signUpLL;
    protected static Owner owner;
    protected static Car car;
    protected static CollectionReference ratersReference;
    protected static CollectionReference carsReference;
    protected static String email;
    protected FirebaseFirestore db;
    protected static FirebaseAuth mAuth;
    Button finishedBT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        ratersReference = db.collection("raters");
        carsReference = db.collection("cars");

        finishedBT = findViewById(R.id.finishedBT);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new SignUpFragment()).commit();
    }

    protected static void setEmail(String emailIn) {
        email = emailIn;
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

    protected static Car createNewCar(Owner owner, String dbIDIn, String makeIn, String modelIn,
                                      int yearIn) {
        //creates a new car with given owner, make, model, year, and then initializes carImage array to null
        car = new Car(owner, makeIn, modelIn, yearIn, 0, 0, 0, dbIDIn);
        return car;
    }

    protected static void setOwner(String emailIn, String usernameIn) {
        owner = new Owner(emailIn, usernameIn);
    }

    public void swapToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}


