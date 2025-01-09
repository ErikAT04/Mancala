package androids.erikat.pruebamancala

import android.content.Intent
import android.os.Bundle
import androids.erikat.pruebamancala.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var mibinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniciarComponentes()
    }

    private fun iniciarComponentes() {
        mibinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mibinding.root)
        val btnSolitario= mibinding.btnSolitario
        val btnVariosJugadores= mibinding.btn2Jugadores

        // En función de que boton se pulse, nos enviará a la pantalla de juego en solitario
        // o la pantalla de creación/selección de jugadores.
        btnSolitario.setOnClickListener {
            var intent= Intent(this, MancalaSolitarioActivity::class.java)
            startActivity(intent)
        }

        btnVariosJugadores.setOnClickListener {
            var intent= Intent(this, CrearJugadoresActivity::class.java)
            startActivity(intent)
        }




    }
}