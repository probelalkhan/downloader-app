package net.simplifiedcoding.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_videos.*
import kotlinx.coroutines.launch
import net.simplifiedcoding.R
import net.simplifiedcoding.data.models.VideoContent
import net.simplifiedcoding.ui.base.BaseFragment
import net.simplifiedcoding.ui.base.RecyclerViewItemClickListener
import org.kodein.di.generic.instance

class VideosFragment : BaseFragment() {

    private val factory: VideosViewModelFactory by instance()
    private lateinit var videosAdapter: VideosAdapter
    private lateinit var viewModel: VideosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_videos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(VideosViewModel::class.java)
        videosAdapter = VideosAdapter()
        recycler_view_videos.adapter = videosAdapter

        fetchAllVideos()

        viewModel.videos.observe(viewLifecycleOwner, Observer {
            videosAdapter.setVideos(it)
        })
    }

    private fun fetchAllVideos() = launch {
        viewModel.fetchAllVideos()
    }

}
