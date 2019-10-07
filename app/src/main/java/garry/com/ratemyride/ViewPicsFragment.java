package garry.com.ratemyride;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ViewPicsFragment extends Fragment {
    private Car car;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pics, container, false);
        final ImageButton ib = view.findViewById(R.id.carPicIB);
        car.setCurrentCar(0);
        Glide.with(ib.getContext())
                .load(car.getCarImageUrl(0))
                .placeholder(R.drawable.blue_gradient_background)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ib);
        if (car.getCarImageUrls().size() > 1)
            Glide.with(getContext())
                    .load(car.getCarImageUrl(car.getCurrentCar() + 1))
                    .preload();
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
            if (car.getCurrentCar() + 1 > car.getCarImageUrls().size() - 1) {
                Glide.with(getContext())
                        .load(car.getCarImageUrl(0))
                        .preload();
            } else {
                Glide.with(getContext())
                        .load(car.getCarImageUrl(car.getCurrentCar() + 1))
                        .preload();
            }

        });
        return view;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}

