package com.example.agileus.models

class UserMessageDetailReport (
    val id: String = "",
    val name: String = "",
    val send: Int = 0,
    val received: Int = 0,
    val read: Int = 0,
    val total: Int = 0,
    val sendBroadcast: Int = 0,
    val receivedBroadcast: Int = 0
)