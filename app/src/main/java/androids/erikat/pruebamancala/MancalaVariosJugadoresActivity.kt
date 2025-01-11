package androids.erikat.pruebamancala

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androids.erikat.pruebamancala.databinding.MancalaVariosJugadoresBinding
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MancalaVariosJugadoresActivity : AppCompatActivity() {
    lateinit var mibinding: MancalaVariosJugadoresBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniciarComponentes()
    }

    fun iniciarComponentes() {
        mibinding= MancalaVariosJugadoresBinding.inflate(layoutInflater)
        setContentView(mibinding.root)

        // Recibo los nombres de los jugadores creados/seleccionados en la pantalla anterior.
        val jugador1 = intent.getBundleExtra("jugador1")
        val jugador2 = intent.getBundleExtra("jugador2")

        // falta la funcionalidad de varias personas
    }

    fun mostrarDialogoVictoria(jugadorgana: String, jugadorpierde: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("$jugadorgana ha ganado!!")
            .setMessage("¿Qué deseas hacer?")
            .setPositiveButton("Salir") { dialog, _ ->
                finish()
            }
            .setNeutralButton("Volver a jugar") { dialog, _ ->
                iniciarComponentes()
                dialog.dismiss()
            }
            .setNegativeButton("Enviar a un amigo") { dialog, _ ->
                val mensajeVictoria = "$jugadorgana ha ganado a $jugadorpierde al Mancala. ¿Quieres retar a tus amigos? (Mejor enganchate al LoL)"
                enviarMensajeTexto(mensajeVictoria)
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }


    fun mostrarDialogoEmpate(jugadorgana: String, jugadorpierde: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("EMPATE")
            .setMessage("¿Qué deseas hacer?")
            .setPositiveButton("Salir") { dialog, _ ->
                finish()
            }
            .setNeutralButton("Volver a jugar") { dialog, _ ->
                iniciarComponentes()
                dialog.dismiss()
            }
            .setNegativeButton("Enviar a un amigo") { dialog, _ ->
                val mensajeVictoria = "¡¡$jugadorgana y $jugadorpierde han intentado competir al Mancala y han empatado!! ¿Crees que puedes ganar a tus amigos? (coña, no tienes amigos)"
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
}