package com.example.myshopkute.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopkute.databinding.ItemDashboardLayouyBinding
import com.example.myshopkute.databinding.ItemListLayoutBinding
import com.example.myshopkute.models.Produces
import com.example.myshopkute.units.GlideLoader

class DashboardListAdapter(
    private val context: Context,
    private var list: ArrayList<Produces>): RecyclerView.Adapter<DashboardListAdapter.DashboardViewHolder>() {
        inner class DashboardViewHolder(private val itembinding: ItemDashboardLayouyBinding): RecyclerView.ViewHolder(itembinding.root)
        {
            fun bindData(product: Produces){
                itembinding.apply {
                    GlideLoader(context).loadProductPicture(product.image, ivDashboardItemImage)
                    tvDashboardItemTitle.text = product.title
                    tvDashboardItemPrice.text= "$${product.price}"
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        return DashboardViewHolder(
            ItemDashboardLayouyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val product = list[position]
        holder.bindData(product)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}