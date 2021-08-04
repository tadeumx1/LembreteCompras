package com.br.lembretedecompras.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.br.lembretedecompras.R
import com.br.lembretedecompras.databinding.ActivityLoginBinding
import com.br.lembretedecompras.main.MainActivity
import com.br.lembretedecompras.models.RequestState
import com.br.lembretedecompras.models.Usuario

class LoginActivity : AppCompatActivity() {

    private lateinit var animacaoDoMascote: Animation
    private lateinit var animacaoDoFormulario: Animation

    private lateinit var binding: ActivityLoginBinding

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        esconderTeclado()
        iniciarAnimacao()

        iniciarListener()

        iniciarViewModel()
        iniciarObserver()

        loginViewModel.getUsuarioLogado()
    }

    private fun iniciarViewModel() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private fun iniciarObserver() {
        loginViewModel.usuarioLogadoState.observe(this, Observer {
            when (it) {
                is RequestState.Success -> {
                    binding.etEmail.setText(it.data)
                }
                is RequestState.Error -> {
                    binding.tvPasswordFeedback.text = it.throwable.message
                }
                is RequestState.Loading -> {
                    Log.i("LEMBRETECOMPRAS", "CARREGANDO")
                }
            }
        })

        loginViewModel.loginState.observe(this, Observer {
            when (it) {
                is RequestState.Success -> {
                    binding.tvPasswordFeedback.text = ""
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is RequestState.Error -> {
                    binding.tvPasswordFeedback.text = it.throwable.message
                }
                is RequestState.Loading -> {
                    Log.i("LEMBRETECOMPRAS", "CARREGANDO")
                }
            }
        })
    }

    private fun iniciarListener() {
        binding.btLogin.setOnClickListener {
            loginViewModel.logar(
                Usuario(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            )
        }

        binding.etPassword.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.ivLogin.speed = 2f
                binding.ivLogin.setMinAndMaxProgress(0.0f, 0.65f)
            } else {
                binding.ivLogin.speed = 1f
                binding.ivLogin.setMinAndMaxProgress(0.65f, 1.0f)
            }

            binding.ivLogin.playAnimation()
        }

    }

    private fun esconderTeclado() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    private fun iniciarAnimacao() {
        animacaoDoMascote = AnimationUtils.loadAnimation(this, R.anim.animacao_mascote)
        animacaoDoFormulario = AnimationUtils.loadAnimation(this, R.anim.animacao_formulario)

        binding.ivLogin.clearAnimation()
        binding.containerLogin.clearAnimation()

        binding.containerLogin.startAnimation(animacaoDoFormulario)
        binding.ivLogin.startAnimation(animacaoDoMascote)
    }
}