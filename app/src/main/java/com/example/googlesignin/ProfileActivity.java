package com.example.googlesignin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    public static final String GOOGLE_ACCOUNT = "google_account";
    private static final String TAG = "GoogleSignIn";
  //  private Button signOut;
    private GoogleSignInClient googleSignInClient;
    TextView userName,userEmail;
    ImageView profileImage;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);

        profileImage = findViewById(R.id.profileImage);
    //    signOut = (Button) findViewById(R.id.sign_out);
        findViewById(R.id.sign_out).setOnClickListener(this);

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.web_client_id))
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInClient GoogleSignInClient = GoogleSignIn.getClient (this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);



   /*    gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
               .build();
*/
    /*     gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.web_client_id))
                .build();
*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            userName.setText(account.getDisplayName());
            userEmail.setText(account.getEmail());
            TextView token =  (TextView) findViewById(R.id.idToken);
            token.setText(account.getIdToken());
            String logtoken = account.getIdToken();
            Log.d("DD", logtoken);

        //   HttpClient httpClient = new DefaultHttpClient();
        //   HttpPost httpPost = new HttpPost("http://localhost/my-oauth2/token.php");
       //     HttpClient httpClient = new DefaultHttpClient();
        //    HttpPost httpPost = new HttpPost("https://1c980a4b0c8a473da3c06a33fc74854b.vfs.cloud9.us-east-1.amazonaws.com/auth");

            try{
                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);

/*
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("idToken", logtoken));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                final String responseBody = EntityUtils.toString(response.getEntity());
                Log.i(TAG, "Signed in as: " + responseBody);
            } catch (ClientProtocolException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
            } catch (IOException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);  */
            }
            catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
            }

        }else{
           // gotoMainActivity();
        }
    }

   // GoogleSignInAccount acct = getIntent().getParcelableExtra(GOOGLE_ACCOUNT);
 //   HttpClient httpClient = new DefaultHttpClient();
 //   HttpPost httpPost = new HttpPost("https://localhost/google-api/public/redirect.php");
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                      //  String idToken = account.getIdToken();
                        //  String idTokens = account.getIdToken();

                    /*
                     Write to the logic send this id token to server using HTTPS
                     */
              /*          List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("idToken", idToken));
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        HttpResponse response = httpClient.execute(httpPost);
                        int statusCode = response.getStatusLine().getStatusCode();
                        final String responseBody = EntityUtils.toString(response.getEntity());
                        Log.i(TAG, "Signed in as: " + responseBody);
                    } catch (ClientProtocolException e) {
                        Log.e(TAG, "Error sending ID token to backend.", e);
                    } catch (IOException e) {
                        Log.e(TAG, "Error sending ID token to backend.", e);  */
                    } catch (ApiException e) {

                    }

                    break;
            }
    }

    private void updateUI(boolean isLogin){
        if(isLogin){

        //    Prof_Section.setVisibility(View.VISIBLE);
        //    SignIn.setVisibility(View.GONE);
        }
        else{
          //  Prof_Section.setVisibility(View.GONE);
          //  SignIn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // ...
            case R.id.sign_out:
                signOut();
                break;
            // ...
        }
    }

    private void signOut() {

        googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Intent intent=new Intent(ProfileActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}