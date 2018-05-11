package com.rnviewpager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.applyDimension
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import com.facebook.react.ReactRootView


const val PARAM_PAGE_NR = "pageNr"

class PageFragment : Fragment() {

    companion object {

        fun newInstance(pageNr: Int): PageFragment {
            val f = PageFragment()
            val args = Bundle()
            args.putInt(PARAM_PAGE_NR, pageNr)
            f.setArguments(args)
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (container == null) {
            return null
        }

        val reactInstanceManager =
                (activity as? MainActivity)?.mReactInstanceManager
                        ?: throw IllegalStateException("BaseReactFragment must be part of a BaseReactFragmentActivity")


        val mReactRootView = ReactRootView(context)

        val bundle = Bundle()
        bundle.putInt("pageNr", arguments?.getInt(PARAM_PAGE_NR) ?: 0)

        mReactRootView.startReactApplication(reactInstanceManager, "RNViewPager", bundle)

        return mReactRootView
    }

    override fun onDestroy() {
        (view as? ReactRootView?)?.unmountReactApplication()
        super.onDestroy()
    }
}