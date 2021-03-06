package com.example.thanh.htlshop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.thanh.adapter.AdapterSanPham;
import com.example.thanh.model.DanhMuc;
import com.example.thanh.model.SanPham;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FragmentDanhMucSanPham extends Fragment {

    private ListView lvDanhMuc;
    private ArrayAdapter<DanhMuc> adapterDanhMuc;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_danh_muc_san_pham, container, false);
        anKeyBoard();
        addControls(view);
        addEvents();
        return view;
    }


    private void addControls(View view) {
        lvDanhMuc = view.findViewById(R.id.lvDanhMuc);
        adapterDanhMuc = new ArrayAdapter<DanhMuc>(getActivity(), android.R.layout.simple_list_item_1);
        lvDanhMuc.setAdapter(adapterDanhMuc);
        progressBar = view.findViewById(R.id.load_data_progress);
        showProgress(true);
        DanhSachDanhMucTask task = new DanhSachDanhMucTask();
        task.execute();
    }

    private void addEvents() {
        lvDanhMuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle x = new Bundle();
                x.putInt("loaisp", position + 1);

                FragmetSanPhamTheoDanhMuc fragmetSanPhamTheoDanhMuc = new FragmetSanPhamTheoDanhMuc();
                FragmentManager fragmentManager = getFragmentManager();
                fragmetSanPhamTheoDanhMuc.setArguments(x);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_context, fragmetSanPhamTheoDanhMuc);
                transaction.commit();
            }
        });
    }

    class DanhSachDanhMucTask extends AsyncTask<Void, Void, ArrayList<DanhMuc>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<DanhMuc> danhMucs) {
            super.onPostExecute(danhMucs);
            showProgress(false);
            adapterDanhMuc.clear();
            adapterDanhMuc.addAll(danhMucs);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<DanhMuc> doInBackground(Void... voids) {
            ArrayList<DanhMuc> dsDanhMuc = new ArrayList<>();
            try {
                URL url = new URL("http://www.tripletstore.somee.com/api/loaisp");
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
                    DanhMuc dm = new DanhMuc();

                    dm.setMaLoai(jsonObject.getInt("maloai"));
                    dm.setTenLoai(jsonObject.getString("tenloai"));

                    dsDanhMuc.add(dm);
                }
                br.close();
                isr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dsDanhMuc;
        }
    }

    private void anKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = 0;
            if (getActivity() != null && isAdded()) {
                shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            }

            lvDanhMuc.setVisibility(show ? View.GONE : View.VISIBLE);
            lvDanhMuc.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    lvDanhMuc.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            lvDanhMuc.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}