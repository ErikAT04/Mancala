package androids.erikat.pruebamancala

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androids.erikat.pruebamancala.databinding.ActivityTwoPlayerGameBinding
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class TwoPlayerGameView : AppCompatActivity() {
    /**Binding de la actividad*/
    lateinit var viewBinding: ActivityTwoPlayerGameBinding
    /*Listas de botones agrupadas por jugador*/
    lateinit var listaBotones: List<List<Button>>
    /**String del nombre del jugador 1*/
    lateinit var p1: String
    /**String del nombre del jugador 2*/
    lateinit var p2: String
    /**Este entero lleva la cuenta de quién de los dos jugadores está jugando
     * Al empezar la actividad, para dar algo más de dinamismo, se empieza eligiendo un número aleatorio entre 0 y 1
     */
    var jugadorActual: Int = (0..1).random()

    /**Función sobrescrita de la creación de la vista
     *
     * Inicia los componentes y botones, además de iniciar el juego*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniciarComponentes()
        iniciarBotones()
        iniciarJuego()
    }

    /**Función de chequeo del estado de los botones -> Devuelve verdadero si alguno sigue activo.*/
    private fun checkAllButtons(): Boolean {
        for (sublista in listaBotones) {
            for (boton in sublista) {
                if (boton.isEnabled) return true
            }
        }
        return false
    }

    /**Función de activación de botones -> Activa todos los botones de un jugador siempre y cuando tengan más de una ficha*/
    private fun activarBotones() {
        for (boton in listaBotones[jugadorActual].subList(0, 4)) {
            boton.isEnabled = boton.text.toString().toInt() > 1
        }
    }

    /**Función de bloqueo de botones -> Según el último botón modificado, lleva a cabo distintas acciones
     *
     * Si el último botón modificado está en la lista del jugador actual y es la ruma, tiene otro turno entero
     *
     * Si el último botón modificado está en la lista del jugador y solo tiene 1 semilla (Estaba vacía):
     *  - Si la posición opuesta (sin contar las rumas) del otro jugador tiene semillas, el actual se queda con sus semillas
     *  - Si la posición opuesta del otro jugador no tiene semillas, se acaba el juego
     *
     * Si el último botón modificado está en la otra lista, se cambia de jugador
     *
     * Si no cumple ninguna de las anteriores condiciones, simplemente se sigue jugando desde ese botón
     * Después de eso, se mira todos los botones, y si todos están apagados (no se puede seguir jugando), se termina el juego
     */
    private fun bloquearBotones(posLista: Int, posSubLista: Int) {
        desactivarBotones()
        if (jugadorActual == posLista && 4 == posSubLista) {
            activarBotones()
        } else if (posLista == jugadorActual && listaBotones[posLista][posSubLista].text.toString()
                .toInt() == 1
        ) {
            if (listaBotones[(jugadorActual + 1) % 2][3 - posSubLista].text.toString()
                    .toInt() != 0
            ) {
                var suma: Int = (listaBotones[jugadorActual][4].text.toString()
                    .toInt()) + (listaBotones[(jugadorActual + 1) % 2][3 - posSubLista].text.toString()
                    .toInt())
                listaBotones[jugadorActual][4].text = "$suma"
                listaBotones[(jugadorActual + 1) % 2][3 - posSubLista].text = "0"
                activarBotones()
            } else {
                terminarJuego()
            }
        } else if (posLista != jugadorActual) {
            cambiarJugador()
        } else {
            listaBotones[posLista][posSubLista].isEnabled = true
        }
        if (!checkAllButtons()) {
            terminarJuego()
        }
    }

    /**Función de fin de juego -> Según los valores de las rumas de cada jugador, gana uno u otro*/
    private fun terminarJuego() {
        if (checkAllButtons()) {
            desactivarBotones()
        }
        val cuentaRuma1: Int = listaBotones[0][4].text.toString().toInt()
        val cuentaRuma2: Int = listaBotones[1][4].text.toString().toInt()
        if (cuentaRuma1 > cuentaRuma2) {
            Toast.makeText(this, "¡Ha ganado ${p1}!", Toast.LENGTH_SHORT).show()
        } else if (cuentaRuma1 < cuentaRuma2) {
            Toast.makeText(this, "¡Ha ganado ${p2}!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "¡Ha habido un empate!", Toast.LENGTH_SHORT).show()
        }
    }

    /**Función de desactivación de botones -> Se desactivan todos los botones*/
    private fun desactivarBotones() {
        for (sublista in listaBotones) {
            for (boton in sublista) {
                boton.isEnabled = false
            }
        }
    }

    ///Función de cambio de jugador -> Se cambia el jugador en la variable de jugador actual y se activan sus botones
    private fun cambiarJugador() {
        jugadorActual = (jugadorActual + 1) % 2
        viewBinding.turnoTView.text = "Turno de ${if (jugadorActual == 0) p1 else p2}"
        activarBotones()
    }


    /**
     * Función de inicio de botones -> Se da a todos los botones la misma lógica para jugar
     *
     * Aquí he trabajado con los índices de las listas y explico ahora por qué:
     *
     * Haciendo pruebas, vi que esos índices parece ser que se guardan en memoria.
     *
     * Es decir: El botón en la posición [0,0] cuando se presione siempre va a trabajar con indiceLista = 0 e indiceSubLista = 0
     *
     * Con ello, podía automatizar sin problema el listener de los botones a la vez.
     *
     * La lógica es la siguiente:
     *
     * - Se guarda el número de dentro de la casilla y se pone 0 en ese botón
     * - Se suma 1 a n casillas en sentido antihorario, siendo n el número guardado previamente
     *     - Después de las rumas, hay que cambiar de lado, eso se hace sumando 1 a la lista previa
     *       pero nos encontramos el problema de que la lista solo tiene 2 valores, por lo que hay que hacer una division.
     *     - Al estar trabajando modificando indiceSubLista (el valor que se mueve dentro de las sublistas), hay que hacer lo siguiente:
     *         - Para que no se salga del valor de las sublistas: (indiceSubLista + x)%tamaño de sublista (haciendo esto va de 0 al tamaño-1 de la sublista)
     *         - Para que no se salga del valor de la lista: (indiceLista + ((indiceSubLista + x))/tamaño de sublista)) % tamaño lista
     *             - Esto se hace asi por lo siguiente: Si el indice de la sublista es mayor al tamaño, si se dividen los enteros sale 1
     *             - Este 1 se suma al indice de la lista y se saca el resto entre el indice de la lista y su tamaño (0 o 1 en este caso) para pasar a la siguiente sublista
     *             - Haciendo esto, si estamos en la posicion ruma1 ([0, 4]), pasamos a la posicion 1 del jugador 2 ([1, 0])
     * - A parte de eso, una regla del juego es que no se añaden los huecos a la ruma del contrincante si pasas por ellos, por lo que hay que sumar 1 a las posiciones en ese momento
     */
    private fun iniciarBotones() {
        desactivarBotones()
        for (indiceLista in listaBotones.indices) {
            for (indiceSubLista in listaBotones[indiceLista].indices) {
                listaBotones[indiceLista][indiceSubLista].setOnClickListener {
                    try {
                        var numSumas: Int =
                            listaBotones[indiceLista][indiceSubLista].text.toString().toInt()
                        listaBotones[indiceLista][indiceSubLista].text = "0"
                        var indiceSubListaFinal = 0
                        var indiceListaFinal = 0
                        var sumaPos = 0
                        for (i in 1..numSumas) {
                            sumaPos++
                            indiceSubListaFinal =
                                (indiceSubLista + sumaPos) % listaBotones[indiceLista].size
                            indiceListaFinal =
                                (indiceLista + ((indiceSubLista + sumaPos) / listaBotones[indiceLista].size)) % listaBotones.size
                            if (indiceListaFinal != jugadorActual && indiceSubListaFinal == 4) {
                                sumaPos++
                                indiceSubListaFinal =
                                    (indiceSubLista + sumaPos) % listaBotones[indiceLista].size
                                indiceListaFinal =
                                    (indiceLista + ((indiceSubLista + sumaPos) / listaBotones[indiceLista].size)) % listaBotones.size
                            }
                            val botonActual = listaBotones[indiceListaFinal][indiceSubListaFinal]
                            botonActual.text = (botonActual.text.toString().toInt() + 1).toString()
                        }
                        bloquearBotones(indiceListaFinal, indiceSubListaFinal)
                    } catch (e: Exception) {
                        AlertDialog.Builder(this).setMessage(e.message).create().show()
                    }
                }
            }
        }
    }

    /**Función de inicio de juego -> Se da un numero aleatorio de 2 a 4 a las casillas que no sean la ruma*/
    private fun iniciarJuego() {
        for (sublista in listaBotones) {
            for (indice in sublista.indices - 4) {
                sublista[indice].text = ((2..4).random().toString())
            }
        }
        cambiarJugador()
    }

    /**Función de inicio de componentes -> Se inicia el layout y la lista de listas*/
    private fun iniciarComponentes() {
        viewBinding = ActivityTwoPlayerGameBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        listaBotones = listOf(
            listOf(
                viewBinding.semilla1p1,
                viewBinding.semilla2p1,
                viewBinding.semilla3p1,
                viewBinding.semilla4p1,
                viewBinding.rumap1
            ),
            listOf(
                viewBinding.semilla1p2,
                viewBinding.semilla2p2,
                viewBinding.semilla3p2,
                viewBinding.semilla4p2,
                viewBinding.rumap2
            )
        )
        p1 = intent.getStringExtra("jugador1") ?: "Jugador 1"
        p2 = intent.getStringExtra("jugador2") ?: "Jugador 2"

        viewBinding.p1Name.text = p1
        viewBinding.p2Name.text = p2
    }
}