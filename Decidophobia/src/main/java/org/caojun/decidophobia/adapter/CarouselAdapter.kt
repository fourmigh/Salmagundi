package org.caojun.decidophobia.adapter

import android.content.Context
import org.caojun.decidophobia.entity.Trojan
import org.caojun.decidophobia.entity.TrojanItem
import org.caojun.library.carousel.adapter.CarouselBaseAdapter
import org.caojun.library.carousel.view.CarouselItem

/**
 * Created by CaoJun on 2017/10/26.
 */
class CarouselAdapter: CarouselBaseAdapter<Trojan> {

    constructor(context: Context, carousel: List<Trojan>): super(context, carousel)

    override fun getCarouselItem(context: Context): CarouselItem<Trojan> = TrojanItem(context)
}