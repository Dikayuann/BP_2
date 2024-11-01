package com.example.ewallet

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityOutcome : AppCompatActivity() {
    private lateinit var editAmount: EditText
    private lateinit var editDescription: EditText
    private lateinit var buttonSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_outcome)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editAmount = findViewById(R.id.editTextAmount) // Updated ID
        editDescription = findViewById(R.id.editTextDesc)
        buttonSave = findViewById(R.id.buttonSave)

        findViewById<TextView>(R.id.textTitle1).setOnClickListener { finish() }

        buttonSave.setOnClickListener { recordOutcome() }
    }

    private fun recordOutcome() {
        val amountString = editAmount.text.toString()
        if (amountString.isEmpty()) {
            showToast("Masukkan jumlah pengeluaran")
            return
        }

        val amount = amountString.toDoubleOrNull() ?: run {
            showToast("Jumlah tidak valid")
            return
        }

        val sharedPreferences = getSharedPreferences("eWallet", Context.MODE_PRIVATE)
        val currentBalance = sharedPreferences.getFloat("balance", 0f)

        if (amount > currentBalance) {
            showToast("Saldo tidak cukup untuk pengeluaran")
            return
        }

        with(sharedPreferences.edit()) {
            putFloat("balance", currentBalance - amount.toFloat())
            putStringSet("transactionHistory", getTransactionHistory(sharedPreferences).apply {
                add("Outcome - Rp. $amount - ${editDescription.text}")
            })
            apply()
        }

        showToast("Pengeluaran berhasil: Rp. $amount")
        editAmount.setText("")
        editDescription.setText("")
        setResult(RESULT_OK)
        finish()
    }

    private fun getTransactionHistory(sharedPreferences: SharedPreferences): MutableSet<String> {
        return sharedPreferences.getStringSet("transactionHistory", mutableSetOf()) ?: mutableSetOf()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
