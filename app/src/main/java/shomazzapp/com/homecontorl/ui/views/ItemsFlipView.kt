package shomazzapp.com.homecontorl.ui.views

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import shomazzapp.com.homecontorl.R

class ItemsFlipView : RecyclerView {
    private var itemsVisible: Int = 5
    private lateinit var topColor: Color
    private lateinit var bottomColor: Color
    private lateinit var cardsPaints: List<Paint>

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        //todo sweep between top and bottom colors

    }


}