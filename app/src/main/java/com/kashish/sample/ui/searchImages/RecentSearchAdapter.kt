package com.kashish.sample.ui.searchImages

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kashish.sample.R
import kotlinx.android.synthetic.main.item_search.view.*

class RecentSearchAdapter(private val context: Context, private var searchList: MutableList<String>,
                          private val searchInterface: SearchInterface) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tvSearch.text = searchList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search,
                parent, false))
    }

    fun updateList(temp: ArrayList<String>) {
        searchList = temp
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.tvSearch.setOnClickListener {
                searchInterface.onSearchTextClick(searchList[adapterPosition])
            }
        }
    }

    interface SearchInterface {
        fun onSearchTextClick(searchText: String)
    }

}


