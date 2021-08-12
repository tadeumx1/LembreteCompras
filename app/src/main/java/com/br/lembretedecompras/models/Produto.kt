package com.br.lembretedecompras.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabela_produto")
data class Produto(
    @PrimaryKey @ColumnInfo(name = "nome_produto") val nome: String
)
