package com.example.mydiary.ui.picker.mediafragment

import android.annotation.SuppressLint
import android.net.Uri
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mydiary.databinding.MediaItemBinding
import com.example.mydiary.ui.picker.models.Cons
import com.example.mydiary.ui.picker.models.DiaryMedia
import com.example.mydiary.ui.picker.models.MediaFile
import java.io.File

class MediaAdapter(
    var type: String,
    var selectedmedia: ArrayList<DiaryMedia> = arrayListOf()
) : RecyclerView.Adapter<MediaAdapter.MyviewHolder>() {

    val diffcallback = object : DiffUtil.ItemCallback<MediaFile>() {
        override fun areContentsTheSame(
            oldItem: MediaFile,
            newItem: MediaFile
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(
            oldItem: MediaFile,
            newItem: MediaFile
        ): Boolean {
            return oldItem.path == newItem.path
        }
    }
    val differ = AsyncListDiffer(this, diffcallback)
    fun submitlist(list: List<MediaFile>) = differ.submitList(ArrayList(list))
/* ------------------------------ differ ended-------------------------------------- */

    inner class MyviewHolder(val binding: MediaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: MediaFile) {
            if (type == Cons.VIDEO) {
                binding.tvDuration.visibility = View.VISIBLE
                binding.tvDuration.setText(DateUtils.formatElapsedTime(item.duration / 1000))
            } else {
                binding.tvDuration.visibility = View.GONE

            }
            Glide.with(binding.root).load(
                Uri.fromFile(File(item.path!!))
            ).into(binding.img)
            binding.checkedimg.isVisible = selectedmedia.contains(DiaryMedia(item.path, type))

        }


        init {

            itemView.setOnClickListener {
                var dm =
                    DiaryMedia(differ.currentList.get(adapterPosition).path, type)

                if (binding.checkedimg.isVisible)
                    selectedmedia.remove(dm)
                else
                    selectedmedia.add(dm)

                (binding.checkedimg.isVisible) = (binding.checkedimg.visibility == View.GONE)


            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        val view =
            MediaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyviewHolder(view)
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}