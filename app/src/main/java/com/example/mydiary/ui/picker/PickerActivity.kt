package com.example.mydiary.ui.picker

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiary.R
import com.example.mydiary.databinding.ActivityPickerBinding
import com.example.mydiary.ui.picker.mediafragment.AlbumMediaFragment
import com.example.mydiary.ui.picker.models.Cons
import com.example.mydiary.ui.picker.models.MediaFolder
import com.example.mydiary.ui.picker.utils.MediaPicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PickerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPickerBinding
    lateinit var popupWindow: PopupWindow
    lateinit var mytype: String

    val pickerViewModel: PickerViewModel by viewModels()
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.picker_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.done){
            val result = Intent()
          var  myselectedmedia=pickerViewModel.getSelectedMedia()
            result.putParcelableArrayListExtra(Cons.SELECTED_MEDIA, myselectedmedia)
            setResult(RESULT_OK, result)
            finish()
            return true
        }
        if(item.itemId==android.R.id.home){
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mytype = intent.getStringExtra(Cons.TYPE).toString()
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setTitle("")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        CoroutineScope(Dispatchers.IO).launch {

            var final = MediaPicker(this@PickerActivity, mytype)

            var m = final.queryMedia()

            var f = final.getMediaFolder(m)
            popupWindow = showfolders(f)
            withContext(Dispatchers.Main) {
                binding.pb.visibility = View.GONE
                binding.selectedAlbum.visibility = View.VISIBLE
                binding.selectedAlbum.setText(f.get(0).folderName)
                val fragment: Fragment = AlbumMediaFragment.mynewInstance(f.get(0), mytype)
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, fragment, "ali")
                    .commitAllowingStateLoss()
            }
        }

        binding.selectedAlbum.setOnClickListener {
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val density = this.resources.displayMetrics.density
//,(16 * density).toInt(),(-48 * density).toInt()
            popupWindow.showAsDropDown(binding.selectedAlbum, 0, (-10 * density).toInt())
        }
    }

    private fun showfolders(list: List<MediaFolder>): PopupWindow {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.fragment_media_selection, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)

        val adapter = FolderAdapter(mytype)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.submitlist(list)
        adapter.onfolderclick = {
            if (it.folderName == null) {
                binding.selectedAlbum.setText("Unknown")
            }
            binding.selectedAlbum.setText(it.folderName)
            popupWindow.dismiss()

            val fragment: Fragment = AlbumMediaFragment.mynewInstance(it, mytype)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, fragment, "ali")
                .commitAllowingStateLoss()
        }


        return PopupWindow(
            view,
            450, 700
        )
    }

    override fun onBackPressed() {

        super.onBackPressed()

    }

}