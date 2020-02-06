package net.simplifiedcoding.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.simplifiedcoding.R
import net.simplifiedcoding.data.models.VideoContent
import net.simplifiedcoding.databinding.RecyclerViewVideoBinding

class VideosAdapter : RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {

    private var videos = listOf<VideoContent>()
    var listener: RecyclerViewButtonClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VideoViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_view_video,
            parent,
            false
        )
    )

    override fun getItemCount() = videos.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.binding.video = videos[position]
        holder.binding.imageButtonDownload.setOnClickListener {
            listener?.onRecyclerViewItemClick(holder.binding, videos[position])
        }
        holder.binding.executePendingBindings()
    }

    fun setVideos(videos: List<VideoContent>) {
        this.videos = videos
        notifyDataSetChanged()
    }


    inner class VideoViewHolder(val binding: RecyclerViewVideoBinding) :
        RecyclerView.ViewHolder(binding.root)
}