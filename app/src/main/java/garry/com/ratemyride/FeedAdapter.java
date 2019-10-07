
package garry.com.ratemyride;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;


public class FeedAdapter extends FirestoreRecyclerAdapter<NonRatedCar, FeedAdapter.CarHolder> {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    FeedAdapter(@NonNull FirestoreRecyclerOptions<NonRatedCar> options) {
        super(options);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull final CarHolder holder, int position, @NonNull final NonRatedCar model) {

        DocumentReference car = db.collection("cars").document(model.getDbID());
        car.get().addOnSuccessListener(documentSnapshot -> {
            Car carToRate = documentSnapshot.toObject(Car.class);

            if (carToRate != null) {
                carToRate.setCurrentCar(0);
                model.color = false;
                model.design = false;
                holder.vColorBT.setBackgroundResource(R.drawable.color_bt_design);
                holder.vDesignBT.setBackgroundResource(R.drawable.color_bt_design);
                if (carToRate.getCarImageUrls().size() > 0) {
                    holder.vUsername.setText(carToRate.owner.username);
                    Glide.with(holder.vCarImage.getContext())
                            .load(carToRate.getCarImageUrl(carToRate.getCurrentCar()))
                            .placeholder(new ColorDrawable(Color.BLACK))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.vCarImage);
                    if (carToRate.getCarImageUrls().size() > 1)
                        Glide.with(holder.vCarImage.getContext())
                                .load(carToRate.getCarImageUrl(carToRate.getCurrentCar() + 1))
                                .preload();
                    holder.vMake.setText(carToRate.getMake());
                    holder.vModel.setText(carToRate.getModel());
                    holder.vyear.setText(String.valueOf(carToRate.getYear()));

                    holder.vCarImage.setOnClickListener(v -> {

                        if (carToRate.getCurrentCar() + 1 > carToRate.getCarImageUrls().size() - 1) {
                            carToRate.setCurrentCar(0);
                        } else {
                            carToRate.setCurrentCar(carToRate.getCurrentCar() + 1);
                        }

                        Glide.with(holder.vCarImage.getContext())
                                .load(carToRate.getCarImageUrl(carToRate.getCurrentCar()))
                                .placeholder(new ColorDrawable(Color.BLACK))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.vCarImage);
                        if (carToRate.getCurrentCar() + 1 > carToRate.getCarImageUrls().size() - 1) {
                            Glide.with(holder.vCarImage.getContext())
                                    .load(carToRate.getCarImageUrl(0))
                                    .preload();
                        } else {
                            Glide.with(holder.vCarImage.getContext())
                                    .load(carToRate.getCarImageUrl(carToRate.getCurrentCar() + 1))
                                    .preload();
                        }
                    });

                    holder.vColorBT.setOnClickListener(v -> {
                        Log.i("Color: ", String.valueOf(model.color));
                       if(this.getItem(holder.getAdapterPosition()).color) {
                           holder.vColorBT.setBackgroundResource(R.drawable.color_bt_design);
                           this.getItem(holder.getAdapterPosition()).color = false;
                       }else {
                           holder.vColorBT.setBackgroundResource(R.drawable.green_button_design);
                           this.getItem(holder.getAdapterPosition()).color = true;
                       }

                        Log.i("Color: ", String.valueOf(model.color));
                    });

                    holder.vDesignBT.setOnClickListener(v -> {
                        Log.i("Design: ", String.valueOf(model.design));
                        if(this.getItem(holder.getAdapterPosition()).design) {
                            holder.vDesignBT.setBackgroundResource(R.drawable.color_bt_design);
                            this.getItem(holder.getAdapterPosition()).design = false;
                        }else {
                            holder.vDesignBT.setBackgroundResource(R.drawable.green_button_design);
                            this.getItem(holder.getAdapterPosition()).design = true;
                        }
                        Log.i("Design: ", String.valueOf(model.design));
                    });

                    if (position % 5 == 0) {
                        AdRequest adRequest = new AdRequest.Builder().build();
                        holder.mAd.loadAd(adRequest);
                    } else {
                        holder.mAd.setVisibility(View.GONE);
                    }
                }
            } else {
                getSnapshots().getSnapshot(position).getReference().delete();
            }


        });

    }

    @NonNull
    @Override


    public CarHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,
                parent, false);
        return new CarHolder(v);
    }


    class CarHolder extends RecyclerView.ViewHolder {
        TextView vUsername;
        ImageButton vCarImage;
        TextView vMake;
        TextView vModel;
        TextView vyear;
        Button vDesignBT;
        Button vColorBT;
        AdView mAd;

        CarHolder(@NonNull View itemViewIn) {
            super(itemViewIn);
            vUsername = itemViewIn.findViewById(R.id.txtUsername);
            vCarImage = itemViewIn.findViewById(R.id.carImageButton);
            vMake = itemViewIn.findViewById(R.id.txtMake);
            vModel = itemViewIn.findViewById(R.id.txtModel);
            vyear = itemViewIn.findViewById(R.id.txtYear);
            vDesignBT = itemViewIn.findViewById(R.id.designBT);
            vColorBT = itemViewIn.findViewById(R.id.colorBT);
            mAd = itemViewIn.findViewById(R.id.adView);
        }
    }


    void rateCarPositively(final int position, final int raterScoreIn) {
        WriteBatch batch = db.batch();
        NonRatedCar c = getSnapshots().getSnapshot(position).toObject(NonRatedCar.class);
        assert c != null;
        int numOfBonus = 0;
        if (getSnapshots().get(position).design) {
            numOfBonus++;
        }
        if (getSnapshots().get(position).color) {
            numOfBonus++;
        }
        final DocumentReference carToRate = db.collection("cars").document(c.getDbID());
        final int finalRatingBonus = numOfBonus + raterScoreIn;
        Rating rating = new Rating(MainActivity.owner.getUsername(), finalRatingBonus);
        carToRate.get().addOnSuccessListener(documentSnapshot -> {
            final Car car = documentSnapshot.toObject(Car.class);

            assert car != null;
            //d.update("totalRating", ((car.posRatings + raterScoreIn) / (car.numOfRatings + raterScoreIn)) * 100);
            //d.update("totalRating", car.totalRating + raterScoreIn);
            batch.update(carToRate,"posRatings", car.getPosRatings() + finalRatingBonus);
            batch.update(carToRate,"numOfRatings", car.getNumOfRatings() + 3);
            batch.commit();
            carToRate.collection("usersWhoRated").document(MainActivity.owner.username).set(rating);
        });
    }



    void rateCarNegatively(final int position, final int raterScoreIn) {
        NonRatedCar c = getSnapshots().getSnapshot(position).toObject(NonRatedCar.class);
        WriteBatch batch = db.batch();
        assert c != null;
        int numOfBonus = 0;

        if (getSnapshots().get(position).color) {
            numOfBonus++;
        }
        if (getSnapshots().get(position).design) {
            numOfBonus++;
        }

        final DocumentReference newCarToRate = db.collection("cars").document(c.getDbID());
        Rating rating = new Rating(MainActivity.owner.getUsername(), numOfBonus);
        final int finalNumOfBonus = numOfBonus;
        newCarToRate.get().addOnSuccessListener(documentSnapshot -> {
            final Car car = documentSnapshot.toObject(Car.class);
            assert car != null;
            batch.update(newCarToRate, "positiveRatings", car.getPosRatings() + finalNumOfBonus);
            batch.update(newCarToRate, "numOfRatings", car.getNumOfRatings() + raterScoreIn);
            batch.commit();
            newCarToRate.collection("usersWhoRated").document(MainActivity.owner.getUsername()).set(rating);
        });
    }
}

    class Rating {
        private String username;
        private int rating;

        //Empty constructor needed to add to firestore
        public Rating() {

        }
        Rating(String usernameIn, int ratingIn) {
            username = usernameIn;
            rating = ratingIn;
        }
        public String getUsername() {
            return username;
        }

        public int getRating() {
            return rating;
        }
    }

