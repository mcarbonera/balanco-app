package com.carbonera.balancoapp.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.carbonera.balancoapp.R
import com.carbonera.balancoapp.database.DatabaseHandler.Companion.getIdByCursor
import com.carbonera.balancoapp.database.DatabaseHandler.Companion.getMovimentacaoByCursor
import com.carbonera.balancoapp.model.Movimentacao

class MeuAdapter(private val context : Context, private val cursor : Cursor): BaseAdapter() {
    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(position: Int): Any {
        cursor.moveToPosition(position)
        val cadastro = getMovimentacaoByCursor(cursor)
        return cadastro
    }

    override fun getItemId(position: Int): Long {
        cursor.moveToPosition(position)
        return getIdByCursor(cursor)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.elemento_lista, null)

        val tvTipo = v.findViewById<TextView>(R.id.tvElementoTipo)
        val tvDataMovimentacao = v.findViewById<TextView>(R.id.tvElementoData)
        val tvDetalhe = v.findViewById<TextView>(R.id.tvElementoDetalhe)
        val tvValor = v.findViewById<TextView>(R.id.tvElementoValor)

        var movimentacao = getItem(position) as? Movimentacao

        tvTipo.text = movimentacao?.tipo?.getLabel()
        tvDataMovimentacao.text = movimentacao?.dataLancamento
        tvDetalhe.text = movimentacao?.detalhe?.descricao
        tvValor.text = movimentacao?.valor.toString()

        return v
    }
}