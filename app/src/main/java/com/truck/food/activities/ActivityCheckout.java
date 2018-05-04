package com.truck.food.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.truck.food.App;
import com.truck.food.R;
import com.truck.food.adapters.AdapterCheckout;
import com.truck.food.adapters.SimpleDividerItemDecoration;
import com.truck.food.db.Dish;
import com.truck.food.db.User;
import com.truck.food.model.Order;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityCheckout extends AppCompatActivity {

    private FloatingActionButton btnSend;
    private RecyclerView listCheckout;
    private AutoCompleteTextView edtFName;
    private EditText edtLName;
    private EditText edtPhone;
    private TextView txtTotal;
    private EditText edtComment;
    private EditText edtEmail;
    private Spinner spinFacility;
    private AdapterCheckout adapterCheckout;
    private List<Dish> dishes;
    private List<User> users;
    private ArrayAdapter<CharSequence> adapter;
    private String Result;
    private Order order;
    private List<String> names;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        initToolbar();
        order = new Order();
        names = new ArrayList<>();
        //it's sugar, baby
        dishes = Dish.listAll(Dish.class);
        users = User.listAll(User.class);
        findAllNames();
        initViews();
        setLastUser();
    }

    private void setLastUser() {
        if (users.size() != 0) {
            for (User user : users) {
                if(user.isLast()){
                    edtFName.setText(user.getfName());
                    edtLName.setText(user.getlName());
                    edtPhone.setText(user.getPhoneNumber());
                    if (user.getEmail() != null)
                        edtEmail.setText(user.getEmail());
                    ArrayList<String> listOfFacilities = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.facilities)));

                    if (listOfFacilities.contains(user.getFacility())) {
                        int spinnerPosition = adapter.getPosition(user.getFacility());
                        spinFacility.setSelection(spinnerPosition);
                    }
                    break;
                }
            }
        }

    }

    private void findAllNames() {
        if (users.size() != 0) {
            for (User user : users) {
                names.add(user.getfName());
            }
        }
    }

    private void initViews() {
        listCheckout = (RecyclerView) findViewById(R.id.listCheckout);
        edtFName = (AutoCompleteTextView) findViewById(R.id.edtFName);
        edtLName = (EditText) findViewById(R.id.edtLName);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtComment = (EditText) findViewById(R.id.edtComment);
        edtComment.requestFocus();

        spinFacility = (Spinner) findViewById(R.id.spinner);

        btnSend = (FloatingActionButton) findViewById(R.id.btnSend);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.facilities, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        spinFacility.setAdapter(adapter);

        if (names != null) {
            ArrayAdapter namesAdapter = new ArrayAdapter(this, R.layout.spinner_item, names.toArray());

            edtFName.setAdapter(namesAdapter);
            edtFName.setThreshold(1);
            edtFName.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    edtFName.showDropDown();
                    return false;
                }
            });

            edtFName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("HasilProses", users.get(position).getEmail());
                    edtLName.setText(users.get(position).getlName());
                    edtPhone.setText(users.get(position).getPhoneNumber());
                    if (users.get(position).getEmail() != null)
                        edtEmail.setText(users.get(position).getEmail());
                    ArrayList<String> listOfFacilities = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.facilities)));

                    if (listOfFacilities.contains(users.get(position).getFacility())) {
                        int spinnerPosition = adapter.getPosition(users.get(position).getFacility());
                        spinFacility.setSelection(spinnerPosition);
                    }
                }
            });
        }

        // event listener to handle send button when pressed

        GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        listCheckout.setLayoutManager(mLayoutManager);
        listCheckout.setItemAnimator(new DefaultItemAnimator());

        adapterCheckout = new AdapterCheckout(Dish.listAll(Dish.class), getString(R.string.currency));

        listCheckout.setVisibility(View.VISIBLE);
        listCheckout.setAdapter(adapterCheckout);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        listCheckout.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));

        btnSend.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                ProgressDialog dialog;
                dialog = ProgressDialog.show(ActivityCheckout.this, "",
                        getString(R.string.sending_alert), true);

                getOrder();
                final View content = findViewById(R.id.coordinator);

                if (order.getFacility().equalsIgnoreCase("") ||
                        order.getfName().equalsIgnoreCase("") ||
                        order.getlName().equalsIgnoreCase("") ||
                        order.getPhone().equalsIgnoreCase("")) {
                    dialog.dismiss();

                    Snackbar snackbar = Snackbar.make(content, R.string.form_alert, Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                    snackbar.show();
                } else if ((dishes.size() == 0)) {
                    dialog.dismiss();
                    Snackbar snackbar = Snackbar.make(content, R.string.order_alert, Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                    snackbar.show();
                } else {
                    dialog.dismiss();
                    App.getApi().sendData(getMap()).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            saveUser(order);
                            Result = response.body().toString();
                            resultAlert(Result);
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Snackbar snackbar = Snackbar.make(content, R.string.failed_alert, Snackbar.LENGTH_SHORT);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                            snackbar.show();
                        }
                    });
                }
            }
        });
        txtTotal.setText("Итого: " + getTotal() + " " + getString(R.string.currency));
    }

    private void saveUser(Order order) {
        String eMail = "";
        if (order.getEmail() != null)
            eMail = order.getEmail();
        User user = new User(order.getfName(), order.getlName(), order.getPhone(), order.getFacility(), eMail, true);

        changeUserLast();
        user.setLast(true);
        user.save();
    }

    private void changeUserLast() {
        for (User user : users) {
            user.setLast(false);
            user.save();
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.about_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent iMyOrder = new Intent(ActivityCheckout.this, ActivityCart.class);
                startActivity(iMyOrder);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                return true;

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private Map<String, String> getMap() {
        String date = new SimpleDateFormat("yyyy.MM.dd  hh:mm:ss").format(Calendar.getInstance().getTime());
        Map<String, String> map = new HashMap<String, String>();
        map.put("facility", order.getFacility());
        map.put("fname", order.getfName());
        map.put("lname", order.getlName());
        map.put("date_n_time", date);
        map.put("phone", order.getPhone());
        map.put("order_list", order.getOrderList());
        map.put("comment", order.getComment());
        map.put("email", order.getEmail());
        return map;
    }

    private void getOrder() {
        order.setFacility(spinFacility.getSelectedItem().toString());
        order.setfName(edtFName.getText().toString());
        order.setlName(edtLName.getText().toString());
        order.setPhone(edtPhone.getText().toString());
        order.setOrderList(txtTotal.getText().toString());
        order.setComment(edtComment.getText().toString());
        order.setEmail(edtEmail.getText().toString());
    }

    // method to show toast message
    public void resultAlert(String HasilProses) {
        if (HasilProses.trim().equalsIgnoreCase("OK")) {
            Toast.makeText(ActivityCheckout.this, R.string.ok_alert, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ActivityCheckout.this, ActivityConfirmMessage.class);
            startActivity(i);
            overridePendingTransition(R.anim.open_next, R.anim.close_next);
            finish();
        } else if (HasilProses.trim().equalsIgnoreCase("Failed")) {
            Toast.makeText(ActivityCheckout.this, R.string.failed_alert, Toast.LENGTH_SHORT).show();
        } else {
            Log.d("HasilProses", HasilProses);
        }
    }

    int getTotal() {
        int t = 0;
        // store data to arraylist variables
        for (int i = 0; i < dishes.size(); i++) {
            Dish dish = dishes.get(i);
            t += Double.parseDouble(dish.getPrice()) * dish.getCount();
        }
        return t;
    }

    // when back button pressed close database and back to previous page
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(ActivityCheckout.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapterCheckout.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }
}
