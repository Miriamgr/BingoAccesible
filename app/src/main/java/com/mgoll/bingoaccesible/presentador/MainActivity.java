package com.mgoll.bingoaccesible.presentador;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mgoll.bingoaccesible.R;
import com.mgoll.bingoaccesible.modelo.Bombo;
import com.mgoll.bingoaccesible.modelo.Celda;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.mgoll.bingoaccesible.R.string.navigation_drawer_close;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_BJ;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_DIFICULTAD;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_LJ;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_MODO;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_NOMBRE;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_PMBJ;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_PMCJ;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_PMCOJ;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_PT;
import static com.mgoll.bingoaccesible.presentador.PreferencesFragment.KEY_PREF_VELOCIDAD;

/**
 * Esta clase será la única actividad de toda la aplicación, desde aquí nos comunicaremos con los distintos fragmentos y con las clases de datos
 *
 * @authors: MGR, OLC
 * @version: 1.0
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ReglasFragment.OnFragmentInteractionListener,
        LogoMain.OnFragmentInteractionListener, BotonesmainFragment.OnFragmentInteractionListener,
        BomboFragment.OnFragmentInteractionListener, EmpezarFragment.OnFragmentInteractionListener,
        BolasSalidasFragment.OnFragmentInteractionListener, CartonFragment.OnFragmentInteractionListener,
        SeleccionarCartonFragment.OnFragmentInteractionListener, ListaCartonesFragment.OnFragmentInteractionListener,
        AdapterView.OnItemClickListener, SobreNosotrosFragment.OnFragmentInteractionListener, MicuentaFragment.OnFragmentInteractionListener{

    //--*Declaramos las variables privadas de la Clase*--//
    private String modoJuego;
    private String n_usuario;
    private Timer timer; // Timer necesario para programar la salida de bolas
    private tareaTimer tt; // Tarea con la que se cargará el timer
    private boolean timer_activo; //Variable que indica si el timer está o no en funcionamiento

    private RecyclerView rv; //Variable RV que posteriormente cargaremos con datos
    private BolasSalidasAdapter bsa, bsa_todas; //Adaptadores para el RV, necesitamos 2 uno para cada tipo de vista de bolas

   // private ArrayList<Integer> variables_juego = new ArrayList<Integer>();;
    private ArrayList<String> data9 = new ArrayList<String>(); // Array con los datos de las 6 últimas bolas salidas
    private ArrayList<String> data3 = new ArrayList<String>(); // Array con los datos de las 3 últimas bolas salidas
    private ArrayList<String> todasBolas = new ArrayList<String>(); // Array con los datos de TODAS las bolas salidas
    private ArrayList<String> listaCartones = new ArrayList<String>();
    private ArrayList<String> marcadosCarton = new ArrayList<String>();

    private Bombo bombo = new Bombo();

    private int[] cartones_disponibles;
    private int ultima_bola; // Variable que guarda la última bola salida
    private int carton_jugado;
    private Boolean[] anuncio_completas = {false, false, false};
    private boolean volver_inicio = false;
    private boolean automodo = false;
    private boolean partida_empezada = false;
    private boolean resetear_partida = true;

    private Celda[][] matriz_carton;
    private boolean cargar_fragmento_carton = false;
    private GridView gv;
    private AdaptadorCeldas ac;
    private Toast toast;
    private boolean juegoActivo = false;
    private int velocidadBombo;
    private int retardoInicio;
    private int dificultad;
    private SharedPreferences.Editor editor;

    static final int COLUMNAS = 9;
    static final int FILAS = 3;


    /**
     * Esta clase será llamada cuando se cree la vista
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // establecemos como contenido principal el layout activity_main

        // Inicializamos las variables del Timer
        timer = new Timer();
        tt = new tareaTimer();

        cargar_cartones();

        PreferenceManager.setDefaultValues(this, R.xml.preferencias, true);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
        editor.clear();
        editor.commit();
        boolean b = sp.getBoolean("iniciar", true);

        if(b) {
            Intent nuevoUsuario = new Intent(getApplicationContext(), PrimeraConfiguracionActivity.class);
            startActivity(nuevoUsuario);
            editor.putBoolean("iniciar", false);
            editor.commit();
        }

        if(sp!=null){
            n_usuario = sp.getString(KEY_PREF_NOMBRE, "Usuario");
            velocidadBombo = sp.getInt(KEY_PREF_VELOCIDAD, 5)*1000;
            if(velocidadBombo== 0)
                velocidadBombo = 500;
            automodo = sp.getBoolean(KEY_PREF_MODO, true);
            dificultad = sp.getInt(KEY_PREF_DIFICULTAD, 2);
        }

        matriz_carton = new Celda[FILAS][COLUMNAS];
        modoJuego = null;
        retardoInicio = 3000;

        //Colocamos los primeros fragmentos que componen la vista inicial
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        LogoMain logo= new LogoMain();
        BotonesmainFragment botones= new BotonesmainFragment();

        if(logo != null && botones != null) { // Si se han inicializado bien los fragmentos, entonces los ponemos en sus layout
            ft.add(R.id.fragmento_top, logo, "logomain");
            ft.addToBackStack("logo");
            ft.add(R.id.fragmento_bottom, botones, "botonesmain");
            ft.addToBackStack("botones");
            ft.commit();
        }
        fm.executePendingTransactions();

        //Creamos toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Creamos el navigationDrawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, navigation_drawer_close){

            @Override
            public void onDrawerOpened(View drawerview){
                if(timer_activo){
                    timer.cancel();
                    timer_activo = false;
                }
                super.onDrawerOpened(drawerview);
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        this.actualizarHeader();
        //this.onOptionsItemSelected(navigationView.getMenu().getItem(0));
    }

    /**
     * Función llamada al pulsar el botón de atrás en el dispositivo
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fm = getSupportFragmentManager();

        if(timer_activo){
            timer.cancel();
            timer_activo = false;
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(volver_inicio){
                getSupportActionBar().setTitle("Bingo Accesible");
                for (int i = 0; i< fm.getFragments().size(); i++){
                    if(fm.getFragments().get(i)!=null) {
                        fm
                                .beginTransaction()
                                .remove(fm.getFragments().get(i))
                                .commit();
                        fm.executePendingTransactions();
                    }
                }
                while (fm.getBackStackEntryCount() != 0) {
                    fm.popBackStackImmediate();
                }
                fm.executePendingTransactions();
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 45);
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 55);

                LogoMain logo = (LogoMain) fm.findFragmentByTag("logo");
                if(logo == null)
                    logo = new LogoMain();
                BotonesmainFragment botones = (BotonesmainFragment) fm.findFragmentByTag("botones");
                if(botones == null)
                    botones= new BotonesmainFragment();

                if(logo != null && botones != null) { // Si se han inicializado bien los fragmentos, entonces los ponemos en sus layout
                    fm
                            .beginTransaction()
                            .replace(R.id.fragmento_top, logo, "logo")
                            .commit();
                    fm.executePendingTransactions();
                    fm
                            .beginTransaction()
                            .replace(R.id.fragmento_bottom, botones, "botones")
                            .commit();
                    fm.executePendingTransactions();
                }
                volver_inicio = false;
            }
            else {
                if (fm.getBackStackEntryCount() > 1) { // Comprobamos que hay más de un fragmento al que volver

                    if(fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName().equals("ocultar")){
                        this.muestraBolas("OCULTARTODOS");
                    }
                    else if(fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName().equals("botones")){
                        //No hacemos nada
                    }
                    else if(fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName().equals("logo")){
                        //No hacemos nada
                    }
                    else{
                        fm.popBackStack();
                    }
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    /**
     * Función que crea las opciones del menú e infla la vista
     * @param menu
     * @return éxito o fallo
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Función para navegar por los items del menú superior derecho
     * @param item
     * @return éxito o fallo
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 0);
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 100);
            Fragment fragmento = new PreferencesFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (fragmento != null) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmento_top, fragmento, "ajustes")
                        .commit();
                fragmentManager.executePendingTransactions();
            }
            volver_inicio = true;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Función para navegar por los items del NavigationDrawer
     * @param item
     * @return éxito o fallo
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String tag1 = null, tag2 = null;

        Fragment fragmento = null; //Fragmento superior de la aplicación
        Fragment fragmento2 = null; //Fragmento inferior de la aplicación
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_inicio) {
            tag1= "logo";
            tag2= "botones";
            fragmento = fragmentManager.findFragmentByTag(tag1);
            if(fragmento == null)
                fragmento = new LogoMain();
            fragmento2 = fragmentManager.findFragmentByTag(tag2);
            if(fragmento2 == null)
                fragmento2 = new BotonesmainFragment();
        } else if (id == R.id.nav_ajustes) {
            tag1= "ajustes";
            fragmento = fragmentManager.findFragmentByTag(tag1);
            if(fragmento == null)
                fragmento = new PreferencesFragment();
        } else if (id == R.id.nav_reglas) {
            tag1= "reglas";
            fragmento = fragmentManager.findFragmentByTag(tag1);
            if(fragmento == null)
                fragmento = new ReglasFragment();
        } else if (id == R.id.nav_sobrenosotros) {
            tag1="about";
            fragmento = fragmentManager.findFragmentByTag(tag1);
            if(fragmento == null)
                fragmento = new SobreNosotrosFragment();
        } else if (id == R.id.nav_micuenta) {
            tag1="cuenta";
            fragmento = new MicuentaFragment();
        } else if (id == R.id.nav_cerrarsesion) {
            this.finish();
        }

        if(fragmento2 == null){
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 0);
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 100);
        }
        else{
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 45);
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 55);
        }
        if (fragmento != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmento_top, fragmento, tag1)
                    .addToBackStack(tag1)
                    .commit();
            fragmentManager.executePendingTransactions();
        }
        if (fragmento2 != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmento_bottom, fragmento2, tag2)
                    .addToBackStack(tag2)
                    .commit();
            fragmentManager.executePendingTransactions();
        }
        item.setChecked(true);
        if(item.getTitle().equals("Inicio"))
            getSupportActionBar().setTitle("Bingo Accesible");
        else
            getSupportActionBar().setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        volver_inicio = true;
        return true;
    }

    @Override
    public void onFragmentInteraction(String nombreboton) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        String titulo, texto, boton1, boton2;

        titulo = "Aviso";
        boton1 = "Continuar";

        if (nombreboton.equals("COMP")) {
            modoCompleto();
        }
        else if (nombreboton.equals("BOMB") ) {

            if(partida_empezada) {
                if (!modoJuego.equals("bombo")) {
                    texto = "Si continúas, perderás la partida empezada en el modo " + modoJuego;
                    boton2 = "Cancelar";
                }
                else {
                    texto = "¿Deseas continuar con la partida anterior?";
                    boton2 = "Nueva partida";
                }
                AlertDialog ad = LanzarNotificacion(titulo, texto, boton1, boton2, "bombo", modoJuego);
                ad.show();
            }
            else{
                modoJuego = "bombo";
                this.modoBombo(nombreboton);
            }
        }
        else if (nombreboton.equals("CART") ){
            if(partida_empezada) {
                if (!modoJuego.equals("carton")) {
                    texto = "Si continúas, perderás la partida empezada en el modo " + modoJuego;
                    boton2 = "Cancelar";
                }
                else {
                    texto = "¿Deseas continuar con la partida anterior?";
                    boton2 = "Nueva partida";
                }
                AlertDialog ad = LanzarNotificacion(titulo, texto, boton1, boton2, "carton", modoJuego);
                ad.show();
            }
            else{
                modoJuego = "carton";
                juegoActivo = true;
                this.modoCarton("SELECT");
            }
        }
        else if (nombreboton.equals("EMPEZAR")) {
           if(!modoJuego.equals("completo"))
                bombo.inicializa_bombo(0);
            juegoActivo = true;
            this.modoBombo("EMP");
        }
        else if (nombreboton.equals("ATRAS") || nombreboton.equals("SIG") || nombreboton.equals("CONT") || nombreboton.equals("PARAR")){
            this.controlaBombo(nombreboton.substring(0,1));
        }
        else if (nombreboton.equals("MOSTRARTODOS") || nombreboton.equals("OCULTARTODOS")){
            muestraBolas(nombreboton);
        }
        else if(nombreboton.equals("CARTON_ALEA")){
            this.modoCarton("ALEA");
        }
        else if(nombreboton.equals("CARTON_NUM")){
            this.modoCarton("NUM");
        }
        else if(nombreboton.equals("MARCADOS")){
            String text = "";
            for (String i : marcadosCarton){
                text = text.concat(i + " ");
            }
            toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
            toast.show();
        }
        else if(nombreboton.equals("Resetear")){
           reiniciaEstadisticas();
        }
    }

    @Override
    public void onFragmentInteraction(int elem) {
        carton_jugado = elem;
        this.cargar_fragmento_carton = true;
        this.modoCarton("");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Celda item = (Celda) adapterView.getItemAtPosition(i);

        if(!anuncio_completas[ac.getItemRow(i)]) {

            item.setSeleccionada(!item.isSeleccionada());

            if(item.isSeleccionada())
                marcadosCarton.add(item.getValor());
            else{
                marcadosCarton.remove(item.getValor());
            }

            for (int j = 0; j < FILAS; j++) {
                for (int h = 0; h < COLUMNAS; h++) {
                    if (matriz_carton[j][h].getId() == item.getId())
                        matriz_carton[j][h] = item;
                }
            }
            ac.notifyDataSetChanged();
        }
        comprueba_lineas();
    }

    private void modoBombo(String boton){
        volver_inicio = true;
        String boton2 = "";
        String boton3 = "";

        if(boton.equals("BOMBB")){
            boton2 = "B";
            boton = "BOMB";
        }
        if(boton.equals("BOMBBC")){
            boton2 = "B";
            boton = "BOMB";
            boton3 = "C";
        }

        FragmentManager fm = getSupportFragmentManager();

        if(modoJuego.equals("bombo")) {
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 70);
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 30);
        }

        if(boton.equalsIgnoreCase("EMP")){
            if(modoJuego.equals("bombo"))
                aumentaVariable(KEY_PREF_PMBJ);

            partida_empezada = true;
            BomboFragment f1 = BomboFragment.newInstance(modoJuego);
            if (f1 != null) {
                fm
                        .beginTransaction()
                        .replace(R.id.fragmento_top, f1, "fragbomb")
                        .addToBackStack("bombo")
                        .commit();
                fm.executePendingTransactions();
                if(modoJuego.equals("completo"))
                    modoBombo("BOMB");

                if(!timer_activo && automodo){
                    timer = new Timer();
                    tt = new tareaTimer();
                    timer.scheduleAtFixedRate(tt, retardoInicio, velocidadBombo); //Programamos tarea para iniciarse a los 2 sg, cada 5 sg
                    timer_activo = true;
                }
            }
        }
        else if(boton.equalsIgnoreCase("BOMB")){
            EmpezarFragment f2= new EmpezarFragment();
            BolasSalidasFragment f4, f3 = new BolasSalidasFragment();
            int columnas = 0;
            String tag = "";

            if (boton3.equals("") && (automodo) && (f2 != null) && ((modoJuego.equals("bombo") && boton2.equalsIgnoreCase("")) || (modoJuego.equals("completo") && boton2.equalsIgnoreCase("B")))) {
                fm
                        .beginTransaction()
                        .replace(R.id.fragmento_top, f2, "botonEmpezar")
                        .commit();
                fm.executePendingTransactions();
            }

            if(boton2.equals("")) {
                if (f3 != null && modoJuego.equalsIgnoreCase("bombo")) {
                    fm
                            .beginTransaction()
                            .replace(R.id.fragmento_bottom, f3, "bolasSalidas")
                            .commit();
                    fm.executePendingTransactions();
                    tag = "bolasSalidas";
                    columnas = 3;
                    if( resetear_partida ){
                        data9.clear();
                    }
                    bsa = new BolasSalidasAdapter(this, data9);
                    bsa.cambiarTamCeldas(325, 325);
                } else if (f3 != null && modoJuego.equalsIgnoreCase("completo")) {
                    fm
                            .beginTransaction()
                            .add(R.id.rl_subfragmento1, f3, "bolasSalidas2")
                            .commit();

                    fm.executePendingTransactions();

                    tag = "bolasSalidas2";
                    columnas = 4;
                    if(resetear_partida){
                        data3.clear();
                    }
                    bsa = new BolasSalidasAdapter(this, data3);
                    bsa.cambiarTamCeldas(200, 200);
                }


                f4 = (BolasSalidasFragment) getSupportFragmentManager().findFragmentByTag(tag);
                GridLayoutManager glm = new GridLayoutManager(this.getApplicationContext(), columnas);

                rv = (RecyclerView) f4.getView().findViewById(R.id.rv_bolassalidas);
                rv.setLayoutManager(glm);
                rv.setAdapter(bsa);
            }
            if(resetear_partida) {
                if(modoJuego.equals("completo"))
                    bombo.inicializa_bombo(dificultad);
                else
                    bombo.inicializa_bombo(0);
                todasBolas.clear();
            }
            if((automodo && modoJuego.equals("completo") && boton3.equalsIgnoreCase("C")) || (!automodo && ((modoJuego.equals("bombo") && boton2.equalsIgnoreCase("")) || (modoJuego.equals("completo") && boton2.equalsIgnoreCase("B"))))){
                juegoActivo = true;
                modoBombo("EMP");
            }
        }
    }

    private void modoCarton(String boton){
        volver_inicio = true;
        FragmentManager fm = getSupportFragmentManager();

        if(boton.equalsIgnoreCase("SELECT")&& resetear_partida) {
            SeleccionarCartonFragment f1 = new SeleccionarCartonFragment();

            if (f1 != null) {
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 0);
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 100);
                fm
                        .beginTransaction()
                        .replace(R.id.fragmento_top, f1, "selectCart")
                        .addToBackStack("select")
                        .commit();
                fm.executePendingTransactions();
            }
        }
        else{
            if(boton.equalsIgnoreCase("ALEA")&& resetear_partida){
                carton_jugado = cartones_disponibles[(int)Math.floor(Math.random()* cartones_disponibles.length)];
                cargar_fragmento_carton = true;
            }
            else if(boton.equalsIgnoreCase("NUM")&& resetear_partida) {
                volver_inicio = false;
                ListaCartonesFragment fragmento_listacartones = ListaCartonesFragment.newInstance(listaCartones);

                if(fragmento_listacartones != null){
                    fm
                            .beginTransaction()
                            .replace(R.id.fragmento_top,  fragmento_listacartones, "ListaCartones")
                            .addToBackStack("lista")
                            .commit();
                    fm.executePendingTransactions();
                }
            }
            if(!resetear_partida)
                cargar_fragmento_carton = true;
        }

        if(cargar_fragmento_carton && !boton.equals("OCULTARTODOS")){
            partida_empezada = true;
            if(modoJuego.equals("completo"))
                aumentaVariable(KEY_PREF_PMCOJ);
            else
                aumentaVariable(KEY_PREF_PMCJ);
            volver_inicio = true;
            if(resetear_partida) {
                buscar_carton(carton_jugado);
                marcadosCarton.clear();
                for(int i=0; i<anuncio_completas.length; i++)
                    anuncio_completas[i] = false;

            }

            CartonFragment cartonFragment = CartonFragment.newInstance(carton_jugado);

            if(modoJuego.equals("carton") && cartonFragment!= null) {
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 0);
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 100);
                fm.beginTransaction().remove(cartonFragment).commit();
                fm.popBackStackImmediate("fragmento_carton", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.executePendingTransactions();

                fm
                        .beginTransaction()
                        .replace(R.id.fragmento_top,  cartonFragment, "fragmento_carton")
                        //.addToBackStack("fragmento_carton")
                        .commit();
                fm.executePendingTransactions();

            }
            else if(modoJuego.equals("completo") && cartonFragment!= null){
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 50);
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 50);
                fm.beginTransaction().remove(cartonFragment).commit();
                fm.popBackStackImmediate("fragmento_carton", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.executePendingTransactions();

                fm
                        .beginTransaction()
                        .replace(R.id.fragmento_bottom ,  cartonFragment, "fragmento_carton")
                        .commit();
                fm.executePendingTransactions();
            }

            fm.executePendingTransactions();

            if(resetear_partida)
                ac = new AdaptadorCeldas(this, matriz_carton);
            gv = (GridView) findViewById(R.id.gv_carton);
            if (modoJuego.equals("completo"))
                gv.setPadding(0, 50, 0, 0);
            gv.setNumColumns(9);
            gv.setAdapter(ac);
            gv.setOnItemClickListener(this);

            cargar_fragmento_carton = false;

            if(modoJuego.equals("completo"))
                    modoBombo("BOMBB");
        }

        if(modoJuego.equals("completo") && boton.equals("OCULTARTODOS"))
            modoBombo("BOMBBC");
    }

    private void modoCompleto(){
        String titulo, texto, boton1, boton2;
        titulo = "Aviso";
        boton1 = "Continuar";

        if(partida_empezada) {
            if (!modoJuego.equals("completo")) {
                texto = "Si continúas, perderás la partida empezada en el modo " + modoJuego;
                boton2 = "Cancelar";
            }
            else {
                texto = "¿Deseas continuar con la partida anterior?";
                boton2 = "Nueva partida";
            }
            AlertDialog ad = LanzarNotificacion(titulo, texto, boton1, boton2, "completo", modoJuego);
            ad.show();
        }
        else{
            modoJuego = "completo";
            modoCarton("SELECT");
        }
    }

    private void controlaBombo(String boton){
        BomboFragment bf = (BomboFragment) getSupportFragmentManager().findFragmentByTag("fragbomb");

        if (boton.equalsIgnoreCase("A") || boton.equalsIgnoreCase("S")){
            if(timer_activo) {
                timer.cancel();
                mueveBolas(boton, bf);
                timer = new Timer();
                tt = new tareaTimer();
                timer.scheduleAtFixedRate(tt, retardoInicio, velocidadBombo);
            }
            else{
                mueveBolas(boton, bf);
            }
        }
        else if (boton.equalsIgnoreCase("C") && !timer_activo){
            timer = new Timer();
            tt = new tareaTimer();
            timer.scheduleAtFixedRate(tt, retardoInicio, velocidadBombo);
            timer_activo = true;
        }
        else if (boton.equalsIgnoreCase("P") && timer_activo){
            timer.cancel();
            timer_activo = false;
        }
    }

    private void muestraBolas(String boton){
        BolasSalidasFragment f2, f1 = new BolasSalidasFragment();
        FragmentManager fm = getSupportFragmentManager();
        String tag="";
        int numColumns = 0;
        GridLayoutManager glm;

        if(!modoJuego.equals("completo")) {
            if (boton.equalsIgnoreCase("MOSTRARTODOS")) {
                volver_inicio = false;
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 100);
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 0);
                tag = "ocultar";
                numColumns = 5;
            } else if (boton.equalsIgnoreCase("OCULTARTODOS")) {
                volver_inicio = true;
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 70);
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 30);
                tag = "mostrar";
                numColumns = 3;
            }
        }
        else{
            if (boton.equalsIgnoreCase("MOSTRARTODOS")) {
                volver_inicio = false;
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 50);
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 50);
                tag = "ocultar";
                numColumns = 5;
            } else if (boton.equalsIgnoreCase("OCULTARTODOS")) {
                volver_inicio=true;
                cargar_fragmento_carton = true;
                resetear_partida = false;
                modoCarton(boton);
            }
        }


        if(boton.equalsIgnoreCase("MOSTRARTODOS") || (boton.equalsIgnoreCase("OCULTARTODOS") && !modoJuego.equals("completo"))) {

            if (f1 != null) {
                if(modoJuego.equals("completo")){
                    fm
                            .beginTransaction()
                            .replace(R.id.fragmento_top, f1, tag)
                            .addToBackStack(tag)
                            .commit();
                }
                else{
                    fm
                            .beginTransaction()
                            .replace(R.id.fragmento_bottom, f1, tag)
                            .addToBackStack(tag)
                            .commit();
                }
                fm.executePendingTransactions();
            }

            fm.executePendingTransactions();
            f2 = (BolasSalidasFragment) getSupportFragmentManager().findFragmentByTag(tag);
            f2.cambiarNombreBoton(tag);
            rv = (RecyclerView) f2.getView().findViewById(R.id.rv_bolassalidas);
            glm = new GridLayoutManager(this, numColumns);
            rv.setLayoutManager(glm);

            if (boton.equalsIgnoreCase("MOSTRARTODOS")) {
                volver_inicio = false;
                bsa_todas = new BolasSalidasAdapter(this, todasBolas);
                bsa_todas.cambiarTamCeldas(200, 200);
                rv.setAdapter(bsa_todas);
            } else if (boton.equalsIgnoreCase("OCULTARTODOS")) {
                volver_inicio = true;
                bsa.cambiarTamCeldas(350, 350);
                rv.setAdapter(bsa);
            }
        }
    }

    private void mueveBolas(String ant, BomboFragment bf) {
        String text = "";
        int bola;

        if(ant.equalsIgnoreCase("A")){
            if(bombo.getNumbolas() == 0){
                text = text.concat("Aún no han salido bolas");
            }
            else{
                if(bombo.getPosicion_actual()<2){
                    text = text.concat("No hay más bolas anteriores");
                }
                else{
                    bombo.mueve_posicion(-2);
                    bola = bombo.getBola(bombo.getPosicion_actual());
                    bf.actualizaNumBola(bola);
                    bombo.incrementa_posicion(1);
                }
            }
        }
        else {
            if (ant.equalsIgnoreCase("S")) {
                if (bombo.getPosicion_actual() == bombo.getMaxBolas()) {
                    text = text.concat("Ya no quedan bolas que mostrar");
                    aumentaVariable(KEY_PREF_PT);
                    juegoActivo = false;
                } else {
                    bola = bombo.getBola(bombo.getPosicion_actual());
                    bf.actualizaNumBola(bola);

                    if(modoJuego.equals("completo")) {
                        for (int j = 0; j < FILAS; j++) {
                            for (int h = 0; h < COLUMNAS; h++) {
                                if (Integer.toString(bola).equalsIgnoreCase(matriz_carton[j][h].getValor())) {
                                    matriz_carton[j][h].setSalida(true);
                                }
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ac.notifyDataSetChanged();
                            }
                        });
                    }

                    bombo.mueve_posicion(1);
                    if (bombo.getPosicion_actual() > bombo.getNumbolas()) {
                        ultima_bola = bola;
                        bombo.incrementa_numbolas();
                        todasBolas.add(Integer.toString(ultima_bola));
                        if (data9.size() > 8)
                            data9.remove(0);
                        data9.add(Integer.toString(ultima_bola));
                        if (data3.size() > 3)
                            data3.remove(0);
                        data3.add(Integer.toString(ultima_bola));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bsa.notifyDataSetChanged();
                            }
                        });
                        //  bsa.notifyDataSetChanged();
                    }
                }
            }
        }

        final String t = text;
        if(!t.equals("")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast = Toast.makeText(getApplicationContext(), t, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

    private LinearLayout configuraLayout (LinearLayout ll, int weight){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                weight
        );
        ll.setLayoutParams(params);
        return ll;
    }

    private void cargar_cartones(){
        try
        {
            ArrayList<String> aux = new ArrayList<String>();
            InputStream fraw = getResources().openRawResource(R.raw.cartones);

            BufferedReader brin = new BufferedReader(new InputStreamReader(fraw));

            String l = "", linea = brin.readLine();
            String[] num;

            while(linea != null) {
                num = linea.substring(0,2).split(" ");
                for(int i = 0 ; i < num.length; i++)
                {
                    if(!num[i].equals(" ")) {
                        l = l.concat(num[i]);
                    }
                }
                aux.add(l);
                linea = brin.readLine();
                l="";
            }
            fraw.close();

            this.cartones_disponibles = new int[aux.size()];
            for (int i = 0; i< aux.size(); i++){
                this.cartones_disponibles[i]=Integer.parseInt(aux.get(i));
            }

            //Cargamos la lista de cartones
            String inicio = "Cartón nº: ";

            for(int i = 0; i< cartones_disponibles.length; i++){
                listaCartones.add(inicio.concat(Integer.toString(cartones_disponibles[i])));
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde recurso raw");
        }
    }

    private void buscar_carton(int numero){
        try
        {
            InputStream fraw = getResources().openRawResource(R.raw.cartones);

            BufferedReader brin = new BufferedReader(new InputStreamReader(fraw));
            Boolean encontrado = false;

            String l= "", linea = brin.readLine();
            String[] num;

            while(linea != null && !encontrado) {
                num = linea.substring(0,2).split(" ");

                for(int i = 0 ; i < num.length; i++){
                    if(!num[i].equals(" "))
                        l =l.concat(num[i]);
                }

                if(Integer.parseInt(l)== numero)
                    encontrado = true;
                else {
                    linea = brin.readLine();
                    l = "";
                }
            }
            fraw.close();

            if(carton_jugado >9)
                linea = linea.substring(6, linea.length()-1);
            else
                linea = linea.substring(5, linea.length()-1);

            String[] numeros = linea.split(",");
            int h = 0;
            for (int i = 0; i < FILAS; i++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    matriz_carton[i][j] = new Celda(numeros[h], false, false, h) ;
                    h++;
                }
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde recurso raw");
        }
    }

    private void comprueba_lineas(){
        String[] lineasCompletas = new String[FILAS];
        Boolean bingo = true;
        int duration = Toast.LENGTH_SHORT;
        String numlin, text = "";

        for(int i = 0 ; i< FILAS; i++){
            lineasCompletas[i] = this.comprueba_linea(i);

            numlin = Integer.toString(i+1);
            if(lineasCompletas[i].equalsIgnoreCase("l") && !anuncio_completas[i]) {
                text = text.concat(" ¡Línea :" + numlin + " completa! ");
                anuncio_completas[i] = true;
                aumentaVariable(KEY_PREF_LJ);
            }
            else if(lineasCompletas[i].equalsIgnoreCase("m")){
                text = text.concat(" Comprueba la línea :" + numlin + ".Algunos de los números seleccionados no han salido ");
            }
            else if(lineasCompletas[i].equalsIgnoreCase("s")){
                text = text.concat(" Comprueba la línea :" + numlin + ".Te faltan números por marcar ");
            }
        }

        for(boolean i : anuncio_completas){
            if (!i) bingo = false;
        }

        if(bingo) {
            text = text.concat("¡BINGO!");

            if (timer_activo) {
                timer.cancel();
            }

            aumentaVariable(KEY_PREF_BJ);
            AlertDialog ad = LanzarNotificacion("Partida finalizada", "¡Enhorabuena! Has ganado.", "Salir", "Nueva partida", "fin", modoJuego);
            ad.show();

        }
        else{
            if(!juegoActivo && modoJuego.equals("completo")){
                AlertDialog ad = LanzarNotificacion("Partida finalizada", "Lo sentimos. Has perdido. ¡Más suerte la próxima vez!", "Salir", "Nueva partida", "fin", modoJuego);
                ad.show();
            }
        }
        if (!text.equalsIgnoreCase("")) {
            toast = Toast.makeText(this, text, duration);
            toast.show();
        }


    }

    private String comprueba_linea (int linea){

        Boolean select = true; //seleccionadas en el carton
        Boolean salida = true; //salidas
        String res = " ";

        for (int j = 0; j < COLUMNAS; j++) {
            if(!matriz_carton[linea][j].getValor().equals(" ")){
                if(!matriz_carton[linea][j].isSeleccionada())
                    select = false;
                if(!matriz_carton[linea][j].isSalida())
                    salida = false;
            }
        }

        if(modoJuego.equalsIgnoreCase("carton")){
            if(select)
                res = "l";
        }
        else if (modoJuego.equalsIgnoreCase("completo")){
            if (select) {
                if (salida)
                    res = "l";
                else
                    res = "m";
            } else {
                if (salida)
                    res = "s";
            }
        }

        return res;
    }

    private void actualizarHeader(){

        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);

        TextView tv_nombre = (TextView)  nv.getHeaderView(0).findViewById(R.id.tv_nombre);
        tv_nombre.setText(n_usuario);
    }

    public void actualizarPreferencias(String nombreUsuario, int velocidadJuego, boolean modoAuto, String imagen, int dificultadJuego){
        if(!n_usuario.equals(nombreUsuario)){
            n_usuario = nombreUsuario;
            actualizarHeader();
        }
        velocidadBombo = velocidadJuego*1000;
        if(velocidadBombo== 0)
            velocidadBombo = 500;
        automodo = modoAuto;
        dificultad = dificultadJuego;
    }

    private AlertDialog LanzarNotificacion(String titulo, String texto, String boton_1, String boton_2, String mJuegoAct, String mJuegoAnt) {
        final String mjact = mJuegoAct;
        final String mjant = mJuegoAnt;

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        if(titulo != null) {
            builder.setTitle(titulo);
        }
        builder.setMessage(texto)
                .setPositiveButton(boton_1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                respuestaNotificacion(true, mjact, mjant);
                            }
                        })
                .setNegativeButton(boton_2,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                respuestaNotificacion(false, mjact, mjant);
                            }
                        });

        return builder.create();
    }

    private void respuestaNotificacion(boolean respuesta, String mJuegoAc, String mJuegoAn) {

        if (mJuegoAc.equals("fin")) {
            aumentaVariable(KEY_PREF_PT);
            resetear_partida = true;
            partida_empezada = false;
            if(respuesta){
                FragmentManager fm = getSupportFragmentManager();
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 45);
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 55);

                LogoMain logo = (LogoMain) fm.findFragmentByTag("logo");
                if(logo == null)
                    logo = new LogoMain();
                BotonesmainFragment botones = (BotonesmainFragment) fm.findFragmentByTag("botones");
                if(botones == null)
                    botones= new BotonesmainFragment();

                if(logo != null && botones != null) { // Si se han inicializado bien los fragmentos, entonces los ponemos en sus layout
                    fm
                            .beginTransaction()
                            .replace(R.id.fragmento_top, logo, "logo")
                            .commit();
                    fm.executePendingTransactions();
                    fm
                            .beginTransaction()
                            .replace(R.id.fragmento_bottom, botones, "botones")
                            .commit();
                    fm.executePendingTransactions();
                }
                volver_inicio = false;
            }
            else{
                if (mJuegoAn.equals("completo"))
                    modoCompleto();
                else if (mJuegoAn.equals("bombo")) {
                    modoJuego = "bombo";
                    modoBombo("BOMB");
                } else {
                    modoJuego = "carton";
                    modoCarton("SELECT");
                }
            }
        }
        else {
            if (mJuegoAc.equals(mJuegoAn)) {
                partida_empezada = false;
                resetear_partida = !respuesta;
                if (mJuegoAc.equals("completo"))
                    modoCompleto();
                else if (mJuegoAc.equals("bombo")) {
                    modoJuego = "bombo";
                    modoBombo("BOMB");
                } else {
                    modoJuego = "carton";
                    modoCarton("SELECT");
                }
            } else {
                resetear_partida = respuesta;
                if (respuesta) {
                    partida_empezada = false;
                    if (mJuegoAc.equals("completo"))
                        modoCompleto();
                    else if (mJuegoAc.equals("bombo")) {
                        modoJuego = "bombo";
                        modoBombo("BOMB");
                    } else {
                        modoJuego = "carton";
                        modoCarton("SELECT");
                    }
                }
            }
        }
    }

    private void aumentaVariable(String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int num = Integer.parseInt(sp.getString(key, "0")) + 1;
        editor = sp.edit();
        editor.putString(key, Integer.toString(num));
        editor.commit();
    }

    private void reiniciaEstadisticas() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();

        editor.putString(KEY_PREF_PMBJ, "0");
        editor.putString(KEY_PREF_PMCJ, "0");
        editor.putString(KEY_PREF_PMCOJ, "0");
        editor.putString(KEY_PREF_PT, "0");
        editor.putString(KEY_PREF_BJ, "0");
        editor.putString(KEY_PREF_LJ, "0");

        editor.commit();
    }

    class tareaTimer extends TimerTask{
        @Override
        public void run() {
            BomboFragment bf = (BomboFragment) getSupportFragmentManager().findFragmentByTag("fragbomb");

            mueveBolas("S", bf);

            if(!juegoActivo){
                this.cancel();
            }
        }
    }
}


