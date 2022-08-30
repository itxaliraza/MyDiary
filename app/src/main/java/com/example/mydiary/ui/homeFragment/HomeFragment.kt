package com.example.mydiary.ui.homeFragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydiary.R
import com.example.mydiary.utils.makeGone
import com.example.mydiary.databinding.HomeFragmentBinding
import com.example.mydiary.ui.DetailsFragment.DetailsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    val homeViewModel: HomeViewModel by viewModels()
    lateinit var diaryAdapter: DiaryAdapter
    lateinit var searchitem: MenuItem
    lateinit var searchView: SearchView
    var ishidden = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding =
            HomeFragmentBinding.bind(view)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
                searchitem = menu.findItem(R.id.search)
                searchView = searchitem.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        homeViewModel.setQuery(newText)
                        return true
                    }
                })
            }

            override fun onPrepareMenu(menu: Menu) {
                searchitem.isVisible = !ishidden
                if (ishidden)
                    searchitem.collapseActionView()
                searchView.isVisible = !ishidden

                super.onPrepareMenu(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false

                // Handle option Menu Here
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        diaryAdapter = DiaryAdapter()

        binding.rvnotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = diaryAdapter
        }

        lifecycleScope.launch {
            homeViewModel.results.collectLatest {
                binding.pb.makeGone()
                diaryAdapter.submitlist(it)
            }
        }


        diaryAdapter.ondiaryclick = {
            var f = DetailsFragment()
            val args = Bundle()
            args.putParcelable("diary", it)
            f.arguments = args
            requireActivity().supportFragmentManager.beginTransaction()
                .addToBackStack("diary")
                .add(R.id.navhostfragment, f, "details").hide(this).commit()


        }

        binding.fab.setOnClickListener {
              requireActivity().supportFragmentManager.beginTransaction()
                  .add(R.id.navhostfragment, DetailsFragment(), "details2").hide(this)
                  .addToBackStack("home").commit()
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

}