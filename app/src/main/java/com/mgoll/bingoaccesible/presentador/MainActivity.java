package com.mgoll.bingoaccesible.presentador;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mgoll.bingoaccesible.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ReglasFragment.OnFragmentInteractionListener, LogoMain.OnFragmentInteractionListener, BotonesmainFragment.OnFragmentInteractionListener, BomboFragment.OnFragmentInteractionListener, EmpezarFragment.OnFragmentInteractionListener, BolasSalidasFragment.OnFragmentInteractionListener, CartonFragment.OnFragmentInteractionListener{

    private Timer timer;
    private tareaTimer tt;
    private boolean timer_activo;

    private RecyclerView rv;
    private BolasSalidasAdapter bsa, bsa_todas;
    private Context context;
    ArrayList<String> data = new ArrayList<String>();
    ArrayList<String> todasBolas = new ArrayList<String>();

    private int ultimaBola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instanciamos el timer
        timer = new Timer();
        tt = new tareaTimer();

       //Colocamos los fragmentos de inicio
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LogoMain logo= new LogoMain();
        BotonesmainFragment botones= new BotonesmainFragment();
        ft.add(R.id.fragmento_top, logo);
        ft.add(R.id.fragmento_bottom, botones, "botonesmain");
        ft.commit();

        //Creamos toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/ //Este es el icono del mensaje rosa

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //this.onOptionsItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fm = getSupportFragmentManager();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if(fm.getBackStackEntryCount()>1){
                fm.popBackStack();
            }
            else{
                super.onBackPressed();
            }
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragmento = null;
        Fragment fragmento2 = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_inicio) {
            fragmento = new LogoMain();
            fragmento2 = new BotonesmainFragment();
        } else if (id == R.id.nav_ajustes) {

        } else if (id == R.id.nav_reglas) {
            fragmento = new ReglasFragment();
        } else if (id == R.id.nav_sobrenosotros) {
            fragmento = new SobreNosotrosFragment();
        } else if (id == R.id.nav_micuenta) {
            fragmento = new MicuentaFragment();
        } else if (id == R.id.nav_cerrarsesion) {

        }

        /*if (fragmento != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_main, fragmento)
                    .commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }*/
        if (fragmento != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmento_top, fragmento)
                    .commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }
        if (fragmento2 != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmento_bottom, fragmento2)
                    .commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(String nombreboton) {

        Fragment fragmento_bombo, fragmento_empezar, fragmento_bolas, fragmento_carton;
        FragmentManager fragmentManager = getSupportFragmentManager();


        if (nombreboton == "COMP") {

        }
        else if (nombreboton == "BOMB") {
            fragmento_empezar= new EmpezarFragment();
            fragmento_bolas = new BolasSalidasFragment();
            if (fragmento_empezar != null) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmento_top, fragmento_empezar)
                        .commit();
            }
            if(fragmento_bolas != null){
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmento_bottom, fragmento_bolas, "bolasSalidas")
                        .commit();
            }

           fragmentManager.executePendingTransactions();

            BolasSalidasFragment bsf = (BolasSalidasFragment) getSupportFragmentManager().findFragmentByTag("bolasSalidas");
            View v = bsf.getView();
            rv = (RecyclerView) v.findViewById(R.id.rv_bolassalidas);

           // rv = (RecyclerView) getSupportFragmentManager().findFragmentByTag("bolasSalidas").getView().findViewById(R.id.rv_bolassalidas);
           // rv = (RecyclerView) findViewById(R.id.rv_bolassalidas);
            int numberOfColumns = 3;
            context = this.getApplicationContext();
            GridLayoutManager glm = new GridLayoutManager(context, numberOfColumns);
           //Como mostrar en la parte de arriba los numeros?
            //glm.setReverseLayout(true);
            rv.setLayoutManager(glm);
            bsa = new BolasSalidasAdapter(this, data);
            bsa.cambiarTamCeldas(350,350);
            rv.setAdapter(bsa);

        } else if (nombreboton == "EMPEZAR"){
            fragmento_bombo = new BomboFragment();
            if (fragmento_bombo != null) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmento_top, fragmento_bombo, "fragbomb")
                        .commit();
                timer.scheduleAtFixedRate(tt, 3000, 5000); //Programamos tarea para iniciarse a los 3 sg, cada 5 sg
                timer_activo = true;
            }

        } else if (nombreboton == "CART"){
            fragmento_carton = new CartonFragment();
            if (fragmento_carton != null) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmento_top, fragmento_carton)
                        .commit();
            }

            BotonesmainFragment bmf = (BotonesmainFragment) getSupportFragmentManager().findFragmentByTag("botonesmain");

            fragmentManager
                    .beginTransaction()
                    .remove(bmf)
                    .commit();

        }
        else if (nombreboton == "ATRAS"){
            BomboFragment bf = (BomboFragment) getSupportFragmentManager().findFragmentByTag("fragbomb");

            if(timer_activo) {
                timer.cancel();
                bf.bolaAnterior();
                timer = new Timer();
                tt = new tareaTimer();
                timer.scheduleAtFixedRate(tt, 3000, 5000);
            }
            else{
                bf.bolaAnterior();
            }

        }
        else if (nombreboton == "SIG"){
            BomboFragment bf = (BomboFragment) getSupportFragmentManager().findFragmentByTag("fragbomb");
           // BolasSalidasFragment bsf = (BolasSalidasFragment) getSupportFragmentManager().findFragmentByTag("bolasSalidas");

            if(timer_activo) {
                timer.cancel();
                bf.bolaSiguiente();
                timer = new Timer();
                tt = new tareaTimer();
                timer.scheduleAtFixedRate(tt, 3000, 1000);
            }
            else{
                bf.bolaSiguiente();
                ultimaBola = bf.getUltimaBola();
                if(ultimaBola != -1) {
                    todasBolas.add(Integer.toString(ultimaBola));

                    if(data.size()>8) {
                        data.remove(0);
                    }
                    data.add(Integer.toString(ultimaBola));
                    bsa.notifyDataSetChanged();

                }
            }
        }
        else if (nombreboton == "CONT"){
            if(!timer_activo) {
                timer = new Timer();
                tt = new tareaTimer();
                timer.scheduleAtFixedRate(tt, 1000, 1000);
                timer_activo = true;
            }
        }
        else if (nombreboton == "PARAR"){
            if(timer_activo) {
                timer.cancel();
                timer_activo = false;
            }
        }
        else if (nombreboton == "MOSTRARTODOS"){

            if(timer_activo) {
                timer.cancel();
                timer_activo = false;
            }

            Fragment todas_bolas_salidas = BolasSalidasFragment.newInstance("Entero");

            if(todas_bolas_salidas != null){
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmento_bottom, todas_bolas_salidas, "todasBolas")
                        .addToBackStack(null)
                        .commit();
            }

            fragmentManager.executePendingTransactions();

            BolasSalidasFragment bsf_todas = (BolasSalidasFragment) getSupportFragmentManager().findFragmentByTag("todasBolas");
            bsf_todas.cambiarNombreBoton("ocultar");
            View v = bsf_todas.getView();
            rv = (RecyclerView) v.findViewById(R.id.rv_bolassalidas);

            int numberOfColumns = 5;
            context = this.getApplicationContext();
            GridLayoutManager glm = new GridLayoutManager(context, numberOfColumns);

            glm.generateLayoutParams(new GridLayoutManager.LayoutParams(GridLayoutManager.LayoutParams.MATCH_PARENT, 600 ));





            //Como mostrar en la parte de arriba los numeros?
            //glm.setReverseLayout(true);
            rv.setLayoutManager(glm);
            bsa_todas = new BolasSalidasAdapter(this, todasBolas);

            bsa_todas.cambiarTamCeldas(200,200);

            rv.setAdapter(bsa_todas);

            /*
            * Voy a reutilizar el fragment de las bolas salidas, pero hay que personalizar algunas cosas:
            * 1. Poner función para hacer botón mostrar todos invisible o que lo podamos reutilizar, ej: cuando lo vuelves a pulsar te devuelve al fragment anterior...
            * 2. Nuevo adapter, pero usamos la misma clase, por tanto pamos a tener que hacer el constructor del adapter con atributos específicos
            * 3. Toda las bolas salidas se guardarán en el nuevo Array List creado.
            * /*/
            //Reutilizar f
            //Poner atributos al adapter personalizados para cuando lo abres desd

            //En este caso cargaremos una nueva ventana que nos muestre el recycler view pero cargado con todos los números.

            //Boton poner invisible
        }
        else if (nombreboton == "OCULTARTODOS"){

            // hacer popoBackstack
            Fragment todas_bolas_salidas = new BolasSalidasFragment();

            if(todas_bolas_salidas != null){
                fragmentManager
                        .beginTransaction()
                        .add(R.id.fragmento_bottom, todas_bolas_salidas, "todasBolas")
                        .addToBackStack(null)
                        .commit();
            }

            fragmentManager.executePendingTransactions();

            BolasSalidasFragment bsf_todas = (BolasSalidasFragment) getSupportFragmentManager().findFragmentByTag("todasBolas");
            View v = bsf_todas.getView();
            rv = (RecyclerView) v.findViewById(R.id.rv_bolassalidas);

            int numberOfColumns = 5;
            context = this.getApplicationContext();
            GridLayoutManager glm = new GridLayoutManager(context, numberOfColumns);
            //Como mostrar en la parte de arriba los numeros?
            //glm.setReverseLayout(true);
            rv.setLayoutManager(glm);
            bsa = new BolasSalidasAdapter(this, data);
            rv.setAdapter(bsa);

            /*
            * Voy a reutilizar el fragment de las bolas salidas, pero hay que personalizar algunas cosas:
            * 1. Poner función para hacer botón mostrar todos invisible o que lo podamos reutilizar, ej: cuando lo vuelves a pulsar te devuelve al fragment anterior...
            * 2. Nuevo adapter, pero usamos la misma clase, por tanto pamos a tener que hacer el constructor del adapter con atributos específicos
            * 3. Toda las bolas salidas se guardarán en el nuevo Array List creado.
            * /*/
            //Reutilizar f
            //Poner atributos al adapter personalizados para cuando lo abres desd

            //En este caso cargaremos una nueva ventana que nos muestre el recycler view pero cargado con todos los números.

            //Boton poner invisible
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class tareaTimer extends TimerTask{
        @Override
        public void run() {
            BomboFragment bf = (BomboFragment) getSupportFragmentManager().findFragmentByTag("fragbomb");
           // boolean prueba = bf.isAdded();
            boolean terminado = bf.bolaSiguiente();
            //Lo que se va a ejecutar con el timer

            ultimaBola = bf.getUltimaBola();

            if(ultimaBola != -1) {
                todasBolas.add(Integer.toString(ultimaBola));
                if(data.size()>8) {
                    data.remove(0);
                }
                data.add(Integer.toString(ultimaBola));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bsa.notifyDataSetChanged();
                    }
                });
            }

            if(terminado){
                this.cancel();
            }
        }
    }

    //Para seleccionar el cartón deseado (se llama desde el FRAGMENT del BOMBO

    //TODO

    /*/
    1. Crear clase cartón
    2. Crear fragmento pre-Cartón con dos botones: Elegir cartón ó cartón aleatorio.
    3. Si elegir cartón mostramos desplegable o algo. Si no aleatorio.
    4. Guardamos elección y se la pasamos a un nuevo fragmento cartón (bunddle)
    2. Crear fragmento cartón:
        2.1. En el fragmento del cartón cargaremos el cartón con la opción que le pasamos
        2.2. Aquí vendrá la lógica del juego, quizás dos matrices?
        2.3. Cargamos el cartón en la vista
        2.4 Tenemos que cargar los listener que no incluye el recycler view para que se puedan ir actualizando los atributos del cartón.
     */


    private void selecciona_carton(int numero){
        /*try
        {
            InputStream fraw =
                    getResources().openRawResource(R.raw.prueba_raw);

            BufferedReader brin =
                    new BufferedReader(new InputStreamReader(fraw));

            String linea = brin.readLine();

            fraw.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde recurso raw");
        }*/
    }

}


