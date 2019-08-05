package com.example.googlesignin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import com.example.googlesignin.Global;
import com.example.googlesignin.pojo.userDetail;

import com.bumptech.glide.Glide;
import com.example.googlesignin.R;
import com.example.googlesignin.databinding.ActivityHomeBinding;
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
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class HomeActivity extends AppCompatActivity  implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{
private ActivityHomeBinding mBinding;
private userDetail mUserDetails;
    public static final String GOOGLE_ACCOUNT = "google_account";
    private static final String TAG = "GoogleSignIn";
    //  private Button signOut;
    private GoogleSignInClient googleSignInClient;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

//    @Override
//    protected void onResume() {
//        if(mUserDetails!=null)
//        {
//           mUserDetails.cl
//            getData();
//        }
//
//
//        super.onResume();
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(HomeActivity.this,R.layout.activity_home);
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

        mUserDetails=new userDetail();

    }

    private void getData() {
        MyTaskPostRequest task = new MyTaskPostRequest();
        task.execute();
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
           mUserDetails.setUsername(account.getDisplayName());
            mUserDetails.setUserEmail(account.getEmail());
            mUserDetails.setIdToken(account.getIdToken());
            mUserDetails.setClientId("641695863884-4qrd5s5sdom1719nppm03nn2r5q6mats.apps.googleusercontent.com");
            String logtoken = account.getIdToken();
            Log.d("DD", logtoken);
            getData();

            //   HttpClient httpClient = new DefaultHttpClient();
            //   HttpPost httpPost = new HttpPost("http://localhost/my-oauth2/token.php");
            //     HttpClient httpClient = new DefaultHttpClient();
            //    HttpPost httpPost = new HttpPost("https://1c980a4b0c8a473da3c06a33fc74854b.vfs.cloud9.us-east-1.amazonaws.com/auth");

//            try{
//                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);

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
//            }
//            catch (NullPointerException e){
//                Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
//            }

        }else{
            // gotoMainActivity();
        }
    }

    class MyTaskPostRequest extends AsyncTask<Void,Void,String>
    {

        private ArrayList<NameValuePair> nameValuePairs;

        @Override
        protected void onPreExecute() {

            mBinding.progressBar.setVisibility(View.VISIBLE);

            nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("idToken",mUserDetails.getIdToken()));
                nameValuePairs.add(new BasicNameValuePair("clientId", mUserDetails.getClientId()));




//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            try
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Global.BASE_URL+"addtoken.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                String response = EntityUtils.toString(httpEntity);
                return response;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            mBinding.progressBar.setVisibility(View.GONE);

            Toast.makeText(HomeActivity.this,""+s,Toast.LENGTH_LONG).show();
        }

    }


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



    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
