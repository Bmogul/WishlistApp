package com.example.wishlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val wishList = mutableListOf<WishItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameEntry = findViewById<EditText>(R.id.itemEntry)
        val priceEntry = findViewById<EditText>(R.id.itemPriceEntry)
        val linkEntry = findViewById<EditText>(R.id.itemLinkEntry)

        val submit = findViewById<Button>(R.id.submitButton)

        val recyclerView: RecyclerView = findViewById(R.id.wishlist)
        val layoutManager = LinearLayoutManager(this)

        val adapter = WishAdapter(wishList, recyclerView)

        submit.setOnClickListener {
            val name = nameEntry.text.toString()
            val priceText = priceEntry.text.toString()
            val link = linkEntry.text.toString()

            if (name.isNotEmpty() && priceText.isNotEmpty()) {
                try {
                    val price = priceText.toFloat()
                    wishList.add(WishItem(name, price, link))
                    adapter.notifyDataSetChanged()
21
                    nameEntry.setText("")
                    linkEntry.setText("")
                    priceEntry.setText("")
                } catch (e: NumberFormatException) {
                     Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show()
                }
            } else {
                 Toast.makeText(this, "Please add an item name and price", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter


    }
}