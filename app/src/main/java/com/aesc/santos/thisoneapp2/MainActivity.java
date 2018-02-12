package com.aesc.santos.thisoneapp2;

import android.content.BroadcastReceiver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.aesc.santos.thisoneapp2.Entidades.Usuario;
import com.aesc.santos.thisoneapp2.adapters.UsuariosImagenURLAdapters;
import com.aesc.santos.thisoneapp2.fragments.AddFragment;
import com.aesc.santos.thisoneapp2.fragments.BookFragment;
import com.aesc.santos.thisoneapp2.fragments.HomeFragment;
import com.aesc.santos.thisoneapp2.fragments.ProfileFragment;
import com.aesc.santos.thisoneapp2.fragments.SearchFragment;
import com.facebook.stetho.Stetho;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        AddFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        BookFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener{

    private ArrayList<Usuario> stringArrayList;
    private RecyclerView recyclerView;
    UsuariosImagenURLAdapters adapters;

    private BottomNavigationView bottomNavigationView;
    BroadcastReceiver broadcastReceiver;

    //private Toolbar toolbar;

    //public ImageView photoUser;
    public String nameUser,passUser,idUser;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

        //FIN COSAS DE GOOGLE
        if (savedInstanceState==null){
            Fragment miFragment= new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,miFragment).commit();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                Fragment miFragment = null;
                boolean fragmentSeleccionado = false;

                if (id == R.id.inicioItem) {
                    miFragment = new HomeFragment();
                    fragmentSeleccionado=true;
                } else if (id == R.id.addItem) {
                    miFragment = new AddFragment();
                    fragmentSeleccionado = true;
                } else if (id == R.id.searchItem) {
                    miFragment = new SearchFragment();
                    fragmentSeleccionado=true;
                } else if (id == R.id.bookItem) {
                    miFragment = new BookFragment();
                    fragmentSeleccionado=true;
                } else if (id == R.id.profileItem) {
                    //miFragment = new ProfileFragment();
                    //toolbar = findViewById(R.id.toolbarMain);
                    //setSupportActionBar(toolbar);
                    //getSupportActionBar().setTitle("Profile");
                    //fragmentSeleccionado=true;
                }

                if (fragmentSeleccionado==true){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,miFragment).commit();
                }

                return true;
            }
        });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
