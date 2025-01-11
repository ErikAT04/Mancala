package androids.erikat.pruebamancala

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androids.erikat.pruebamancala.databinding.MancalaSolitarioBinding
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MancalaSolitarioActivity : AppCompatActivity() {
    lateinit var mibinding: MancalaSolitarioBinding
    lateinit var listabotones: List<Button>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniciarComponentes()
    }

    private fun iniciarComponentes() {
        mibinding = MancalaSolitarioBinding.inflate(layoutInflater)

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

    fun mostrarDialogoPerdida() {
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
                val mensajeVictoria = "No consigo ganar en este juego!! Se llama Mancala y lo puedes probar si quieres gritar"
                enviarMensajeTexto(mensajeVictoria)
                dialog.dismiss()
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
                val mensajeVictoria = "He conseguido ganar al Mancala!! Ahora prueba tú (no lo hagas)"
                enviarMensajeTexto(mensajeVictoria)
                dialog.dismiss()
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

    private fun desactivarBotones(num: Int) {
        if (num in (0..3) && listabotones[num].text.toString().toInt() == 1) {
            for (boton in listabotones){
                boton.isEnabled = false
            }
            mostrarDialogoPerdida()
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
                mostrarDialogoVictoria()
            }
        }
    }
}