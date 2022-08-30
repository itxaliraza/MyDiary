package com.example.mydiary.ui.homeFragment

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mydiary.Database.entity.Diary
import com.example.mydiary.R
import com.example.mydiary.databinding.ItemDiaryBinding
import com.example.mydiary.utils.makeGone
import com.example.mydiary.utils.makeInvisible
import com.example.mydiary.utils.makeVisible
import com.example.mydiary.ui.picker.models.Cons
import java.io.File

class DiaryAdapter : RecyclerView.Adapter<DiaryAdapter.MyviewHolder>() {
    var ondiaryclick: ((Diary) -> Unit)? = null

    val diffcallback = object : DiffUtil.ItemCallback<Diary>() {
        override fun areContentsTheSame(
            oldItem:Diary,
            newItem:Diary
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(
            oldItem:Diary,
            newItem:Diary
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }
    val differ = AsyncListDiffer(this, diffcallback)
    fun submitlist(list: List<Diary>) = differ.submitList(ArrayList(list))
/* ------------------------------ differ ended-------------------------------------- */

    inner class MyviewHolder(val binding: ItemDiaryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(diary:Diary) {
            binding.title.setText(diary.title)
            binding.des.setText(diary.des)
            //   "dd MMM yyyy HH:mm aa")
            var time = diary.timeStamp
            binding.day.setText(time.substring(0..1))
            binding.month.setText(time.substring(3..5))
            binding.year.setText(time.substring(7..10))
            binding.time2.setText(time.substring(12..19))
            if (diary.mood != null) {
                binding.mood.setImageResource(diary.mood)
                binding.mood.makeVisible()

            }
            else
                binding.mood.makeInvisible()
            when (diary.media.size) {
                0 -> {
                    binding.il.makeGone()
                }
                1 -> {
                    showimage1(diary, binding)
                }
                2 -> {
                    showimage2(diary, binding)

                }
                3 -> {
                    showimage3(diary, binding)

                }
                4 -> {
                    showimage4(diary, binding)

                }
                else -> {
                    showimagesmorethan(diary, binding)
                }
            }


            binding.root.setOnClickListener {
                ondiaryclick?.invoke(diary)
            }
        }


        init {
            itemView.setOnClickListener {
                ondiaryclick?.invoke(differ.currentList[adapterPosition])
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryAdapter.MyviewHolder {
        val view =
            ItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyviewHolder(view)
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun showimage1(note: Diary, binding: ItemDiaryBinding) {
        binding.il.makeVisible()

        if (note.media[0].type == Cons.AUDIO) {
            Glide.with(binding.root.context).load(R.drawable.ic_baseline_audiotrack_24)
                .into(binding.iv1)
        } else {
            Glide.with(binding.root).load(
                Uri.fromFile(File(note.media.get(0).path))
            ).into(binding.iv1)
        }


        if (note.media.get(0).type == Cons.VIDEO) {
            binding.playbtn1.makeVisible()
        }
        binding.rl1.makeVisible()

        binding.rl2.makeInvisible()
        binding.rl3.makeInvisible()
        binding.rl4.makeInvisible()

    }

    fun showimage2(diary: Diary, binding: ItemDiaryBinding) {
        showimage1(diary, binding)
        if (diary.media[1].type == Cons.AUDIO) {
            Glide.with(binding.root.context).load(R.drawable.ic_baseline_audiotrack_24)
                .into(binding.iv2)
        } else {
            Glide.with(binding.root).load(
                Uri.fromFile(File(diary.media.get(1).path))
            ).into(binding.iv2)
        }


        if (diary.media.get(1).type == Cons.VIDEO) {
            binding.playbtn2.makeVisible()
        }
        binding.rl2.makeVisible()

        binding.rl3.makeInvisible()
        binding.rl4.makeInvisible()

    }

    fun showimage3(diary:Diary, binding: ItemDiaryBinding) {
        showimage1(diary, binding)
        showimage2(diary, binding)
        if (diary.media[2].type == Cons.AUDIO) {
            Glide.with(binding.root.context).load(R.drawable.ic_baseline_audiotrack_24)
                .into(binding.iv3)
        } else {
            Glide.with(binding.root).load(
                Uri.fromFile(File(diary.media.get(2).path))
            ).into(binding.iv3)
        }


        if (diary.media.get(2).type == Cons.VIDEO) {
            binding.playbtn3.makeVisible()
        }
        binding.rl3.makeVisible()

        binding.rl4.makeInvisible()
    }

    fun showimage4(diary:Diary, binding: ItemDiaryBinding) {
        showimage1(diary, binding)
        showimage2(diary, binding)
        showimage3(diary, binding)
        if (diary.media[3].type == Cons.AUDIO) {
            Glide.with(binding.root.context).load(R.drawable.ic_baseline_audiotrack_24)
                .into(binding.iv4)
        } else {
            Glide.with(binding.root).load(
                Uri.fromFile(File(diary.media.get(3).path))
            ).into(binding.iv4)
        }


        if (diary.media.get(3).type == Cons.VIDEO) {
            binding.playbtn4.makeVisible()
        }
        binding.rl4.makeVisible()

        binding.noitems.makeGone()
        binding.playbtn4.alpha=1f
        binding.iv4.setColorFilter(null)

    }

    fun showimagesmorethan(diary:Diary, binding: ItemDiaryBinding) {
        showimage4(diary, binding)
        binding.iv4.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.transparentimage))
        binding.playbtn4.alpha=0.1f
        var n = diary.media.size - 4
        binding.noitems.setText("+$n")
        binding.noitems.makeVisible()


    }
}

