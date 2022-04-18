package com.smiledocuser.act;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smiledocuser.R;
import com.smiledocuser.databinding.ActivityEditProfileBinding;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.retrofit.Constant;
import com.smiledocuser.utils.DataManager;
import com.smiledocuser.utils.GPSTracker;
import com.smiledocuser.utils.RealPathUtil;
import com.smiledocuser.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {
    public String TAG = "EditProfile";
    ActivityEditProfileBinding binding;
    APIInterface apiInterface;
    String filePaths = "";
    String location = "";
    String latitude = "", longitude = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);

        binding.dob.setOnClickListener(v -> {
            Calendar myCalendar = Calendar.getInstance();

            int selectedYear = myCalendar.get(Calendar.YEAR);
            int selectedMonth = myCalendar.get(Calendar.MONTH);
            int selectedDayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> binding.dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
            datePickerDialog.show();
        });

        binding.genderName.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenu().add("Male");
            popup.getMenu().add("Female");
            popup.setOnMenuItemClickListener(item -> {
                binding.genderName.setText(item.getTitle());
                return true;
            });
            popup.show();
        });


        binding.imgBack.setOnClickListener(v -> finish());

        binding.image.setOnClickListener(v -> showPictureDialog());

        binding.signUp.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.etFirstName.getText())) {
                Toast.makeText(EditProfile.this, getString(R.string.please_enter_first_name), Toast.LENGTH_SHORT).show();

            } else  if (TextUtils.isEmpty(binding.etLastName.getText())) {
                Toast.makeText(EditProfile.this, getString(R.string.please_enter_last_name), Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.etEmail.getText())) {
                Toast.makeText(EditProfile.this, getString(R.string.please_enter_email_address), Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString()).matches()) {
                Toast.makeText(EditProfile.this, getString(R.string.invalid_email_address), Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.etNumber.getText())) {
                Toast.makeText(EditProfile.this, getString(R.string.please_enter_mobile_number), Toast.LENGTH_SHORT).show();
            }  else {
                uploadMultiFile();
            }
        });

        getCurrentLocation();

        getProfileApi();
    }



    private void getProfileApi() {
        DataManager.getInstance().showProgressMessage(EditProfile.this,getString(R.string.please_wait));
        Call<ResponseBody> call = apiInterface.getProfile(DataManager.getInstance().getUserData(EditProfile.this).result.id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body() != null ? response.body().string() : "";
                        JSONObject object = new JSONObject(responseData);
                        if (object.optString("status").equals("1")) {
                            JSONObject jsonObject = object.optJSONObject("result");

                            binding.etFirstName.setText(jsonObject.optString("first_name"));
                            binding.etLastName.setText(jsonObject.optString("last_name"));
                            binding.etEmail.setText(jsonObject.optString("email"));
                            binding.etNumber.setText(jsonObject.optString("mobile"));
                            binding.dob.setText(jsonObject.optString("dob"));
                            binding.genderName.setText(jsonObject.optString("gender"));
                            binding.zipCode.setText(jsonObject.optString("zipcode"));
                            binding.address.setText(jsonObject.optString("address"));
                            latitude=jsonObject.optString("lat");
                            longitude=jsonObject.optString("lon");


//                            location = jsonObject.optString("location");
                            Glide.with(EditProfile.this).load(jsonObject.optString("image"))
                                    .apply(RequestOptions.circleCropTransform())
                                    .placeholder(R.drawable.ic_user_circle_24)
                                    .error(R.drawable.ic_user_circle_24)
                                    .into(binding.image);

                        } else {
                            Toast.makeText(EditProfile.this, object.optString("result"), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    logException(e);
                    Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
             DataManager.getInstance().hideProgressMessage();
            Toast.makeText(EditProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void   uploadMultiFile() {
        DataManager.getInstance().showProgressMessage(EditProfile.this,getString(R.string.please_wait));
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("user_id", DataManager.getInstance().getUserData(EditProfile.this).result.id);
        builder.addFormDataPart("first_name", binding.etFirstName.getText().toString());
        builder.addFormDataPart("last_name", binding.etLastName.getText().toString());
        builder.addFormDataPart("email", binding.etEmail.getText().toString());
        builder.addFormDataPart("mobile", binding.etNumber.getText().toString());
        builder.addFormDataPart("address", binding.address.getText().toString());
        builder.addFormDataPart("gender", binding.genderName.getText().toString());
        builder.addFormDataPart("dob", binding.dob.getText().toString());
        builder.addFormDataPart("zipcode", binding.zipCode.getText().toString());
        builder.addFormDataPart("lat", latitude);
        builder.addFormDataPart("lon", longitude);
        builder.addFormDataPart("register_id",DataManager.getInstance().getUserData(EditProfile.this).result.registerId);

        if (!filePaths.equals("")) {
            File file = new File(filePaths);
            builder.addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }

        MultipartBody requestBody = builder.build();
        Call<ResponseBody> call = apiInterface.updateProfile(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.optString("status").equals("1")) {
                            finish();
                        } else {
                            Toast.makeText(EditProfile.this, object.optString("result"), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    logException(e);
                    Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                DataManager.getInstance().hideProgressMessage();
                Toast.makeText(EditProfile.this,t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(EditProfile.this);
        pictureDialog.setTitle(getString(R.string.select_action));
        String[] pictureDialogItems = {
                getString(R.string.select_photo_from_gallery),
                getString(R.string.capture_photo_from_camera)};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    private void takePhotoFromCamera() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(cameraIntent, 0);

    }

    public Bitmap BITMAP_RESIZER(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    try {

                        if (data != null) {

                            Bundle extras = data.getExtras();
                            Bitmap bitmapNew = (Bitmap) extras.get("data");
                            Bitmap imageBitmap = BITMAP_RESIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());

                            Glide.with(EditProfile.this).load(imageBitmap).apply(RequestOptions.circleCropTransform()).into(binding.image);
                            Uri tempUri = getImageUri(EditProfile.this, imageBitmap);

                            String imag = RealPathUtil.getRealPath(EditProfile.this, tempUri);
                            filePaths = imag;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImage = data.getData();

                        Bitmap bitmapNew = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        Bitmap bitmap = BITMAP_RESIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                        Glide.with(EditProfile.this).load(selectedImage).apply(RequestOptions.circleCropTransform()).into(binding.image);
                        Uri tempUri = getImageUri(EditProfile.this, bitmap);
                        String imag = RealPathUtil.getRealPath(EditProfile.this, tempUri);
                        filePaths = imag;

                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title_"+System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    private void getCurrentLocation() {
        GPSTracker track = new GPSTracker(this);
        if (track.canGetLocation()) {
            latitude = String.valueOf(track.getLatitude());
            longitude = String.valueOf(track.getLongitude());

        } else {
            track.showSettingsAlert();
        }
    }

    public void logException(Exception e) {
        Log.e(TAG, e.getMessage());
    }

}
