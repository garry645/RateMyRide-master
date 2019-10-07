package garry.com.ratemyride;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DecimalFormat;
import java.util.Objects;


public class RatedFragment extends Fragment {


    private Dialog mDialog;
    private RatedRVAdapter adapter;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rated, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collectionGroup("usersWhoRated").whereEqualTo("username", MainActivity.owner.getUsername());
        final FirestoreRecyclerOptions<Rating> options = new FirestoreRecyclerOptions.Builder<Rating>()
                .setQuery(query, Rating.class)
                .build();
        mDialog = new Dialog(Objects.requireNonNull(this.getContext()));
        mDialog.setContentView(R.layout.car_dialog);
        adapter = new RatedRVAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.rated_recycler_view);
        GridLayoutManager glm = new GridLayoutManager(this.getActivity(), 3);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(position -> {
            TextView username = mDialog.findViewById(R.id.dialogUsernameET);
            final ImageButton ib = mDialog.findViewById(R.id.dialogIB);
            TextView make = mDialog.findViewById(R.id.dialogMakeTV);
            final TextView model = mDialog.findViewById(R.id.dialogModelTV);
            TextView year = mDialog.findViewById(R.id.dialogYearTV);
            TextView rating = mDialog.findViewById(R.id.dialogRatingTV);


            DocumentReference documentReference = adapter.getSnapshots().getSnapshot(position).getReference().getParent().getParent();
            Objects.requireNonNull(documentReference).get().addOnCompleteListener(task -> {
                final Car car = Objects.requireNonNull(task.getResult()).toObject(Car.class);
                //final Car car = adapter.getItem(position);
                for (int i = 0; i < Objects.requireNonNull(car).getCarImageUrls().size(); i++) {
                    Glide.with(ib.getContext())
                            .load(car.getCarImageUrl(i))
                            .preload();

                }
                ib.setOnLongClickListener(v -> {
                    ViewPicsFragment fragment = new ViewPicsFragment();
                    fragment.setCar(car);
                    mDialog.dismiss();
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container2, fragment, "findThisFragment")
                            .addToBackStack(null)
                            .commit();
                    return false;
                });
                Glide.with(ib.getContext())
                        .load(car.getCarImageUrl(0))
                        .placeholder(R.drawable.blue_gradient_background)
                        .centerCrop()
                        .into(ib);
                ib.setOnClickListener(v -> {
                    if (car.getCurrentCar() + 1 > car.getCarImageUrls().size() - 1) {
                        car.setCurrentCar(0);
                    } else {
                        car.setCurrentCar(car.getCurrentCar() + 1);
                    }
                    Glide.with(ib.getContext())
                            .load(car.getCarImageUrl(car.getCurrentCar()))
                            .placeholder(R.drawable.blue_gradient_background)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ib);

                });
                DecimalFormat df = new DecimalFormat("#00.##");
                username.setText(car.getOwner().getUsername());
                make.setText(car.getMake());
                model.setText(car.getModel());
                year.setText(String.valueOf(car.getYear()));
                String ratingString = df.format((car.getPosRatings() / car.getNumOfRatings()) * 100) + "%";
                rating.setText(ratingString);
                mDialog.show();
            });
        });
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
