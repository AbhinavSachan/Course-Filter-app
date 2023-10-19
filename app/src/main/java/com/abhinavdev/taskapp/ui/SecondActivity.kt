package com.abhinavdev.taskapp.ui

import android.annotation.SuppressLint
import android.graphics.Insets
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhinavdev.taskapp.Module
import com.abhinavdev.taskapp.R
import com.abhinavdev.taskapp.models.DataModel
import com.abhinavdev.taskapp.models.FilterModel
import com.abhinavdev.taskapp.models.Index
import com.abhinavdev.taskapp.models.Smart
import com.abhinavdev.taskapp.ui.adapters.FilterSheetAdaptor
import com.abhinavdev.taskapp.ui.adapters.GridVideoAdaptor
import com.abhinavdev.taskapp.utils.CourseFilter
import com.google.android.material.bottomsheet.BottomSheetBehavior

class SecondActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val EXTRA_COLLECTION_ID = "collection_id"
    }

    private var clearFilterTT: TextView? = null
    private var videoAdaptor: GridVideoAdaptor? = null
    private var dataModel: DataModel? = null
    private var shadow: View? = null
    private var videoRecView: RecyclerView? = null
    private var searchBar: EditText? = null
    private var smartCollection: Smart? = null


    //filter sheet properties
    private var filterAdaptor: FilterSheetAdaptor? = null
    private var filterSheetBehavior: BottomSheetBehavior<View?>? = null
    private var filterRecView: RecyclerView? = null
    private var filterTitle: TextView? = null
    private var noFilterText: TextView? = null
    private var filterSheet: View? = null

    private var showOwnedSetList: LinkedHashSet<FilterModel>? =
        linkedSetOf(FilterModel("Yes", false), FilterModel("No", false))
    private var skillSetList: LinkedHashSet<FilterModel?>? = null
    private var seriesSetList: LinkedHashSet<FilterModel?>? = null
    private var educatorSetList: LinkedHashSet<FilterModel?>? = null
    private var styleSetList: LinkedHashSet<FilterModel?>? = null
    private var curriculumSetList: LinkedHashSet<FilterModel?>? = null

    private var showOwnedLL: View? = null
    private var showOwnedFT: TextView? = null
    private var showOwnedFN: TextView? = null
    private var skillLL: View? = null
    private var skillFT: TextView? = null
    private var skillFN: TextView? = null
    private var seriesLL: View? = null
    private var seriesFT: TextView? = null
    private var seriesFN: TextView? = null
    private var educatorLL: View? = null
    private var educatorFT: TextView? = null
    private var educatorFN: TextView? = null
    private var styleLL: View? = null
    private var styleFT: TextView? = null
    private var styleFN: TextView? = null
    private var curriculumLL: View? = null
    private var curriculumFT: TextView? = null
    private var curriculumFN: TextView? = null

    private val filterSheetCallback: BottomSheetBehavior.BottomSheetCallback =
        object : BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        shadow!!.isClickable = true
                        shadow!!.isFocusable = true
                        shadow!!.alpha = 1f
                        filterSheet!!.elevation = 20f
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        shadow!!.isClickable = true
                        shadow!!.isFocusable = true
                        shadow!!.alpha = 1f
                        filterSheet!!.elevation = 20f
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        shadow!!.isClickable = false
                        shadow!!.isFocusable = false
                        filterSheet!!.elevation = 0f
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                shadow!!.alpha = slideOffset + 1f
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        smartCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getSerializable(EXTRA_COLLECTION_ID, Smart::class.java)
        } else {
            intent.extras?.getSerializable(EXTRA_COLLECTION_ID) as Smart?
        }
        dataModel = Module.getData()


        // toolbar setup
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val backBtn: View = findViewById(R.id.back_btn)
        val title: TextView = findViewById(R.id.screen_title)
        backBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        title.text = smartCollection?.label

        filterSheet = findViewById(R.id.filter_sheet)
        filterRecView = findViewById(R.id.filter_rec_view)
        filterTitle = findViewById(R.id.filter_title)
        shadow = findViewById(R.id.shadow)
        filterSheetBehavior = filterSheet?.let { BottomSheetBehavior.from(it) }

        setObserverForBottomSheetHeight(filterSheet)

        filterSheetBehavior!!.addBottomSheetCallback(filterSheetCallback)
        videoRecView = findViewById(R.id.video_rec_view)
        searchBar = findViewById(R.id.search_bar)
        videoRecView?.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this, 2)

        val result = dataModel?.record?.result

        val videoListToDisplay: MutableList<Index?> = mutableListOf()
        // since i know that courses are not null so i m using this (!!) otherwise its not recommended
        for (course in smartCollection?.courses!!) {
            for (index in result?.index!!) {
                if (index?.id == course) {
                    videoListToDisplay.add(index)
                }
            }
        }

        searchBar?.addTextChangedListener {
            val query = it?.toString()
            if (query?.isNotEmpty() == true){
                videoAdaptor?.search(query)
            }else{
                videoAdaptor?.clearSearch()
            }
        }

        videoAdaptor = GridVideoAdaptor(videoListToDisplay, this)

        videoRecView?.layoutManager = layoutManager
        videoRecView?.adapter = videoAdaptor

        setFilterList()
        setFilterButtons()

    }

    private fun setFilterList() {
        skillSetList = linkedSetOf()
        seriesSetList = linkedSetOf()
        educatorSetList = linkedSetOf()
        styleSetList = linkedSetOf()
        curriculumSetList = linkedSetOf()
        for (index in dataModel?.record?.result?.index!!) {
            educatorSetList?.add(FilterModel(index?.educator, false))
            for (filter in index?.skillTags!!) {
                skillSetList?.add(FilterModel(filter, false))
            }
            for (filter in index.styleTags!!) {
                styleSetList?.add(FilterModel(filter, false))
            }
            for (filter in index.seriesTags!!) {
                seriesSetList?.add(FilterModel(filter, false))
            }
            for (filter in index.curriculumTags!!) {
                curriculumSetList?.add(FilterModel(filter, false))
            }
        }
    }

    private fun setFilterButtons() {
        clearFilterTT = findViewById(R.id.clear_filters_btn)
        showOwnedLL = findViewById(R.id.show_owned_ll)
        skillLL = findViewById(R.id.skill_ll)
        seriesLL = findViewById(R.id.series_ll)
        educatorLL = findViewById(R.id.educator_ll)
        styleLL = findViewById(R.id.style_ll)
        curriculumLL = findViewById(R.id.curriculum_ll)
        showOwnedFT = findViewById(R.id.show_owned_title)
        showOwnedFN = findViewById(R.id.show_owned_text)
        skillFT = findViewById(R.id.skill_title)
        skillFN = findViewById(R.id.skill_text)
        seriesFT = findViewById(R.id.series_title)
        seriesFN = findViewById(R.id.series_text)
        educatorFT = findViewById(R.id.educator_title)
        educatorFN = findViewById(R.id.educator_text)
        styleFT = findViewById(R.id.style_title)
        styleFN = findViewById(R.id.style_text)
        curriculumFT = findViewById(R.id.curriculum_title)
        curriculumFN = findViewById(R.id.curriculum_text)

        showOwnedLL?.setOnClickListener(this)
        skillLL?.setOnClickListener(this)
        seriesLL?.setOnClickListener(this)
        educatorLL?.setOnClickListener(this)
        styleLL?.setOnClickListener(this)
        curriculumLL?.setOnClickListener(this)
        clearFilterTT?.setOnClickListener {
            clearAllFilters()
            clearFilterTT?.visibility = View.GONE
        }
        log("set")
    }

    private fun log(s: String) {
        Log.d("TAG", s)

    }

    override fun onBackPressed() {
        if (filterSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED || filterSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED) {
            filterSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            shadow?.alpha = 0f
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.show_owned_ll -> {
                setFilterAdaptor(CourseFilter.SHOW_OWNED)
            }

            R.id.skill_ll -> {
                setFilterAdaptor(CourseFilter.SKILL)
            }

            R.id.series_ll -> {
                setFilterAdaptor(CourseFilter.SERIES)
            }

            R.id.educator_ll -> {
                setFilterAdaptor(CourseFilter.EDUCATOR)
            }

            R.id.style_ll -> {
                setFilterAdaptor(CourseFilter.STYLE)
            }

            R.id.curriculum_ll -> {
                setFilterAdaptor(CourseFilter.CURRICULUM)
            }
        }

    }

    fun applyOwnedFilter() {
        clearFilterTT?.visibility = View.VISIBLE
        videoAdaptor?.applyOwnedFilter()
    }

    fun clearOwnedFilter() {
        videoAdaptor?.clearOwnedFilter()
        clearShowOwnedButtonBg()
    }

    private fun clearAllFilters() {
        videoAdaptor?.clearAllFilters()
        setSelected(null, false)
        setSelectedTitle(null, "All")
    }

    private val appliedFilters: LinkedHashMap<CourseFilter, FilterModel?> = linkedMapOf()

    fun applyFilter(filter: FilterModel?, type: CourseFilter?) {
        clearFilterTT?.visibility = View.VISIBLE
        if (type != null) {
            appliedFilters[type] = filter
        }
        videoAdaptor?.applyFilter(appliedFilters)
    }

    private fun setFilterAdaptor(courseFilter: CourseFilter) {
        log("clicked")
        val layoutManager = LinearLayoutManager(this)
        filterRecView?.layoutManager = layoutManager
        when (courseFilter) {
            CourseFilter.SHOW_OWNED -> {
                filterTitle?.text = "Only Show Owned"
                filterAdaptor = FilterSheetAdaptor(showOwnedSetList?.toMutableList(), this)
                filterRecView?.adapter = filterAdaptor
                filterAdaptor?.setFilterType(CourseFilter.SHOW_OWNED)
            }

            CourseFilter.SKILL -> {
                filterTitle?.text = "Skill"
                filterAdaptor = FilterSheetAdaptor(skillSetList?.toMutableList(), this)
                filterRecView?.adapter = filterAdaptor
                filterAdaptor?.setFilterType(CourseFilter.SKILL)
            }

            CourseFilter.SERIES -> {
                filterTitle?.text = "Series"
                filterAdaptor = FilterSheetAdaptor(seriesSetList?.toMutableList(), this)
                filterRecView?.adapter = filterAdaptor
                filterAdaptor?.setFilterType(CourseFilter.SERIES)
            }

            CourseFilter.EDUCATOR -> {
                filterTitle?.text = "Educator"
                filterAdaptor = FilterSheetAdaptor(educatorSetList?.toMutableList(), this)
                filterRecView?.adapter = filterAdaptor
                filterAdaptor?.setFilterType(CourseFilter.EDUCATOR)
            }

            CourseFilter.STYLE -> {
                filterTitle?.text = "Style"
                filterAdaptor = FilterSheetAdaptor(styleSetList?.toMutableList(), this)
                filterRecView?.adapter = filterAdaptor
                filterAdaptor?.setFilterType(CourseFilter.STYLE)
            }

            CourseFilter.CURRICULUM -> {
                filterTitle?.text = "Curriculum"
                filterAdaptor = FilterSheetAdaptor(curriculumSetList?.toMutableList(), this)
                filterRecView?.adapter = filterAdaptor
                filterAdaptor?.setFilterType(CourseFilter.CURRICULUM)
            }
        }
        filterSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun setSelected(courseFilter: CourseFilter?, isSelected: Boolean) {
        when (courseFilter) {
            CourseFilter.SHOW_OWNED -> {
                setBackground(showOwnedLL, showOwnedFT, showOwnedFN, isSelected)
            }

            CourseFilter.SKILL -> {
                setBackground(skillLL, skillFT, skillFN, isSelected)
            }

            CourseFilter.SERIES -> {
                setBackground(seriesLL, seriesFT, seriesFN, isSelected)
            }

            CourseFilter.EDUCATOR -> {
                setBackground(educatorLL, educatorFT, educatorFN, isSelected)
            }

            CourseFilter.STYLE -> {
                setBackground(styleLL, styleFT, styleFN, isSelected)
            }

            CourseFilter.CURRICULUM -> {
                setBackground(curriculumLL, curriculumFT, curriculumFN, isSelected)
            }

            null -> {
                setBackground(showOwnedLL, showOwnedFT, showOwnedFN, isSelected)
                setBackground(skillLL, skillFT, skillFN, isSelected)
                setBackground(seriesLL, seriesFT, seriesFN, isSelected)
                setBackground(educatorLL, educatorFT, educatorFN, isSelected)
                setBackground(styleLL, styleFT, styleFN, isSelected)
                setBackground(curriculumLL, curriculumFT, curriculumFN, isSelected)
            }
        }
    }

    private fun clearShowOwnedButtonBg() {
        setBackground(showOwnedLL, showOwnedFT, showOwnedFT, false)
    }

    fun setSelectedTitle(courseFilter: CourseFilter?, name: String?) {
        when (courseFilter) {
            CourseFilter.SHOW_OWNED -> {
                showOwnedFN?.text = name
            }

            CourseFilter.SKILL -> {
                skillFN?.text = name
            }

            CourseFilter.SERIES -> {
                seriesFN?.text = name
            }

            CourseFilter.EDUCATOR -> {
                educatorFN?.text = name
            }

            CourseFilter.STYLE -> {
                styleFN?.text = name
            }

            CourseFilter.CURRICULUM -> {
                curriculumFN?.text = name
            }

            null -> {
                showOwnedFN?.text = "No"
                skillFN?.text = name
                seriesFN?.text = name
                educatorFN?.text = name
                styleFN?.text = name
                curriculumFN?.text = name
            }
        }
    }

    private fun setBackground(
        layout: View?, titleTv: TextView?, nameTv: TextView?, isSelected: Boolean
    ) {
        if (isSelected) {
            layout?.background =
                ResourcesCompat.getDrawable(resources, R.drawable.round_selected_btn, null)
            titleTv?.setTextColor(getColor(R.color.text_color_light))
            nameTv?.setTextColor(getColor(R.color.text_color_light))
        } else {
            layout?.background =
                ResourcesCompat.getDrawable(resources, R.drawable.round_not_selected_btn, null)
            titleTv?.setTextColor(getColor(R.color.text_color))
            nameTv?.setTextColor(getColor(R.color.text_color))
        }
    }

    private fun setObserverForBottomSheetHeight(view: View?) {
        view?.let {
            val vto: ViewTreeObserver = it.viewTreeObserver
            vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val height = it.measuredHeight
                    if (height != 0) {
                        setBottomSheet(height, BottomSheetBehavior.STATE_HIDDEN)
                    }
                    it.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }

    private fun setBottomSheet(minimumHeight: Int, state: Int) {
        val height = getDisplaySize().height
        val finalHeight = height.coerceAtMost(minimumHeight)
        Log.d(
            "TAG", "display height $height minimum height $minimumHeight final height $finalHeight"
        )
        shadow?.alpha = 0f
        setBottomSheetProperties(
            filterSheetBehavior, height / 2, state
        )
    }

    private fun setBottomSheetProperties(
        sheet: BottomSheetBehavior<View?>?,
        peekHeight: Int,
        state: Int,
    ) {
        sheet?.isHideable = true
        sheet?.peekHeight = peekHeight
        sheet?.skipCollapsed = false
        sheet?.state = state
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        //this function will collapse the option bottom sheet if we click outside of sheet
        if (event.action == MotionEvent.ACTION_DOWN) {
            closeSheetWhenClickOutSide(filterSheetBehavior, filterSheet, event)
        }
        return super.dispatchTouchEvent(event)
    }

    private fun closeSheetWhenClickOutSide(
        sheetBehavior: BottomSheetBehavior<View?>?,
        sheet: View?,
        event: MotionEvent,
    ) {
        if (sheetBehavior!!.state == BottomSheetBehavior.STATE_COLLAPSED || sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            val outRect = Rect()
            sheet!!.getGlobalVisibleRect(outRect)
            if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun getDisplaySize(): Size {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics1 = windowManager.currentWindowMetrics
            // Gets all excluding insets
            val windowInsets = metrics1.windowInsets
            val insets: Insets =
                windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.statusBars())

            val insetsWidth: Int = insets.right + insets.left
            val insetsHeight: Int = insets.top + insets.bottom


            // Legacy size that Display#getSize reports
            val bounds = metrics1.bounds
            return Size(
                bounds.width() - insetsWidth, bounds.height() - insetsHeight
            )
        } else {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            return Size(
                metrics.widthPixels, metrics.heightPixels
            )
        }
    }

    fun hideSheet() {
        filterSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun applyEducatorFilter(filterName: String?) {
        clearFilterTT?.visibility = View.VISIBLE
        videoAdaptor?.applyEducatorFilter(filterName)
    }
}