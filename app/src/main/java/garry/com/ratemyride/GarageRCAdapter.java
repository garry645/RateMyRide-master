package garry.com.ratemyride;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DecimalFormat;

public class GarageRCAdapter extends FirestoreRecyclerAdapter<Car, GarageRCAdapter.GarageCarHolder> {


    public interface OnItemLongCLickListener {
        void onItemLongClick(int position);
    }

    void setOnItemClickListener(OnItemLongCLickListener listener) {
    }

    GarageRCAdapter(@NonNull FirestoreRecyclerOptions<Car> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull final GarageRCAdapter.GarageCarHolder holder, int position, @NonNull final Car model) {

        holder.v.setOnLongClickListener(v -> {
            Dialog mDialog = new Dialog(holder.itemView.getContext());
            mDialog.setContentView(R.layout.edit_car_dialog);
            Button noBt = mDialog.findViewById(R.id.ECDNoBT);
            Button yesBT = mDialog.findViewById(R.id.ECDYesBT);
            noBt.setOnClickListener(k -> mDialog.dismiss());

            yesBT.setOnClickListener(l -> {
                mDialog.dismiss();
                Intent intent = new Intent(holder.itemView.getContext(), EditCarActivity.class);
                intent.putExtra(MainActivity.CARDBID_EXTRA, model.getDbID());
                holder.itemView.getContext().startActivity(intent);
            });
            mDialog.show();
            return true;
        });
        if (model.getCarImageUrls().size() > 0) {

            holder.vUsername.setText(model.owner.username);
            model.setCurrentCar(0);
            Glide.with(holder.vCarImage.getContext())
                    .load(model.getCarImageUrl(model.getCurrentCar()))
                    .placeholder(R.drawable.blue_gradient_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(holder.vCarImage);
            if (model.getCarImageUrls().size() > 1) {
                Glide.with(holder.vCarImage.getContext())
                        .load(model.getCarImageUrl(model.getCurrentCar() + 1))
                        .preload();
            }
            DecimalFormat df = new DecimalFormat("#00.##");
            holder.vMake.setText(model.getMake());
            holder.vModel.setText(model.getModel());
            holder.vYear.setText(String.valueOf(model.getYear()));
            String ratingString = df.format((model.getPosRatings() / model.getNumOfRatings()) * 100) + "%";
            holder.vRating.setText(ratingString);
            holder.vCarImage.setOnClickListener(v -> {

                if (model.getCurrentCar() + 1 > model.getCarImageUrls().size() - 1) {
                    model.setCurrentCar(0);
                } else {
                    model.setCurrentCar(model.getCurrentCar() + 1);
                }
                Glide.with(holder.vCarImage.getContext())
                        .load(model.getCarImageUrl(model.getCurrentCar()))
                        .placeholder(R.drawable.blue_gradient_background)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .into(holder.vCarImage);
                if (model.getCurrentCar() + 1 > model.getCarImageUrls().size() - 1) {
                    Glide.with(holder.vCarImage.getContext())
                            .load(model.getCarImageUrl(0))
                            .preload();

                } else {
                    Glide.with(holder.vCarImage.getContext())
                            .load(model.getCarImageUrl(model.getCurrentCar() + 1))
                            .preload();
                }
            });


        } else {
            getSnapshots().getSnapshot(position).getReference().delete();
        }

    }

    @NonNull
    @Override
    public GarageRCAdapter.GarageCarHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.garage_card_layout,
                parent, false);
        return new GarageRCAdapter.GarageCarHolder(v);
    }

    class GarageCarHolder extends RecyclerView.ViewHolder {
        View v;
        TextView vUsername;
        ImageButton vCarImage;
        TextView vMake;
        TextView vModel;
        TextView vYear;
        TextView vRating;

        GarageCarHolder(@NonNull View itemViewIn) {
            super(itemViewIn);

            v = itemViewIn;
            vUsername = itemViewIn.findViewById(R.id.garage_usernameTV);
            vCarImage = itemViewIn.findViewById(R.id.garage_carIB);
            vMake = itemViewIn.findViewById(R.id.garage_makeTV);
            vModel = itemViewIn.findViewById(R.id.garage_modelTV);
            vYear = itemViewIn.findViewById(R.id.garage_yearTV);
            vRating = itemViewIn.findViewById(R.id.garage_ratingTV);
        }
    }
}
