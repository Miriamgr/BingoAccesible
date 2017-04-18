package com.mgoll.bingoaccesible.modelo;

/**
 * Created by Miriam on 11/04/2017.
 */

public class Carton {

    static final int COLUMNAS = 9;
    static final int FILAS = 3;

    private Celda[][] carton = new Celda[FILAS][COLUMNAS];

    public Carton(String contenido) {
        this.inicializa_variables(contenido);
        String[] p = this.comprueba_lineas("carton");
        String[] p2 = this.comprueba_lineas("completo");
    }

    private void inicializa_variables(String contenido){
        contenido = contenido.substring(5, contenido.length()-1);
        String[] numeros = contenido.split(",");
        int h = 0;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                carton[i][j] = new Celda(numeros[h], false, false,h) ;
                h++;
            }
        }

        for (int j = 0; j < COLUMNAS; j++) {
            if(!carton[0][j].getValor().equalsIgnoreCase(" ") ){
                carton[0][j].setSeleccionada(true);
            }
        }
        for (int j = 0; j < COLUMNAS; j++) {
            if(!carton[1][j].getValor().equalsIgnoreCase(" ") ){
                carton[1][j].setSalida(true);
            }
        }
        for (int j = 0; j < COLUMNAS; j++) {
            if(!carton[2][j].getValor().equalsIgnoreCase(" ") ){
                carton[2][j].setSeleccionada(true);
                carton[2][j].setSalida(true);
            }
        }

    }

    public String[] comprueba_lineas(String modo){
        String[] lineasCompletas = new String[FILAS]; // "l" línea, "s" salida pero no marcada, "m" marcada pero no salida, " " nada

        for(int i = 0 ; i< FILAS; i++){
            lineasCompletas[i] = this.comprueba_linea(i, modo);
        }

        return lineasCompletas;
    }

    private String comprueba_linea (int linea, String modo){

        Boolean select = true; //seleccionadas en el carton
        Boolean salida = true; //salidas
        String res = " ";

        for (int j = 0; j < COLUMNAS; j++) {
            if(!carton[linea][j].getValor().equals(" ")){
                if(!carton[linea][j].isSeleccionada())
                    select = false;
                if(modo.equalsIgnoreCase("completo") && !carton[linea][j].isSalida())
                    salida = false;
            }
        }

        if(modo.equalsIgnoreCase("carton")){
            if(select)
                res = "l";
        }
        else {
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

    public String comprueba_Bingo(String modo){
        String bingo = "";
        String[] lineasCompletas = comprueba_lineas (modo);

     //Compeltar
        for(int i = 0; i<lineasCompletas.length; i++){


            if(modo.equalsIgnoreCase("carton")){

            }
            else{

            }
        }

        return bingo;
    }


}
