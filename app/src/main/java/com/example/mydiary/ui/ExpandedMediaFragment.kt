package com.example.mydiary.ui

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mydiary.utils.makeGone
import com.example.mydiary.utils.makeVisible
import com.example.mydiary.databinding.FragmentExpandedmediaBinding
import com.example.mydiary.ui.picker.models.Cons
import com.example.mydiary.ui.picker.models.DiaryMedia
import java.io.File


class ExpandedMediaFragment : Fragment(com.example.mydiary.R.layout.fragment_expandedmedia) {
    private var _binding: FragmentExpandedmediaBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun mynewInstance(media: DiaryMedia): ExpandedMediaFragment {
            val fragment = ExpandedMediaFragment()
            val args = Bundle()
            args.putParcelable(Cons.MEDIA_FOLDER, media)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding =
            FragmentExpandedmediaBinding.bind(view)
        var f: DiaryMedia = arguments?.get(Cons.MEDIA_FOLDER) as DiaryMedia
        (activity as AppCompatActivity).supportActionBar?.setTitle(f.path.split("/").last())
        if (f.type == Cons.IMAGE) {
            Glide.with(binding.root).load(File(f.path)).into(binding.mediaimage)
            binding.media.makeGone()
        } else {
            if (f.type == Cons.AUDIO) {
                binding.music.makeVisible()
            }
            val videoView: VideoView = binding.media
            binding.media.setZOrderOnTop(true)
// Set video link (mp4 format )
            // Set video link (mp4 format )
            val video: Uri = Uri.parse(f.path)
            videoView.setVideoURI(video)

            /*   mediaController.setAnchorView(videoView)
               videoView.setMediaController(mediaController)*/
            var mediaController = MediaController(context)
            var isbackpressed = false
            videoView.setOnPreparedListener { mp ->
                mediaController =
                    object : MediaController(context) {
                        override fun hide() {
                           // if (isbackpressed)
                                super.hide()
                        }

                        override fun dispatchKeyEvent(event: KeyEvent): Boolean {
                            if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                                isbackpressed = true
                                mediaController.hide()
                                activity?.onBackPressed()
                                //    mp.reset()
                                return true //If press Back button, finish here
                            }
                            //If not Back button, other button (volume) work as usual.
                            return super.dispatchKeyEvent(event)
                        }
                    }

                videoView.setMediaController(mediaController)
                videoView.start()
                mediaController.setAnchorView(videoView)
                mediaController.show()

            }


        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}