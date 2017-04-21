package com.mgoll.bingoaccesible.presentador;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        AdapterView.OnItemClickListener{

    //--*Declaramos las variables privadas de la Clase*--//
    private String modoJuego;

    private Timer timer; // Timer necesario para programar la salida de bolas
    private tareaTimer tt; // Tarea con la que se cargará el timer
    private boolean timer_activo; //Variable que indica si el timer está o no en funcionamiento

    private RecyclerView rv; //Variable RV que posteriormente cargaremos con datos
    private BolasSalidasAdapter bsa, bsa_todas; //Adaptadores para el RV, necesitamos 2 uno para cada tipo de vista de bolas
    private Context context; //Variable que hace referencia al contexto de la aplicación
    private ArrayList<String> data9 = new ArrayList<String>(); // Array con los datos de las 6 últimas bolas salidas
    private ArrayList<String> data3 = new ArrayList<String>(); // Array con los datos de las 3 últimas bolas salidas
    private ArrayList<String> todasBolas = new ArrayList<String>(); // Array con los datos de TODAS las bolas salidas
    private ArrayList<String> listaCartones = new ArrayList<String>();
    private ArrayList<String> marcadosCarton = new ArrayList<String>();

    private Bombo bombo = new Bombo();

    private int velocidadJuego;
    private int[] cartonesDisponibles;
    private int ultimaBola; // Variable que guarda la última bola salida
    private int cartonJugado;
    Boolean[] anuncioCompletas = {false, false, false};

    private Celda[][] matrizCarton;
    private boolean cargarFragmentoCarton = false;
    private GridView gv;
    private AdaptadorCeldas ac;
    private Toast toast;
    private boolean juegoActivo = false;

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

        matrizCarton = new Celda[FILAS][COLUMNAS];

       //Colocamos los primeros fragmentos que componen la vista inicial
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        LogoMain logo= new LogoMain();
        BotonesmainFragment botones= new BotonesmainFragment();

        if(logo != null && botones != null) { // Si se han inicializado bien los fragmentos, entonces los ponemos en sus layout
            ft.add(R.id.fragmento_top, logo);
            ft.add(R.id.fragmento_bottom, botones, "botonesmain");
            ft.commit();
        }

        fm.executePendingTransactions();

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

        //Creamos el navigationDrawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //this.onOptionsItemSelected(navigationView.getMenu().getItem(0));
    }

    /**
     * Función llamada al pulsar el botón de atrás en el dispositivo
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fm = getSupportFragmentManager();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if(fm.getBackStackEntryCount()>1){ // Comprobamos que hay más de un fragmento al que volver
                fm.popBackStack();
            }
            else{
                super.onBackPressed();
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

        Fragment fragmento = null; //Fragmento superior de la aplicación
        Fragment fragmento2 = null; //Fragmento inferior de la aplicación
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

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (nombreboton.equals("COMP")) {
            modoCompleto();
        }
        else if (nombreboton.equals("BOMB") ) {
            modoJuego = "bombo";
            this.modoBombo(nombreboton);
        }
        else if (nombreboton.equals("CART") ){
            modoJuego = "carton";
            juegoActivo = true;
            this.modoCarton("SELECT");
        }
        else if (nombreboton == "EMPEZAR") {
            bombo.inicializa_bombo();
            juegoActivo = true;
            this.modoBombo("EMP");
        }
        else if (nombreboton.equals("ATRAS") || nombreboton.equals("SIG") || nombreboton.equals("CONT") || nombreboton.equals("PARAR")){
            this.controlaBombo(nombreboton.substring(0,1));
        }
        else if (nombreboton.equals("MOSTRARTODOS") || nombreboton.equals("OCULTARTODOS")){
            muestraBolas(nombreboton);
        }
        else if(nombreboton == "CARTON_ALEA"){
            this.modoCarton("ALEA");
        }
        else if(nombreboton == "CARTON_NUM"){
            this.modoCarton("NUM");
        }
        else if(nombreboton == "MARCADOS"){
            String text = "";
            for (String i : marcadosCarton){
                text = text.concat(i + " ");
            }
            toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onFragmentInteraction(int elem) {
        cartonJugado = elem;
        this.cargarFragmentoCarton = true;
        this.modoCarton("");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Celda item = (Celda) adapterView.getItemAtPosition(i);

        if(!anuncioCompletas[ac.getItemRow(i)]) {

            item.setSeleccionada(!item.isSeleccionada());

            if(item.isSeleccionada())
                marcadosCarton.add(item.getValor());
            else{
                marcadosCarton.remove(item.getValor());
            }

            for (int j = 0; j < FILAS; j++) {
                for (int h = 0; h < COLUMNAS; h++) {
                    if (matrizCarton[j][h].getId() == item.getId())
                        matrizCarton[j][h] = item;
                }
            }
            ac.notifyDataSetChanged();
        }
        comprueba_lineas();
    }

    private void modoBombo(String boton){

        String boton2 = "";

        if(boton.equals("BOMBB")){
            boton2 = "B";
            boton = "BOMB";
        }

        FragmentManager fm = getSupportFragmentManager();

        if(modoJuego.equals("bombo")) {
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 70);
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 30);
        }

        if(boton.equalsIgnoreCase("EMP")){
            BomboFragment f1 = BomboFragment.newInstance(modoJuego);
            if (f1 != null) {
                fm
                        .beginTransaction()
                        .replace(R.id.fragmento_top, f1, "fragbomb")
                        .commit();
                fm.executePendingTransactions();
                if(modoJuego.equals("completo"))
                    modoBombo("BOMB");

                timer.scheduleAtFixedRate(tt, 2000, 500); //Programamos tarea para iniciarse a los 2 sg, cada 5 sg
                timer_activo = true;
            }
        }
        else if(boton.equalsIgnoreCase("BOMB")){
            EmpezarFragment f2= new EmpezarFragment();
            BolasSalidasFragment f4, f3 = new BolasSalidasFragment();
            int columnas = 0;
            String tag = "";

            if (f2 != null && ((modoJuego.equals("bombo") && boton2.equalsIgnoreCase("")) || (modoJuego.equals("completo") && boton2.equalsIgnoreCase("B")))) {
                fm
                        .beginTransaction()
                        .replace(R.id.fragmento_top, f2, "botonEmpezar")
                        .commit();
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
                    bsa = new BolasSalidasAdapter(this, data3);
                    bsa.cambiarTamCeldas(200, 200);
                }


                f4 = (BolasSalidasFragment) getSupportFragmentManager().findFragmentByTag(tag);
                GridLayoutManager glm = new GridLayoutManager(this.getApplicationContext(), columnas);

                rv = (RecyclerView) f4.getView().findViewById(R.id.rv_bolassalidas);
                rv.setLayoutManager(glm);
                rv.setAdapter(bsa);

            }
        }
    }

    private void modoCarton(String boton){
        FragmentManager fm = getSupportFragmentManager();

        if(boton.equalsIgnoreCase("SELECT")) {
            SeleccionarCartonFragment f1 = new SeleccionarCartonFragment();

            if (f1 != null) {
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 0);
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 100);
                fm
                        .beginTransaction()
                        .replace(R.id.fragmento_top, f1)
                        .commit();
            }
        }
        else{
            if(boton.equalsIgnoreCase("ALEA")){
                cartonJugado = cartonesDisponibles[(int)Math.floor(Math.random()* cartonesDisponibles.length)];
                cargarFragmentoCarton = true;
            }
            else if(boton.equalsIgnoreCase("NUM")) {

                ListaCartonesFragment fragmento_listacartones = ListaCartonesFragment.newInstance(listaCartones);

                if(fragmento_listacartones != null){
                    fm
                            .beginTransaction()
                            .replace(R.id.fragmento_top,  fragmento_listacartones, "ListaCartones")
                            .commit();
                }
            }
        }

        if(cargarFragmentoCarton){ //coger a partir d aqui
            buscar_carton(cartonJugado);
            CartonFragment cartonFragment = CartonFragment.newInstance(cartonJugado);

            if(modoJuego.equals("carton") && cartonFragment!= null) {
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 0);
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 100);
                fm
                        .beginTransaction()
                        .replace(R.id.fragmento_top,  cartonFragment, "fragmento_carton")
                        .commit();

            }
            else if(modoJuego.equals("completo") && cartonFragment!= null){
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 50);
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 50);
                fm
                        .beginTransaction()
                        .replace(R.id.fragmento_bottom ,  cartonFragment, "fragmento_carton2")
                        .commit();
            }

            fm.executePendingTransactions();

            if(!boton.equals("OCULTARTODOS"))
                ac = new AdaptadorCeldas(this, matrizCarton);
            gv = (GridView) findViewById(R.id.gv_carton);
            if(modoJuego.equals("completo"))
                gv.setPadding(0,50,0,0);
            gv.setNumColumns(9);
            gv.setAdapter(ac);
            gv.setOnItemClickListener(this);

            cargarFragmentoCarton = false;
            if(modoJuego.equals("completo") && !boton.equals("OCULTARTODOS"))
                modoBombo("BOMBB");
        }
        boton = null;
    }

    private void modoCompleto(){
        FragmentManager fm = getSupportFragmentManager();
        modoJuego = "completo";

        modoCarton("SELECT");

    }

    private void controlaBombo(String boton){
        BomboFragment bf = (BomboFragment) getSupportFragmentManager().findFragmentByTag("fragbomb");

        if (boton.equalsIgnoreCase("A") || boton.equalsIgnoreCase("S")){
            if(timer_activo) {
                timer.cancel();
                mueveBolas(boton, bf);
                timer = new Timer();
                tt = new tareaTimer();
                timer.scheduleAtFixedRate(tt, 2000, 5000);
            }
            else{
                mueveBolas(boton, bf);
            }
        }
        else if (boton.equalsIgnoreCase("C") && !timer_activo){
            timer = new Timer();
            tt = new tareaTimer();
            timer.scheduleAtFixedRate(tt, 2000, 5000);
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

        if(boton.equalsIgnoreCase("MOSTRARTODOS")){
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 100);
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 0);
            tag = "ocultar";
            numColumns = 5;
        } else if(boton.equalsIgnoreCase("OCULTARTODOS") && !modoJuego.equalsIgnoreCase("completo")){
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 70);
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 30);
            tag = "mostrar";
            numColumns = 3;
        }
        if(modoJuego.equals("completo")&& boton.equalsIgnoreCase("OCULTARTODOS")) {
            cargarFragmentoCarton = true;
            modoCarton(boton);
        }
        else{
            if (f1 != null) {
                fm
                        .beginTransaction()
                        .replace(R.id.fragmento_bottom, f1, tag)
                        .commit();
            }

            fm.executePendingTransactions();
            f2 = (BolasSalidasFragment) getSupportFragmentManager().findFragmentByTag(tag);
            f2.cambiarNombreBoton(tag);
            rv = (RecyclerView) f2.getView().findViewById(R.id.rv_bolassalidas);
            glm = new GridLayoutManager(this, numColumns);
            rv.setLayoutManager(glm);

            if (boton.equalsIgnoreCase("MOSTRARTODOS")) {
                bsa_todas = new BolasSalidasAdapter(this, todasBolas);
                bsa_todas.cambiarTamCeldas(200, 200);
                rv.setAdapter(bsa_todas);
            } else if (boton.equalsIgnoreCase("OCULTARTODOS")) {
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
        else if(ant.equalsIgnoreCase("S")){
            if(bombo.getPosicion_actual()== bombo.getMaxBolas()){
                text = text.concat("Ya no quedan bolas que mostrar");
                juegoActivo = false;
            }
            else{
                bola = bombo.getBola(bombo.getPosicion_actual());
                bf.actualizaNumBola(bola);
                bombo.mueve_posicion(1);
                if(bombo.getPosicion_actual()>bombo.getNumbolas()) {
                    ultimaBola = bola;
                    bombo.incrementa_numbolas();
                    todasBolas.add(Integer.toString(ultimaBola));
                    if(data9.size()>8)
                        data9.remove(0);
                    data9.add(Integer.toString(ultimaBola));
                    if(data3.size()>3)
                        data3.remove(0);
                    data3.add(Integer.toString(ultimaBola));
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

            this.cartonesDisponibles = new int[aux.size()];
            for (int i = 0; i< aux.size(); i++){
                this.cartonesDisponibles[i]=Integer.parseInt(aux.get(i));
            }

            //Cargamos la lista de cartones
            String inicio = "Cartón nº: ";

            for(int i = 0; i< cartonesDisponibles.length; i++){
                listaCartones.add(inicio.concat(Integer.toString(cartonesDisponibles[i])));
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

            if(cartonJugado>9)
                linea = linea.substring(6, linea.length()-1);
            else
                linea = linea.substring(5, linea.length()-1);

            String[] numeros = linea.split(",");
            int h = 0;
            for (int i = 0; i < FILAS; i++) {
                for (int j = 0; j < COLUMNAS; j++) {
                    matrizCarton[i][j] = new Celda(numeros[h], false, false, h) ;
                    h++;
                }
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde recurso raw");
        }
    }

    public void comprueba_lineas(){
        String[] lineasCompletas = new String[FILAS];
        Boolean bingo = true;
        int duration = Toast.LENGTH_SHORT;
        String numlin, text = "";

        for(int i = 0 ; i< FILAS; i++){
            lineasCompletas[i] = this.comprueba_linea(i);

            numlin = Integer.toString(i+1);
            if(lineasCompletas[i].equalsIgnoreCase("l") && !anuncioCompletas[i]) {
                text = text.concat(" ¡Línea :" + numlin + " completa! ");
                anuncioCompletas[i] = true;
            }
            else if(lineasCompletas[i].equalsIgnoreCase("m")){
                text = text.concat(" Comprueba la línea :" + numlin + ".Algunos de los números seleccionados no han salido ");
            }
            else if(lineasCompletas[i].equalsIgnoreCase("s")){
                text = text.concat(" Comprueba la línea :" + numlin + ".Te faltan números por marcar ");
            }
        }

        for(boolean i : anuncioCompletas){
            if (!i) bingo = false;
        }

        if(bingo){
            text = text.concat("¡BINGO!");
            juegoActivo = false;
        }

        if(!text.equalsIgnoreCase("")) {
            toast = Toast.makeText(this, text, duration);
            toast.show();
        }

    }

    private String comprueba_linea (int linea){

        Boolean select = true; //seleccionadas en el carton
        Boolean salida = true; //salidas
        String res = " ";

        for (int j = 0; j < COLUMNAS; j++) {
            if(!matrizCarton[linea][j].getValor().equals(" ")){
                if(!matrizCarton[linea][j].isSeleccionada())
                    select = false;
                if(!matrizCarton[linea][j].isSalida())
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

    /**
     * The type Tarea timer.
     */
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

  //TODO

    /*/
    1. Comentar código
    2. Ajustes->velocidad, modo auto
    3. Parar timer cuando atrás
    5. fragments info panel izdo
    6. Cuando panel izquierdo, para timer.
     */

    // ¿INTERESA UN MODO AUTO DE MARCADO DE CARTON?
    // ¿PONEMOS LOS NUMEROS MARCADOS EN EL CARTON DE FORMA ORDENADA?
    // ¿INTERESA QUE GOOGLE TALKBACK ANUNCIE QUE EMPIEZA EL JUEGO EN MODO AUTOMÁTICO/MANUAL?
}


