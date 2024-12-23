package com.example.cashbook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cashbook.databinding.ItemCashBinding

class MyAdapter(var mainActivity: MainActivity, var List: ArrayList<UserData>) :RecyclerView.Adapter<MyAdapter.Holder>() {

    class Holder(var binding: ItemCashBinding):RecyclerView.ViewHolder(binding.root){



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        var  layoutInflater =LayoutInflater.from(mainActivity)
        var binding=ItemCashBinding.inflate(layoutInflater,parent,false)
        var holder:Holder=Holder(binding)
        return holder
    }

    override fun getItemCount(): Int {
       return List.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        var model: UserData = List[position]

        holder.binding.cashAmount.text=model.Amount
        holder.binding.notes.text = model.notes
        holder.binding.dates.text=model.dateYear
        holder.binding.times.text=model.Time


    }
}