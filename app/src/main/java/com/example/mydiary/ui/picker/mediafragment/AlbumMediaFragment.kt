package com.example.mydiary.ui.picker.mediafragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydiary.databinding.AlbumMediaBinding
import com.example.mydiary.ui.picker.PickerViewModel
import com.example.mydiary.ui.picker.models.Cons
import com.example.mydiary.ui.picker.models.MediaFolder


class AlbumMediaFragment : Fragment(){

    private var _binding: AlbumMediaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val pickerViewModel: PickerViewModel by activityViewModels()

    companion object {
        fun mynewInstance(mediafolder: MediaFolder, type: String): AlbumMediaFragment {
            val fragment = AlbumMediaFragment()
            val args = Bundle()
            args.putParcelable(Cons.MEDIA_FOLDER, mediafolder)
            args.putString(Cons.TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = AlbumMediaBinding.inflate(inflater, container, false)
        return binding.root

    }

    private var type = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        type = arguments?.getString(Cons.TYPE).toString()
        var f: MediaFolder = arguments?.get(Cons.MEDIA_FOLDER) as MediaFolder

        if (type == Cons.AUDIO) {
            binding.rv.apply {
                layoutManager = LinearLayoutManager(context)
                var musicAdapter = MusicAdapter(pickerViewModel.getSelectedMedia())
                adapter = musicAdapter
                musicAdapter.submitlist(f.mediaFiles)

            }
        } else {
            binding.rv.apply {
                layoutManager = GridLayoutManager(context, 3)
                var mediaAdapter = MediaAdapter(type, pickerViewModel.getSelectedMedia())
                adapter = mediaAdapter
                mediaAdapter.submitlist(f.mediaFiles)

            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}