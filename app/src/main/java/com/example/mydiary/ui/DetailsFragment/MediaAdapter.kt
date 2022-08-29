package com.example.mydiary.ui.DetailsFragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mydiary.R
import com.example.mydiary.utils.makeGone
import com.example.mydiary.utils.makeVisible
import com.example.mydiary.utils.px
import com.example.mydiary.databinding.ItemMediaBinding
import com.example.mydiary.ui.picker.models.Cons
import com.example.mydiary.ui.picker.models.DiaryMedia

class MediaAdapter : RecyclerView.Adapter<MediaAdapter.MyviewHolder>() {
    var onmediaclick: ((DiaryMedia) -> Unit)? = null

    val diffcallback = object : DiffUtil.ItemCallback<DiaryMedia>() {
        override fun areContentsTheSame(
            oldItem: DiaryMedia,
            newItem: DiaryMedia
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(
            oldItem: DiaryMedia,
            newItem: DiaryMedia
        ): Boolean {
            return oldItem.path == newItem.path
        }
    }
    val differ = AsyncListDiffer(this, diffcallback)
    fun submitlist(list: List<DiaryMedia>) = differ.submitList(ArrayList(list))
/* ------------------------------ differ ended-------------------------------------- */

    inner class MyviewHolder(val binding: ItemMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(media: DiaryMedia) {
            Log.i("type", media.type)
            if (media.type == Cons.IMAGE) {
                binding.media.updateLayoutParams {
                    width=100.px
                    height=100.px
                }
                Glide.with(binding.root).load(media.path).into(binding.media)
                binding.playbtn.makeGone()
                binding.title.makeGone()


            } else if (media.type == Cons.VIDEO) {
                binding.media.updateLayoutParams {
                    width=100.px
                    height=100.px
                }
                Glide.with(binding.root).load(media.path).into(binding.media)
                binding.playbtn.makeVisible()

                binding.title.makeGone()

            } else {
                binding.media.updateLayoutParams {
                    width=50.px
                    height=50.px
                }
                binding.title.setText(media.path.split("/").last())
                binding.title.makeVisible()

                binding.media.setImageResource(R.drawable.ic_baseline_audiotrack_24)
            }
        }


        init {
            itemView.setOnClickListener {
                onmediaclick?.invoke(differ.currentList[adapterPosition])
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaAdapter.MyviewHolder {
        val view =
            ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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