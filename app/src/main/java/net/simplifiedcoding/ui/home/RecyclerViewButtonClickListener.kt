package net.simplifiedcoding.ui.home

import net.simplifiedcoding.data.models.VideoContent
import net.simplifiedcoding.databinding.RecyclerViewVideoBinding

interface RecyclerViewButtonClickListener {
    fun onRecyclerViewItemClick(binding: RecyclerViewVideoBinding, video: VideoContent)
}