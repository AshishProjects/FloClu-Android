package com.example.googlesignin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.googlesignin.ProfileActivity;
import com.example.googlesignin.R;
import com.example.googlesignin.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity implements TextWatcher {
private ActivityLoginBinding mBinding;
    private static final String TAG = "GoogleSignIn";
    private GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(LoginActivity.this,R.layout.activity_login);
        mBinding.emailLogin.addTextChangedListener(this);
        mBinding.singinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        mBinding.googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        if (s.toString().length() >= 1) {
            mBinding.singinButton.setVisibility(View.INVISIBLE);
            mBinding.activeButton.setVisibility(View.VISIBLE);
        } else {

            mBinding.singinButton.setVisibility(View.VISIBLE);
            mBinding.activeButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)

            switch (requestCode) {
                case 101:
                    //  HttpClient httpClient = new DefaultHttpClient();
                    //  HttpPost httpPost = new HttpPost("https://localhost/google-api/public/redirect.php");
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        String idToken = account.getIdToken();
                        onLoggedIn(account);
                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                        if (acct != null) {
                            String personName = acct.getDisplayName();
                            // String personGivenName = acct.getGivenName();
                            // String personFamilyName = acct.getFamilyName();
                            String personEmail = acct.getEmail();

                        }

                   /*    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("idToken", idToken));
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        HttpResponse response = httpClient.execute(httpPost);
                        int statusCode = response.getStatusLine().getStatusCode();
                        final String responseBody = EntityUtils.toString(response.getEntity());
                        Log.i(TAG, "Signed in as: " + responseBody);
                    } catch (ClientProtocolException e) {
                        Log.e(TAG, "Error sending ID token to backend.", e);
                    } catch (IOException e) {
                        Log.e(TAG, "Error sending ID token to backend.", e);   */
                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }
    }
    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(ProfileActivity.GOOGLE_ACCOUNT, googleSignInAccount);

        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            onLoggedIn(alreadyloggedAccount);
        } else {
            Log.d(TAG, "Not logged in");
        }
    }
}
