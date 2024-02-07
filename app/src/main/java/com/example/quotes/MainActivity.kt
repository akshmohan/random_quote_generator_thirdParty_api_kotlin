package com.example.quotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.quotes.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getQuote()

        binding.nxtBtn.setOnClickListener{
            getQuote()
        }
    }

    private fun getQuote() {
            setInProgress(true)

            GlobalScope.launch {
                try {
                    val response = RetrofitInstance.quoteApi.getRandomQuote()

                    runOnUiThread {
                        setInProgress(false)
                        response.body()?.first().let {
                            if (it != null) {
                                setUI(it)
                            }
                        }
                    }
                } catch (e : Exception) {
                    runOnUiThread {
                        setInProgress(false)
                        Toast.makeText(applicationContext, "Not connected to the internet",
                            Toast.LENGTH_SHORT
                            ).show()
                    }
                }
            }
    }

    private fun setUI(quote : QuoteModel) {
        binding.quoteTv.text = quote.q
        binding.authorTv.text = quote.a
    }

    private fun  setInProgress(inProgress : Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.nxtBtn.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.nxtBtn.visibility = View.VISIBLE
        }
    }
}