package com.bawaaba.rninja4.rookie.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static android.widget.ListPopupWindow.WRAP_CONTENT;

/**
 * Created by rninja4 on 3/4/18.
 */

public class MyDialogFragment extends DialogFragment {

    public InterfaceCommunicator interfaceCommunicator;
    private static final int REQUEST_WRITE_PERMISSION = 2;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    final CharSequence[] items = {"Take Photo",
            "Choose from Library",
            "Cancel"};
    private RatingBar ratingBar;
    private AppCompatTextView choose_file;
    private ImageView pic_file;
    private EditText review_text;
    private Button Submit;
    private final String LOG_TAG = MyDialogFragment.class.getSimpleName();

    private static final int CAMERA_REQUEST_PROOF = 100;
    private static final int CAMERA_REQUEST_KEY_CODE = 110;
    private static int GALLERY_REQUEST_PROOF = 101;
    private static int GALLERY_REQUEST_KEY_CODE = 111;
    private Bitmap bitmap;
    private String imageRating = "no";


    public MyDialogFragment() {
    }

    public void setFragmentContainerId(@IdRes int containerId) {
        Bundle b = getArguments();
        if (b == null) {
            b = new Bundle();
        }
        b.putInt("id", containerId);
        if (getArguments() == null)
            setArguments(b);
    }

    // onCreate --> (onCreateDialog) --> onCreateView --> onActivityCreated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");

        View dialogView = inflater.inflate(R.layout.add_review, container, false);

        choose_file = (AppCompatTextView) dialogView.findViewById(R.id.choose_file);
        ratingBar = (RatingBar) dialogView.findViewById(R.id.rating);
        review_text = (EditText) dialogView.findViewById(R.id.review);
        pic_file = (ImageView) dialogView.findViewById(R.id.pick_file);
        Submit = (Button) dialogView.findViewById(R.id.btnreview);
        pic_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pickImageSingle();
                int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if (result == PackageManager.PERMISSION_GRANTED) {
                    if (result1 == PackageManager.PERMISSION_GRANTED) {
                        takePhoto();
                    } else {
                        alert();
                    }
                } else {
                    buildAlertMessageNoCamera();
                }
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                float rate = ratingBar.getRating();
                System.out.println("ProfileView.onRatingChanged " + rate);
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String review = review_text.getText().toString().trim();
                float rate = ratingBar.getRating();
                String rating = String.valueOf((int) rate);

                if (review != null && review.length() < 10) {
                    review_text.setBackgroundResource(R.drawable.red_alert);
                    Toast.makeText(getActivity(), "Minimum 10 characters required for review", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!review.isEmpty() && !rating.isEmpty()) {
                    interfaceCommunicator.sendRequestCode(rating, rate, review, imageRating);
                    //  setReviewApi(rating, rate, review, dialog);
                } else {
                    Toast.makeText(getActivity(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        return dialogView;
    }

    // If shown as dialog, set the width of the dialog window
    // onCreateView --> onActivityCreated -->  onViewStateRestored --> onStart --> onResume
    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "onResume");
        if (getShowsDialog()) {
            // Set the width of the dialog to the width of the screen in portrait mode
            DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
            int dialogWidth = Math.min(metrics.widthPixels, metrics.heightPixels);
            getDialog().getWindow().setLayout(dialogWidth, WRAP_CONTENT);
        }
    }

    private void showToast(String buttonName) {
        Toast.makeText(getActivity(), "Clicked on \"" + buttonName + "\"", Toast.LENGTH_SHORT).show();
    }

    // If dialog is cancelled: onCancel --> onDismiss
    @Override
    public void onCancel(DialogInterface dialog) {
        Log.v(LOG_TAG, "onCancel");
    }

    // If dialog is cancelled: onCancel --> onDismiss
    // If dialog is dismissed: onDismiss
    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.v(LOG_TAG, "onDismiss");
    }

    private void takePhoto() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(Constants.ProfileFragment.ADD_PHOTO);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals(Constants.ProfileFragment.TAKE_PHOTO)) {

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //    buildAlertMessageNoCamera();
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_PROOF);
                        //Glide.with(getActivity()).load(ObjectFactory.getInstance().getLoginManager(getContext()).getUserPhoto()).fitCenter().into(mProfilePic);

                    } else if (items[item].equals(Constants.ProfileFragment.FROM_LIBRARY)) {

                        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryintent, GALLERY_REQUEST_PROOF);

                    } else if (items[item].equals(Constants.ProfileFragment.CANCEL)) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alert() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_PERMISSION);
    }

    private void buildAlertMessageNoCamera() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_PROOF) {
            final Bitmap photo;
            try {
                photo = (Bitmap) data.getExtras().get("data");
                bitmap = photo;
                imageRating = getStringImage(bitmap);
                File photopath = null;
                photopath = saveToInternalStorage(photo);
                choose_file.setText("1 item selected");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == GALLERY_REQUEST_PROOF && (data != null)) {
            try {
                Uri selected_image = data.getData();
                String[] filepath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selected_image, filepath, null, null, null);
                cursor.moveToFirst();
                int columIntex = cursor.getColumnIndex(filepath[0]);
                String filePath = cursor.getString(columIntex);
                cursor.close();
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                bitmap = yourSelectedImage;
                imageRating = getStringImage(bitmap);
                choose_file.setText("1 item selected");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private File saveToInternalStorage(Bitmap bitmap) throws IOException {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }
        }
        ContextWrapper cw = new ContextWrapper(getActivity());
        File directory = cw.getDir("patientphoto", Context.MODE_PRIVATE);
        Random rand = new Random();
        int num = rand.nextInt(5000) + 1;
        File path = new File(directory, "" + num + ".jpg");
        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }
        return path;
    }

    public interface InterfaceCommunicator {
        //  setReviewApi(rating, rate, review, dialog);
        void sendRequestCode(String rating, float rate, String review, String imageRating);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (activity instanceof AppCompatActivity && getArguments() != null && getArguments().getInt("id", 0) != 0) {
                interfaceCommunicator=(InterfaceCommunicator)((AppCompatActivity)activity).getSupportFragmentManager().findFragmentById(getArguments().getInt("id"));
            } else {
                interfaceCommunicator = (InterfaceCommunicator) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException((getTargetFragment() != null ? getTargetFragment().toString() : "Fragment") + " must implement ConfirmDeletePopupFragment.DialogListener");
        }
    }
}
