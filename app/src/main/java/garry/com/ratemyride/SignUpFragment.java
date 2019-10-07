package garry.com.ratemyride;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

import static garry.com.ratemyride.SignUpActivity.EMAIL_EXTRA;
import static garry.com.ratemyride.SignUpActivity.carsReference;
import static garry.com.ratemyride.SignUpActivity.createNewCar;
import static garry.com.ratemyride.SignUpActivity.email;
import static garry.com.ratemyride.SignUpActivity.mAuth;
import static garry.com.ratemyride.SignUpActivity.owner;
import static garry.com.ratemyride.SignUpActivity.ratersReference;
import static garry.com.ratemyride.SignUpActivity.setEmail;
import static garry.com.ratemyride.SignUpActivity.setOwner;

public class SignUpFragment extends Fragment {

    private EditText emailET;
    private EditText passwordET;
    private EditText usernameET;
    private Button signUpBT;
    private Boolean rideAdd;

    private static DocumentReference docRef;
    static DocumentReference newCarRef;

    private EditText makeET;
    private EditText modelET;
    private EditText yearET;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);


        rideAdd = false;

        makeET = view.findViewById(R.id.signUpMakeET);
        modelET = view.findViewById(R.id.signUpModelET);
        yearET = view.findViewById(R.id.signUpYearET);

        emailET = view.findViewById(R.id.signUpEmailET);
        passwordET = view.findViewById(R.id.signUpPasswordET);
        usernameET = view.findViewById(R.id.usernameET);
        signUpBT = view.findViewById(R.id.signUpButton);
        Button addRideBT = view.findViewById(R.id.addRideBT);
        addRideBT.setOnClickListener(v -> addRide(view));
        signUpBT.setOnClickListener(v -> signUp());

        return view;
    }

    private void signUp() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }


        setEmail(emailET.getText().toString().trim());
        String password = passwordET.getText().toString().trim();
        String username = usernameET.getText().toString().trim();
        //if valid credentials create new Owner object, create user account
        //create new rater document with account, and then add all existing cars to new rater
        if (validateForm(email, username, password)) {
            //Creator Owner object
            setOwner(email, username);
            //Create mAuth account
            createAccount(email, password);
            //The form wasn't verified so reset and try again
        } else {
            signUpBT.setEnabled(true);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }

    private void createAccount(String email, String password) {
        Log.d("Tag", "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Objects.requireNonNull(this.getActivity()), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, add all existing cars to the new rater's "nonRatedCars" db
                        Log.d("Tag", "createUserWithEmail:success");
                        //Create new Document for it in FireStore
                        docRef = ratersReference.document(email);
                        //Set new Document to be the new Owner object
                        docRef.set(owner);


                    carsReference.get().addOnCompleteListener(task1 -> {
                        for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task1.getResult())) {
                            Car car = documentSnapshot.toObject(Car.class);
                            if (!car.getOwner().getEmail().equals(SignUpActivity.email) && car.getCarImageUrls().size() > 0) {
                                DocumentReference newCarDocRef = docRef.collection("nonRatedCars").document(car.getDbID());
                                NonRatedCar newCar = new NonRatedCar(car.getDbID());
                                newCarDocRef.set(newCar);
                            }
                        }
                    });

                    //if ride is not added start new feed activity, else create new car and continue to photo cropping activity
                    if (!rideAdd) {
                        docRef.update("Timestamp", FieldValue.serverTimestamp()).addOnSuccessListener(task1 -> {
                            Intent intent = new Intent(this.getActivity(), MainActivity.class);
                            intent.putExtra(EMAIL_EXTRA, email);
                            startActivity(intent);
                        });

                    } else {
                        //create new car database reference then get id and create new car
                        docRef.update("Timestamp", FieldValue.serverTimestamp());
                        newCarRef = carsReference.document();
                        String carDocRefId = newCarRef.getId();
                        String make = makeET.getText().toString();
                        String model = modelET.getText().toString();
                        int year = Integer.parseInt(yearET.getText().toString());
                        Car carIn = createNewCar(owner, carDocRefId, make, model, year);
                        newCarRef.set(carIn);
                        newCarRef.update("timestamp", FieldValue.serverTimestamp());
                        //start photo adding/cropping activity
                        Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SignUpAddPicsFragment()).addToBackStack(null).commit();
                    }


                } else{
            // If sign in fails, display a message to the user.
            Log.w("tag", "createUserWithEmail:failure", task.getException());
            Toast.makeText(getActivity(), "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }

    });

    // [END create_user_with_email]


}

    private boolean validateForm(String emailCheck, String usernameCheck, String passwordCheck) {
        boolean valid = true;

        if (TextUtils.isEmpty(emailCheck)) {
            emailET.setError("Required.");
            valid = false;
        } else {
            emailET.setError(null);
            signUpBT.setEnabled(true);
        }

        if (TextUtils.isEmpty(usernameCheck)) {
            usernameET.setError("Username Required");
            valid = false;
        } else {
            usernameET.setError(null);

        }

        if (TextUtils.isEmpty(passwordCheck)) {
            passwordET.setError("Required.");
            valid = false;
        } else if (passwordCheck.length() < 6) {
            passwordET.setError("Password must be at least 6 characters long");
            valid = false;

        } else {
            passwordET.setError(null);
        }

        return valid;
    }


    private void addRide(View view) {
        //LinearLayout ll1 = findViewById(R.id.imageLL1);
        //LinearLayout ll2 = findViewById(R.id.imageLL2);
        Button addRideBT = view.findViewById(R.id.addRideBT);
        if (rideAdd) {
            makeET.setVisibility(View.GONE);
            modelET.setVisibility(View.GONE);
            yearET.setVisibility(View.GONE);
            //ll1.setVisibility(View.GONE);
            //ll2.setVisibility(View.GONE);
            addRideBT.setText(R.string.add_ride);
            signUpBT.setText(R.string.sign_up_without_a_ride);
            rideAdd = false;
        } else {
            addRideBT.setText(R.string.remove_ride);
            signUpBT.setText(R.string.sign_up_with_a_ride);
            makeET.setVisibility(View.VISIBLE);
            modelET.setVisibility(View.VISIBLE);
            yearET.setVisibility(View.VISIBLE);
            //ll1.setVisibility(View.VISIBLE);
            //ll2.setVisibility(View.VISIBLE);
            rideAdd = true;
        }
    }


}
