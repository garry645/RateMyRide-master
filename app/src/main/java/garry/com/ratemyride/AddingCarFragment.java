package garry.com.ratemyride;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;

import static garry.com.ratemyride.AddingCarActivity.carsReference;
import static garry.com.ratemyride.AddingCarActivity.createNewCar;
import static garry.com.ratemyride.AddingCarActivity.owner;

public class AddingCarFragment extends Fragment {
    static DocumentReference newCarRef;

    private EditText makeET;
    private EditText modelET;
    private EditText yearET;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adding_car, container, false);

        makeET = view.findViewById(R.id.addingCarMakeET);
        modelET = view.findViewById(R.id.addingCarModelET);
        yearET = view.findViewById(R.id.addingCarYearET);

        Button addCarButton = view.findViewById(R.id.add_car_button);
        addCarButton.setOnClickListener(v -> addRide());

        return view;
    }

    private void addRide() {
        //create new car database reference then get id and create new car
        newCarRef = carsReference.document();
        String carDocRefId = newCarRef.getId();
        String make = makeET.getText().toString();
        String model = modelET.getText().toString();
        int year = Integer.parseInt(yearET.getText().toString());
        Car carIn = createNewCar(owner, carDocRefId, make, model, year);
        newCarRef.set(carIn);
        //start photo adding/cropping activity
        Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.add_ride_fragment_container,
                new AddingCarPicsFragment()).addToBackStack(null).commit();


    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }


}
