package garry.com.ratemyride;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Objects;
import static garry.com.ratemyride.MainActivity.owner;
import static java.security.AccessController.getContext;
import static java.util.Objects.requireNonNull;

public class EditCarActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST1 = 1;
    private static final int PICK_IMAGE_REQUEST2 = 2;
    private static final int PICK_IMAGE_REQUEST3 = 3;
    private static final int PICK_IMAGE_REQUEST4 = 4;
    private static final int PICK_IMAGE_REQUEST5 = 5;
    private static final int PICK_IMAGE_REQUEST6 = 6;
    private static final int PICK_IMAGE_REQUEST7 = 7;
    private static final int PICK_IMAGE_REQUEST8 = 8;
    private static final int PICK_IMAGE_REQUEST9 = 9;
    private static final int PICK_IMAGE_REQUEST10 = 10;

    private ImageButton carImage1;
    private ImageButton carImage2;
    private ImageButton carImage3;
    private ImageButton carImage4;
    private ImageButton carImage5;
    private ImageButton carImage6;
    private ImageButton carImage7;
    private ImageButton carImage8;
    private ImageButton carImage9;
    private ImageButton carImage10;

    private StorageReference fileReference1;
    private StorageReference fileReference2;
    private StorageReference fileReference3;
    private StorageReference fileReference4;
    private StorageReference fileReference5;
    private StorageReference fileReference6;
    private StorageReference fileReference7;
    private StorageReference fileReference8;
    private StorageReference fileReference9;
    private StorageReference fileReference10;

    private Dialog mDialog;
    private Button yesBT;
    private int imageAmount;
    private LinearLayout linearLayout1;
    private StorageReference mStorageRef;
    private ArrayList<StorageReference> fileReferences;
    ArrayList<ImageButton> IBs;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference carsDBRef = db.collection("cars");
    Car carToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);

        mStorageRef = FirebaseStorage.getInstance().getReference(owner.getEmail());

        linearLayout1 = findViewById(R.id.ECImageLL1);


        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.delete_image_dialog);
        Button noBt = mDialog.findViewById(R.id.noBT);
        yesBT = mDialog.findViewById(R.id.yesBT);
        noBt.setOnClickListener(v -> mDialog.dismiss());

        Intent intent = getIntent();
        String carDBID = intent.getStringExtra(MainActivity.CARDBID_EXTRA);
        assert carDBID != null;
        DocumentReference carDocRef = carsDBRef.document(carDBID);
        carDocRef.get().addOnCompleteListener(v -> {
            carToEdit = requireNonNull(v.getResult()).toObject(Car.class);
            assert carToEdit != null;
            loadFileReferences(carToEdit.getDbID());
            loadImageButtons();
            imageAmount = carToEdit.getCarImageUrls().size();

            for(int i = 0; i <= imageAmount; i++) {
                if(i < imageAmount) {
                    Glide.with(this)
                            .load(carToEdit.getCarImageUrl(i))
                            .into(IBs.get(i));
                }
                IBs.get(i).setVisibility(View.VISIBLE);
            }
        });

    }

    private void loadFileReferences(String dbIdIn) {
        fileReference1 = mStorageRef.child(dbIdIn + "/"
                + 1);
        fileReference2 = mStorageRef.child(dbIdIn + "/"
                + 2);
        fileReference3 = mStorageRef.child(dbIdIn + "/"
                + 3);
        fileReference4 = mStorageRef.child(dbIdIn + "/"
                + 4);
        fileReference5 = mStorageRef.child(dbIdIn + "/"
                + 5);
        fileReference6 = mStorageRef.child(dbIdIn + "/"
                + 6);
        fileReference7 = mStorageRef.child(dbIdIn + "/"
                + 7);
        fileReference8 = mStorageRef.child(dbIdIn + "/"
                + 8);
        fileReference9 = mStorageRef.child(dbIdIn + "/"
                + 9);
        fileReference10 = mStorageRef.child(dbIdIn + "/"
                + 10);

    }

    private void loadImageButtons() {
        carImage1 = findViewById(R.id.ECCarImage1);
        carImage2 = findViewById(R.id.ECCarImage2);
        carImage3 = findViewById(R.id.ECCarImage3);
        carImage4 = findViewById(R.id.ECCarImage4);
        carImage5 = findViewById(R.id.ECCarImage5);
        carImage6 = findViewById(R.id.ECCarImage6);
        carImage7 = findViewById(R.id.ECCarImage7);
        carImage8 = findViewById(R.id.ECCarImage8);
        carImage9 = findViewById(R.id.ECCarImage9);
        carImage10 = findViewById(R.id.ECCarImage10);
        IBs = new ArrayList<>();
        IBs.add(carImage1);
        IBs.add(carImage2);
        IBs.add(carImage3);
        IBs.add(carImage4);
        IBs.add(carImage5);
        IBs.add(carImage6);


        Button finishedBT = findViewById(R.id.ECFinishedBT);
        finishedBT.setOnClickListener(v -> finish());


        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

        }

        carImage1.setOnClickListener(v -> {
            Intent intent1 = CropImage.activity()
                    .setAspectRatio(4, 3)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(requireNonNull(this));
            startActivityForResult(intent1, PICK_IMAGE_REQUEST1);
        });

        carImage1.setOnLongClickListener(v -> {
            mDialog.show();
            yesBT.setOnClickListener(b ->
                    deleteImage(carImage1, fileReference1));
            return false;
        });

        carImage2.setOnClickListener(v -> {
            Intent intent = CropImage.activity()
                    .setAspectRatio(4, 3)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(requireNonNull(this));
            startActivityForResult(intent, PICK_IMAGE_REQUEST2);
        });

        carImage2.setOnLongClickListener(v -> {
            mDialog.show();
            yesBT.setOnClickListener(b ->
                    deleteImage(carImage2, fileReference2));
            return false;
        });
        carImage3.setOnClickListener(v -> {
            Intent intent = CropImage.activity()
                    .setAspectRatio(4, 3)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(requireNonNull(this));
            startActivityForResult(intent, PICK_IMAGE_REQUEST3);
        });

        carImage3.setOnLongClickListener(v -> {
            mDialog.show();
            yesBT.setOnClickListener(b ->
                    deleteImage(carImage3, fileReference3));

            return false;
        });

        carImage4.setOnClickListener(v -> {
            Intent intent = CropImage.activity()
                    .setAspectRatio(4, 3)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(requireNonNull(this));
            startActivityForResult(intent, PICK_IMAGE_REQUEST4);
        });

        carImage4.setOnLongClickListener(v -> {
            mDialog.show();
            yesBT.setOnClickListener(b ->
                    deleteImage(carImage4, fileReference4));
            return false;
        });

        carImage5.setOnClickListener(v -> {
            Intent intent = CropImage.activity()
                    .setAspectRatio(4, 3)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(requireNonNull(this));
            startActivityForResult(intent, PICK_IMAGE_REQUEST5);
        });

        carImage5.setOnLongClickListener(v -> {
            mDialog.show();
            yesBT.setOnClickListener(b ->
                    deleteImage(carImage5, fileReference5));
            return false;
        });

        carImage6.setOnClickListener(v -> {
            Intent intent = CropImage.activity()
                    .setAspectRatio(4, 3)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(requireNonNull(this));
            startActivityForResult(intent, PICK_IMAGE_REQUEST6);
        });

        carImage6.setOnLongClickListener(v -> {
            mDialog.show();
            yesBT.setOnClickListener(b ->
                    deleteImage(carImage6, fileReference6));
            return false;
        });
        carImage7.setOnClickListener(v -> {
            Intent intent = CropImage.activity()
                    .setAspectRatio(4, 3)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(requireNonNull(this));
            startActivityForResult(intent, PICK_IMAGE_REQUEST7);
        });

        carImage7.setOnLongClickListener(v -> {
            mDialog.show();
            yesBT.setOnClickListener(b ->
                    deleteImage(carImage7, fileReference7));
            return false;
        });

        carImage8.setOnClickListener(v -> {
            Intent intent = CropImage.activity()
                    .setAspectRatio(4, 3)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(requireNonNull(this));
            startActivityForResult(intent, PICK_IMAGE_REQUEST8);
        });

        carImage8.setOnLongClickListener(v -> {
            mDialog.show();
            yesBT.setOnClickListener(b ->
                    deleteImage(carImage8, fileReference8));
            return false;
        });
        carImage9.setOnClickListener(v -> {
            Intent intent = CropImage.activity()
                    .setAspectRatio(4, 3)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(requireNonNull(this));
            startActivityForResult(intent, PICK_IMAGE_REQUEST9);
        });

        carImage9.setOnLongClickListener(v -> {
            mDialog.show();
            yesBT.setOnClickListener(b ->
                    deleteImage(carImage9, fileReference9));
            return false;
        });

        carImage10.setOnClickListener(v -> {
            Intent intent = CropImage.activity()
                    .setAspectRatio(4, 4)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(requireNonNull(this));
            startActivityForResult(intent, PICK_IMAGE_REQUEST10);
        });

        carImage10.setOnLongClickListener(v -> {
            mDialog.show();
            yesBT.setOnClickListener(b ->
                    deleteImage(carImage10, fileReference10));
            return false;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == SignUpActivity.RESULT_OK && data != null) {

            switch (requestCode) {


                case PICK_IMAGE_REQUEST1:
                    CropImageView.CropResult result = CropImage.getActivityResult(data);
                    Uri uri1 = result.getUri();
                    uploadImage(uri1, carImage1, fileReference1);
                    break;


                case PICK_IMAGE_REQUEST2:
                    CropImageView.CropResult result2 = CropImage.getActivityResult(data);
                    Uri uri2 = result2.getUri();
                    uploadImage(uri2, carImage2, fileReference2);
                    break;

                case PICK_IMAGE_REQUEST3:
                    CropImageView.CropResult result3 = CropImage.getActivityResult(data);
                    Uri uri3 = result3.getUri();
                    uploadImage(uri3, carImage3, fileReference3);
                    break;

                case PICK_IMAGE_REQUEST4:
                    CropImageView.CropResult result4 = CropImage.getActivityResult(data);
                    Uri uri4 = result4.getUri();
                    uploadImage(uri4, carImage4, fileReference4);
                    break;

                case PICK_IMAGE_REQUEST5:
                    CropImageView.CropResult result5 = CropImage.getActivityResult(data);
                    Uri uri5 = result5.getUri();
                    uploadImage(uri5, carImage5, fileReference5);
                    break;

                case PICK_IMAGE_REQUEST6:
                    CropImageView.CropResult result6 = CropImage.getActivityResult(data);
                    Uri uri6 = result6.getUri();
                    uploadImage(uri6, carImage6, fileReference6);
                    break;

                case PICK_IMAGE_REQUEST7:
                    CropImageView.CropResult result7 = CropImage.getActivityResult(data);
                    Uri uri7 = result7.getUri();
                    uploadImage(uri7, carImage7, fileReference7);
                    break;

                case PICK_IMAGE_REQUEST8:
                    CropImageView.CropResult result8 = CropImage.getActivityResult(data);
                    Uri uri8 = result8.getUri();
                    uploadImage(uri8, carImage8, fileReference8);
                    break;

                case PICK_IMAGE_REQUEST9:
                    CropImageView.CropResult result9 = CropImage.getActivityResult(data);
                    Uri uri9 = result9.getUri();
                    uploadImage(uri9, carImage9, fileReference9);
                    break;

                case PICK_IMAGE_REQUEST10:
                    CropImageView.CropResult result10 = CropImage.getActivityResult(data);
                    Uri uri10 = result10.getUri();
                    uploadImage(uri10, carImage10, fileReference10);
                    break;
            }
        }

    }
    private void uploadImage(Uri uriIn, ImageButton currentIB, StorageReference fileReference) {
        fileReference.putFile(uriIn).addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Log.d("", "onSuccess: uri= " + uri.toString());
            carToEdit.addImageUrl(linearLayout1.indexOfChild(currentIB), uri.toString());
        })).addOnCompleteListener(task -> {
            Glide.with(this).load(uriIn).into(currentIB);
            imageAmount++;
            if (imageAmount < 10) {
                linearLayout1.getChildAt(imageAmount).setVisibility(View.VISIBLE);
            }
        });
    }

    private void deleteImage(ImageButton iBToDelete, StorageReference refToDel) {
        //refToDel.delete();
        int index = IBs.indexOf(iBToDelete);
        mStorageRef.child(carToEdit.getCarImageUrl(index)).delete();
        carToEdit.deleteImageUrl(linearLayout1.indexOfChild(iBToDelete));

        iBToDelete.setVisibility(View.GONE);
        iBToDelete.setImageResource(R.drawable.plus);

        linearLayout1.removeView(iBToDelete);
        linearLayout1.addView(iBToDelete, 9);
        imageAmount--;

        mDialog.dismiss();
    }

}

