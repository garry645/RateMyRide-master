package garry.com.ratemyride;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;





import static garry.com.ratemyride.LoginActivity.EMAIL_EXTRA;

public class MainActivity extends AppCompatActivity {

    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected static CollectionReference ratersReference;
    protected static CollectionReference carsReference;
    protected static Owner owner;
    protected static String email;

    protected static DocumentReference user;
    protected static DocumentSnapshot userSnapshot;
    public static final String CARDBID_EXTRA = "garry.com.ratemyride.CARBDID_EXTRA_TEXT";



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ratersReference = db.collection("raters");
        carsReference = db.collection("cars");
        Intent intent = getIntent();
        email = intent.getStringExtra(SignUpActivity.EMAIL_EXTRA);

        assert email != null;
        user = ratersReference.document(email);
        user.get().addOnCompleteListener(task -> {
                    userSnapshot = task.getResult();
                    if (userSnapshot == null) {
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                    owner = userSnapshot.toObject(Owner.class);
                }

        );
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_garage);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,
                new GarageFragment()).commit();

    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            menuItem -> {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_feed:
                        selectedFragment = new FeedFragment();
                        break;

                    case R.id.nav_garage:
                        selectedFragment = new GarageFragment();
                        break;

                    case R.id.nav_rated:
                        selectedFragment = new RatedFragment();
                        break;
                }
                assert selectedFragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,
                        selectedFragment).commit();

                return true;
            };

    public void openNewCarPage(View view) {
        if (owner.getCarsOwned() < owner.getCarSlots()) {
            Intent intent = new Intent(this, AddingCarActivity.class);
            intent.putExtra(EMAIL_EXTRA, email);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Not enough car slots", Toast.LENGTH_SHORT).show();
        }
    }


    /*static public void openCarEditPage() {
        Intent intent = new Intent(this, EditCarActivity.class);
        //intent.putExtra(CARDBID_EXTRA, dbIDIn);
        startActivity(intent);
    }*/
}
