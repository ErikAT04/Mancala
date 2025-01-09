package androids.erikat.pruebamancala

import android.os.Bundle
import android.widget.Button
import androids.erikat.pruebamancala.databinding.ActivityOnePlayerGameBinding
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class OnePlayerGameView : AppCompatActivity() {
    lateinit var mibinding: ActivityOnePlayerGameBinding
    lateinit var listabotones: List<Button>

    /**Función sobrescrita de creación de vista
     *
     * Inicia los componentes y los botones, además de iniciar el juego*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniciarComponentes()
        iniciarBotones()
    }

    /**Función de inicio de botones -> Les da a todos los botones una función
     *
     * Haciendo pruebas vi que se guarda en la memoria de cada botón la posición de la lista, es decir, si hago el listener en lista(0), lo que se referencie como indiceboton en este siempre va a tener valor 0
     *
     * Se hace de la siguiente manera:
     *
     *  - Se guarda el valor del botón y se pone 0 en su texto.
     *  - Del siguiente botón a n puestos hacia la derecha siendo n el valor guardado, se suma 1 en cada puesto
     * - Para controlar que no se sale de la lista, saco la posición como el resto de (indice + contador)/tamaño de lista, así las posiciones van de 0 al tamaño menos 1)
     */
    private fun iniciarBotones() {
        for (indiceBoton in listabotones.indices) {
            listabotones[indiceBoton].setOnClickListener {
                try {
                    var numSumas: Int = listabotones[indiceBoton].text.toString().toInt()
                    listabotones[indiceBoton].text = "0"
                    var numFinal = 0
                    for (i in 1..numSumas) {
                        numFinal = (indiceBoton + i) % listabotones.size
                        var botonActual = listabotones[numFinal]
                        botonActual.text = (botonActual.text.toString().toInt() + 1).toString()
                    }
                    cambiarEstadoBotones(numFinal)
                } catch (e: Exception) {
                    AlertDialog.Builder(this).setMessage(e.message).create().show()
                }
            }
        }
    }

    /**Funcion de inicio de componentes*/
    private fun iniciarComponentes() {
        mibinding = ActivityOnePlayerGameBinding.inflate(layoutInflater)

        setContentView(mibinding.root)

        listabotones = listOf(
            mibinding.semilla1,
            mibinding.semilla2,
            mibinding.semilla3,
            mibinding.semilla4,
            mibinding.rumaBt
        )
        iniciarJuego()
    }

    /**Función de inicio de juego
     *
     * Todos los botones salvo la ruma reciben un valor numérico entre 2 y 4*/
    private fun iniciarJuego() {
        mibinding.rumaBt.isEnabled = false
        for (boton in listabotones.subList(0, 4)){
            boton.text = ((3..5).random().toString())
        }
        cambiarEstadoBotones(4)
    }

    /**Función de desactivación de botones
     * Hay distintas condiciones:
     *   - Si el último botón modificado no es la ruma y el valor del botón es igual a 1 (estaba vacio), se termina el juego habiendo perdido
     *   - Si el último botón modificado es es la ruma, se desbloquean todos los botones con valor (numero != 0)
     *   - Si el numero es distinto a 4 y el boton de dicha posicion no es 1, se bloquean todos menos ese
    */
    private fun cambiarEstadoBotones(num: Int) {
        if (num != 4 && listabotones[num].text.toString().toInt() == 1) { //Si el numero no es 4 y el boton de esa posicion es 1, se pierde
            for (boton in listabotones){
                boton.isEnabled = false
            }
            AlertDialog.Builder(this).setMessage("¡Perdiste!").show()
        } else if (num == 4) { //Si el numero es 4, se desbloquean todos los botones distintos a 0
            for (boton in listabotones.subList(0, 4)){
                boton.isEnabled = boton.text.toString().toInt() != 0
            }
            comprobarBotones()
        } else{ //Si el numero es distinto a 4 y el boton de dicha posicion no es 1, se bloquean todos menos ese
            for (boton in listabotones.subList(0, 4)) {
                boton.isEnabled = listabotones[num] == boton
            }
            comprobarBotones()
        }
    }

    /**Función de comprobación de botones: Mira si se ha terminado el juego (Las semillas no tienen valor)*/
    private fun comprobarBotones() {
        var sumaBtts = 0
        for (indice in listabotones.indices-4){ //Se recorren todos los indices menos el numero 4 (la ruma)
            sumaBtts += listabotones[indice].text.toString().toInt()
        }
        if(sumaBtts==0){
            AlertDialog.Builder(this).setMessage("¡Ganaste!").show()
        }
    }
}