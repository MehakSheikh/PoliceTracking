package com.example.policetracking.adapters





import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.policetracking.databinding.FragmentDefaultBinding
import com.example.policetracking.utils.ItemClickListener

private const val ITEM_VIEW_TYPE_DEFAULT = 0
private const val ITEM_VIEW_TYPE_LOADER_DEFAULT = 1


class ListAdapterDefault(
    private val clickListener: ItemClickListener<String> // Here string will be custom object
) :
    ListAdapter<DataItemDefaultListing, RecyclerView.ViewHolder>(DiffCallbackDefaultList()) {


    override fun getItemViewType(position: Int): Int {

        return when (getItem(position)) {
            is DataItemDefaultListing.DefaultItemListing -> ITEM_VIEW_TYPE_DEFAULT

            is DataItemDefaultListing.ShimmerLoaderDefault -> ITEM_VIEW_TYPE_LOADER_DEFAULT

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)


        return when (viewType) {

            ITEM_VIEW_TYPE_DEFAULT -> {
                val binding = FragmentDefaultBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                ) // add binding accordingly
                DefaultListingViewHolder(binding)
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

        val modelProductItem = getItem(position) as DataItemDefaultListing

        when (holder) {

            is DefaultListingViewHolder -> {
                holder.bind(
                    modelProductItem as DataItemDefaultListing.DefaultItemListing,
                    clickListener
                )
            }


            is ShimmerLoaderDefaultViewHolder -> {
                holder.bind()
            }
        }
    }

    private inner class DefaultListingViewHolder(private val binding: FragmentDefaultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            modelItem: DataItemDefaultListing.DefaultItemListing,
            itemClickListener: ItemClickListener<String> // custom object here
        ) {
            binding.apply {

                //data binding to be added here
                modelItem.data.apply {
//                    model = this

//                    clickListener = itemClickListener

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


sealed class DataItemDefaultListing {

    data class DefaultItemListing(val data: String) : //Here data will be custom objectz
        DataItemDefaultListing() {
        override val id = data
    }

    data class ShimmerLoaderDefault(val loaderId: String = "-1") : DataItemDefaultListing() {
        override val id = loaderId
    }


    abstract val id: String


}

class DiffCallbackDefaultList :
    DiffUtil.ItemCallback<DataItemDefaultListing>() {

    override fun areItemsTheSame(
        oldItem: DataItemDefaultListing,
        newItem: DataItemDefaultListing
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: DataItemDefaultListing,
        newItem: DataItemDefaultListing
    ): Boolean {
        return oldItem == newItem
    }
}
