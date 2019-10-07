package garry.com.ratemyride;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;


import static garry.com.ratemyride.MainActivity.owner;
import static garry.com.ratemyride.MainActivity.user;
import static garry.com.ratemyride.MainActivity.userSnapshot;

public class FeedFragment extends Fragment {

    private FeedAdapter adapter;



    public FeedFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_feed, container, false);

        Query query = user.collection("nonRatedCars").whereEqualTo("carIsRated", false).limit(31);

        FirestoreRecyclerOptions<NonRatedCar> options = new FirestoreRecyclerOptions.Builder<NonRatedCar>()
                .setQuery(query, NonRatedCar.class)
                .build();

        RecyclerView recyclerView = view.findViewById(R.id.feed_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FeedAdapter(options);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
                            viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                        if (i == ItemTouchHelper.LEFT) {
                                DocumentReference dr = options.getSnapshots().getSnapshot(viewHolder.getLayoutPosition()).getReference();
                                dr.update("carIsRated", true);
                                user.update("totalRatings", owner.getTotalRatings() + 1);
                                owner.setTotalRatings(owner.getTotalRatings() + 1);
                                adapter.rateCarNegatively(viewHolder.getAdapterPosition(), owner.getRaterScore());
                                adapter.notifyDataSetChanged();
                                //options.getSnapshots().getSnapshot(viewHolder.getLayoutPosition()).getReference().update();
                        }

                        if (i == ItemTouchHelper.RIGHT) {
                                DocumentReference dr = options.getSnapshots().getSnapshot(viewHolder.getLayoutPosition()).getReference();
                                dr.update("carIsRated", true);
                                user.update("totalRatings", owner.getTotalRatings() +1);
                                owner.setTotalRatings(owner.getTotalRatings() + 1);
                                adapter.rateCarPositively(viewHolder.getAdapterPosition(), owner.getRaterScore());
                                adapter.notifyDataSetChanged();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).attachToRecyclerView(recyclerView);
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
