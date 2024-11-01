package com.example.ewallet

import TransactionAdapter
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var balanceTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private val transactions: MutableList<TransaksiModel> = mutableListOf() // Menyimpan transaksi

    companion object {
        private const val REQUEST_CODE_TOP_UP = 1
        private const val REQUEST_CODE_OUTCOME = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Set padding for the main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("eWallet", MODE_PRIVATE)

        // Initialize TextView for balance
        balanceTextView = findViewById(R.id.textBallance)
        updateBalance() // Display balance when activity starts

        // Initialize RecyclerView for transaction history
        recyclerView = findViewById(R.id.recyclerTransaction)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TransactionAdapter(transactions)
        recyclerView.adapter = adapter

        loadTransactionHistory() // Load transaction history on startup

        // Set click listener for top-up menu
        val txtTop: TextView = findViewById(R.id.menu1)
        txtTop.setOnClickListener {
            val intent = Intent(this, TopUpActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_TOP_UP) // Request result on return
        }

        // Set click listener for outcome menu
        val txtOutcome: TextView = findViewById(R.id.menu2)
        txtOutcome.setOnClickListener {
            val intent = Intent(this, ActivityOutcome::class.java)
            startActivityForResult(intent, REQUEST_CODE_OUTCOME) // Request result on return
        }
        // Set click listener for outcome menu
        val txtHistory: TextView = findViewById(R.id.menu3)
        txtHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_OUTCOME) // Request result on return
        }

        // Set click listener for phone dialer
        val dial: TextView = findViewById(R.id.textHallo2)
        dial.setOnClickListener {
            val dialIntent: Intent = Uri.parse("tel:08977888989").let { number ->
                Intent(Intent.ACTION_DIAL, number)
            }
            startActivity(dialIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            updateBalance() // Update balance after returning from activity
            loadTransactionHistory() // Reload transaction history after top-up or outcome
        }
    }

    private fun updateBalance() {
        val currentBalance = sharedPreferences.getFloat("balance", 0f)
        balanceTextView.text = "Rp. ${currentBalance.toInt()}" // Display balance with currency
    }

    private fun loadTransactionHistory() {
        transactions.clear() // Clear existing transactions
        val transactionSet = sharedPreferences.getStringSet("transactionHistory", emptySet()) ?: emptySet()

        transactionSet.forEach { transactionString ->
            val parts = transactionString.split(" - ")
            if (parts.size >= 2) {
                val description = parts[0]
                val amountString = parts[1].replace("Rp. ", "").trim() // Clean up the amount string
                val amount = amountString.toDoubleOrNull() ?: 0.0 
                transactions.add(TransaksiModel(amount, description))
            }
        }

        adapter.notifyDataSetChanged()
    }
}
