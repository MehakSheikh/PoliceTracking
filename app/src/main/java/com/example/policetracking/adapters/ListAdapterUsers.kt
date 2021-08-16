package com.example.policetracking.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.policetracking.databinding.FragmentDefaultBinding
import com.example.policetracking.databinding.ListItemUsersBinding
import com.example.policetracking.models.helper.UserModel
import com.example.policetracking.utils.ItemClickListener

private const val ITEM_VIEW_TYPE_DEFAULT = 0
private const val ITEM_VIEW_TYPE_LOADER_DEFAULT = 1


class ListAdapterUsers(
    private val clickListener: ItemClickListener<UserModel> // Here string will be custom object
) :
    ListAdapter<DataItemUserListing, RecyclerView.ViewHolder>(DiffCallbackUserList()) {


    override fun getItemViewType(position: Int): Int {

        return when (getItem(position)) {
            is DataItemUserListing.UserItemListing -> ITEM_VIEW_TYPE_DEFAULT

            is DataItemUserListing.ShimmerLoaderDefault -> ITEM_VIEW_TYPE_LOADER_DEFAULT

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)


        return when (viewType) {

            ITEM_VIEW_TYPE_DEFAULT -> {
                val binding = ListItemUsersBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                ) // add binding accordingly
                UserListingViewHolder(binding)
            }


            ITEM_VIEW_TYPE_LOADER_DEFAULT -> {
                val binding =
                    FragmentDefaultBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    ) // add binding accordingly
                ShimmerLoaderDefaultViewHolder(binding)
            }


            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val modelProductItem = getItem(position) as DataItemUserListing

        when (holder) {

            is UserListingViewHolder -> {
                holder.bind(
                    modelProductItem as DataItemUserListing.UserItemListing,
                    clickListener
                )
            }


            is ShimmerLoaderDefaultViewHolder -> {
                holder.bind()
            }
        }
    }

    private inner class UserListingViewHolder(private val binding: ListItemUsersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            modelItem: DataItemUserListing.UserItemListing,
            itemClickListener: ItemClickListener<UserModel> // custom object here
        ) {
            binding.apply {

                //data binding to be added here
                modelItem.data.apply {
                    model = this

                    clickListener = itemClickListener

                }

                executePendingBindings()
            }
        }

    }


    private inner class ShimmerLoaderDefaultViewHolder(private val binding: FragmentDefaultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.apply {

                executePendingBindings()
            }
        }

    }

}


sealed class DataItemUserListing {

    data class UserItemListing(val data: UserModel) : //Here data will be custom object
        DataItemUserListing() {
        override val id = data.id ?: ""
    }

    data class ShimmerLoaderDefault(val loaderId: String = "-1") : DataItemUserListing() {
        override val id = loaderId
    }


    abstract val id: String


}

class DiffCallbackUserList :
    DiffUtil.ItemCallback<DataItemUserListing>() {

    override fun areItemsTheSame(
        oldItem: DataItemUserListing,
        newItem: DataItemUserListing
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: DataItemUserListing,
        newItem: DataItemUserListing
    ): Boolean {
        return oldItem == newItem
    }
}
