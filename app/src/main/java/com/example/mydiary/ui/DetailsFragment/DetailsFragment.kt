package com.example.mydiary.ui.DetailsFragment

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiary.Database.entity.Diary
import com.example.mydiary.MainActivity
import com.example.mydiary.databinding.DetailsFragmentBinding
import com.example.mydiary.ui.ExpandedMediaFragment
import com.example.mydiary.ui.picker.PickerActivity
import com.example.mydiary.ui.picker.models.Cons
import com.example.mydiary.ui.picker.models.DiaryMedia
import com.example.mydiary.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class DetailsFragment : Fragment(com.example.mydiary.R.layout.details_fragment) {
    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private var nid = -1

    var ishidden = false
    lateinit var selectedtype: String

    var deletedpaths = ArrayList<String>()
    var newmedia = ArrayList<DiaryMedia>()
    lateinit var selectedmedia: DiaryMedia
    var selectedmood: Int? = null
    val detailsviewmodel: DetailsViewModel by viewModels()
    lateinit var mediaAdapter: MediaAdapter
    lateinit var medialist: ArrayList<DiaryMedia>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding =
            DetailsFragmentBinding.bind(view)
        binding.rvMedia.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            mediaAdapter = MediaAdapter()
            adapter = mediaAdapter
        }
        medialist = arrayListOf()
        mediaAdapter.onmediaclick = {
            selectedmedia = it
            val fragment: Fragment = ExpandedMediaFragment.mynewInstance(it)
            activity?.supportFragmentManager?.beginTransaction()?.addToBackStack("oa")
                ?.add(com.example.mydiary.R.id.navhostfragment, fragment, "iali")
                ?.hide(this)
                ?.commitAllowingStateLoss()
            /*  childFragmentManager.beginTransaction().add(R.id.rl, fragment, "l")
                  .addToBackStack("ll")
                  .commit()*/
        }
        binding.photo.setOnClickListener {
            selectedtype = Cons.IMAGE
            val perms =
                Manifest.permission.READ_EXTERNAL_STORAGE
            if (hasPermissions(requireContext(), perms)) {
                var i = Intent(activity, PickerActivity::class.java)
                i.putExtra(Cons.TYPE, Cons.IMAGE)
                resultLauncher.launch(i)
            } else
                requestPermissionLauncher.launch(perms)


        }
        binding.video.setOnClickListener {
            selectedtype = Cons.VIDEO

            val perms =
                Manifest.permission.READ_EXTERNAL_STORAGE
            if (hasPermissions(requireContext(), perms)) {
                var i = Intent(activity, PickerActivity::class.java)
                i.putExtra(Cons.TYPE, selectedtype)
                resultLauncher.launch(i)
            } else
                requestPermissionLauncher.launch(perms)
        }
        binding.audio.setOnClickListener {
            selectedtype = Cons.AUDIO

            val perms =
                Manifest.permission.READ_EXTERNAL_STORAGE
            if (hasPermissions(requireContext(), perms)) {
                var i = Intent(activity, PickerActivity::class.java)
                i.putExtra(Cons.TYPE, selectedtype)
                resultLauncher.launch(i)
            } else
                requestPermissionLauncher.launch(perms)

        }
        binding.mood.setOnClickListener {
            showmoodsdialog()
        }

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                menuInflater.inflate(com.example.mydiary.R.menu.menu, menu)
            }

            override fun onPrepareMenu(menu: Menu) {
                if (nid == -1 && !ishidden) {
                    menu.findItem(com.example.mydiary.R.id.delete).isVisible = false
                } else {
                    menu.findItem(com.example.mydiary.R.id.delete).isVisible = true

                }
                super.onPrepareMenu(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == com.example.mydiary.R.id.delete) {
                    if (this@DetailsFragment.isHidden) {
                        context?.alert {
                            setTitle("Confirm")
                            setMessage("Do you want to delete this media?")
                            negativeButton {
                            }
                            positiveButton {
                                deletedpaths.add(selectedmedia.path)
                                medialist.remove(selectedmedia)
                                mediaAdapter.submitlist(medialist)
                                requireActivity().onBackPressed()

                            }

                        }
                    } else {
                        context?.alert {
                            setTitle("Confirm")
                            setMessage("Do you want to delete this Memo?")
                            negativeButton {
                            }
                            positiveButton {
                               delete()
                            }

                        }
                    }


                    return true
                } else if (menuItem.itemId == android.R.id.home) {
                    requireActivity().onBackPressed()
                    return true
                }
                return false

                // Handle option Menu Here
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val bundle = this.arguments

        if (bundle != null) {
            var diary: Diary = bundle.getParcelable("diary")!!
            binding.ettitle.setText(diary.title)
            binding.etDes.setText(diary.des)
            medialist = ArrayList(diary.media)
          mediaAdapter.submitlist(medialist)
            selectedmood = diary.mood
            selectedmood?.let { binding.mood.setImageResource(it) }
            nid = diary.id!!
            (activity as MainActivity).supportActionBar?.setTitle("Update Memory")


        } else
            (activity as MainActivity).supportActionBar?.setTitle("Add Memory")

        activity?.invalidateOptionsMenu()

        binding.btnSave.setOnClickListener {
            save()
        }
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val hm2 = intent?.getParcelableArrayListExtra<DiaryMedia>(Cons.SELECTED_MEDIA)

                if (hm2 != null) {
                    medialist.addAll(hm2)
                    newmedia.addAll(hm2)
                }
                mediaAdapter.submitlist(medialist)

            }
        }


    fun save() {
        if (binding.ettitle.text.toString().equals("")) {

            Toast.makeText(
                requireContext(),
                "Title cannot be empty",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        ProgressDialogUtil.showProgressDialog()
        CoroutineScope(Dispatchers.IO).launch {
            deletedpaths.forEach {

                StorageUtils.deletefile(requireContext(), it)
            }
            for (i in newmedia) {
                var p = StorageUtils.savefile(requireContext(), i.path)
                i.path = p
            }
            withContext(Dispatchers.Main) {


                val sdf = SimpleDateFormat("dd MMM yyyy hh:mm aa")
                val currentDateAndTime: String = sdf.format(Date())
                var diary = Diary(
                    title = binding.ettitle.text.toString(),
                    des = binding.etDes.text.toString(),
                    timeStamp = currentDateAndTime, media = medialist, mood = selectedmood
                )

                if (nid == -1) {
                    detailsviewmodel.insertnote(diary)
                } else {
                    diary.id = nid
                    detailsviewmodel.updatenote(diary)
                }
                ProgressDialogUtil.dismiss()
                requireActivity().onBackPressed()


            }
        }
    }

    fun showmoodsdialog() {
        val view = layoutInflater.inflate(com.example.mydiary.R.layout.mood_layout, null);
        val moods_rv: RecyclerView = view.findViewById(com.example.mydiary.R.id.rv)
        var m = MoodsAdapter()
        moods_rv.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = m
        }
        m.onmoodclick = {
            binding.mood.setImageResource(it)
        }
        requireContext().alert {
            setView(view)
            m.onmoodclick = {
                dismissme()
                binding.mood.setImageResource(it)
                selectedmood = it

            }

        }


    }

    override fun onHiddenChanged(hidden: Boolean) {
        ishidden = hidden
        activity?.invalidateOptionsMenu()
        super.onHiddenChanged(hidden)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private val requestPermissionLauncher = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { result: Boolean ->
        if (result) {
            var i = Intent(activity, PickerActivity::class.java)
            i.putExtra(Cons.TYPE, selectedtype)
            resultLauncher.launch(i)
        } else {
            //permission denied

            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    READ_EXTERNAL_STORAGE
                )
            ) {

                context?.alert {
                    setTitle("Permission Denied")
                    setMessage("Please allow Storage permissions from settings to continue")
                    setCancelable(true)

                    setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri: Uri =
                                Uri.fromParts("package", requireActivity().packageName, null)
                            intent.data = uri
                            this@DetailsFragment.startActivity(intent)
                        }

                    })


                }

            }
        }
    }

    private fun delete() {
        for (i in medialist) {
            context?.let { StorageUtils.deletefile(it, i.path) }
        }
        detailsviewmodel.deletenote(nid)
        requireActivity().onBackPressed()

    }

    private fun hasPermissions(context: Context, permissions: String): Boolean =
        ActivityCompat.checkSelfPermission(
            context,
            permissions
        ) == PackageManager.PERMISSION_GRANTED


}