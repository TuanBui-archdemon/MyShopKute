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
//    private val listener: ProductListener
): RecyclerView.Adapter<MyProductsListAdapter.ProductViewHolder>() {
    inner class ProductViewHolder(private val itembinding: ItemListLayoutBinding): RecyclerView.ViewHolder(itembinding.root)
    {
        fun bindData(product: Produces){
            itembinding.apply {
                GlideLoader(context).loadProductPicture(product.image, ivItemImage)
                tvItemName.text = product.title
                tvItemPrice.text= "$${product.price}"
                ibDeleteProduct.setOnClickListener{
                    fragment.deleteProduct(product.product_id)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = list[position]
        holder.bindData(product)
    }

    override fun getItemCount(): Int {
        return list.size
    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return MyViewHolder(
//            LayoutInflater.from(context).inflate(
//                R.layout.item_list_layout,
//                parent,
//                false
//            )
//        )
//    }
//    inner class ItemListViewHolder(val itemListLayoutBinding: ItemListLayoutBinding):
//    RecyclerView.ViewHolder(itemListLayoutBinding.root)
//
//    override fun onBindViewHolder(holder: itemListViewHolder, position: Int) {
//        val model = list[position]
//
//        holder.itemListLayoutBinding.apply {
//
//
//                GlideLoader(context).loadProductPicture(model.image, ivItemImage)
//
//               tvItemName.text = model.title
//               tvItemPrice.text = "$${model.price}"
//
//               ibDeleteProduct.setOnClickListener {
//
//                    fragment.deleteProduct(model.product_id)
//                }
//            }
//        }
//
//
//    /**
//     * Gets the number of items in the list
//     */
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    /**
//     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
//     */
//    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
//interface ProductListener{
//    fun onProductClick(product: Produces, position: Int)
//    fun onDeleteProduct(product: Produces, position: Int)
//
//}