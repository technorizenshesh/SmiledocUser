package com.smiledocuser.act;

import static com.smiledocuser.retrofit.Constant.emailPattern;

import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.smiledocuser.MainActivity;
import com.smiledocuser.R;
import com.smiledocuser.databinding.ActivityLoginBinding;
import com.smiledocuser.model.VerifyOtpModal;
import com.smiledocuser.retrofit.APIInterface;
import com.smiledocuser.retrofit.ApiClient;
import com.smiledocuser.retrofit.Constant;
import com.smiledocuser.utils.DataManager;
import com.smiledocuser.utils.GPSTracker;
import com.smiledocuser.utils.NetworkAvailablity;
import com.smiledocuser.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAct extends AppCompatActivity {
    public String TAG = "LoginAct";
    ActivityLoginBinding binding;
    APIInterface apiInterface;
    String latitude = "",longitude = "";
    static final int GOOGLE_SIGN_IN_REQUEST_CODE = 1234;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        initViews();
    }

    private void initViews() {

    /*    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            try {
                refreshedToken = instanceIdResult.getToken();
                Log.e("Token===", refreshedToken);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/


        binding.loginBtn.setOnClickListener(v -> { validation(); });


        binding.btnForgotPass.setOnClickListener(v -> { startActivity(new Intent(LoginAct.this,ForgotPasswordAct.class)); });


        FirebaseApp.initializeApp(LoginAct.this);
        callbackManager = CallbackManager.Factory.create();



        //FacebookSdk.setApplicationId(getString(R.string.facebook_key));

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        binding.btnFacebook.setOnClickListener(v -> {
            if(NetworkAvailablity.checkNetworkStatus(LoginAct.this)) fbLogin();
            else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        });


        binding.btnGplus.setOnClickListener(v -> {
            if(NetworkAvailablity.checkNetworkStatus(LoginAct.this)) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
              /*  Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(50);
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);*/

            }
            else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        });



        binding.regBtn1.setOnClickListener(v -> {
             startActivity(new Intent(LoginAct.this,RegisterAct.class));
        });
    }




    private void validation() {
        if (binding.etEmail.getText().toString().equals("")) {
            Toast.makeText(LoginAct.this, getString(R.string.please_enter_email_address), Toast.LENGTH_SHORT).show();
        } else if (!binding.etEmail.getText().toString().matches(emailPattern)) {
            Toast.makeText(LoginAct.this, getString(R.string.invalid_email_address), Toast.LENGTH_SHORT).show();
        }else if (binding.edtPassword.getText().toString().equals("")) {
            Toast.makeText(LoginAct.this, getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
        }
        else {
            if(NetworkAvailablity.checkNetworkStatus(LoginAct.this)) userLogin();
            else Toast.makeText(LoginAct.this,getString(R.string.network_failure), Toast.LENGTH_LONG).show();
        }
    }

    private void userLogin() {
        DataManager.getInstance().showProgressMessage(LoginAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("email", binding.etEmail.getText().toString());
        map.put("password", binding.edtPassword.getText().toString());
        map.put("register_id", "textggg");
        map.put("lat", latitude);
        map.put("lon", longitude);
        map.put("type", Constant.USER);
        Log.e(TAG, "Login Request " + map);
        Call<ResponseBody> loginCall = apiInterface.login(map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body() != null ? response.body().string() : "";
                        //JSONObject object = new JSONObject(responseData);
                        VerifyOtpModal userModel = new Gson().fromJson(responseData, VerifyOtpModal.class);

                        if (userModel.status.equals("1")) {
                            SessionManager.writeString(LoginAct.this, Constant.USER_INFO,responseData) ;
                             startActivity(new Intent(LoginAct.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        } else {
                            Toast.makeText(LoginAct.this,userModel.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    logException(e);
                    Toast.makeText(LoginAct.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    public void logException(Exception e) {
        Log.e(TAG, e.getMessage());
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

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation();
    }


    private void fbLogin() {
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(LoginAct.this);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.e("kjsgdfkjdgsf","onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("kjsgdfkjdgsf","error = " + error.getMessage());
            }

        });

     /*   Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        binding.btnFacebook.startAnimation(anim);*/

    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();

                            if(user !=  null) {
                                Log.e("Google Plus",String.valueOf(user.getPhotoUrl()));
                                if(NetworkAvailablity.checkNetworkStatus(LoginAct.this)) socialLogin(user.getDisplayName(), user.getEmail(), String.valueOf(user.getPhotoUrl()), user.getUid());
                                else Toast.makeText(LoginAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show(); }

                            // updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            // Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            String profilePhoto = "https://graph.facebook.com/" + token.getUserId() + "/picture?height=500";

                            Log.e("kjsgdfkjdgsf","profilePhoto = " + profilePhoto);
                            if(NetworkAvailablity.checkNetworkStatus(LoginAct.this)) socialLogin(user.getDisplayName(), user.getEmail(), profilePhoto, user.getUid());
                            else Toast.makeText(LoginAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();


                            // updateUI(user);

                        } else {
                            Log.e("facebook=======",task.getException().getLocalizedMessage());
                            Toast.makeText(LoginAct.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            // updateUI(null);

                        }

                        // ...

                    }
                });
    }

    private void socialLogin(String name, String email, String profilePhoto, String uid) {
        DataManager.getInstance().showProgressMessage(LoginAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("first_name", name);
        map.put("last_Name", name);
        map.put("email", email);
        map.put("mobile","" );
        map.put("social_id", uid);
       // map.put("language",SessionManager.readString(LoginAct.this,Constant.LANGUAGE,""));
          map.put("lat",latitude );
          map.put("lon",longitude );
        map.put("register_id", "fgfdffdf");
        map.put("image", profilePhoto);
        map.put("type", Constant.USER);

        Log.e(TAG, "SOCIAL LOGIN REQUEST" + map);
        Call<ResponseBody> loginCall = apiInterface.socialLogin( map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                        if (response.isSuccessful()) {
                            String responseData = response.body() != null ? response.body().string() : "";
                            //JSONObject object = new JSONObject(responseData);
                            VerifyOtpModal userModel = new Gson().fromJson(responseData, VerifyOtpModal.class);

                            if (userModel.status.equals("1")) {
                                SessionManager.writeString(LoginAct.this, Constant.USER_INFO, responseData);
                                startActivity(new Intent(LoginAct.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            } else {
                                Toast.makeText(LoginAct.this, userModel.message, Toast.LENGTH_SHORT).show();
                            }
                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            /* Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);*/
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                e.getStatusCode();
                // ...
            }

        }

    }

}
