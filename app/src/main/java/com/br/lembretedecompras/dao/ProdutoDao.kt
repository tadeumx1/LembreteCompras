package com.br.lembretedecompras.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.br.lembretedecompras.models.Produto

@Dao
interface ProdutoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(produto: Produto)

    @Query("SELECT * from tabela_produto ORDER BY nome_produto ASC")
    fun getListaDeProdutos(): LiveData<List<Produto>>

    @Query("DELETE FROM tabela_produto")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(produto: Produto)
}