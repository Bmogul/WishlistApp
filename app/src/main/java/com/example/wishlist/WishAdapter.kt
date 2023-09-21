package com.example.wishlist

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class WishAdapter(private val wishList: MutableList<WishItem>, private val recyclerView: RecyclerView) : RecyclerView.Adapter<WishAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.wish_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = wishList[position]
        holder.itemNameView.text = item.itemName
        holder.itemPriceView.text = "%.2f".format(item.itemPrice)
        holder.itemLinkView.text = item.itemLink
        holder.itemPos = position
        item.shake = false

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            if(item.shake){
                removeItem(holder.itemPos)
                Toast.makeText(context, "Removed: ${item.itemName}", Toast.LENGTH_SHORT).show()
            }else{
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.itemLink))
                    ContextCompat.startActivity(it.context, browserIntent, null)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(it.context, "Invalid URL for " + item.itemName, Toast.LENGTH_LONG).show()
                }
            }
            true
        }
        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context
            val shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake)
            if (item.shake){
                holder.itemView.clearAnimation()
                Toast.makeText(context, "Removing Stopped", Toast.LENGTH_SHORT).show()
                item.shake = false
            }
            else{
                Toast.makeText(context, "click again to remove: ${item.itemName}\nOr hold to stop", Toast.LENGTH_SHORT).show()
                item.shake = true
                holder.itemView.startAnimation(shakeAnimation)
            }

            true // Return true to indicate that the long click was handled
        }
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < wishList.size) {
            wishList.removeAt(position)
            notifyItemRemoved(position)
            // Update itemPos for each affected ViewHolder
            updateItemPositions()
        }
    }

    // Function to update itemPos for all ViewHolders
    private fun updateItemPositions() {
        for (i in 0 until itemCount) {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(i) as? ViewHolder
            viewHolder?.itemPos = i
        }
    }

    override fun getItemCount(): Int {
        return wishList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemNameView: TextView = itemView.findViewById<TextView>(R.id.itemName)
        val itemPriceView: TextView = itemView.findViewById<TextView>(R.id.itemPrice)
        val itemLinkView: TextView = itemView.findViewById<TextView>(R.id.itemLink)
        var itemPos: Int = 0
    }
}