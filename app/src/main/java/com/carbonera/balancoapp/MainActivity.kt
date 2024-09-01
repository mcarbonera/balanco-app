package com.carbonera.balancoapp

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.carbonera.balancoapp.database.DatabaseHandler
import com.carbonera.balancoapp.databinding.ActivityMainBinding
import com.carbonera.balancoapp.enums.DetalheMovimentacaoEnum
import com.carbonera.balancoapp.enums.TipoMovimentacaoEnum
import com.carbonera.balancoapp.model.Movimentacao
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var tipoSpinner: Spinner
    private lateinit var detalheSpinner: Spinner
    private lateinit var valorEditText: EditText
    private lateinit var dataLancamentoEditText: EditText

    private lateinit var buttonLancar : Button
    private lateinit var buttonVerLancamento : Button
    private lateinit var buttonConsultarSaldo : Button

    private lateinit var banco : DatabaseHandler
    private lateinit var movimentacao: Movimentacao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler(this)

        movimentacao = Movimentacao(null,null,null,null,null)
        bindingComponentes()
        inicializaTipoSpinner()
        inicializaButtonHandlers()

        clearInputs()
    }

    private fun bindingComponentes() {
        tipoSpinner = binding.spTipo
        detalheSpinner = binding.spDetalhe
        valorEditText = binding.etValor
        dataLancamentoEditText = binding.etData

        buttonLancar = binding.btLancar
        buttonVerLancamento = binding.btVer
        buttonConsultarSaldo = binding.btSaldo
    }

    private fun inicializaTipoSpinner () {
        tipoSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            TipoMovimentacaoEnum.entries)

        tipoSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                setDetalheDropdown(true, position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                setDetalheDropdown(false, null)
            }
        }

        detalheSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                movimentacao.detalhe = DetalheMovimentacaoEnum.entries[position]
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                movimentacao.detalhe = null
            }
        }
    }

    private fun setDetalheDropdown(ativo: Boolean, position: Int?) {
        if(ativo) {
            val tipo = TipoMovimentacaoEnum.entries[position!!]
            movimentacao.tipo = tipo
            detalheSpinner.isActivated = true
            detalheSpinner.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                DetalheMovimentacaoEnum.detalheDropdown(tipo)
            )
        } else {
            detalheSpinner.isActivated = false
            movimentacao.tipo = null
            movimentacao.detalhe = null
        }
    }

    private fun inicializaButtonHandlers() {
        buttonLancar.setOnClickListener {
            btLancarOnClick()
        }
        buttonVerLancamento.setOnClickListener {
            btVerLancamentosOnClick()
        }
        buttonConsultarSaldo.setOnClickListener {
            btConsultarSaldoOnClick()
        }
    }

    private fun validateMovimentacao(): Boolean {
        if (movimentacao.tipo == null) {
            tipoSpinner.requestFocus()
            Toast.makeText(this, "O campo Tipo deve ser preenchido!", Toast.LENGTH_LONG).show()
            return false
        }
        if (movimentacao.detalhe == null) {
            detalheSpinner.requestFocus()
            Toast.makeText(this, "O campo Detalhe deve ser preenchido!", Toast.LENGTH_LONG).show()
            return false
        }
        if (movimentacao.valor == null) {
            valorEditText.requestFocus()
            Toast.makeText(this, "O campo Valor deve ser preenchido!", Toast.LENGTH_LONG).show()
            return false
        }
        if (movimentacao.dataLancamento == null) {
            dataLancamentoEditText.requestFocus()
            Toast.makeText(this, "O campo Data de Lançamento deve ser preenchido!", Toast.LENGTH_LONG).show()
            return false
        } else {
            val pattern = """\d{2}/\d{2}/\d{4}"""
            if(!Regex(pattern).matches(movimentacao.dataLancamento.toString())) {
                Toast.makeText(this, "O Campo Data de Lançamento deve estar no formado dd/MM/aaaa!", Toast.LENGTH_LONG).show()
                return false
            }
            try {
                movimentacao.setDateTime()
            } catch(exception: Exception) {
                Toast.makeText(this, "A Data de Lançamento é inválida", Toast.LENGTH_LONG).show()
                return false
            }
        }
        return true
    }

    private fun btLancarOnClick() {
        val valor = valorEditText.text.toString()
        if(valor != "") movimentacao.valor = valor.toDouble()
        movimentacao.dataLancamento = dataLancamentoEditText.text.toString()

        if (!validateMovimentacao()) return

        banco.save(movimentacao)
        clearInputs()
        Toast.makeText(this, "Sucesso!", Toast.LENGTH_LONG).show()
    }

    private fun btVerLancamentosOnClick() {
        val intent = Intent(this, ListarMovimentacaoActivity::class.java)
        startActivity(intent)
    }

    private fun btConsultarSaldoOnClick() {
        var total = 0.0

        var lista = banco.findAll();
        for(mov in lista) {
            if(TipoMovimentacaoEnum.DEBITO == mov.tipo)
                total -= mov.valor!!
            else
                total += mov.valor!!
        }

        val builder = AlertDialog.Builder(this)
        val tvValorTotal = TextView(this)

        tvValorTotal.text = total.toString()
        tvValorTotal.textSize = 24.0f
        tvValorTotal.gravity = Gravity.CENTER

        builder.setTitle("Saldo")
        builder.setView(tvValorTotal)
        builder.setCancelable(false)
        builder.setPositiveButton("OK", null)

        builder.show()
    }

    private fun clearInputs() {
        movimentacao.clear()
        tipoSpinner.setSelection(0)
        detalheSpinner.setSelection(0)
        valorEditText.setText("")
        val calendar = Calendar.getInstance()
        movimentacao.dataLancamentoDate = calendar.time
        var date = SimpleDateFormat("dd/MM/yyyy").format(movimentacao.dataLancamentoDate)
        dataLancamentoEditText.setText(date)

    }
}