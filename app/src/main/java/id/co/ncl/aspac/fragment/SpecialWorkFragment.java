package id.co.ncl.aspac.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ncl.aspac.R;
import id.co.ncl.aspac.activity.HomeActivity;
import id.co.ncl.aspac.adapter.WorkListAdapter;
import id.co.ncl.aspac.customClass.CustomJSONObjectRequest;
import id.co.ncl.aspac.dao.MachineDao;
import id.co.ncl.aspac.dao.ServiceDao;
import id.co.ncl.aspac.database.DatabaseManager;
import id.co.ncl.aspac.model.Machine;
import id.co.ncl.aspac.model.Service;
import id.co.ncl.aspac.model.Work;

public class SpecialWorkFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    @BindView(R.id.special_work_list_listview)
    ListView special_work_list_listview;
    @BindView(R.id.SpecialswipeLayoutWork)
    SwipeRefreshLayout SpecialswipeLayoutWork;
    //@BindView(R.id.refresh_service_btn) Button refresh_service_btn;

    private ListView lv;
    private Resources res;
    private JSONArray dataGlobalArray;
    private WorkListAdapter adapter;
    private SharedPreferences sharedPref;
    private ProgressDialog progressDialog;
    private ArrayList<Work> workData = new ArrayList<>();
    List<Long> serviceIDs = new ArrayList<>();
    List<Long> machineIDs = new ArrayList<>();

    private DatabaseManager dbManager;

    public SpecialWorkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("onCreate", "I AM CREATED!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_special_work, container, false);
        ButterKnife.bind(this, view);
        //Log.d("onCreate", "MY VIEW IS CREATED!");
        getActivity().setTitle("Daftar Pekerjaan Perbaikan");
        dbManager = DatabaseManager.getInstance();

        //create listener for the swipe behaviour
        SpecialswipeLayoutWork.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //wait with dialog
                progressDialog = new ProgressDialog(getActivity(), R.style.CustomDialog);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Mohon tunggu...");
                progressDialog.show();

                ServiceDao serDAO = new ServiceDao(dbManager);
                serDAO.deleteAllRepair();
                serDAO.closeConnection();

                //get work API
                getAllWorkData();
            }
        });
        // Scheme colors for animation
        SpecialswipeLayoutWork.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        if(savedInstanceState != null) {
            //Log.d("onCreate", "SAVEDINSTANCESTATE YEA!");
            workData = savedInstanceState.getParcelableArrayList("work");
            String dataGlobalArrayString = savedInstanceState.getString("globalArray");
            try {
                dataGlobalArray = new JSONArray(dataGlobalArrayString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            setAdapter();
        } else {
            //Log.d("onCreate", "INIT DATA!");
            checkForLocalData();
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.d("onCreate", "SAVING INSTANCES!");
        if(dataGlobalArray != null) {
            outState.putString("globalArray", dataGlobalArray.toString());
        }
        if(workData.size() > 0) {
            outState.putParcelableArrayList("work", workData);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        SpecialswipeLayoutWork.setRefreshing(false);
        Log.d("ErrorJSON", "Error Message: "+error);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            progressDialog.dismiss();
            SpecialswipeLayoutWork.setRefreshing(false);
            workData.clear();
            Log.d("JSONResponse", "JSON Response: "+response.toString(2));
            //Log.d("onCreate", "API SUCCESS!");
            //create local JSONObj
            JSONObject jsonObj = response;
            JSONArray dataArray = jsonObj.getJSONArray("data");
            dataGlobalArray = dataArray;
            Log.d("JSONAspac", "Record size: "+dataArray.length());
            //try to loop the array
            if(workData.size() == 0) {
                for (int z = 0; z < dataArray.length(); z++) {
                    JSONObject obj = dataArray.getJSONObject(z);
                    //Log.d("JSONContent", "Starting new array of values "+z);
                    //Log.d("JSONContent", obj.getString("date_service"));
                    JSONObject custJSON = obj.getJSONObject("customer");
                    //Log.d("JSONContent", custJSON.getString("branch_name"));
                    //Log.d("JSONContent", custJSON.getString("branch_status"));
                    //Log.d("JSONContent", custJSON.getString("branch_address"));
                    //Log.d("JSONContent", custJSON.getString("office_phone_number"));

                    //attempt number two. let's do this, SQLite
                    Service service = new Service();
                    //service.setDateService(obj.getString("date_service"));
                    service.setNoLPS(obj.getString("no_lps"));
                    Log.d("noLPS", obj.getString("no_lps"));
                    //service.setTypeService(obj.getInt("type_lps"));
                    service.setCBID(custJSON.getInt("customer_branch_id"));
                    //service.setCode(custJSON.getString("branch_code"));
                    //service.setInitial(custJSON.getString("branch_initial"));
                    service.setName(custJSON.getString("customer_branch_name"));
                    service.setAddress(custJSON.getString("customer_branch_address"));
                    //service.setOfficePhoneNumber("123123123");
                    service.setOfficePhoneNumber(custJSON.getString("customer_branch_office_phone_number"));
                    service.setCustomerID(custJSON.getInt("customer_id"));
                    service.setCustomerName(custJSON.getString("customer_name"));
                    service.setKerusakan(obj.getString("kerusakan"));
                    service.setRepairdID(obj.getInt("repair_service_id"));
//                    Log.d("repair_service_id", String.valueOf(obj.getInt("repair_service_id")));
//                    Log.d("customer_branch_id", String.valueOf(custJSON.getInt("customer_branch_id")));
//                    Log.d("customer_branch_name", custJSON.getString("customer_branch_name"));
//                    Log.d("customer_branch_address", custJSON.getString("customer_branch_address"));
//                    Log.d("office_phone_number", custJSON.getString("customer_branch_office_phone_number"));
//                    Log.d("customer_id", String.valueOf(custJSON.getInt("customer_id")));
//                    Log.d("customer_name", custJSON.getString("customer_name"));
//                    Log.d("kerusakan", String.valueOf(obj.getString("kerusakan")));

                    //JSONArray detailsArray = obj.getJSONArray("details");
                    //JSONArray machinesArray = obj.getJSONArray("machines");
                    JSONObject machineJSON = obj.getJSONObject("machines");
                    Machine machine = new Machine();
                    //machine.setTempServiceID(machineJSON.getInt("temporary_service_id"));
                    //Log.d("machineID", machineJSON.getString("temporary_service_id"));
                    machine.setName(machineJSON.getString("machine_name"));
                    machine.setSerialNumber(machineJSON.getString("serial_number"));
                    machine.setMachineID(machineJSON.getString("machine_id"));

                    //JSONObject detailObj = detailsArray.getJSONObject(0);
                    //JSONObject tempObj = detailObj.getJSONObject("temporary_service");

                    //service.setDateService(tempObj.getString("date_lps"));

                    //JSONObject teknisiJSON = obj.getJSONObject("teknisi");

                    //service.setTID(custJSON.getInt("teknisi_id"));
                    //service.setTusername(custJSON.getString("username"));
                    //service.setTname(teknisiJSON.getString("name"));
                    //service.setDob(teknisiJSON.getString("dob"));
                    //service.setEmail(teknisiJSON.getString("email"));
                    //service.setApiToken(teknisiJSON.getString("api_token"));
                    //service.setRoleID(teknisiJSON.getInt("role_id"));
                    //service.setBranchID(teknisiJSON.getInt("branch_id"));
                    //service.setSuperiorID(teknisiJSON.getString("superior_id"));
                    //service.setTcreatedAt(custJSON.getString("created_at"));
                    //service.setTupdatedAt(custJSON.getString("updated_at"));

                    ServiceDao serDAO = new ServiceDao(dbManager);
                    long serviceID = serDAO.insertRepair(service);
                    Log.d("serviceIDSuccess", String.valueOf(serviceID));
                    serviceIDs.add(serviceID);
                    serDAO.closeConnection();

                    machine.setServiceID(serviceIDs.get(z).intValue());

                    MachineDao macDAO = new MachineDao(dbManager);
                    long machineID = macDAO.insertRepairMachine(machine);
                    Log.d("machineIDDB", String.valueOf(machineID));
                    macDAO.closeConnection();
                    machineIDs.add(machineID);

                    //final long[] machineIDs = new long[detailsArray.length()];
//                    for (int y = 0; y < machinesArray.length(); y++) {
//                        JSONObject mesinObj = machinesArray.getJSONObject(y);
//                        //Log.d("JSONContent", mesinJSON.getString("brand"));
//                        //Log.d("JSONContent", mesinJSON.getString("model"));
//                        //Log.d("JSONContent", mesinJSON.getString("serial_number"));
//
//                        //attempt number two. let's do this, SQLite
//                        Machine machine = new Machine();
//                        machine.setTempServiceID(mesinObj.getInt("temporary_service_id"));
//                        Log.d("machineID", mesinObj.getString("temporary_service_id"));
//                        //Log.d("machineTempID", String.valueOf(detailObjt.getInt("temp_service_id")));
//                        //Log.d("detail length", String.valueOf(detailsArray.length()));
//                        machine.setName(mesinObj.getString("machine_name"));
//                        machine.setSerialNumber(mesinObj.getString("serial_number"));
//                        machine.setServiceID(serviceIDs.get(z).intValue());
//
//                        MachineDao macDAO = new MachineDao(dbManager);
//                        long machineID = macDAO.insert(machine);
//                        Log.d("machineIDDB", String.valueOf(machineID));
//                        macDAO.closeConnection();
//                        machineIDs.add(machineID);
//                    }

                    //create new work object
                    Work newWork = new Work();
                    //newWork.setWorkTitle("Pekerjaan Rutin " + (z + 1));
                    newWork.setWorkTitle(custJSON.getString("customer_branch_name"));
                    newWork.setWorkDescShort(machineJSON.getString("machine_name") + " - " + machineJSON.getString("serial_number"));
                    //newWork.setWorkStatus("Pending");
                    //newWork.setWorkDateTime(date);

                    workData.add(newWork);
                }
                setAdapter();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkForLocalData() {
        ServiceDao serDAO = new ServiceDao(dbManager);
        int result = serDAO.getCountRepair();

        if(result > 0) {
            //assume for now if data exists, dont check for API first.
            Log.d("serviceLocal", "Service has been cached!");
            workData.clear();

            //get data from sqlite
            List<Service> services = serDAO.getAllRepair();

            for(int z = 0; z < services.size(); z++) {
                Service ser = services.get(z);
                List<Machine> mach = new MachineDao(dbManager).getAllByServiceID(ser.getId());
                //create new work object
                Work newWork = new Work();
                newWork.setWorkTitle(ser.getName());
                newWork.setWorkDescShort(mach.get(0).getName() + " - " + mach.get(0).getSerialNumber());
//                newWork.setWorkStatus("Pending");
//                newWork.setWorkDateTime(date);

                workData.add(newWork);
                serviceIDs.add((long) ser.getId());
            }
            serDAO.closeConnection();
            //lastly, setadapter
            setAdapter();
        } else {
            //wait with dialog
            progressDialog = new ProgressDialog(getActivity(), R.style.CustomDialog);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Mohon tunggu...");
            progressDialog.show();
            //get work API
            getAllWorkData();
        }
    }

    private void getAllWorkData() {
        //Log.d("onCreate", "GET SOME API!");
        //set the url
        //String url = getString(R.string.list_all_post);
        String url = "http://103.26.208.118/api/getRepairServiceSchedule";

        String token = "";
        if(checkforSharedPreferences()) {
            sharedPref = getActivity().getSharedPreferences("userCred", Context.MODE_PRIVATE);
            token = "Bearer "+sharedPref.getString("token", "empty token");
        }

        //set headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", token);
        //set params
//        HashMap<String, String> params = new HashMap<>();
        JSONObject param = new JSONObject();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        CustomJSONObjectRequest jsObjRequest = new CustomJSONObjectRequest(Request.Method.GET, url, param, headers, this, this);
        //add the request to the queue
        requestQueue.add(jsObjRequest);
    }

    private void setAdapter() {
        //Log.d("onCreate", "SET ADAPTERS!");
        if(workData.size()>0){
            Log.d("setAdapter", "Setting up work list adapter");

            adapter = new WorkListAdapter(getActivity(), workData, res);
            special_work_list_listview.setAdapter(adapter);
            special_work_list_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getActivity(), "Hey! You clicked on some work!", Toast.LENGTH_SHORT).show();
                    HomeActivity act = (HomeActivity) getActivity();
                    Bundle args = new Bundle();
                    Log.d("position", String.valueOf(position));
                    Log.d("selectedPosID", String.valueOf(serviceIDs.get(position)));
                    args.putLong("service_id", serviceIDs.get(position));
                    Fragment newFrag = new SpecialCreateFragment();
                    newFrag.setArguments(args);
                    act.changeFragment(newFrag);
                }
            });
        }
        else {
            Log.d("setAdapter", "The workData array is empty!");
        }
    }

    private boolean checkforSharedPreferences() {
        boolean result = false;
        sharedPref = getActivity().getSharedPreferences("userCred", Context.MODE_PRIVATE);
        if(sharedPref.contains("token")) {
            //sharedpref exist
            result = true;
        } else {
            //sharedpref does not exist. do nothing
        }
        return result;
    }
}
