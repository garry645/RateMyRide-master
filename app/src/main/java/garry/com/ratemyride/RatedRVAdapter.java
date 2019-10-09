package garry.com.ratemyride;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;

import java.text.DecimalFormat;
import java.util.Objects;

public class RatedRVAdapter extends FirestoreRecyclerAdapter<Rating, RatedRVAdapter.NewCarHolder> {

    private OnItemCLickListener mListener;

    public interface OnItemCLickListener {
        void onItemClick(int position);
    }

    void setOnItemClickListener(OnItemCLickListener listener) {
        mListener = listener;
    }

    RatedRVAdapter(@NonNull FirestoreRecyclerOptions<Rating> options) {
        super(options);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull final RatedRVAdapter.NewCarHolder holder, int position, @NonNull final Rating model) {

        DecimalFormat df = new DecimalFormat("#00.##");

        DocumentReference docRef = getSnapshots().getSnapshot(position).getReference().getParent().getParent();
        Objects.requireNonNull(docRef).get().addOnCompleteListener(task -> {
            Car car = Objects.requireNonNull(task.getResult()).toObject(Car.class);

            if(car != null && car.getCarImageUrls().size() > 0) {
                car.setCurrentCar(0);
                holder.vUsername.setText(car.owner.getUsername());
                holder.vRating.setText(df.format((car.getPosRatings() / car.getNumOfRatings()) * 100) + "%");
                Glide.with(holder.vCarImage.getContext())
                        .load(car.getCarImageUrl(0))
                        .placeholder(R.drawable.placeholder)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.vCarImage);

            } else {
                if(position < getSnapshots().size())
                getSnapshots().getSnapshot(position).getReference().delete();
            }
        });
    }

    @NonNull
    @Override


    public RatedRVAdapter.NewCarHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rated_card_layout,
                parent, false);
        return new RatedRVAdapter.NewCarHolder(v, mListener);
    }


    static class NewCarHolder extends RecyclerView.ViewHolder {

        TextView vUsername;
        ImageView vCarImage;
        TextView vRating;

        NewCarHolder(@NonNull View itemViewIn, final OnItemCLickListener listener) {
            super(itemViewIn);

            vUsername = itemViewIn.findViewById(R.id.txtUsername2);
            vCarImage = itemViewIn.findViewById(R.id.carImageView);
            vRating = itemView.findViewById(R.id.txtRating2);

            itemViewIn.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }


    }


}
