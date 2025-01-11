package androids.erikat.pruebamancala

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
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
            mostrarDialogoPerdida()
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
            mostrarDialogoVictoria()
        }
    }

    fun mostrarDialogoPerdida() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("PERDISTE")
            .setMessage("¿Qué deseas hacer?")
            .setPositiveButton("Salir") { dialog, _ ->
                finish()
            }
            .setNeutralButton("Volver a jugar") { dialog, _ ->
                iniciarComponentes()
                dialog.dismiss()
            }
            .setNegativeButton("Enviar a un amigo") { dialog, _ ->
                val mensajeVictoria = "No consigo ganar en este juego!! Se llama Mancala y lo puedes probar a través de este enlace: https://github.com/ErikAT04/Mancala/releases"
                enviarMensajeTexto(mensajeVictoria)
                dialog.dismiss()
                mostrarDialogoPerdida()
            }

        val dialog = builder.create()
        dialog.show()
    }

    fun mostrarDialogoVictoria() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("GANASTE")
            .setMessage("¿Qué deseas hacer?")
            .setPositiveButton("Salir") { dialog, _ ->
                finish()
            }
            .setNeutralButton("Volver a jugar") { dialog, _ ->
                iniciarComponentes()
                dialog.dismiss()
            }
            .setNegativeButton("Enviar a un amigo") { dialog, _ ->
                val mensajeVictoria = "He conseguido ganar al Mancala!! Ahora prueba tú: https://github.com/ErikAT04/Mancala/releases"
                enviarMensajeTexto(mensajeVictoria)
                dialog.dismiss()
                mostrarDialogoVictoria()
            }

        val dialog = builder.create()
        dialog.show()
    }

    fun enviarMensajeTexto(mensaje: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, mensaje)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(intent, "Compartir mensaje con"))
        } else {
            Toast.makeText(this, "No hay aplicaciones disponibles para enviar el mensaje.", Toast.LENGTH_SHORT).show()
        }
    }
}