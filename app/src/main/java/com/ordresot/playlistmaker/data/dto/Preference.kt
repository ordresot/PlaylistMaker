package com.ordresot.playlistmaker.data.dto

import java.lang.reflect.Type

open class Preference(
    var value: Any? = null,
    val key: String? = null,
    val type: Type? = null
    )