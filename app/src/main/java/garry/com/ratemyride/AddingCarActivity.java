package garry.com.ratemyride;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AddingCarActivity extends AppCompatActivity {



    protected static Owner owner;
    protected static Car car;
    protected static String email;
    protected static CollectionReference ratersReference;
    protected static CollectionReference carsReference;
    protected static DocumentReference usersDocumentRef;
    protected FirebaseFirestore db;
    protected static FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_car);

        Intent intent = getIntent();
        email = intent.getStringExtra(SignUpActivity.EMAIL_EXTRA);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        ratersReference = db.collection("raters");
        carsReference = db.collection("cars");



        usersDocumentRef = ratersReference.document(email);
        usersDocumentRef.get().addOnCompleteListener(task -> {
            owner = Objects.requireNonNull(task.getResult()).toObject(Owner.class);
            getSupportFragmentManager().beginTransaction().replace(R.id.add_ride_fragment_container,
                    new AddingCarFragment()).commit();
        });


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
}
