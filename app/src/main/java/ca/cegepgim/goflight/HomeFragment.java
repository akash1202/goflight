package ca.cegepgim.goflight;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.cegepgim.goflight.adapter.TotalFlightsAdapter;
import ca.cegepgim.goflight.model.Airport;
import ca.cegepgim.goflight.model.Flight;
import timber.log.Timber;


public class HomeFragment extends Fragment {

    SearchView simpleSearchView;
    TextView activeCasesValue,recoveredCasesValue,totalDeathsvalue,totalCasesValue,stateName;
    Flight fight;
    ArrayList<Flight> flights=new ArrayList<>();
    CharSequence query;
    ListView listView;
    Airport airport;
    TotalFlightsAdapter listViewAdapter;
    SearchView editSearch;
    ProgressDialog process;
    String url="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home,container,false);
       // query = simpleSearchView.getQuery(); // get the query string currently in the text field
        url=getString(R.string.url_airports);
       try {
           retrieveFlights(url,view);
       }catch (Exception e){
           Toast.makeText(getActivity(), "Parse Exception", Toast.LENGTH_SHORT).show();
       }
       return view;
    }

    /*public void changeUI(Flight state){
        if(state!=null){
            totalCasesValue.setText(state.getTotal()+"");
            recoveredCasesValue.setText(state.getRecovered()+"");
            totalDeathsvalue.setText(state.getDeaths()+"");
            activeCasesValue.setText(state.getActive()+"");
            stateName.setText(state.getName()+"("+state.getCode()+")");
        }
    }*/

    public void retrieveFlights(String url, View view) {
        process = new ProgressDialog(getActivity());
        process.setMessage("Wait...");
        process.setCancelable(false);
        process.show();
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {  //response we are getting from request
                process.cancel();
                Log.d("response:", response);
                Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Timber.d("response:", jsonObject.toString());
                   /* JSONArray summaryList = jsonObject.getJSONArray("summary");

                    for(int i=0;i<summaryList.length();i++){
                        JSONObject summary=summaryList.getJSONObject(i);
                        int active=summary.getInt("active_cases");
                        int total=summary.getInt("cumulative_cases");
                        int death=summary.getInt("cumulative_deaths");
                        int recovered=summary.getInt("cumulative_recovered");
                        String date=summary.getString("date");
                        String province="";//getProviceFUllName(summary.getString("province"));
                        String code="";//getProviceCode(province);*/
                       // covidStates.add(new Flight());
                   // }

                    Log.d("response", response);
                  /*  state=getState("QC");
                    changeUI(getState("QC"));
                  if(state!=null){
                       recoveredCasesValue.setText(state.getRecovered());
                       totalDeathsvalue.setText(state.getDeaths());
                       activeCasesValue.setText(state.getActive());
                   }*/

                } catch (JSONException ex) {
                    process.cancel();
                    Toast.makeText(getActivity(), "Json parsing Exception!" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                process.cancel();
                Toast.makeText(getActivity(), "Something gone Wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(sr);
    }
/*

    public String getCountryFUllName(String stateName){
        ArrayList<State> stateList=new ArrayList<>();
        stateList.add(new State("Alberta","AB"));
        stateList.add(new State("British Columbia","BC"));
        stateList.add(new State("Manitoba","MB"));
        stateList.add(new State("New Brunswick","NB"));
        stateList.add(new State("Newfoundland and Labrador","NL"));
        stateList.add(new State("Northwest Territories","NT"));
        stateList.add(new State("Nova Scotia","NS"));
        stateList.add(new State("Nunavut","NU"));
        stateList.add(new State("Ontario","ON"));
        stateList.add(new State("Prince Edward Island","PEI"));
        stateList.add(new State("Quebec","QC"));
        stateList.add(new State("Saskatchewan","SK"));
        stateList.add(new State("Yukon","YT"));
        stateList.add(new State("Repatriated Travellers","RT"));
        for(int i=0;i<stateList.size();i++){
            if(stateList.get(i).getName().equalsIgnoreCase(stateName) || stateList.get(i).getCode().equalsIgnoreCase(stateName)) {
                return stateList.get(i).getName();
            }
        }
        return "";
    }

    public String getProviceCode(String stateName){
        ArrayList<State> stateList=new ArrayList<>();
        stateList.add(new State("Alberta","AB"));
        stateList.add(new State("British Columbia","BC"));
        stateList.add(new State("Manitoba","MB"));
        stateList.add(new State("New Brunswick","NB"));
        stateList.add(new State("Newfoundland and Labrador","NL"));
        stateList.add(new State("Northwest Territories","NT"));
        stateList.add(new State("Nova Scotia","NS"));
        stateList.add(new State("Nunavut","NU"));
        stateList.add(new State("Ontario","ON"));
        stateList.add(new State("Prince Edward Island","PEI"));
        stateList.add(new State("Quebec","QC"));
        stateList.add(new State("Saskatchewan","SK"));
        stateList.add(new State("Yukon","YT"));
        stateList.add(new State("Repatriated Travellers","RT"));
        for(int i=0;i<stateList.size();i++){
            if(stateList.get(i).getName().equalsIgnoreCase(stateName) || stateList.get(i).getCode().equalsIgnoreCase(stateName)) {
                return stateList.get(i).getCode();
            }
        }
        return "";
    }

    public State getState(String stateName){
        for(int i=0;i<covidStates.size();i++){
         if(covidStates.get(i).getName().equalsIgnoreCase(stateName)  || covidStates.get(i).getCode().equalsIgnoreCase(stateName)){
             return covidStates.get(i);
         }
        }
        return  null;
    }

  *//*  @Override
    public boolean onQueryTextSubmit(String query) {
        if(!TextUtils.isEmpty(query)){
           state=getState(query);
           Log.d("searched",state.toString());
            if(state!=null){
               recoveredCasesValue.setText(state.getRecovered());
               totalDeathsvalue.setText(state.getDeaths());
               activeCasesValue.setText(state.getActive());
           }
        }
        return false;
    }*//*



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }*/
}
