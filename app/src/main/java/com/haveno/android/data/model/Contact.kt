package com.haveno.android.data.model

import java.util.Date

data class Contact(
    val id: String,
    val name: String,
    val address: String,
    val isVerified: Boolean = false,
    val lastUsed: Date? = null,
    val notes: String? = null
)