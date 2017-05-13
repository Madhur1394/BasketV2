package com.example.goku.basket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView userName, userEmail;
    private ImageView imageView_userPhoto;
    private NavigationView navigationView;
    private View navHeader;

    public UserInformation userInformation;

    private RecyclerView recyclerView;
    private MyViewHolder adapter;
    private ArrayList<Basket> basketList;

    public Basket basket, basket_1;

    public LinearLayoutManager linearLayout;
    private ProgressBar progressBar;

    private FirebaseDatabase basketDatabase;
    private DatabaseReference basketReference, userReference;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);

        //Get View Instance
        userName = (TextView) navHeader.findViewById(R.id.txtViewuserName);
        userEmail = (TextView) navHeader.findViewById(R.id.textViewEmailId);
        imageView_userPhoto = (ImageView) navHeader.findViewById(R.id.imageView_user);

        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewBasket);
        linearLayout = new LinearLayoutManager(MainActivity.this);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);

        //GEt Firebase Instance
        mAuth = FirebaseAuth.getInstance();
        basketDatabase = FirebaseDatabase.getInstance();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        basket_1 = basketList.get(position);
                        //Toast.makeText(getApplicationContext(),basket_1.getBasketTitle(),Toast.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("UserId", userId);
                        bundle.putString("BasketId", basket_1.getBasketId());
                        Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                        intent.putExtra("BasketInfo", bundle);
                        startActivity(intent);
                        finish();
                    }
                })
        );


        basketList = new ArrayList<>();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    userId = user.getUid();
                    //Get Firebase DB basket Instance
                    basketReference = basketDatabase.getReference("baskets").child(userId);
                    userReference = basketDatabase.getReference("userInfo").child(userId);
                    getUserInfo(userReference);
                    //Adapter
                    adapter = new MyViewHolder(MainActivity.this, retrieve(basketReference));
                    if (user.isEmailVerified()) {

                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        SubActionButton.Builder itemButton = new SubActionButton.Builder(this);

        ImageView imageIcon1, imageIcon2;
        imageIcon1 = new ImageView(this);
        imageIcon2 = new ImageView(this);

        imageIcon1.setImageDrawable(getDrawable(R.drawable.addrec));
        imageIcon2.setImageDrawable(getDrawable(R.drawable.basketplus));

        SubActionButton btn1 = itemButton.setContentView(imageIcon1).setLayoutParams(new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.LayoutParams(150, 150))
                .build();
        SubActionButton btn2 = itemButton.setContentView(imageIcon2).setLayoutParams(new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.LayoutParams(150, 150))
                .build();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(btn1)
                .addSubActionView(btn2)
                .attachTo(fab)
                .build();

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        *.setAction("Action", null).show();*/
                startActivity(new Intent(MainActivity.this, AddBasketApp.class));
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void getUserInfo(final DatabaseReference userReference) {
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInformation = dataSnapshot.getValue(UserInformation.class);

                userEmail.setText(userInformation.getEmail());
                userName.setText(userInformation.getName());
                userName.setAllCaps(true);
                userName.setTextSize(20);
                // Loading profile image
                try {
                    Glide.with(MainActivity.this).load(userInformation.getPhotoUrl())
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(MainActivity.this))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView_userPhoto);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }
        if (id == R.id.action_signOut) {
            mAuth.signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //READ BY HOOKING ONTO DATABASE OPERATION CALLBACKS
    public ArrayList<Basket> retrieve(DatabaseReference basketReference) {
        basketReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return basketList;
    }

    //IMPLEMENT FETCH DATA AND FILL ARRAYLIST
    public void fetchData(DataSnapshot dataSnapshot) {
        basketList.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Basket basket = ds.getValue(Basket.class);
            basketList.add(basket);
            setAdap(basketList);
        }
    }

    public void setAdap(ArrayList<Basket> adap) {
        adapter = new MyViewHolder(MainActivity.this, adap);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter, MainActivity.this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}