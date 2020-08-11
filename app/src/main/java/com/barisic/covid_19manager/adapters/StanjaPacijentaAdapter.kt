package com.barisic.covid_19manager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barisic.covid_19manager.databinding.ItemStanjePacijentaBinding
import com.barisic.covid_19manager.models.StanjePacijenta

class StanjaPacijentaAdapter : RecyclerView.Adapter<StanjaPacijentaAdapter.StanjeViewHolder>() {
    private lateinit var binding: ItemStanjePacijentaBinding
    private lateinit var stanjaPacijentaList: ArrayList<StanjePacijenta>

    fun StanjaPacijentaAdapter(list: ArrayList<StanjePacijenta>) {
        this.stanjaPacijentaList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StanjeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemStanjePacijentaBinding.inflate(inflater, parent, false)
        return StanjeViewHolder(binding)
    }

    class StanjeViewHolder(binding: ItemStanjePacijentaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var binding: ItemStanjePacijentaBinding

        fun StanjeViewHolder(binding: ItemStanjePacijentaBinding) {
            this.binding = binding
        }

        fun bind() {

        }
    }

    override fun getItemCount(): Int {
        return stanjaPacijentaList.count()
    }

    override fun onBindViewHolder(holder: StanjeViewHolder, position: Int) {
        holder
    }
}