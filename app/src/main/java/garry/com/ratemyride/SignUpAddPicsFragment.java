package garry.com.ratemyride;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;
import java.util.UUID;

import static garry.com.ratemyride.SignUpActivity.EMAIL_EXTRA;
import static garry.com.ratemyride.SignUpActivity.car;
import static garry.com.ratemyride.SignUpActivity.email;
import static garry.com.ratemyride.SignUpActivity.owner;
import static garry.com.ratemyride.SignUpActivity.ratersReference;
import static garry.com.ratemyride.SignUpFragment.newCarRef;

public class SignUpAddPicsFragment extends Fragment {

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_ride, container, false);

        mStorageRef = FirebaseStorage.getInstance().getReference(owner.getEmail() + "/" + car.getDbID());
        loadFileReferences(car.getDbID());
        loadImageButtons(view);

        imageAmount = 0;
        linearLayout1 = view.findViewById(R.id.imageLL1);

        mDialog = new Dialog(Objects.requireNonNull(this.getContext()));
        mDialog.setContentView(R.layout.delete_image_dialog);
        Button noBt = mDialog.findViewById(R.id.noBT);
        yesBT = mDialog.findViewById(R.id.yesBT);
        noBt.setOnClickListener(v -> mDialog.dismiss());

        return view;
    }

    private void loadImageButtons(View view) {
        carImage1 = view.findViewById(R.id.carImage1);
        carImage2 = view.findViewById(R.id.carImage2);
        carImage3 = view.findViewById(R.id.carImage3);
        carImage4 = view.findViewById(R.id.carImage4);
        carImage5 = view.findViewById(R.id.carImage5);
        carImage6 = view.findViewById(R.id.carImage6);
        carImage7 = view.findViewById(R.id.carImage7);
        carImage8 = view.findViewById(R.id.carImage8);
        carImage9 = view.findViewById(R.id.carImage9);
        carImage10 = view.findViewById(R.id.carImage10);


        Button finishedBT = view.findViewById(R.id.finishedBT);
        finishedBT.setOnClickListener(v -> finishForm());


        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

        }

        carImage1.setOnClickListener(v -> {
            Intent intent1 = CropImage.activity()
                    .setAspectRatio(4, 3)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(Objects.requireNonNull(this.getActivity()));
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
                    .getIntent(Objects.requireNonNull(this.getActivity()));
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
                    .getIntent(Objects.requireNonNull(this.getActivity()));
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
                    .getIntent(Objects.requireNonNull(this.getActivity()));
            startActivityForResult(intent, PICK_IMAGE_REQUEST4);
        });

        carImage4.setOnLongClickListener(v -> {
            if(!carImage4.getBackground().equals(Drawable.createFromPath("R.drawable.plus"))) {
                mDialog.show();
                yesBT.setOnClickListener(b ->
                        deleteImage(carImage4, fileReference4));
            }
            return false;
        });

        carImage5.setOnClickListener(v -> {
            Intent intent = CropImage.activity()
                    .setAspectRatio(4, 3)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(Objects.requireNonNull(this.getActivity()));
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
                    .getIntent(Objects.requireNonNull(this.getActivity()));
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
                    .getIntent(Objects.requireNonNull(this.getActivity()));
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
                    .getIntent(Objects.requireNonNull(this.getActivity()));
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
                    .getIntent(Objects.requireNonNull(this.getActivity()));
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
                    .getIntent(Objects.requireNonNull(this.getActivity()));
            startActivityForResult(intent, PICK_IMAGE_REQUEST10);
        });

        carImage10.setOnLongClickListener(v -> {
            mDialog.show();
            yesBT.setOnClickListener(b ->
                    deleteImage(carImage10, fileReference10));
            return false;
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

    //public void uploadImage(Uri uriIn, int position, ImageButton currentIB, ImageButton nextIB) {
    private void uploadImage(Uri uriIn, ImageButton currentIB, StorageReference fileReference) {

        String fileID = UUID.randomUUID().toString();
        mStorageRef.child(fileID).putFile(uriIn).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
            Log.d("", "onSuccess: uri= " + uri.toString());
            car.addImageUrl(linearLayout1.indexOfChild(currentIB), uri.toString());
        })).addOnCompleteListener(task -> {
            Glide.with(getContext()).load(uriIn).into(currentIB);
            imageAmount++;
            if (imageAmount < 10) {
                linearLayout1.getChildAt(imageAmount).setVisibility(View.VISIBLE);
            }
        });
    }

    private void deleteImage(ImageButton iBToDelete, StorageReference refToDel) {
        //refToDel.delete();
        FirebaseStorage.getInstance().getReferenceFromUrl(car.getCarImageUrl(linearLayout1.indexOfChild(iBToDelete))).delete();
        car.deleteImageUrl(linearLayout1.indexOfChild(iBToDelete));

        iBToDelete.setVisibility(View.GONE);
        iBToDelete.setImageResource(R.drawable.plus);

        linearLayout1.removeView(iBToDelete);
        linearLayout1.addView(iBToDelete, 9);
        if(imageAmount == 10) {
            linearLayout1.getChildAt(9).setVisibility(View.VISIBLE);
        }
        imageAmount--;

        mDialog.dismiss();
    }

    private void finishForm() {
        //add the new car to every raters "non rated cars" database
        newCarRef.update("carImageUrls", car.getCarImageUrls()).addOnCompleteListener(task -> ratersReference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        DocumentReference carInD = documentSnapshot.getReference().collection("nonRatedCars").document(car.getDbID());
                        NonRatedCar newCar = new NonRatedCar(car.getDbID());
                        carInD.set(newCar);
                    }
                }));

        DocumentReference documentReference = ratersReference.document(email);
        documentReference.update("carsOwned", owner.getCarsOwned() + 1);
        owner.setCarsOwned(owner.getCarsOwned() + 1);
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(EMAIL_EXTRA, owner.getEmail());
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }

    /*private String getFileExtension(Uri uri) {
        ContentResolver cR = Objects.requireNonNull(this.getActivity()).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }*/
}
