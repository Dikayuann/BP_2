package com.example.ewallet

import TransactionAdapter
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private val transactions: MutableList<TransaksiModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        sharedPreferences = getSharedPreferences("eWallet", MODE_PRIVATE)

        recyclerView = findViewById(R.id.recycleViewHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TransactionAdapter(transactions)
        recyclerView.adapter = adapter

        loadTransactionHistory()


        val txtKembali: TextView = findViewById(R.id.textTitle1)
        txtKembali.setOnClickListener {
            finish() // Kembali ke MainActivity
        }
    }

    private fun loadTransactionHistory() {
        transactions.clear()
        val transactionSet = sharedPreferences.getStringSet("transactionHistory", emptySet()) ?: emptySet()

        transactionSet.forEach { transactionString ->
            val parts = transactionString.split(" - ")
            if (parts.size == 2) {
                val description = parts[0]
                val amount = parts[1].replace("Rp. ", "").toDouble()
                transactions.add(TransaksiModel(amount, description))
            }
        }

        adapter.notifyDataSetChanged()
    }
}
