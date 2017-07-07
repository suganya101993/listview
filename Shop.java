package mindmade.akshayaproducts.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import mindmade.akshayaproducts.adapter.Shop_list_Adapter;
import mindmade.akshayaproducts.R;
import mindmade.akshayaproducts.util.NetworkConnectivity;

public class Shop extends Fragment {
    ListView list;
    View v;
    SharedPreferences prefs;
    NetworkConnectivity network;
    ArrayList shoplist = new ArrayList();
    private Shop_list_Adapter adapter;
    TextView nodata;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       v = inflater.inflate(R.layout.shop, container, false);

        network=new NetworkConnectivity(getActivity());
        list=(ListView)v.findViewById(R.id.shop_list);
        nodata=(TextView)v.findViewById(R.id.no_data);

        prefs = getActivity().getSharedPreferences("MY_PREFS1", Context.MODE_PRIVATE);

        //dealer id
        final String dealerid = prefs.getString("Dealerid", null);

        //Routeid
        final String Routeid =prefs.getString("Routeid",null);
        //user id
        final String id=prefs.getString("userid",null);

        String url=(getString(R.string.base_url)+"site/mshoplocations?user_id="+id+"&dealer_id="+dealerid+"&route_id="+Routeid);
        Shopurl(url);

        //+"&route_id="+Routeid


        return v;
    }



    private void Shopurl(String url) {
        if (network.CheckInternetConnection()) {
            Log.d("success", "URL" + url);
            final ProgressDialog dialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
            dialog.setMessage("Please wait..");
            dialog.setCancelable(false);
            dialog.show();
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    Log.d("shopresponse", "" + response);
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray array = response.getJSONArray("stores");
                            for (int i = 0; i < array.length(); i++) {

                                Log.d("shoplist", "array" + array);
                                JSONObject object = array.getJSONObject(i);
                                HashMap<String, String> hashMap = new HashMap<String, String>();

                                hashMap.put("store_id", String.valueOf(object.getString("store_id")));
                                hashMap.put("store_name", object.getString("store_name"));
                                hashMap.put("address", object.getString("address"));

                                Log.d("shoplist", String.valueOf(object.getString("store_id")));     Log.d("shoplist", object.getString("store_name"));     Log.d("shoplist", object.getString("address"));

                                shoplist.add(hashMap);
                                Log.d("listview", "" + shoplist);

                            }
                            adapter = new Shop_list_Adapter(getActivity(), shoplist);
                            list.setAdapter(adapter);
                        }
                        else if (response.getString("status").equals("failure"))
                        {
                            nodata.setVisibility(View.VISIBLE);
                            nodata.setText(response.getString("message"));
                          //  Toast.makeText(getActivity().getApplicationContext(), "failure  ", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error:shop " + error.getMessage());

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                        String toast = (getResources().getString(R.string.timeerror));
                        Log.e("Error", "" + getString(R.string.timeerror));
                     //   Toast.makeText(getActivity().getApplicationContext(), "Error " + toast, Toast.LENGTH_LONG).show();

                    } else if (error instanceof AuthFailureError) {

                        String auth = (getResources().getString(R.string.autherror));
                        Log.e("Error", "" + getString(R.string.autherror));
                     //   Toast.makeText(getActivity().getApplicationContext(), "Error " + auth, Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        String server = (getResources().getString(R.string.servererr));
                        Log.e("Error", "" + getString(R.string.servererr));
                    //    Toast.makeText(getActivity(), "Error " + server, Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {

                        String net = (getResources().getString(R.string.network));
                        Log.e("Error", "" + getString(R.string.network));
                   //     Toast.makeText(getActivity().getApplicationContext(), "Error " + net, Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {

                        String parser = (getResources().getString(R.string.parseerr));
                        Log.e("Error", "" + getString(R.string.parseerr));
                    //    Toast.makeText(getActivity().getApplicationContext(), "Error " + parser, Toast.LENGTH_LONG).show();

                    }
                }
            });
            int socketTimeout = 40000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            queue.add(request);
        }
        else{
            Log.d("success","No Nework connection");
            Toast.makeText(getActivity(),"No Nework connection",Toast.LENGTH_SHORT).show();
        }
    }




}
