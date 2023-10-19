package com.abhinavdev.taskapp

import com.abhinavdev.taskapp.models.DataModel

object Module {
    private var dataModel: DataModel? = null
    fun getData(): DataModel? {
        return dataModel
    }

    fun setData(dataModel: DataModel?) {
        this.dataModel = dataModel
    }
}