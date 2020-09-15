package com.barisic.covid_19manager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barisic.covid_19manager.databinding.ItemStanjePacijentaBinding
import com.barisic.covid_19manager.models.StanjePacijenta
import java.util.*

class StanjaPacijentaAdapter(private var stanjaPacijentaList: ArrayList<StanjePacijenta>) :
    RecyclerView.Adapter<StanjaPacijentaAdapter.StanjeViewHolder>() {
    private lateinit var binding: ItemStanjePacijentaBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StanjeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemStanjePacijentaBinding.inflate(inflater, parent, false)

        return StanjeViewHolder(binding)
    }

    class StanjeViewHolder(private val binding: ItemStanjePacijentaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stanjePacijenta: StanjePacijenta) {
            binding.stanjePacijenta = stanjePacijenta
        }
    }

    override fun getItemCount(): Int {
        return stanjaPacijentaList.count()
    }

    fun reverseListOrder() {
        stanjaPacijentaList.reverse()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: StanjeViewHolder, position: Int) {
        holder.bind(stanjaPacijentaList[position])
    }
}