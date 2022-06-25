package br.com.murilofarias.checkineventos.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.murilofarias.checkineventos.R
import br.com.murilofarias.checkineventos.data.model.Event
import br.com.murilofarias.checkineventos.ui.eventlist.EventAdapter
import br.com.murilofarias.checkineventos.ui.eventlist.EventApiStatus
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Event>?) {
        val adapter = recyclerView.adapter as EventAdapter
        adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon()
            .scheme( if(imgUrl.contains("https")) "https" else "http" )
            .build()

        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

@BindingAdapter("imageEventLocation")
fun bindLocationImage(imgView: ImageView, event: Event?) {
    event?.let {
        val imgUrl = "https://maps.googleapis.com/maps/api/staticmap?center=${event.latitude},${event.longitude}&zoom=12&size=266x266&key=AIzaSyDmEiZCbS2UllDgzMHrEXyR97xfNpxI8ss"

        val imgUri = imgUrl.toUri().buildUpon()
            .scheme( "https")
            .build()

        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

@BindingAdapter("formattedDateText")
fun bindFormattedDateText(txtView: TextView, date: Long?) {
    date?.let {
        txtView.text = convertLongToDateString(it)
    }
}

@BindingAdapter("formattedPriceText")
fun bindFormattedDateText(txtView: TextView, price: Double?) {
    price?.let {
        txtView.text = String.format("$%,.2f", price).replace(".", ",")
    }
}


@BindingAdapter("eventApiStatus")
fun bindStatus(statusImageView: ImageView, status: EventApiStatus?) {
    when (status) {
        EventApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        EventApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        EventApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}
