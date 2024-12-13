package com.dicoding.mypemudaapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SimpleAdapter(private var jobs: List<JobOffer>) :
    RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvJobType: TextView = view.findViewById(R.id.tvJobType)
        val tvSummary: TextView = view.findViewById(R.id.tvSummary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rekomendasi_pekerjaan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobs[position]

        // Set data ke View
        holder.tvTitle.text = job.position
        holder.tvJobType.text = job.jobType
        holder.tvSummary.text = job.summary

        // Tambahkan klik listener
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, JobDetailActivity::class.java)
            intent.putExtra("jobId", job.idJobOffer)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = jobs.size

    fun updateData(newJobs: List<JobOffer>) {
        jobs = newJobs
        notifyDataSetChanged()
    }
}
