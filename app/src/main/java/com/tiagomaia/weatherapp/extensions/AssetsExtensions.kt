package com.tiagomaia.weatherapp.extensions

import android.content.res.AssetManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tiagomaia.weatherapp.model.usecase.City
import java.lang.reflect.TypeVariable

fun AssetManager.readAssetsFile(fileName: String): String = open(fileName).bufferedReader().use { it.readText() }

fun <T: Any> AssetManager.readAssetsFile(fileName: String, model: Class<Array<T>>): List<T>  {
    return try {
        val s = open(fileName).bufferedReader().use { it.readText() }
        //val listType = object : TypeToken<List<type>() {}.type
        val l =  Gson().fromJson(s, model).toList()
        Log.d("Asset Manager", l.toString())
        return l
    } catch (e:Exception) {
        Log.e("Asset Manager", e.message ?:"error")
        emptyList<T>()
    }
}

