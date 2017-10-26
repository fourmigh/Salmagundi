package org.caojun.decidophobia.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_carousel.*
import org.caojun.decidophobia.R
import org.caojun.decidophobia.adapter.CarouselAdapter
import org.caojun.decidophobia.entity.Trojan

/**
 * Created by CaoJun on 2017/10/26.
 */
class CarouselActivity: AppCompatActivity() {

    private val list = ArrayList<Trojan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carousel)

        list.add(Trojan(0x333333, "Text 1"))
        list.add(Trojan(0x666666, "Text 2"))
        list.add(Trojan(0x999999, "Text 3"))

        val adapter = CarouselAdapter(this, list)
        carousel.setAdapter(adapter)
    }
}