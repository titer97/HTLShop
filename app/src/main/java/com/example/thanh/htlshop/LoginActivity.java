package com.example.thanh.htlshop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanh.model.TaiKhoan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String FileName = "UsernameAndPassword";
    private String usernameHienTai;
    private String passwordHienTai;
    private Button btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameView = findViewById(R.id.username);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin(1);
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(1);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        btnDangKy = findViewById(R.id.btnDangKy);
        btnDangKy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin(2);
            }
        });
    }

    private void attemptLogin(int type) {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (type == 1) {
                //dang nhap
                showProgress(true);
                mAuthTask = new UserLoginTask(username, password);
                mAuthTask.execute((Void) null);
            } else if (type == 2) {
                //dang ky
                showProgress(true);
                DangKyTask task = new DangKyTask(username, password);
                task.execute((Void) null);
            }
        }
    }

/*    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }*/

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private int mMaKh;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            ArrayList<TaiKhoan> listTaiKhoan = new ArrayList<>();
            try {
                URL url = new URL("http://tripletstore.somee.com/api/taikhoan");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                JSONArray jsonArray = new JSONArray(builder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    TaiKhoan tk = new TaiKhoan();
                    tk.setMakh(jsonObject.getInt("makh"));
                    tk.setUsername(jsonObject.getString("username"));
                    tk.setPassword(jsonObject.getString("password"));
                    listTaiKhoan.add(tk);
                }
                br.close();
                isr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (TaiKhoan tk2 : listTaiKhoan) {
                String username = tk2.getUsername();
                String password = tk2.getPassword();
                mMaKh = tk2.getMakh();
                if (username.equals(mUsername) && password.equals(mPassword)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                moMainActivity(mUsername, mPassword, mMaKh);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void moMainActivity(String username, String password, int makh) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("username", username);
        returnIntent.putExtra("password", password);
        returnIntent.putExtra("makh", makh);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public class DangKyTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        DangKyTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            try {
                //lay danh sach tai khoan
                ArrayList<TaiKhoan> listTaiKhoan = new ArrayList<>();
                URL url = new URL("http://tripletstore.somee.com/api/taikhoan");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                JSONArray jsonArray = new JSONArray(builder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    TaiKhoan tk = new TaiKhoan();
                    tk.setMakh(jsonObject.getInt("makh"));
                    tk.setUsername(jsonObject.getString("username"));
                    tk.setPassword(jsonObject.getString("password"));
                    //them tai khoan vao danh sach
                    listTaiKhoan.add(tk);
                }
                //kiem tra trong danh sach neu ton tai username thi khong cho dang ky
                for (TaiKhoan tk2 : listTaiKhoan) {
                    String username = tk2.getUsername();
                    if (username.equals(mUsername)) {
                        return false;
                    } else {
                        //dang ky tai khoan
                        String thamSo = "username=" + mUsername + "&password=" + mPassword;
                        URL url2 = new URL("http://tripletstore.somee.com/api/taikhoan/?" + thamSo);
                        HttpURLConnection httpURLConnection2 = (HttpURLConnection) url2.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        InputStreamReader isr2 = new InputStreamReader(httpURLConnection2.getInputStream(), "UTF-8");
                        BufferedReader br2 = new BufferedReader(isr2);
                        StringBuilder builder2 = new StringBuilder();
                        String line2 = null;
                        while ((line2 = br2.readLine()) != null) {
                            builder2.append(line);
                        }
                        //neu ket qua tra ve True thi dang ky tai khoan thanh cong
                        boolean ketqua = builder2.toString().contains("true");
                        br.close();
                        isr.close();
                        return ketqua;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Toast.makeText(getApplication(), "Đăng ký tài khoản thành công!", Toast.LENGTH_LONG).show();
                mUsernameView.setText(mUsername);
                mPasswordView.setText("");
                mPasswordView.requestFocus();
            } else {
                Toast.makeText(getApplication(), "Đăng ký tài khoản thất bại!", Toast.LENGTH_LONG).show();
                mUsernameView.setText("");
                mPasswordView.setText("");
                mUsernameView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}

