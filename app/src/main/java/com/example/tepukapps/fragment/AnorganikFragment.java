package com.example.tepukapps.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tepukapps.Constant;
import com.example.tepukapps.PupukAdapter;
import com.example.tepukapps.R;
import com.example.tepukapps.dialog.CartDialog;
import com.example.tepukapps.dialog.SessionDialog;
import com.example.tepukapps.model.Pupuk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AnorganikFragment extends Fragment {
    private RecyclerView recyclerView;
    private View view;
    private ArrayList<Pupuk> arrayList;
    private PupukAdapter adapter;
    private SharedPreferences userPref;


    public AnorganikFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_anorganik, container, false);
        init();
        return view;
    }
    private void init() {
        recyclerView = view.findViewById(R.id.rvAnorganik);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        getPupuk();
    }

    private void getPupuk() {
        arrayList = new ArrayList<>();
        userPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        StringRequest request = new StringRequest(Request.Method.GET, Constant.ANORGANIK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("success")){
                        JSONArray array = new JSONArray(object.getString("pupuk"));
                        for(int i = 0 ;i<array.length();i++){
                            JSONObject pupukObject = array.getJSONObject(i);

                            Pupuk pupuk = new Pupuk();
                            pupuk.setId(pupukObject.getInt("id"));
                            pupuk.setName(pupukObject.getString("nama_pupuk"));
                            pupuk.setCategory(pupukObject.getString("jenis_pupuk"));
                            pupuk.setDescription(pupukObject.getString("deskripsi_pupuk"));
                            pupuk.setComposition(pupukObject.getString("komposisi_pupuk"));
                            pupuk.setPrice(pupukObject.getInt("harga_pupuk"));
                            pupuk.setPhoto(pupukObject.getString("foto_pupuk"));
                            arrayList.add(pupuk);
                        }
                        adapter = new PupukAdapter(getContext(),arrayList);
                        recyclerView.setAdapter(adapter);
                    }else {
                        openDialog();
                    }
                } catch (JSONException e) {
                    openDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                openDialog();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                Log.d("ojan", String.valueOf(map));
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
    private void openDialog() {
        SessionDialog sessionDialog = new SessionDialog(getContext());
        sessionDialog.show(getActivity().getSupportFragmentManager(),"sessionDialog");
    }
}
