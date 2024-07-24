package com.example.openinapptask.views.fragment

import amr_handheld.network.MyViewModelFactory
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openinapptask.viewmodels.MainViewModel
import com.example.openinapptask.Adapter.LinkAdapter
import com.example.openinapptask.Model.DashboardModel
import com.example.openinapptask.R
import com.example.openinapptask.Util.TokenManager
import com.example.openinapptask.databinding.FragmentLinksBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.example.openinapptask.repo.Repository
import com.example.openinapptask.retrofit.RetrofitInterface
import java.time.LocalTime
import java.util.Calendar


class LinksFragment : Fragment() {

    lateinit var linksBinding: FragmentLinksBinding
    private var dashboardModel: DashboardModel?= null
    lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        linksBinding = FragmentLinksBinding.inflate(layoutInflater)
        val view = linksBinding.root
        TokenManager.saveToken(requireContext(), "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI")
        val mainRepository = Repository(RetrofitInterface.getInstance())
        val token = TokenManager.getToken(requireContext())
        viewModel = ViewModelProvider(this, MyViewModelFactory(mainRepository)).get(MainViewModel::class.java)
        viewModel.getDashboardData(token)
        linksBinding.txtGreeting.text = greeting()
        viewModel.dashboardData.observe(viewLifecycleOwner) { data ->
                if(data?.message=="success"){
                    linksBinding.progressBar.visibility = View.GONE
                    linksBinding.llViewAllLinks.visibility = View.VISIBLE
                    var adapter = LinkAdapter(requireContext(), data.data?.top_links,"Single")
                    linksBinding.list.adapter = adapter
                    linksBinding.list.layoutManager = LinearLayoutManager(requireContext())
                    linksBinding.list.visibility = View.VISIBLE
                    linksBinding.todayCLick.text = data.today_clicks.toString()
                    linksBinding.topLocation.text = data.top_location
                    linksBinding.topSource.text = data.top_source
                    linksBinding.totalLinks.text = data.total_links.toString()
                    linksBinding.txtRecentLinks.setOnClickListener {
                        linksBinding.txtRecentLinks.setBackgroundResource(R.drawable.round_back)
                        linksBinding.txtRecentLinks.setTextColor(Color.parseColor("#ffffff"))
                        linksBinding.txtTopLinks.setTextColor(Color.parseColor("#999CA0"))
                        linksBinding.txtTopLinks.setBackgroundResource(0)
                        adapter = LinkAdapter(requireContext(), data?.data?.recent_links,"Single")
                        linksBinding.list.adapter = adapter
                        linksBinding.list.layoutManager = LinearLayoutManager(requireContext())
                    }

                    linksBinding.txtTopLinks.setOnClickListener {
                        linksBinding.txtRecentLinks.setTextColor(Color.parseColor("#999CA0"))
                        linksBinding.txtTopLinks.setTextColor(Color.parseColor("#ffffff"))
                        linksBinding.txtTopLinks.setBackgroundResource(R.drawable.round_back)
                        linksBinding.txtRecentLinks.setBackgroundResource(0)
                        adapter = LinkAdapter(requireContext(), data?.data?.top_links,"Single")
                        linksBinding.list.adapter = adapter
                        linksBinding.list.layoutManager = LinearLayoutManager(requireContext())
                    }

                }else{
                    linksBinding.llViewAllLinks.visibility = View.GONE
                    linksBinding.list.visibility = View.GONE
                    linksBinding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Something went wrong, Please try again.", Toast.LENGTH_SHORT).show()
                }
                dashboardModel = data

            }

        linksBinding.llViewAllLinks.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_links_to_allLinksFragment)
        }

        // I have used Dummy Data to plot Chart. Api was giving null data.

        val overallUrlChart = mapOf(
            "00:00" to 0,
            "01:00" to 1,
            "02:00" to 2,
            "03:00" to 0,
            "04:00" to 4,
            "05:00" to 2,
            "06:00" to 0,
            "07:00" to 0,
            "08:00" to 7,
            "09:00" to 0,
            "10:00" to 1,
            "11:00" to 0,
            "12:00" to 3,
            "13:00" to 0,
            "14:00" to 4,
            "15:00" to 0,
            "16:00" to 0,
            "17:00" to 5,
            "18:00" to 2,
            "19:00" to 0,
            "20:00" to 6,
            "21:00" to 0,
            "22:00" to 8,
            "23:00" to 0
        )

        val entries = ArrayList<Entry>()
        val labels = ArrayList<String>()

        var index = 0
        for ((key, value) in overallUrlChart) {
            entries.add(Entry(index.toFloat(), value.toFloat()))
            labels.add(key)
            index++
        }

        val dataSet = LineDataSet(entries, "Overall URL Chart")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK
        dataSet.setDrawFilled(true)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_fill)
        dataSet.fillDrawable = drawable

        val lineData = LineData(dataSet)
        linksBinding.graph.data = lineData
        linksBinding.graph.invalidate()
        val xAxis = linksBinding.graph.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        val xMinIndex = 10
        val xMaxIndex = 15
        linksBinding.graph.setVisibleXRange(xMinIndex.toFloat(), xMaxIndex.toFloat())
        linksBinding.graph.moveViewToX((xMinIndex + xMaxIndex) / 2f)

        linksBinding.llTalkWithUs.setOnClickListener {
            openWhatsApp(dashboardModel?.support_whatsapp_number)
        }

        return view
    }

    private fun openWhatsApp(number:String?) {
        Log.e("TAG", "openWhatsApp: "+number )
        val message = "Hey OpenInApp, I have some query."
        try {
            val url = "https://api.whatsapp.com/send?phone=$number&text=${Uri.encode(message)}"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun greeting(): String {
        val hour: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For API level 26 and above
            LocalTime.now().hour
        } else {
            // For API levels below 26
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        }

        return when {
            hour in 5..11 -> "Good Morning,"
            hour in 12..16 -> "Good Afternoon,"
            hour in 17..23 -> "Good Evening,"
            else -> ""
        }
    }

}