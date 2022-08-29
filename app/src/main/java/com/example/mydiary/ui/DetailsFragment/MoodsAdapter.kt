package com.example.mydiary.ui.DetailsFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiary.R
import com.example.mydiary.databinding.ItemMoodBinding

class MoodsAdapter: RecyclerView.Adapter<MoodsAdapter.MoodviewHolder>() {
    var onmoodclick: ((Int) -> Unit)? = null


/* ------------------------------ differ ended-------------------------------------- */

    inner class MoodviewHolder(val binding: ItemMoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(mood:Int) {
         binding.moodIcon.setImageResource(mood)
        }


        init {
            itemView.setOnClickListener {
                onmoodclick?.invoke(moods[adapterPosition])
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodsAdapter.MoodviewHolder {
        val view =
            ItemMoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoodviewHolder(view)
    }

    override fun onBindViewHolder(holder: MoodviewHolder, position: Int) {
        val mood = moods[position]
        holder.bind(mood)
    }

    var moods= arrayListOf(
        R.drawable.ic_happy,
        R.drawable.ic_veryhappy,
        R.drawable.ic_veryhapp,
        R.drawable.ic_excited,
        R.drawable.ic_lovely,
        R.drawable.ic_cool,
        R.drawable.ic_sad,
        R.drawable.ic_verysad,
        R.drawable.ic_veryverysad,
        R.drawable.ic_angry
    )
    override fun getItemCount(): Int {
        return moods.size
    }
}