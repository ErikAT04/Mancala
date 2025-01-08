package androids.erikat.pruebamancala

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androids.erikat.pruebamancala.databinding.CrearJugadoresBinding

import androidx.appcompat.app.AppCompatActivity


class CrearJugadoresActivity : AppCompatActivity() {
    lateinit var mibinding: CrearJugadoresBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniciarComponentes()

    }

    fun iniciarComponentes() {
        mibinding = CrearJugadoresBinding.inflate(layoutInflater)
        setContentView(mibinding.root)
        val btnJugar = mibinding.btnJugar
        btnJugar.setOnClickListener {
            var nombreJ1 = mibinding.etNombreJ1.text.toString()
            var nombreJ2 = mibinding.etNombreJ2.text.toString()

            if (nombreJ1.isNotEmpty() && nombreJ2.isNotEmpty()) {
                var intent = Intent(this, MancalaVariosJugadoresActivity::class.java)
                intent.putExtra("jugador1", nombreJ1)
                intent.putExtra("jugador2", nombreJ2)
                startActivity(intent)
            }else{
                if(nombreJ1.isEmpty() && nombreJ2.isNotEmpty()){
                    Toast.makeText(
                        this,
                        "El campo del nombre del jugador 1 no esta rellenado.",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    if(nombreJ2.isEmpty() && nombreJ1.isNotEmpty()) {
                        Toast.makeText(
                            this,
                            "El campo del nombre del jugador 2 no esta rellenado.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            this,
                            "Ninguno de los campos esta rellenado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }



            }


        }
    }
}


