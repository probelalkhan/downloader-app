package net.simplifiedcoding.ui.base

import android.view.View

interface RecyclerViewItemClickListener<T : Any> {
    fun onRecyclerViewItemClick(view: View, item: T)
}