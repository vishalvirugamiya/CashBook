package com.example.cashbook

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.cashbook.databinding.ItemCashBinding

@Suppress("UNREACHABLE_CODE")

class MyAdapter(
    private val mainActivity: MainActivity,
    private val list: ArrayList<UserData>
) : RecyclerView.Adapter<MyAdapter.Holder>() {

    inner class Holder(val binding: ItemCashBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(mainActivity)
        val binding = ItemCashBinding.inflate(layoutInflater, parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val model = list[position]

        holder.binding.notes.text = model.notes
        holder.binding.dates.text = model.dateYear
        holder.binding.times.text = model.Time

        if (model.type == "IN") {
            holder.binding.cashAmount.text = model.Amount.toString()
//            holder.binding.cashAmount.visibility = View.VISIBLE
//            holder.binding.cashOutAmount.visibility = View.GONE
        } else {
            holder.binding.cashOutAmount.text = model.Amount.toString()
//            holder.binding.cashAmount.visibility = View.GONE
//            holder.binding.cashOutAmount.visibility = View.VISIBLE
        }

        holder.binding.cliclinear.setOnClickListener {
            showPopupMenu(holder.binding.cliclinear, model, position)
        }
    }

    private fun showPopupMenu(view: View, model: UserData, position: Int) {
        val popupMenu = PopupMenu(mainActivity, view)
        popupMenu.menu.add("Delete")
        popupMenu.menu.add("Edit Transaction")

        popupMenu.setOnMenuItemClickListener {
            when (it.title) {
                "Delete" -> {

                    if (position >= 0 && position < list.size) {
                        val db = Databasehelper(mainActivity)
                        db.deleteData(model.id)
                        list.removeAt(position)
                        notifyItemRemoved(position)
                    }
                }
                "Edit Transaction" -> {
                    val intent = Intent(
                        mainActivity,
                        when (model.type) {
                            "IN" -> Cash_In::class.java
                            "OUT" -> Cash_Out::class.java
                            else -> throw IllegalArgumentException("Invalid type: ${model.type}")
                        }
                    )
                    intent.putExtra("DataIn", model)  // Ensure this key is the same as the one you use in Cash_Out
                    mainActivity.startActivity(intent)
                }
            }
            true
        }
        popupMenu.show()
    }
}
