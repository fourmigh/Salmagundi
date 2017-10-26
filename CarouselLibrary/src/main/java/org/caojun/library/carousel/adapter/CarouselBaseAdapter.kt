package org.caojun.library.carousel.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import org.caojun.library.carousel.view.CarouselItem
import java.util.ArrayList

/**
 * Created by CaoJun on 2017/10/25.
 */
abstract class CarouselBaseAdapter<T>: BaseAdapter {

    private val items = ArrayList<CarouselItem<T>>()

    constructor(context: Context, items: List<T>) {
        for (i in items.indices) {
            val item = getCarouselItem(context)
            item.index = i
            item.update(items[i])
            this.items.add(item)
        }
    }

    override fun getCount(): Int = items?.size

    override fun getItem(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View = items[position]

    abstract fun getCarouselItem(context: Context): CarouselItem<T>
}