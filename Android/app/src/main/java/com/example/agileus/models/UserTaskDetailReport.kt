package com.example.agileus.models

class UserTaskDetailReport(
    val id: String = "",
    val name: String = "",
    val totals: Int = 0,
    val finished: Int = 0,
    val lowPriority: Int = 0,
    val mediumPriority: Int = 0,
    val highPriority: Int = 0,
    val pendings: Int = 0,
    val canceled: Int = 0,
    val started: Int = 0,
    val revision: Int = 0,
    val onTime: Int = 0,
    val outTime: Int = 0
)