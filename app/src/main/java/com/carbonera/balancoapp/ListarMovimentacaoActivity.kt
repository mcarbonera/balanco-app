package com.carbonera.balancoapp

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carbonera.balancoapp.adapter.MeuAdapter
import com.carbonera.balancoapp.database.DatabaseHandler
import com.carbonera.balancoapp.databinding.ActivityListarMovimentacaoBinding

class ListarMovimentacaoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListarMovimentacaoBinding
    private lateinit var banco : DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListarMovimentacaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler(this)

        binding.btVoltar.setOnClickListener {
            btVoltarOnClick()
        }
        loadCadastros()
    }

    private fun listar(): Cursor {
        return banco.cursorList()
    }

    private fun loadCadastros() {
        val registros = listar()
        val adapter = MeuAdapter(
            this,
            registros
        )

        binding.lvPrincipal.adapter = adapter
    }

    private fun btVoltarOnClick() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        loadCadastros()
    }
}

/*
binding.lvPrincipal.isClickable = true
        binding.lvPrincipal.setOnItemClickListener(OnItemClickListener { arg0, arg1, position, arg3 ->
            val o: Any = binding.lvPrincipal.getItemAtPosition(position)

            val builder = AlertDialog.Builder(this)
            val tvValorTotal = TextView(this)

            tvValorTotal.text = "OI"

            builder.setTitle("Saldo")
            builder.setView(tvValorTotal)
            builder.setCancelable(false)
            builder.setPositiveButton("OK", null)

            builder.show()
        })
*/