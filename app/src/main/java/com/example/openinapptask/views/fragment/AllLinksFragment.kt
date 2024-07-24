package com.example.openinapptask.views.fragment

import amr_handheld.network.MyViewModelFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openinapptask.viewmodels.MainViewModel
import com.example.openinapptask.Adapter.LinkAdapter
import com.example.openinapptask.Util.TokenManager
import com.example.openinapptask.databinding.FragmentAllLinksFragmwntBinding
import com.example.openinapptask.repo.Repository
import com.example.openinapptask.retrofit.RetrofitInterface

class AllLinksFragment : Fragment() {

    lateinit var allLinksFragmentBinding: FragmentAllLinksFragmwntBinding
    lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allLinksFragmentBinding = FragmentAllLinksFragmwntBinding.inflate(layoutInflater)
        val view = allLinksFragmentBinding.root
        val mainRepository = Repository(RetrofitInterface.getInstance())
        val token = TokenManager.getToken(requireContext())
        viewModel = ViewModelProvider(this, MyViewModelFactory(mainRepository)).get(MainViewModel::class.java)
        viewModel.getDashboardData(token)
        viewModel.dashboardData.observe(viewLifecycleOwner) { data ->
            if(data?.message=="success"){
                allLinksFragmentBinding.progressBar.visibility = View.GONE
                val adapter = LinkAdapter(requireContext(), data.data?.top_links,"AllLink")
                allLinksFragmentBinding.recyclerLinks.adapter = adapter
                allLinksFragmentBinding.recyclerLinks.layoutManager = LinearLayoutManager(requireContext())
                allLinksFragmentBinding.recyclerLinks.visibility = View.VISIBLE

            }else{
                allLinksFragmentBinding.recyclerLinks.visibility = View.GONE
                allLinksFragmentBinding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Something went wrong, Please try again.", Toast.LENGTH_SHORT).show()
            }

        }

        return view
    }

}