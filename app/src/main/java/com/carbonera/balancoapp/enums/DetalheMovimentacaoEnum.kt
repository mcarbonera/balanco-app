package com.carbonera.balancoapp.enums

enum class DetalheMovimentacaoEnum(
        val codigo: Int,
        val descricao: String,
        val tipoMovimentacaoEnum: TipoMovimentacaoEnum) {
    SALARIO(0, "Salário", TipoMovimentacaoEnum.CREDITO),
    EXTRAS(1, "Extras", TipoMovimentacaoEnum.CREDITO),

    ALIMENTACAO(2, "Alimentação", TipoMovimentacaoEnum.DEBITO),
    TRANSPORTE(3, "Transporte", TipoMovimentacaoEnum.DEBITO),
    SAUDE(4, "Saúde", TipoMovimentacaoEnum.DEBITO),
    MORADIA(5, "Moradia", TipoMovimentacaoEnum.DEBITO);

    override fun toString(): String {
        return descricao
    }

    companion object {
        fun detalheDropdown(tipoSelecionado: TipoMovimentacaoEnum) : List<DetalheMovimentacaoEnum> {
            return DetalheMovimentacaoEnum.entries
                .filter { detalheEnum -> detalheEnum.tipoMovimentacaoEnum == tipoSelecionado }
        }

        fun of(codigo: Int) : DetalheMovimentacaoEnum {
            return DetalheMovimentacaoEnum.entries
                .filter { detalheEnum -> detalheEnum.codigo == codigo }
                .first()
        }
    }
}