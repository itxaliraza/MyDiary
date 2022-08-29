package com.example.mydiary.ui.picker.mediafragment

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiary.databinding.ItemAudioBinding
import com.example.mydiary.ui.picker.models.Cons
import com.example.mydiary.ui.picker.models.DiaryMedia
import com.example.mydiary.ui.picker.models.MediaFile


class MusicAdapter(
    var selectedmusic: ArrayList<DiaryMedia>
) : RecyclerView.Adapter<MusicAdapter.MyviewHolder>() {

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

    inner class MyviewHolder(val binding: ItemAudioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: MediaFile) {

            if (item.folderName == null) {
                item.folderName = "Unknown"
            }
            binding.tvName.setText(item.folderName)
            binding.tvDuration.setText(DateUtils.formatElapsedTime(item.duration / 1000))
            binding.img.isChecked =
                selectedmusic.contains(item.path.let { DiaryMedia(it, Cons.AUDIO) })
        }


        init {

            itemView.setOnClickListener {
                var dm =
                    DiaryMedia(differ.currentList.get(adapterPosition).path, Cons.AUDIO)
                if (binding.img.isChecked)
                    selectedmusic.remove(dm)
                else
                    selectedmusic.add(dm)

                (binding.img.isChecked) = !binding.img.isChecked

            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        val view =
            ItemAudioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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