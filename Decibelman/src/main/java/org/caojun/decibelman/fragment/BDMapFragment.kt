package org.caojun.decibelman.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_bdmap.*
import org.caojun.decibelman.R

/**
 * Created by CaoJun on 2017/9/13.
 */
class BDMapFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_bdmap, null)
    }

    override fun onResume() {
        super.onResume()
        bdMapView.onResume()
    }

    override fun onDestroyView() {
        bdMapView.onDestroy()
        super.onDestroyView()
    }
}