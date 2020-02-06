package net.simplifiedcoding.ui.home

import android.view.View
import net.simplifiedcoding.data.models.VideoContent

interface RecyclerViewButtonClickListener {
    fun onRecyclerViewItemClick(view: View, video: VideoContent)
}