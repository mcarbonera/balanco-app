package com.carbonera.balancoapp.model

import com.carbonera.balancoapp.enums.DetalheMovimentacaoEnum
import com.carbonera.balancoapp.enums.TipoMovimentacaoEnum
import java.util.Calendar
import java.util.Date

class Movimentacao(_id: Int?,
                   tipo: TipoMovimentacaoEnum?,
                   detalhe: DetalheMovimentacaoEnum?,
                   valor: Double?,
                   dataLancamento: String?) {
    var _id: Int?
    var tipo: TipoMovimentacaoEnum?
    var detalhe: DetalheMovimentacaoEnum?
    var valor: Double?
    var dataLancamento: String?
    var dataLancamentoDate: Date? = null

    init {
        this._id = _id
        this.tipo = tipo
        this.detalhe = detalhe
        this.valor = valor
        this.dataLancamento = dataLancamento

        setDateTime()
    }

    fun setDateTime() {
        if(!dataLancamento.isNullOrEmpty()) {
            val valores = dataLancamento?.split("/")
            if(valores?.size != 3) return
            val data = Calendar.getInstance()
            data.isLenient = false
            data.set(
                valores[2].toInt(),
                valores[1].toInt()-1,
                valores[0].toInt())
            this.dataLancamentoDate = data.time
        }
    }

    fun clear() {
        _id = null
        tipo = null
        detalhe = null
        valor = null
        dataLancamento = ""
        dataLancamentoDate = null
    }
}