package com.example.goku.basket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by goku on 4/5/17.
 */

public class AddBasketApp extends AppCompatActivity {

    private EditText editTextBasketTitle,editTextBasketDes,editTextItemName;
    private FloatingActionButton fabAddItem;
    private TextInputLayout input_title;
    private ProgressBar progressbar;

    private LinearLayoutManager linearLayout;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<ItemList> itemList;
    private ItemList item;

    private String itemName,basketName1,basketDescription1;
    private int itemQuantity,itemCost,basketCost1;

    private Basket basket;

    private FirebaseDatabase basketDatabase;
    private DatabaseReference basketReference;
    private FirebaseAuth mAuth;

    private String userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_basket);

        //Get Firebase Instance
        basketDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        //Get Firebase Data Reference

        basketReference = basketDatabase.getReference("baskets").child(userId);

        //Get View Instance
        input_title = (TextInputLayout) findViewById(R.id.inputLayoutBasketTitle);
        editTextBasketTitle = (EditText) findViewById(R.id.editText_basket_title);
        editTextBasketDes = (EditText) findViewById(R.id.editText_basket_description);
        editTextItemName = (EditText) findViewById(R.id.editTextAddItem);
        progressbar = (ProgressBar) findViewById(R.id.progressBarBasket);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewItemList);
        linearLayout = new LinearLayoutManager(AddBasketApp.this);
        recyclerView.setLayoutManager(linearLayout);

        itemList = new ArrayList<>();

        fabAddItem = (FloatingActionButton) findViewById(R.id.fabItemAdd);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar_basket_activity);
        setSupportActionBar(tb);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemName = editTextItemName.getText().toString();
                final EditText quantity = new EditText(AddBasketApp.this);
                final EditText cost = new EditText(AddBasketApp.this);

                LinearLayout ll=new LinearLayout(AddBasketApp.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(quantity);
                ll.addView(cost);
                if(itemName.isEmpty()){
                    //Toast.makeText(getApplicationContext(),"Please fill the field",Toast.LENGTH_LONG).show();
                    editTextItemName.setError("Please fill the field");
                }
                else{

                    quantity.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    quantity.setHint("Enter Quantity");
                    cost.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    cost.setHint("Enter Each Item Cost");
                    MaterialDialog dialog = new MaterialDialog.Builder(AddBasketApp.this)
                            .title(itemName)
                            .customView(ll,true)
                            .positiveText("Save")
                            .negativeText("Cancel")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    if(quantity.getText().toString().isEmpty())
                                    {
                                        quantity.setError("Please fill the field");
                                        return;
                                    }
                                    else if(cost.getText().toString().isEmpty()){
                                        cost.setError("Please fill the field");
                                        return;
                                    }
                                    else {
                                        itemQuantity = Integer.parseInt(quantity.getText().toString());
                                        itemCost = Integer.parseInt(cost.getText().toString());
                                        item = new ItemList(itemName, itemQuantity, itemCost);
                                        basketCost1 =basketCost1+ itemQuantity * itemCost;
                                        itemList.add(item);
                                        adapter = new ItemAdapter(AddBasketApp.this, itemList);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Toast.makeText(AddBasketApp.this, "Item Is Not Saved", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
                hideKeyboard(v);
                editTextItemName.setText("");
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_basket, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case android.R.id.home :
                sendDataIntoMainActivity();
                break;
            case R.id.save :
                //Toast.makeText(getApplicationContext(),"Basket is saved",Toast.LENGTH_LONG).show();
                final TextView basketName = new TextView(AddBasketApp.this);
                final TextView basketCost = new TextView(AddBasketApp.this);
                final TextView basketDescription = new TextView(AddBasketApp.this);
                if(editTextBasketTitle.getText().toString().isEmpty()){
                    editTextBasketTitle.setError("Please Fill the Basket Title");
                }
                else {
                    try {
                        if(itemList.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Basket is Empty!",Toast.LENGTH_LONG).show();
                            basketCost1 = 0;
                        }
                        basketName1 = editTextBasketTitle.getText().toString();
                        basketDescription1 = editTextBasketDes.getText().toString();
                        basketName.setText(basketName1);
                        basketName.setTextSize(25);
                        basketDescription.setText(basketDescription1);
                        basketDescription.setTextSize(20);
                        basketCost.setText(String.valueOf("Total Basket Cost is : "+basketCost1+" Rs."));

                        saveDataIntoDatabase();

                        LinearLayout ll = new LinearLayout(AddBasketApp.this);
                        ll.setOrientation(LinearLayout.VERTICAL);
                        ll.addView(basketName);
                        ll.addView(basketDescription);
                        ll.addView(basketCost);

                        MaterialDialog dialog = new MaterialDialog.Builder(AddBasketApp.this)
                                .title("Basket")
                                .customView(ll, true)
                                .positiveText("Ok")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        try {
                                            sendDataIntoMainActivity();
                                            progressbar.setVisibility(View.GONE);

                                        }
                                        catch(Exception e){
                                            Toast.makeText(AddBasketApp.this, e.toString(), Toast.LENGTH_LONG).show();
                                        }

                                    }

                                })
                                .show();
                    }
                    catch(Exception e){
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                }

                break;
            case R.id.menu_help :
                Toast.makeText(getApplicationContext(),"Help is click",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveDataIntoDatabase() {
        progressbar.setVisibility(View.VISIBLE);
        basket = new Basket(basketName1,basketDescription1,basketCost1,itemList);
        String key = basketReference.push().getKey();
        basket.setBasketId(key);

        basketReference.child(key).setValue(basket).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
                else
                {
                  return;
                }

            }
        });
    }

    private void sendDataIntoMainActivity() {

        Intent intent = new Intent(AddBasketApp.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    //For Hiding Keyboard
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}