package com.example.moviesapp.util

import android.content.Context

class AndroidResourceProvider(private val context: Context) : ResourceProvider {
    override fun getString(resourceId: Int, vararg args: Any): String {
        return if (args.isNotEmpty()) {
            context.resources.getString(resourceId, *args)
        } else {
            context.resources.getString(resourceId)
        }
    }
}

interface ResourceProvider {
    fun getString(resourceId: Int, vararg args: Any): String
}