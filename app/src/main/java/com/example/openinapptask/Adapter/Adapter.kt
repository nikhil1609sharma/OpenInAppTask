package com.example.openinapptask.Adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.openinapptask.Model.LinkItem
import com.example.openinapptask.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import java.util.TimeZone


class LinkAdapter(private val context: Context, private val dataSource: List<LinkItem>?,val type:String) :
    RecyclerView.Adapter<LinkAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.links_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val linkItem = dataSource?.get(position)
        var url = linkItem?.web_link
        holder.title.text = linkItem?.title
        holder.link.text = linkItem?.web_link
        holder.clicks.text = linkItem?.total_clicks.toString()
        val date = convertDate(linkItem?.created_at)
        holder.date.text = date
        Glide.with(context)
            .load(linkItem?.original_image)
            .error(R.drawable.no_image)
        .into(holder.img)

        holder.link.setOnClickListener {
            if (!url?.startsWith("http://")!! && !url?.startsWith("https://")!!) {
                url = "http://$url"
            }

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
        }
        holder.imgCopy.setOnClickListener {
            val clipboardManager = holder.itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", url)
            clipboardManager.setPrimaryClip(clipData)
        }
    }


    override fun getItemCount(): Int {
        return if (type=="Single"){
            4
        }else{
            dataSource?.size!!
        }

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txtTitle)
        val clicks: TextView = view.findViewById(R.id.txtClicks)
        val date: TextView = view.findViewById(R.id.txtDate)
        val link: TextView = view.findViewById(R.id.txtLink)
        val img: ImageView = view.findViewById(R.id.img)
        val imgCopy: ImageView = view.findViewById(R.id.imgCopy)
    }


    private fun convertDate(inputDate: String?): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.ENGLISH)
                val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
                val date = ZonedDateTime.parse(inputDate, inputFormatter)
                date.format(outputFormatter)
            } catch (e: DateTimeParseException) {
                "Invalid date format"
            }
        } else {
            try {
                val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.ENGLISH)
                inputFormatter.timeZone = TimeZone.getTimeZone("UTC")
                val outputFormatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                val date = inputDate?.let { inputFormatter.parse(it) }
                date?.let { outputFormatter.format(it) } ?: "Invalid date format"
            } catch (e: ParseException) {
                "Invalid date format"
            }
        }
    }
}
