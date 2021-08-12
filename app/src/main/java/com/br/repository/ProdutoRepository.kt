package com.br.repository

import androidx.lifecycle.LiveData
import com.br.lembretedecompras.dao.ProdutoDao
import com.br.lembretedecompras.models.Produto

class ProdutoRepository(private val produtoDao: ProdutoDao) {
    val produtos: LiveData<List<Produto>> = produtoDao.getListaDeProdutos()

    suspend fun insert(produto: Produto) {
        produtoDao.insert(produto)
    }

    suspend fun apagarTodos() {
        produtoDao.deleteAll()
    }

    suspend fun apagar(produto: Produto) {
        produtoDao.delete(produto)
    }
}