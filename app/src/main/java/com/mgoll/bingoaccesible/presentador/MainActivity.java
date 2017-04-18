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
    private ArrayList<String> data = new ArrayList<String>(); // Array con los datos de las 6 últimas bolas salidas
    private ArrayList<String> todasBolas = new ArrayList<String>(); // Array con los datos de TODAS las bolas salidas
    private ArrayList<String> listaCartones = new ArrayList<String>();
    private ArrayList<String> marcadosCarton = new ArrayList<String>();

    private int[] cartonesDisponibles;
    private int ultimaBola; // Variable que guarda la última bola salida
    private int cartonJugado;
    Boolean[] anuncioCompletas = {false, false, false};

    private Celda[][] matrizCarton;
    private boolean cargarFragmentoCarton = false;
    private GridView gv;
    private AdaptadorCeldas ac;
    private Toast toast;


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

        this.cargar_cartones();

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

        Fragment fragmento_bombo, fragmento_empezar, fragmento_bolas, fragmento_carton, fragmento_select_carton,
        fragmento_listacartones;
        FragmentManager fragmentManager = getSupportFragmentManager();


        if (nombreboton == "COMP") {
            // modo completo
        }
        else if (nombreboton == "BOMB") {

            this.modoBombo(fragmentManager);

        }
        else if (nombreboton == "CART"){

            this.modoCarton(fragmentManager, "SELECT");
        }
        else if (nombreboton == "EMPEZAR") {
            fragmento_bombo = new BomboFragment();
            if (fragmento_bombo != null) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmento_top, fragmento_bombo, "fragbomb")
                        .commit();
                timer.scheduleAtFixedRate(tt, 2000, 5000); //Programamos tarea para iniciarse a los 1 sg, cada 1 sg
                timer_activo = true;
            }
        }

        else if (nombreboton == "ATRAS"){
            BomboFragment bf = (BomboFragment) getSupportFragmentManager().findFragmentByTag("fragbomb");

            if(timer_activo) {
                timer.cancel();
                bf.bolaAnterior();
                timer = new Timer();
                tt = new tareaTimer();
                timer.scheduleAtFixedRate(tt, 2000, 5000);
            }
            else{
                bf.bolaAnterior();
            }
        }
        else if (nombreboton == "SIG"){
            BomboFragment bf = (BomboFragment) getSupportFragmentManager().findFragmentByTag("fragbomb");

            if(timer_activo) {
                timer.cancel();
                bf.bolaSiguiente();
                timer = new Timer();
                tt = new tareaTimer();
                timer.scheduleAtFixedRate(tt, 2000, 5000);
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
                timer.scheduleAtFixedRate(tt, 2000, 5000);
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

            Fragment todas_bolas_salidas = new BolasSalidasFragment();

            if(todas_bolas_salidas != null){
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 100);
                this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 0);

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

            rv.setLayoutManager(glm);
            bsa_todas = new BolasSalidasAdapter(this, todasBolas);

            bsa_todas.cambiarTamCeldas(200,200);

            rv.setAdapter(bsa_todas);

        }
        else if (nombreboton == "OCULTARTODOS"){
            BolasSalidasFragment bsf_todas = new BolasSalidasFragment();

            //BolasSalidasFragment bsf_todas = (BolasSalidasFragment) getSupportFragmentManager().findFragmentByTag("todasBolas");
            //bsf_todas.cambiarNombreBoton("mostrar");

            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 70);
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 30);
            //fragmentManager.popBackStack();

            //fragmento_empezar= new EmpezarFragment();
            //fragmento_bolas = new BolasSalidasFragment();


            if(bsf_todas != null){
                fragmentManager
                        .beginTransaction()
                       // .remove()
                        .replace(R.id.fragmento_bottom, bsf_todas, "OcultarBolas")
                        .commit();
            }

            fragmentManager.executePendingTransactions();

            BolasSalidasFragment bsf = (BolasSalidasFragment) getSupportFragmentManager().findFragmentByTag("OcultarBolas");
            View v = bsf.getView();
            rv = (RecyclerView) v.findViewById(R.id.rv_bolassalidas);

            int numberOfColumns = 3;
            context = this.getApplicationContext();
            GridLayoutManager glm = new GridLayoutManager(context, numberOfColumns);

            rv.setLayoutManager(glm);
           // bsa = new BolasSalidasAdapter(this, data);
            bsa.cambiarTamCeldas(350,350);
            rv.setAdapter(bsa);
        }
        else if(nombreboton == "CARTON_ALEA"){
            this.modoCarton(fragmentManager, "ALEA");
        }
        else if(nombreboton == "CARTON_NUM"){
            this.modoCarton(fragmentManager, "NUM");
        }
        else if(nombreboton == "MARCADOS"){
            String text = "";
            for (String i : marcadosCarton){
                text = text.concat(i + " ");
            }

            toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        }
    }


    @Override
    public void onFragmentInteraction(int elem) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        cartonJugado = elem;

        this.cargarFragmentoCarton = true;
        this.modoCarton(fragmentManager, "");
        //cargamos carton

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void modoBombo(FragmentManager fm){

        EmpezarFragment f1= new EmpezarFragment();
        BolasSalidasFragment f2 = new BolasSalidasFragment();

        this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 70);
        this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 30);

        if (f1 != null) {
            fm
                    .beginTransaction()
                    .replace(R.id.fragmento_top, f1, "botonEmpezar")
                    .commit();
        }
        if(f2 != null){
            fm
                    .beginTransaction()
                    .replace(R.id.fragmento_bottom, f2, "bolasSalidas")
                    .commit();
        }

        fm.executePendingTransactions();

        BolasSalidasFragment bsf = (BolasSalidasFragment) getSupportFragmentManager().findFragmentByTag("bolasSalidas");
        GridLayoutManager glm = new GridLayoutManager(this.getApplicationContext(), 3);

        bsa = new BolasSalidasAdapter(this, data);
        bsa.cambiarTamCeldas(350,350);

        rv = (RecyclerView) bsf.getView().findViewById(R.id.rv_bolassalidas);
        rv.setLayoutManager(glm);
        rv.setAdapter(bsa);

        modoJuego = "bombo";
    }

    private void modoCarton(FragmentManager fm, String modo){

        if(modo.equalsIgnoreCase("SELECT")) {
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
            if(modo.equalsIgnoreCase("ALEA")){
                cartonJugado = cartonesDisponibles[(int)Math.floor(Math.random()* cartonesDisponibles.length)];
                cargarFragmentoCarton = true;
            }
            else if(modo.equalsIgnoreCase("NUM")) {
                this.cargar_lista_cartones();

                ListaCartonesFragment fragmento_listacartones = ListaCartonesFragment.newInstance(listaCartones);

                if(fragmento_listacartones != null){
                    fm
                            .beginTransaction()
                            .replace(R.id.fragmento_top,  fragmento_listacartones, "ListaCartones")
                            .commit();
                }
            }
        }

        if(cargarFragmentoCarton){
            buscar_carton(cartonJugado);

            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_top), 0);
            this.configuraLayout((LinearLayout) findViewById(R.id.fragmento_bottom), 100);

            CartonFragment cartonFragment = CartonFragment.newInstance(cartonJugado);
            if(cartonFragment != null){
                fm
                        .beginTransaction()
                        .replace(R.id.fragmento_top,  cartonFragment, "fragmento_carton")
                        .commit();
            }
            fm.executePendingTransactions();


            ac = new AdaptadorCeldas(this, matrizCarton);
            gv = (GridView) findViewById(R.id.gv_carton);
            gv.setNumColumns(9);
            gv.setAdapter(ac);
            gv.setOnItemClickListener(this);

            cargarFragmentoCarton = false;
        }
        modo = null;
        modoJuego = "carton";
    }

    private void modoCompleto(){
        FragmentManager fm = getSupportFragmentManager();



        modoJuego = "completo";
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
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde recurso raw");
        }
    }

    private void cargar_lista_cartones() {

        String inicio = "Cartón nº: ";

        for(int i = 0; i< cartonesDisponibles.length; i++){
            listaCartones.add(inicio.concat(Integer.toString(cartonesDisponibles[i])));
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
            cargar_contenido_carton(linea);
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde recurso raw");
        }
    }

    private void cargar_contenido_carton(String contenido){
        if(cartonJugado>9)
            contenido = contenido.substring(6, contenido.length()-1);
        else
            contenido = contenido.substring(5, contenido.length()-1);

        String[] numeros = contenido.split(",");
        int h = 0;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                matrizCarton[i][j] = new Celda(numeros[h], false, false, h) ;
                h++;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Celda item = (Celda) adapterView.getItemAtPosition(i);

        if(!anuncioCompletas[ac.getItemRow(i)]) {

            marcadosCarton.add(item.getValor());
            item.setSeleccionada(!item.isSeleccionada());

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

    public void comprueba_lineas(){
        String[] lineasCompletas = new String[FILAS];
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

        if(comprueba_Bingo()){
            text = text.concat("¡BINGO!");
            modoJuego = "terminado";
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

    public boolean comprueba_Bingo(){
        Boolean bingo = true;
        for(boolean i : anuncioCompletas){
            if (!i) bingo = false;
        }
       return bingo;
    }

    /**
     * The type Tarea timer.
     */
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

  //TODO

    /*/
    1. Comentar código
    2. Modo completo
    3. Parar timer cuando atrás
    4. Nuevo botón para modo completo
    5. fragments info panel izdo
    6. Cuando panel izquierdo, para timer.
    5. Reorganizar código
     */






}


