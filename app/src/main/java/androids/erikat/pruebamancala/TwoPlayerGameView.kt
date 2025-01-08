package androids.erikat.pruebamancala

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androids.erikat.pruebamancala.databinding.ActivityTwoPlayerGameBinding
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class TwoPlayerGameView : AppCompatActivity() {
    lateinit var viewBinding: ActivityTwoPlayerGameBinding //Binding
    lateinit var listaBotones: List<List<Button>> //Listas de botones
    lateinit var p1: String //String del nombre del jugador 1
    lateinit var p2: String //String del nombre del jugador 2
    var jugadorActual: Int = (0..2).random()
    var inicio: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniciarComponentes()
        iniciarBotones()
        iniciarJuego()
    }

    private fun checkAllButtons(): Boolean {
        for (sublista in listaBotones) {
            for (boton in sublista) {
                if (boton.isEnabled) return true
            }
        }
        return false
    }

    private fun activarBotones(){
        for (boton in listaBotones[jugadorActual].subList(0, 4)) {
            boton.isEnabled = boton.text.toString().toInt() > 1
        }
    }

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
                var suma:Int = (listaBotones[jugadorActual][4].text.toString().toInt()) + (listaBotones[(jugadorActual + 1) % 2][3 - posSubLista].text.toString().toInt())
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

    private fun terminarJuego() {
        if (checkAllButtons()) {
            desactivarBotones()
        }
        val cuentaRuma1:Int = listaBotones[0][4].text.toString().toInt()
        val cuentaRuma2:Int = listaBotones[1][4].text.toString().toInt()
        if (cuentaRuma1 > cuentaRuma2) {
            Toast.makeText(this, "¡Ha ganado ${p1}!", Toast.LENGTH_SHORT).show()
        } else if(cuentaRuma1 < cuentaRuma2){
            Toast.makeText(this, "¡Ha ganado ${p2}!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "¡Ha habido un empate!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun desactivarBotones() {
        for (sublista in listaBotones) {
            for (boton in sublista) {
                boton.isEnabled = false
            }
        }
    }

    private fun cambiarJugador() {
        jugadorActual = (jugadorActual + 1) % 2
        viewBinding.turnoTView.text = "Turno de ${if (jugadorActual == 0) p1 else p2}"
        activarBotones()
    }

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

    private fun iniciarJuego() {
        for (sublista in listaBotones) {
            for (indice in sublista.indices - 4) {
                sublista[indice].text = ((2..4).random().toString())
            }
        }
        cambiarJugador()
    }

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
        p1 = intent.getStringExtra("j1") ?: "Jugador 1"
        p2 = intent.getStringExtra("j2") ?: "Jugador 2"

        viewBinding.p1Name.text = p1
        viewBinding.p2Name.text = p2
    }
}