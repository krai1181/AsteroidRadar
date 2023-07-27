package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding
import com.udacity.asteroidradar.network.Asteroid

class AsteroidListAdapter(private val onClickListener: AsteroidClickListener) :
    ListAdapter<Asteroid, AsteroidListViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AsteroidListViewHolder(AsteroidListItemBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: AsteroidListViewHolder, position: Int) {
        val property = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClickListener(property)

        }
        holder.bind(property)
    }

}


    class AsteroidClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClickListener(property: Asteroid) = clickListener(property)
    }

    class AsteroidListViewHolder(private val binding: AsteroidListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }


