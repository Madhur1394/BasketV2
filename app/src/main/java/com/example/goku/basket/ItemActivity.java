package com.example.goku.basket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by goku on 11/5/17.
 */

public class ItemActivity extends AppCompatActivity {

    private String BasketTitle;

    private Basket basket;

    private DatabaseReference basketDatabseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_actvity);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BasketInfo");
        String UserId = bundle.getString("UserId");
        String BasketId = bundle.getString("BasketId");

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar_item_activity);
        tb.setTitle("Basket");
        setSupportActionBar(tb);

        // add back arrow in toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Load all basket Info from Firebase
        loadBasketInfo(UserId, BasketId);
    }

    private void loadBasketInfo(String UserId, String BasketId) {
        basketDatabseRef = FirebaseDatabase.getInstance().getReference("baskets").child(UserId).child(BasketId);
        basketDatabseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                basket = dataSnapshot.getValue(Basket.class);
                BasketTitle = basket.getBasketTitle();
                ShowData(BasketTitle);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ShowData(String basketTitle) {
        Toast.makeText(this, basketTitle, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_basket, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                //Back To Main Screen
                backToMainActivity();
                break;
            case R.id.save:
                Toast.makeText(getApplicationContext(), "Basket is saved", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_help:
                Toast.makeText(getApplicationContext(), "Help is click", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void backToMainActivity() {
        startActivity(new Intent(ItemActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        backToMainActivity();
    }
}