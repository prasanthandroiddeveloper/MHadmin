package com.example.mhadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.cycle);
        getIpaddress();
    }

    private void getIpaddress() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://myhitha.com/admin_panel/settings/ip_settings", response -> {

            recyclerView.setVisibility(VISIBLE);

            List<DataAdapter> list = new ArrayList<>();
            try {

                JSONArray jarr = new JSONArray(response);
                for (int i = 0; i < jarr.length(); i++) {
                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jarr.getJSONObject(i);

                    dataAdapter.setId(jobj.getString("ip_id"));
                    dataAdapter.setName(jobj.getString("ip_name"));
                    dataAdapter.setAddrs(jobj.getString("ip_address"));
                    list.add(dataAdapter);

                }

                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(new employee_names_adapter(list));

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(stringRequest);
    }

    public class employee_names_adapter  extends RecyclerView.Adapter<employee_names_adapter.ViewHolder>  {

        private Context context;
        private List<DataAdapter> listadapter;
        String sId,sName, sAdrs;


        employee_names_adapter(List<DataAdapter> list) {
            super();
            this.listadapter = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull employee_names_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);

            vh.id.setText(DDataAdapter.getId());
            vh.ipname.setText(DDataAdapter.getName());
            vh.ipadrs.setText(DDataAdapter.getAddrs());

        }

        @Override
        public int getItemCount() { return listadapter.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView edit,save;
            EditText id,ipname,ipadrs;
            String upIpadrs,upIpname;


            ViewHolder(View iv) {
                super(iv);

                context = iv.getContext();
                id = iv.findViewById(R.id.Tvid);
                ipname = iv.findViewById(R.id.Tvipnm);
                ipadrs = iv.findViewById(R.id.Tvipadrs);
                id.setEnabled(false);
                ipname.setEnabled(false);
                ipadrs.setEnabled(false);
                edit =iv.findViewById(R.id.btnEdit);
                save =iv.findViewById(R.id.btnSave);

                edit.setOnClickListener(view -> {
                    sId =listadapter.get(getAdapterPosition()).getId();
                    sName=listadapter.get(getAdapterPosition()).getName();
                    sAdrs =listadapter.get(getAdapterPosition()).getAddrs();
                    Toast.makeText(context, "Edit"+"\n"+ sId +"\n"+sName+"\n"+ sAdrs, Toast.LENGTH_SHORT).show();
                    ipname.setEnabled(true);
                    ipadrs.setEnabled(true);
                    ipname.requestFocus();

                });

                save.setOnClickListener(view -> {

                    upIpadrs=ipadrs.getText().toString();
                    upIpname=ipname.getText().toString();

                    sId =listadapter.get(getAdapterPosition()).getId();
                    sName=listadapter.get(getAdapterPosition()).getName();
                    sAdrs =listadapter.get(getAdapterPosition()).getAddrs();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://myhitha.com/admin_panel/settings/update_ip_address/"+sId, response -> {
                        Toast.makeText(context, response+"Updated Successfully", Toast.LENGTH_SHORT).show();
                        getIpaddress();
                    }, error -> {
                        Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                    }){

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params =new HashMap<>();

                            params.put("ip_id",sId);
                            params.put("ip_name",upIpname);
                            params.put("ip_address", upIpadrs);
                            Log.i("p2", String.valueOf(params));
                            return params;
                        }

                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                });

            }
        }
    }
}
