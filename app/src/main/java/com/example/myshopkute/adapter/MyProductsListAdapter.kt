package com.example.myshopkute.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopkute.R
import com.example.myshopkute.activities.fragment.ProductsFragment
import com.example.myshopkute.databinding.ItemListLayoutBinding
import com.example.myshopkute.models.Produces
import com.example.myshopkute.units.GlideLoader

open class MyProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<Produces>,
    private val fragment: ProductsFragment
): RecyclerView.Adapter<MyProductsListAdapter.ItemListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,
                parent,
                false
            )
        )
    }
    inner class ItemListViewHolder(val itemListLayoutBinding: ItemListLayoutBinding):
    RecyclerView.ViewHolder(itemListLayoutBinding.root)

    override fun onBindViewHolder(holder: itemListViewHolder, position: Int) {
        val model = list[position]

        holder.itemListLayoutBinding.apply {


                GlideLoader(context).loadProductPicture(model.image, ivItemImage)

               tvItemName.text = model.title
               tvItemPrice.text = "$${model.price}"

               ibDeleteProduct.setOnClickListener {

                    fragment.deleteProduct(model.product_id)
                }
            }
        }


    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}