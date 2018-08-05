package com.quickblox.sample.core.utils.imagepick.fragment;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.quickblox.sample.core.R;
import com.quickblox.sample.core.utils.ImageUtils;
import com.quickblox.sample.core.utils.SystemPermissionHelper;

public class ImageSourcePickDialogFragment extends DialogFragment {

    private static final int POSITION_GALLERY = 0;
    private static final int POSITION_CAMERA = 1;

    private static SystemPermissionHelper systemPermissionHelper;

    private OnImageSourcePickedListener onImageSourcePickedListener;

    public ImageSourcePickDialogFragment() {
        systemPermissionHelper = new SystemPermissionHelper(this);
    }

    public static void show(FragmentManager fm, OnImageSourcePickedListener onImageSourcePickedListener) {
        ImageSourcePickDialogFragment fragment = new ImageSourcePickDialogFragment();
        fragment.setOnImageSourcePickedListener(onImageSourcePickedListener);
        fragment.show(fm, ImageSourcePickDialogFragment.class.getSimpleName());
    }


    public static Fragment show1(FragmentManager fm, OnImageSourcePickedListener onImageSourcePickedListener) {
        ImageSourcePickDialogFragment fragment = new ImageSourcePickDialogFragment();
        fragment.setOnImageSourcePickedListener(onImageSourcePickedListener);
        fragment.show(fm, ImageSourcePickDialogFragment.class.getSimpleName());
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dlg_choose_image_from);
        builder.setItems(R.array.dlg_image_pick, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (!systemPermissionHelper.isSaveImagePermissionGranted()) {
//                    systemPermissionHelper.requestPermissionsForSaveFileImage();
//                    return;
//                }
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, which);
                    whichValue = which;
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, which);
                } else {
                    showCustomDialog(which);
                }
            }
        });

        return builder.create();
    }

    private int whichValue = -1;
    private void showCustomDialog(int which){
        switch (which) {
            case POSITION_GALLERY:
                onImageSourcePickedListener.onImageSourcePicked(ImageSource.GALLERY);
                break;
            case POSITION_CAMERA:
                onImageSourcePickedListener.onImageSourcePicked(ImageSource.CAMERA);
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((permissions.length > 0 && permissions[0].equalsIgnoreCase(permission.WRITE_EXTERNAL_STORAGE)) && (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                || (permissions.length > 0 && permissions[0].equalsIgnoreCase(permission.CAMERA)) && (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            showCustomDialog(whichValue);
        }
        else onRequestPermissionsResultError(requestCode, permissions, grantResults);
    }

    public void onRequestPermissionsResultError(int requestCode, String[] permissions, int[] grantResults) {
    }

    public void setOnImageSourcePickedListener(OnImageSourcePickedListener onImageSourcePickedListener) {
        this.onImageSourcePickedListener = onImageSourcePickedListener;
    }

    public interface OnImageSourcePickedListener {

        void onImageSourcePicked(ImageSource source);
    }

    public enum ImageSource {
        GALLERY,
        CAMERA
    }

    public static class LoggableActivityImageSourcePickedListener implements OnImageSourcePickedListener {

        private Activity activity;
        private Fragment fragment;

        public LoggableActivityImageSourcePickedListener(Activity activity) {
            this.activity = activity;
        }

        public LoggableActivityImageSourcePickedListener(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void onImageSourcePicked(ImageSource source) {
            switch (source) {
                case GALLERY:
                    if (fragment != null) {
                        ImageUtils.startImagePicker(fragment);
                    } else {
                        ImageUtils.startImagePicker(activity);
                    }
                    break;
                case CAMERA:
                    if (fragment != null) {
                        ImageUtils.startCameraForResult(fragment);
                    } else {
                        ImageUtils.startCameraForResult(activity);
                    }
                    break;
            }
        }
    }
}