package androids.erikat.pruebamancala

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androids.erikat.pruebamancala.databinding.ActivityMainBinding
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var mibinding: ActivityMainBinding
    lateinit var listabotones: List<Button>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniciarComponentes()
    }

    private fun iniciarComponentes() {
        mibinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mibinding.root)

        listabotones = listOf(
            mibinding.semilla1,
            mibinding.semilla2,
            mibinding.semilla3,
            mibinding.semilla4,
            mibinding.rumaBt
        )

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
                    desactivarBotones(numFinal)
                } catch (e: Exception) {
                    AlertDialog.Builder(this).setMessage(e.message).create().show()
                }
            }
            if (indiceBoton < 4) {
                listabotones[indiceBoton].text = ((2..4).random().toString())
            }
        }
        desactivarBotones(-1)
    }

    private fun desactivarBotones(num: Int) {
        if (num in (0..3) && listabotones[num].text.toString().toInt() == 1) {
            for (boton in listabotones){
                boton.isEnabled = false
            }
            AlertDialog.Builder(this).setMessage("¡Perdiste!").show()
        } else {
            for (boton in listabotones) {
                boton.isEnabled = boton.text.toString().toInt() > 1 && boton != mibinding.rumaBt
                if (num != -1 && boton.isEnabled && num != listabotones.indexOf(mibinding.rumaBt)) {
                    boton.isEnabled = listabotones[num] == boton
                }
            }
            var sumaBtts = 0
            for (indice in listabotones.indices-1){
                sumaBtts += listabotones[indice].text.toString().toInt()
            }
            if(sumaBtts==0){
                AlertDialog.Builder(this).setMessage("¡Ganaste!").show()
            }
        }
    }
    }