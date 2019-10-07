package garry.com.ratemyride;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import static garry.com.ratemyride.MainActivity.carsReference;
import static garry.com.ratemyride.MainActivity.email;


public class GarageFragment extends Fragment {

    private GarageRCAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_garage, container, false);
        Query query = carsReference.whereEqualTo("owner.email", email);

        final FirestoreRecyclerOptions<Car> options = new FirestoreRecyclerOptions.Builder<Car>()
                .setQuery(query, Car.class)
                .build();

        adapter = new GarageRCAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.garage_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
