package com.carbonera.balancoapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.carbonera.balancoapp.enums.DetalheMovimentacaoEnum
import com.carbonera.balancoapp.enums.TipoMovimentacaoEnum
import com.carbonera.balancoapp.model.Movimentacao

class DatabaseHandler(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tipo INTEGER, " +
                "detalhe INTEGER, " +
                "valor REAL," +
                "dataLancamento TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "movimentacao"
        private const val ID = 0
        private const val TIPO = 1
        private const val DETALHE = 2
        private const val VALOR = 3
        private const val DATALANCAMENTO = 4

        fun getMovimentacaoByCursor(registro: Cursor): Movimentacao {
            return Movimentacao(
                registro.getInt(ID),
                TipoMovimentacaoEnum.of(registro.getInt(TIPO)),
                DetalheMovimentacaoEnum.of(registro.getInt(DETALHE)),
                registro.getDouble(VALOR),
                registro.getString(DATALANCAMENTO)
            )
        }

        fun getIdByCursor(registro: Cursor): Long {
            return registro.getLong(ID)
        }
    }

    private fun getRegistro(movimentacao: Movimentacao) : ContentValues {
        val registro = ContentValues()
        registro.put("tipo", movimentacao.tipo?.codigo)
        registro.put("detalhe", movimentacao.detalhe?.codigo)
        registro.put("valor", movimentacao.valor)
        registro.put("dataLancamento", movimentacao.dataLancamento)
        return registro
    }

    fun save(movimentacao: Movimentacao) {
        val db = this.writableDatabase
        val registro = getRegistro(movimentacao)
        db.insert(TABLE_NAME, null, registro)
    }

    fun update(movimentacao: Movimentacao) {
        val db = this.writableDatabase
        val registro = getRegistro(movimentacao)
        db.update(
            TABLE_NAME,
            registro,
            "_id=${movimentacao._id}",
            null)
    }

    fun delete(id : Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "_id=${id}", null)
    }

    fun cursorList() : Cursor {
        return cursorList(null)
    }

    fun getIdList(id: Int?) : String? {
        if(id == null)
            return null
        return "_id=${id}"
    }
    fun cursorList(id: Int?) : Cursor {
        val db = this.writableDatabase
        val registro = db.query(
            TABLE_NAME,
            null,
            getIdList(id),
            null,
            null,
            null,
            null
        )
        return registro
    }

    private fun getRegistroById(registro: Cursor) : Movimentacao? {
        val lista = getListaFromRegistros(registro)
        if(lista.isNotEmpty()) return lista[0]
        return null
    }
    private fun getListaFromRegistros(registro: Cursor) : List<Movimentacao> {
        val registros = mutableListOf<Movimentacao>()
        while(registro.moveToNext()) {
            val cadastro = getMovimentacaoByCursor(registro)
            registros.add(cadastro)
        }
        return registros
    }

    fun findById(id : Int) : Movimentacao? {
        val registro = cursorList(id)

        return getRegistroById(registro)
    }

    fun findAll() : List<Movimentacao> {
        val registro = cursorList(null)

        return getListaFromRegistros(registro)
    }
}