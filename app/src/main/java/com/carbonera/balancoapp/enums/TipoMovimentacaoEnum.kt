package com.carbonera.balancoapp.enums

enum class TipoMovimentacaoEnum(val codigo: Int, val descricao: String) {
    CREDITO(0, "Crédito"),
    DEBITO(1, "Débito");

    fun getLabel() : String {
        return this.descricao[0].toString()
    }

    override fun toString(): String {
        return descricao
    }

    companion object {
        fun of(codigo: Int) : TipoMovimentacaoEnum {
            return TipoMovimentacaoEnum.entries
                .filter { tipoEnum -> tipoEnum.codigo == codigo }
                .first()
        }
    }
}