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
        val btnVarios= mibinding.btn2Jugadores

        btnSolitario.setOnClickListener {
            var intent= Intent(this, MancalaSolitarioActivity::class.java)
            startActivity(intent)
        }

        btnVarios.setOnClickListener {
            var intent= Intent(this, CrearJugadoresActivity::class.java)
            startActivity(intent)
        }




    }
}