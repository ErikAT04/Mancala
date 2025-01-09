package androids.erikat.pruebamancala

import android.os.Bundle
import androids.erikat.pruebamancala.databinding.MancalaVariosJugadoresBinding
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
}