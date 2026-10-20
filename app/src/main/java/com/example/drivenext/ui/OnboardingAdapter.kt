package com.example.drivenext.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.drivenext.R
import com.example.drivenext.model.OnboardingItem

class OnboardingAdapter(
    private val items: List<OnboardingItem>
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivOnboarding)
        val tvTitle: TextView = itemView.findViewById(R.id.tvOnboardingTitle)
        val tvDesc: TextView = itemView.findViewById(R.id.tvOnboardingDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding_page, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvDesc.text = item.description
        holder.ivImage.setImageResource(item.imageResId)
    }

    override fun getItemCount(): Int = items.size
}