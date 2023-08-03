package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.udacity.asteroidradar.main.AsteroidListAdapter
import com.udacity.asteroidradar.network.Asteroid

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?){
    val adapter = recyclerView.adapter as AsteroidListAdapter
    adapter.submitList(data)
}


@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("asteroidStatusImageContentDescription")
fun bindDetailsStatusImageContent(imageView: ImageView, isHazardous: Boolean) {
    imageView.contentDescription = if (isHazardous) {
       imageView.context.getString(R.string.not_hazardous_asteroid_image)
    } else {
         imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    }
}

@BindingAdapter("asteroidStatusIconContentDescription")
fun bindDetailsStatusIconContent(imageView: ImageView, isHazardous: Boolean) {
    imageView.contentDescription = if (isHazardous) {
        imageView.context.getString(R.string.not_hazardous_asteroid_icon)
    } else {
        imageView.context.getString(R.string.potentially_hazardous_asteroid_icon)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
@BindingAdapter("nasaImageUrl")
fun bindImage(imageView: ImageView, imgUrl: String?){
    imgUrl?.let { 
        val imageUrl = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(RequestOptions().placeholder(R.drawable.placeholder_picture_of_day))
            .into(imageView)
    }
}

