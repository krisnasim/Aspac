package id.co.ncl.aspac.fragment;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ncl.aspac.R;
import id.co.ncl.aspac.activity.HomeActivity;
import id.co.ncl.aspac.adapter.WorkListAdapter;
import id.co.ncl.aspac.customClass.CustomJSONObjectRequest;
import id.co.ncl.aspac.database.AspacSQLite;
import id.co.ncl.aspac.database.DatabaseManager;
import id.co.ncl.aspac.database.MachineDao;
import id.co.ncl.aspac.database.ServiceDao;
import id.co.ncl.aspac.database.SparepartDao;
import id.co.ncl.aspac.model.Machine;
import id.co.ncl.aspac.model.Sparepart;
import id.co.ncl.aspac.model.Service;
import id.co.ncl.aspac.model.Work;

public class WorkFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    @BindView(R.id.work_list_listview) ListView work_list_listview;

    private ListView lv;
    private Resources res;
    private JSONArray dataGlobalArray;
    private WorkListAdapter adapter;
    private SharedPreferences sharedPref;
    private ProgressDialog progressDialog;
    private ArrayList<Work> workData = new ArrayList<>();
    //final List<Service> services = new ArrayList<Service>();
    //final List<Machine> machines = new ArrayList<Machine>();
    //final List<Sparepart> spareparts = new ArrayList<Sparepart>();
    List<Long> serviceIDs = new ArrayList<>();
    List<Long> machineIDs = new ArrayList<>();

    private DatabaseManager dbManager;
    private AspacSQLite myDb;

    public WorkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate", "I AM CREATED!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work, container, false);
        ButterKnife.bind(this, view);
        Log.d("onCreate", "MY VIEW IS CREATED!");
        getActivity().setTitle("Daftar Pekerjaan Rutin");
        myDb = new AspacSQLite(getContext());
        DatabaseManager.initializeInstance(myDb);
        dbManager = DatabaseManager.getInstance();

        if(savedInstanceState != null) {
            Log.d("onCreate", "SAVEDINSTANCESTATE YEA!");
            workData = savedInstanceState.getParcelableArrayList("work");
            String dataGlobalArrayString = savedInstanceState.getString("globalArray");
            try {
                dataGlobalArray = new JSONArray(dataGlobalArrayString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            setAdapter();
        } else {
            Log.d("onCreate", "INIT DATA!");
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

//        //create date object
//        Calendar myCalendar = Calendar.getInstance();
//        myCalendar.set(2017, 8, 31);
//        Date myDate = myCalendar.getTime();
//        //myDate = myCalendar.getTime();
//
//        for(int x = 0; x < 7; x++) {
//            //create new work object
//            Work newWork = new Work();
//            newWork.setWorkTitle("Benerin Mesin nomor "+(x+1));
//            newWork.setWorkDescShort("Benerin mesin dari klien. Mesin dapat komplain tidak jalan");
//            newWork.setWorkStatus("Pending");
//            newWork.setWorkDateTime(myDate);
//
//            workData.add(newWork);
//        }
//
//        setAdapter();
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
        Log.d("onCreate", "SAVING INSTANCES!");
        outState.putString("globalArray", dataGlobalArray.toString());
        outState.putParcelableArrayList("work", workData);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("ErrorJSON", "Error Message: "+error);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            progressDialog.dismiss();
            //Log.d("JSONResponse", "JSON Response: "+response.toString(2));
            Log.d("onCreate", "API SUCCESS!");
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
                    JSONObject custJSON = obj.getJSONObject("customer_branch");
                    //Log.d("JSONContent", custJSON.getString("branch_name"));
                    //Log.d("JSONContent", custJSON.getString("branch_status"));
                    //Log.d("JSONContent", custJSON.getString("branch_address"));
                    //Log.d("JSONContent", custJSON.getString("office_phone_number"));


                    //attempt number two. let's do this, SQLite
                    Service service = new Service();
                    service.setDateService(obj.getString("date_service"));
                    service.setTypeService(obj.getInt("type_service"));
                    service.setCBID(custJSON.getInt("id"));
                    service.setCode(custJSON.getString("branch_code"));
                    service.setInitial(custJSON.getString("branch_initial"));
                    service.setName(custJSON.getString("branch_name"));
                    service.setStatus(custJSON.getString("branch_status"));
                    //service.setPIC(custJSON.getString("pic"));
                    service.setPIC("asd");
                    //service.setPICPhoneNumber(custJSON.getString("pic_phone_number"));
                    service.setPICPhoneNumber("asd");
                    //service.setPICEmail(custJSON.getString("pic_email"));
                    service.setPICEmail("asd");
                    service.setKW(custJSON.getInt("kw"));
                    service.setSLJJ(custJSON.getString("sljj"));
                    service.setAddress(custJSON.getString("branch_address"));
                    service.setRegencyID(custJSON.getInt("regency_id"));
                    service.setProvinceID(custJSON.getInt("province_id"));
                    //service.setPostCode(custJSON.getString("post_code"));
                    service.setPostCode("asd");
                    service.setOfficePhoneNumber(custJSON.getString("office_phone_number"));
                    //service.setFax(custJSON.getString("fax"));
                    service.setFax("asd");
                    service.setCustomerID(custJSON.getInt("customer_id"));
                    service.setCoordinatorID(custJSON.getInt("koordinator_id"));
                    service.setTeknisiID(custJSON.getInt("teknisi_id"));
                    service.setSalesID(custJSON.getInt("sales_id"));
                    service.setUsername(custJSON.getString("username"));
                    service.setPassword(custJSON.getString("password"));
                    //service.setRememberToken(custJSON.getString("remember_token"));
                    service.setRememberToken("asd");
                    //service.setCreatedAt(custJSON.getString("created_at"));
                    service.setCreatedAt("asd");
                    //service.setUpdatedAt(custJSON.getString("updated_at"));
                    service.setUpdatedAt("asd");

                    JSONObject teknisiJSON = obj.getJSONObject("teknisi");

                    service.setTID(teknisiJSON.getInt("id"));
                    service.setTusername(teknisiJSON.getString("username"));
                    service.setTname(teknisiJSON.getString("name"));
                    service.setDob(teknisiJSON.getString("dob"));
                    service.setEmail(teknisiJSON.getString("email"));
                    service.setApiToken(teknisiJSON.getString("api_token"));
                    service.setRoleID(teknisiJSON.getInt("role_id"));
                    service.setBranchID(teknisiJSON.getInt("branch_id"));
                    service.setSuperiorID(teknisiJSON.getString("superior_id"));
                    service.setTcreatedAt(teknisiJSON.getString("created_at"));
                    service.setTupdatedAt(teknisiJSON.getString("updated_at"));

                    ServiceDao serDAO = new ServiceDao(dbManager);
                    long serviceID = serDAO.insert(service);
                    serviceIDs.add(serviceID);
                    //serDAO.closeConnection();


//                    final Service service = new Service();
//                    service.setDateService(obj.getString("date_service"));
//                    //Customer Branch
//                    service.setCBID(custJSON.getInt("id"));
//                    service.setCode(custJSON.getString("branch_code"));
//                    service.setInitial(custJSON.getString("branch_initial"));
//                    service.setName(custJSON.getString("branch_name"));
//                    service.setStatus(custJSON.getString("branch_status"));
//                    service.setPIC(custJSON.getString("pic"));
//                    service.setPICPhoneNumber(custJSON.getString("pic_phone_number"));
//                    service.setPICEmail(custJSON.getString("pic_email"));
//                    service.setKW(custJSON.getInt("kw"));
//                    service.setSLJJ(Integer.parseInt(custJSON.getString("sljj")));
//                    service.setAddress(custJSON.getString("branch_address"));
//                    service.setRegencyID(custJSON.getString("regency_id"));
//                    service.setProvinceID(custJSON.getInt("province_id"));
//                    //service.setPostCode(Integer.parseInt(custJSON.getString("post_code")));
//                    service.setPostCode(11111);
//                    service.setOfficePhoneNumber(custJSON.getString("office_phone_number"));
//                    //service.setFax(custJSON.getString("fax"));
//                    service.setFax("123-123123");
//                    service.setCustomerID(custJSON.getInt("customer_id"));
//                    service.setCoordinatorID(custJSON.getInt("koordinator_id"));
//                    service.setTeknisiID(custJSON.getInt("teknisi_id"));
//                    service.setSalesID(custJSON.getInt("sales_id"));
//                    service.setUsername(custJSON.getString("username"));
//                    service.setPassword(custJSON.getString("password"));
//                    service.setRememberToken(custJSON.getString("remember_token"));
//                    service.setCreatedAt(custJSON.getString("created_at"));
//                    service.setUpdatedAt(custJSON.getString("updated_at"));
//
//                    JSONObject teknisiJSON = obj.getJSONObject("teknisi");
//                    //Log.d("JSONContent", teknisiJSON.getString("username"));
//                    //Log.d("JSONContent", teknisiJSON.getString("email"));
//                    //Log.d("JSONContent", teknisiJSON.getString("name"));
//
//                    //Technician
//                    service.setTID(teknisiJSON.getInt("id"));
//                    service.setTusername(teknisiJSON.getString("username"));
//                    service.setTname(teknisiJSON.getString("name"));
//                    service.setDob(teknisiJSON.getString("dob"));
//                    service.setEmail(teknisiJSON.getString("email"));
//                    service.setApiToken(teknisiJSON.getString("api_token"));
//                    service.setRoleID(teknisiJSON.getString("role_id"));
//                    service.setBranchID(teknisiJSON.getString("branch_id"));
//                    service.setSuperiorID(teknisiJSON.getString("superior_id"));
//                    service.setTcreatedAt(teknisiJSON.getString("created_at"));
//                    service.setTupdatedAt(teknisiJSON.getString("updated_at"));
//
//                    services.add(service);
//
//                    if(machines.size() > 0) {
//                        machines.clear();
//                    }

                    JSONArray mesinsArray = obj.getJSONArray("machines");
                    //final long[] machineIDs = new long[mesinsArray.length()];
                    for (int y = 0; y < mesinsArray.length(); y++) {
                        JSONObject mesinJSON = mesinsArray.getJSONObject(y);
                        //Log.d("JSONContent", mesinJSON.getString("brand"));
                        //Log.d("JSONContent", mesinJSON.getString("model"));
                        //Log.d("JSONContent", mesinJSON.getString("serial_number"));

                        //attempt number two. let's do this, SQLite
                        Machine machine = new Machine();
                        machine.setMachineID(mesinJSON.getString("id"));
                        machine.setBrand(mesinJSON.getString("brand"));
                        machine.setModel(mesinJSON.getString("model"));
                        machine.setSerialNumber(mesinJSON.getString("serial_number"));
                        machine.setSalesNumber(mesinJSON.getString("sales_number"));
                        machine.setServiceID(serviceIDs.get(z).intValue());

                        MachineDao macDAO = new MachineDao(dbManager);
                        long machineID = macDAO.insert(machine);
                        machineIDs.add(machineID);

                        for (int x = 0; x < 5; x++) {
                            Sparepart sparepart = new Sparepart();
                            sparepart.setCode("123");
                            sparepart.setName("Sparepart X");
                            sparepart.setMachineID(machineIDs.get(y).intValue());

                            SparepartDao spaDAO = new SparepartDao(dbManager);
                            long sparepartID = spaDAO.insert(sparepart);
                        }

//                        final Machine machine = new Machine();
//                        machine.setMachineID(mesinJSON.getString("id"));
//                        machine.setBrand(mesinJSON.getString("brand"));
//                        machine.setModel(mesinJSON.getString("model"));
//                        machine.setSerialNumber(mesinJSON.getString("serial_number"));
//                        machine.setSalesNumber(mesinJSON.getString("sales_number"));
//
//                        machines.add(machine);
//
//                        if(spareparts.size() > 0) {
//                            spareparts.clear();
//                        }
//
//                        for (int x = 0; x < 5; x++) {
//                            final Spare_Part sparepart = new Spare_Part();
//                            sparepart.setName("3RP1340000001");
//                            sparepart.setDescription("UI RUBBER KEYPAD R");
//
//                            spareparts.add(sparepart);
//                        }
                    }

                    //create date formatting
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = formatter.parse(obj.getString("date_service"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //create new work object
                    Work newWork = new Work();
                    newWork.setWorkTitle("Pekerjaan Rutin " + (z + 1));
                    newWork.setWorkDescShort(custJSON.getString("branch_name"));
                    newWork.setWorkStatus("Pending");
                    newWork.setWorkDateTime(date);

                    workData.add(newWork);
                }
                setAdapter();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //WOHOO ADDING NEW ROOM PERSISTENCE LIBRARY
//        try {
//            Thread t = new Thread(new Runnable() {
//                public void run() {
//                    AspacDatabase testDB = ((Aspac) getActivity().getApplication()).getDatabase();
//                    int counter = 0;
//                    for(Service ser : services) {
//                        long serviceID = testDB.serviceDao().insert(ser);
//                        //create date formatting
//                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                        Date date = null;
//                        try {
//                            date = formatter.parse(ser.getDateService());
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        for (Machine mac: machines) {
//                            mac.setServiceID((int) serviceID);
//                            long machineID = testDB.machineDao().insert(mac);
//                            for (Spare_Part spa: spareparts) {
//                                spa.setMachineID((int) machineID);
//                                testDB.sparepartDao().insert(spa);
//                            }
//                        }
//
//                        //create new work object
//                        Work newWork = new Work();
//                        newWork.setWorkTitle("Pekerjaan Rutin " + (counter + 1));
//                        newWork.setWorkDescShort(ser.getName());
//                        newWork.setWorkStatus("Pending");
//                        newWork.setWorkDateTime(date);
//                        Log.d("loopCount", "counting loop "+(counter+1));
//
//                        workData.add(newWork);
//                        counter += 1;
//                    }
//                }
//            }, "Thread Hope");
//            //start thread
//            t.start();
//            //t.join();
//            progressDialog.dismiss();
//            setAdapter();
//        } catch (NumberFormatException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    private void getAllWorkData() {
        Log.d("onCreate", "GET SOME API!");
        //set the url
        //String url = getString(R.string.list_all_post);
        String url = "http://aspac.noti-technologies.com/api/getserviceschedule";

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
        Log.d("onCreate", "SET ADAPTERS!");
        if(workData.size()>0){
            Log.d("setAdapter", "Setting up work list adapter");

            adapter = new WorkListAdapter(getActivity(), workData, res);
            work_list_listview.setAdapter(adapter);
            work_list_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getActivity(), "Hey! You clicked on some work!", Toast.LENGTH_SHORT).show();
                    HomeActivity act = (HomeActivity) getActivity();
                    Bundle args = new Bundle();
                    try {
                        args.putString("data", dataGlobalArray.getJSONObject(position).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Fragment newFrag = new CreateFragment();
                    newFrag.setArguments(args);
                    act.changeFragment(newFrag);
                }
            });
        }
        else {
            Log.d("setAdapter", "The workData array is empty!");
        }

        //WOHOO ADDING NEW ROOM PERSISTENCE LIBRARY
//        try {
//            Thread t = new Thread(new Runnable() {
//                public void run() {
//                    AspacDatabase testDB = ((Aspac) getActivity().getApplication()).getDatabase();
//                    List<Service> services = testDB.serviceDao().getAll();
//                    for(Service service : services) {
//                        Log.d("RoomDemo", service.getName());
//                        Log.d("RoomDemo", service.getStatus());
//                        Log.d("RoomDemo", service.getAddress());
//                        Log.d("RoomDemo", service.getOfficePhoneNumber());
//                    }
//                }
//            }, "Thread Demo");
//            t.start();
//        } catch (NumberFormatException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
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
