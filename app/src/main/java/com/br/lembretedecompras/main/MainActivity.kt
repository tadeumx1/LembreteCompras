package com.br.lembretedecompras.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.lembretedecompras.R
import com.br.lembretedecompras.databinding.ActivityMainBinding
import com.br.lembretedecompras.models.Produto
import com.br.lembretedecompras.novoproduto.NovoProdutoActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainListAdapter: MainListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
        setUpLista()
        iniciarViewModel()
        registerObserver()
    }

    private fun setListeners() {
        binding.fabNovoProduto.setOnClickListener {
            val novoProdutoIntent = Intent(this, NovoProdutoActivity::class.java)
            novoProdutoActivityResult.launch(novoProdutoIntent)
        }
    }

    private val novoProdutoActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.getStringExtra(NovoProdutoActivity.EXTRA_PRODUTO)
                    ?.let { nomeDoProduto ->
                        val produto = Produto(nomeDoProduto)
                        mainViewModel.insert(produto)
                    }
            } else {
                Toast.makeText(this, "Nenhum produto informado", Toast.LENGTH_LONG).show()
            }
        }

    private fun setUpLista() {
        mainListAdapter = MainListAdapter(deleteListener = {
            mainViewModel.apagar(it)
        })
        binding.rvProdutos.adapter = mainListAdapter
        binding.rvProdutos.layoutManager = LinearLayoutManager(this)
    }

    private fun iniciarViewModel() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun registerObserver() {
        mainViewModel.produtos.observe(this, { produtos ->
            produtos.let { mainListAdapter.setProdutos((produtos)) }
        })
    }

    // Método para criar o menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Listener para escutar o clique no botão
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btDelete -> {
                confirmaExclusao().show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmaExclusao(): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle("Lembrete de Compras")
            .setMessage("Deseja apagar sua lista?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Apagar") { dialog, _ ->
                mainViewModel.apagarTodos()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

}