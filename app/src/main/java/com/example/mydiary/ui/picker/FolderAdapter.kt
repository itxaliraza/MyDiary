package com.example.mydiary.ui.picker

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mydiary.R
import com.example.mydiary.databinding.ItemAlbumBinding
import com.example.mydiary.ui.picker.models.MediaFolder

import java.io.File

class FolderAdapter(var type: String) : RecyclerView.Adapter<FolderAdapter.MyviewHolder>() {
    var onfolderclick: ((MediaFolder) -> Unit)? = null

    val diffcallback = object : DiffUtil.ItemCallback<MediaFolder>() {
        override fun areContentsTheSame(
            oldItem: MediaFolder,
            newItem: MediaFolder
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(
            oldItem: MediaFolder,
            newItem: MediaFolder
        ): Boolean {
            return oldItem.folderId == newItem.folderId
        }
    }
    val differ = AsyncListDiffer(this, diffcallback)
    fun submitlist(list: List<MediaFolder>) = differ.submitList(ArrayList(list))
/* ------------------------------ differ ended-------------------------------------- */

    inner class MyviewHolder(val binding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: MediaFolder) {

            if (type == "music")
                Glide.with(binding.root).load(
                    R.drawable.ic_baseline_audiotrack_24
                ).into(binding.albumCover)
            else
                Glide.with(binding.root).load(
                    Uri.fromFile(File(item.folderCover!!))
                ).into(binding.albumCover)
            binding.albumMediaCount.setText(item.mediaFiles.count().toString())
            if(item.folderName==null){
                item.folderName="Unknown"
            }
            binding.albumName.setText(item.folderName)

        }


        init {
/*
            if (clickListener != null) {
*/
            itemView.setOnClickListener {
                onfolderclick?.invoke(differ.currentList[adapterPosition])
/*
            }
*/

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderAdapter.MyviewHolder {
        val view =
            ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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