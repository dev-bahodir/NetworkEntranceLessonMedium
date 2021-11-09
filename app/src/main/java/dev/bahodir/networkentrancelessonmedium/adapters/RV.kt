package dev.bahodir.networkentrancelessonmedium.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.bahodir.networkentrancelessonmedium.databinding.RvItemBinding
import dev.bahodir.networkentrancelessonmedium.room.UserForRoom

class RV(var listener: OnTouchListener) : ListAdapter<UserForRoom, RV.VH>(DU()) {

    inner class VH(var binding: RvItemBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(user: UserForRoom, position: Int) {
            Picasso.get().load(user.image).into(binding.imageUrl)
            binding.moneyName.text = user.CcyNm_UZ
            binding.moneyCourse.text = "1 ${user.Ccy} = ${user.Rate} UZS"

            itemView.setOnClickListener {
                listener.itemClick(user = user, position = position, view = it)
            }

            binding.like.setOnClickListener {
                listener.likeClick(user = user, position = position, view = it)
            }
        }
    }

    class DU : DiffUtil.ItemCallback<UserForRoom>() {
        override fun areItemsTheSame(oldItem: UserForRoom, newItem: UserForRoom): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserForRoom, newItem: UserForRoom): Boolean {
            return oldItem == newItem
        }
    }

    interface OnTouchListener {
        fun itemClick(user: UserForRoom, position: Int, view: View)

        fun likeClick(user: UserForRoom, position: Int, view: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(getItem(position), position)
    }
}