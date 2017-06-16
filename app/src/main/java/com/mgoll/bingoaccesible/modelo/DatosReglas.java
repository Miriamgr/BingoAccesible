package com.mgoll.bingoaccesible.modelo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Miriam on 08/06/2017.
 */

public class DatosReglas {

        public static HashMap<String, List<String>> getData() {
            HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

            List<String> modos = new ArrayList<String>();
            modos.add("Modo juego bombo" + System.getProperty("line.separator") +
                    "En este modo dispondrás de un bombo virtual que por defecto viene configurado en automático. " +
                    "Si el juego está en modo automático, aparecerá una pantalla previa que nos permitirá pulsar el " +
                    "botón de empezar, desde que se pulsa, el bombo esperará 3 segundos antes de empezar a sacar " +
                    "bolas. "+ System.getProperty("line.separator") +
                    "Para parar el bombo pulsa el botón 'Parar', para continuar o iniciar la salida automática " +
                    "de bolas pulsa 'Continuar', para retroceder en el bombo pulsa el botón 'Anterior', este botón " +
                    "podrás pulsarlo tantas veces como bolas hayan salido. Para ver la bola siguiente del bombo pulsa " +
                    "el botón 'Siguiente', si has retrocedido el bombo, las bolas mostradas se mostrará en el orden " +
                    "que hayan salido anteriormente. " + System.getProperty("line.separator") +
                    "En la pantalla se irán mostrando las últimas bolas salidas. Si quieres ver todas las bolas, " +
                    "puedes pulsar el botón situado en la parte inferior de la pantalla y volverlo a pulsar para ir atrás. "
                    + System.getProperty("line.separator") + "El bombo funcionará hasta mostrar todas las bolas.");

            modos.add("Modo juego cartón" + System.getProperty("line.separator") +
                    "Al entrar en este modo, podrás elegir entre seleccionar un cartón de la lista o dejar que la " +
                    "aplicación seleccione uno de manera aleatoria. Cada cartón se corresponde con uno del juego físico, " +
                    "con la misma distribución de números que estos." + System.getProperty("line.separator") +
                    "Cuando selecciones un cartón, este aparece en la pantalla y podrás ir marcando o desmarcando las celdas " +
                    "con los números salidos. Si pulsas el botón de 'Ver marcados' se notificarán los números marcados en el " +
                    "cartón."+ System.getProperty("line.separator") +
                    "Cuando hayas seleccionado el número de celdas suficientes para completar una línea o un bingo el sistema lo " +
                    "notificará.");

            modos.add("Modo juego completo" + System.getProperty("line.separator") +
                    "Este modo tiene las mismas funcionalidades que los dos modos anteriores combinados, con la distinción " +
                    "de que, cuando se complete una línea, el juego comprobará que los números marcados coincidan con las bolas salidas.");

            modos.add("Continuar partida"+ System.getProperty("line.separator") +
                    "Cuando vuelvas al menú inicial, bien porque hayas pulsado algún elemento del menú lateral, o bien " +
                    "porque hayas presionado el botón volver de tu dispositivo, la que tuvieras empezada se guardará." +
                    System.getProperty("line.separator") + "Si entras en un modo de juego y tenías empezada una partida en " +
                    "otro modo, el sistema te dará la opción de iniciar una nueva partida en el modo seleccionado y, en " +
                    "este caso, se borrará la que tenías guardada." + System.getProperty("line.separator") +
                    "Si entras en un modo de juego y la partida que tenías empezada era en ese mismo modo, el sistema te dará" +
                    "la opción de continuar con esa partida o empezar una nueva. " + System.getProperty("line.separator") +
                    "Si sales de la aplicación no se quedará ninguna partida guardada.");

            List<String> configuracion = new ArrayList<String>();
            configuracion.add("El juego te permitirá configurar una serie de opciones desde el apartado" +
                    " 'Ajustes' del menú lateral izquierdo."+ System.getProperty("line.separator") +
                    "Podrás configurar la velocidad a la que salen las bolas del bombo, si quieres que el bombo " +
                    "sea automático por defecto, el nombre de usuario elegido o reestablecer las configuraciones " +
                    "por defecto." + System.getProperty("line.separator") +
                    "Por último, podrás configurar la dificultad de juego. Esta dificultad es por defecto normal. Aumentar " +
                    "o disminuir esta dificultad solo surtirá efecto para el modo completo, aumentando o reduciendo el número" +
                    " de bolas que salen del bombo.");

            List<String> usuario = new ArrayList<String>();
            usuario.add("El menú de usuario, accesible desde el apartado 'Cuenta' del menú lateral izquierdo." +
                    " Te permitirá ver tus estadísticas del juego o resetearlas.");

            expandableListDetail.put("Modos de juego", modos);
            expandableListDetail.put("Configuraciones", configuracion);
            expandableListDetail.put("Datos usuario", usuario);
            return expandableListDetail;
        }
}
