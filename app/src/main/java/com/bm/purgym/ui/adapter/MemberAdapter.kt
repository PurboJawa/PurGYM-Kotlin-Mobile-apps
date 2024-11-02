package com.bm.purgym.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bm.purgym.R
import com.bm.purgym.data.models.MemberModel
import com.bm.purgym.databinding.ItemMemberRowBinding
import com.bm.purgym.utils.Constants

class MemberAdapter : ListAdapter<MemberModel, MemberAdapter.ViewHolder>(DIFF_CALLBACK) {
    var onMemberClick: ((MemberModel, View) -> Unit)? = null
    var currentTime: Long = System.currentTimeMillis()

    fun updateCurrentTime() {
        this.currentTime = System.currentTimeMillis()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMemberRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { listStory ->
            listStory?.let { item ->
                holder.binding.apply {
                    cardItem.setOnClickListener {
                        onMemberClick?.invoke(item, root)
                    }

                    if (!Constants.expirationChecker(item.expirationDate, currentTime)) {
                        root.setCardBackgroundColor(
                            ContextCompat.getColor(root.context, R.color.redPrimary)
                        )
                    } else {
                        root.setCardBackgroundColor(
                            ContextCompat.getColor(root.context, R.color.greenPrimary)
                        )
                    }

                    ivProfile.setImageURI(Uri.parse(item.profilePic))
                    tvName.text = item.name
                    tvGender.text = item.gender
                    tvAge.text = item.age
                    tvTelp.text = item.telp
                    tvExp.text = Constants.convertTimestamp(item.expirationDate)
                }
            }
        }
    }

    inner class ViewHolder(var binding: ItemMemberRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MemberModel>() {
            override fun areItemsTheSame(
                oldStories: MemberModel,
                newStories: MemberModel
            ): Boolean {
                return oldStories == newStories
            }

            override fun areContentsTheSame(
                oldStories: MemberModel,
                newStories: MemberModel
            ): Boolean {
                return oldStories.memberId == newStories.memberId
            }
        }
    }
}