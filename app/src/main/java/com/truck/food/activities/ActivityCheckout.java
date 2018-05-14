package com.truck.food.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.truck.food.App;
import com.truck.food.Constant;
import com.truck.food.SugarHelper;
import com.truck.food.R;
import com.truck.food.adapters.AdapterCheckout;
import com.truck.food.adapters.SimpleDividerItemDecoration;
import com.truck.food.db.DishDB;
import com.truck.food.db.UserDB;
import com.truck.food.model.Dish;
import com.truck.food.model.User;
import com.truck.food.model.dishes_for_av.DataForAv;
import com.truck.food.model.dishes_for_av.DishForAv;
import com.truck.food.model.dishes_for_av.PDMenuForAv;
import com.truck.food.model.menu.Data;
import com.truck.food.model.menu.PDMenu;

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
    private List<DishDB> dishDBs;
    private List<UserDB> userDBs;
    private ArrayAdapter<CharSequence> adapter;
    private String Result;

    private User user;

    private List<String> names;
    private TextView notifCount;
    private ImageView notifImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        initToolbar();
        user = new User();
        names = new ArrayList<>();
        //it's sugar, baby
        dishDBs = DishDB.listAll(DishDB.class);
        userDBs = UserDB.listAll(UserDB.class);
        findAllNames();
        initViews();
        setLastUser();
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
                switch (menuItem.getItemId()) {
                    case R.id.delete_users:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCheckout.this);
                        builder.setTitle(R.string.remove_users);

                        builder.setCancelable(false);
                        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                UserDB.deleteAll(UserDB.class);
                                userDBs.clear();
                            }
                        });

                        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                        break;
                    case R.id.cart:
                        Intent iMyOrder = new Intent(ActivityCheckout.this, ActivityCart.class);
                        startActivity(iMyOrder);
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                }
                return true;
            }
        });
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
                    Log.d("HasilProses", userDBs.get(position).getEmail());
                    edtLName.setText(userDBs.get(position).getlName());
                    edtPhone.setText(userDBs.get(position).getPhoneNumber());
                    if (userDBs.get(position).getEmail() != null)
                        edtEmail.setText(userDBs.get(position).getEmail());
                    ArrayList<String> listOfFacilities = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.facilities)));

                    if (listOfFacilities.contains(userDBs.get(position).getFacility())) {
                        int spinnerPosition = adapter.getPosition(userDBs.get(position).getFacility());
                        spinFacility.setSelection(spinnerPosition);
                    }
                }
            });
        }

        // event listener to handle send button when pressed

        GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        listCheckout.setLayoutManager(mLayoutManager);
        listCheckout.setItemAnimator(new DefaultItemAnimator());

        adapterCheckout = new AdapterCheckout(DishDB.listAll(DishDB.class), getString(R.string.currency));

        listCheckout.setVisibility(View.VISIBLE);
        listCheckout.setAdapter(adapterCheckout);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        listCheckout.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));

        btnSend.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                ProgressDialog dialog;
                dialog = ProgressDialog.show(ActivityCheckout.this, "",
                        getString(R.string.sending_alert), true);

                takeUser();
                final View content = findViewById(R.id.coordinator);

                if (user.getFacility().equalsIgnoreCase("") ||
                        user.getfName().equalsIgnoreCase("") ||
                        user.getlName().equalsIgnoreCase("") ||
                        user.getPhone().equalsIgnoreCase("")) {
                    dialog.dismiss();

                    Snackbar snackbar = Snackbar.make(content, R.string.form_alert, Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                    snackbar.show();
                } else if ((dishDBs.size() == 0)) {
                    dialog.dismiss();
                    Snackbar snackbar = Snackbar.make(content, R.string.order_alert, Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                    snackbar.show();
                } else {
                    dialog.dismiss();
                    checkAvailabilitySendUserCollectAllDishAndSendOrder(content);
                    //sendUserCollectAllDishAndSendOrder(content);
                }
            }
        });
        txtTotal.setText("Итого: " + SugarHelper.getTotal() + " " + getString(R.string.currency));
    }
    
    private void setLastUser() {
        if (userDBs.size() != 0) {
            for (UserDB userDB : userDBs) {
                if (userDB.isLast()) {
                    edtFName.setText(userDB.getfName());
                    edtLName.setText(userDB.getlName());
                    edtPhone.setText(userDB.getPhoneNumber());
                    if (userDB.getEmail() != null)
                        edtEmail.setText(userDB.getEmail());
                    ArrayList<String> listOfFacilities = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.facilities)));

                    if (listOfFacilities.contains(userDB.getFacility())) {
                        int spinnerPosition = adapter.getPosition(userDB.getFacility());
                        spinFacility.setSelection(spinnerPosition);
                    }
                    break;
                }
            }
        }

    }

    private void findAllNames() {
        if (userDBs.size() != 0) {
            for (UserDB userDB : userDBs) {
                names.add(userDB.getfName());
            }
        }
    }
    private void checkAvailabilitySendUserCollectAllDishAndSendOrder(final View content) {

        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.AccessKeyParam, Constant.AccessKeyValue);

        App.getApi().getAllTheDishes(map).enqueue(new Callback<PDMenuForAv>() {
            @Override
            public void onResponse(Call<PDMenuForAv> call, Response<PDMenuForAv> response) {
                if (response.isSuccessful()) {
                    String list = "";
                    PDMenuForAv pdMenu = response.body();
                    ArrayList<DataForAv> datas = pdMenu.getData();

                    for(DataForAv data: datas){
                        DishForAv dish = data.getDish();
                        for(DishDB dishDB: dishDBs){
                            if(dish.getMenuId().equals(dishDB.getMenuId())){
                                if(Integer.parseInt(dish.getQuantity())<dishDB.getCount()){
                                    list = list  + dishDB.getMenuName()+ " осталось всего: " + Integer.parseInt(dish.getQuantity()) + "\n";
                                }
                            }
                        }
                    }
                    if(list.equals("")){
                        sendUserCollectAllDishAndSendOrder(content);
                    }
                    else {
                        Snackbar snackbar = Snackbar.make(content, list, Snackbar.LENGTH_SHORT);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(content, R.string.failed_alert, Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                    snackbar.show();
                }
            }
            @Override
            public void onFailure(Call<PDMenuForAv> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(content, R.string.failed_alert, Snackbar.LENGTH_SHORT);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                snackbar.show();
            }
        });
    }

    private void sendUserCollectAllDishAndSendOrder(final View content) {
        // TODO Auto-generated method stub
        App.getApi().sendUser(getUserMap()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                saveUser(user);
                double res =  Double.valueOf(response.body().toString());
                int userId = (int) res;
                collectAllDishAndSendOrder(userId);
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

    private void collectAllDishAndSendOrder(int userId) {
        String date = new SimpleDateFormat("yyyy.MM.dd  hh:mm:ss").format(Calendar.getInstance().getTime());
        if (dishDBs.size() != 0) {
            for (DishDB dishDB : dishDBs) {
                Dish dish = new Dish();
                dish.setUserId(userId);
                dish.setDishId(Integer.parseInt(dishDB.getMenuId()));
                dish.setCount(dishDB.getCount());
                dish.setDate(date);
                dish.setComment(edtComment.getText().toString());
                sendOrder(dish);
            }
        }
    }

    private void sendOrder(Dish dish) {
        // TODO Auto-generated method stub

        final View content = findViewById(R.id.coordinator);

        App.getApi().sendOrder(getOrderMap(dish)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                resultAlert("OK");
                dishDBs.clear();
                DishDB.deleteAll(DishDB.class);
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

    private void saveUser(User user) {
        String eMail = "";
        if (user.getEmail() != null)
            eMail = user.getEmail();
        UserDB userDB = new UserDB(user.getfName(), user.getlName(), user.getPhone(), user.getFacility(), eMail, true);

        changeUserLast();
        userDB.setLast(true);
        userDB.save();
    }

    private void changeUserLast() {
        for (UserDB userDB : userDBs) {
            userDB.setLast(false);
            userDB.save();
        }
    }

    private Map<String, String> getUserMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("facility", user.getFacility());
        map.put("fname", user.getfName());
        map.put("lname", user.getlName());
        map.put("phone", user.getPhone());
        map.put("email", user.getEmail());
        return map;
    }

    private Map<String, String> getOrderMap(Dish dish) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", String.valueOf(dish.getUserId()));
        map.put("dish_id", String.valueOf(dish.getDishId()));
        map.put("count", String.valueOf(dish.getCount()));
        map.put("date", dish.getDate());
        map.put("comment", dish.getComment());
        return map;
    }

    private void takeUser() {
        user.setfName(edtFName.getText().toString());
        user.setlName(edtLName.getText().toString());
        user.setPhone(edtPhone.getText().toString());
        user.setEmail(edtEmail.getText().toString());
        user.setFacility(spinFacility.getSelectedItem().toString());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_in_checkout, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        notifCount = (TextView) actionView.findViewById(R.id.cart_badge);
        notifImg = (ImageView) actionView.findViewById(R.id.cart_img);
        notifCount.setText(String.valueOf(SugarHelper.getDishCount()));
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iMyOrder = new Intent(ActivityCheckout.this, ActivityCart.class);
                startActivity(iMyOrder);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
            }
        });

        return true;
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

    @Override
    protected void onStart() {
        super.onStart();
        if (notifCount != null)
            notifCount.setText(String.valueOf(SugarHelper.getDishCount()));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
