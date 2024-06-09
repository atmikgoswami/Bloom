package com.example.bloom.profile.data.models

import androidx.annotation.DrawableRes
import com.example.bloom.R

data class Achievement(
    val badgeName:String,
    val badgeCount:Long,
    @DrawableRes val badgeImage: Int
)

val availableAchievements = listOf(
    Achievement(
        badgeName = "GREEN BADGE",
        badgeCount = 10L,
        badgeImage = R.drawable.green_badge
    ),
    Achievement(
        badgeName = "BLUE BADGE",
        badgeCount = 20L,
        badgeImage = R.drawable.blue_badge
    ),
    Achievement(
        badgeName = "RED BADGE",
        badgeCount = 30L,
        badgeImage = R.drawable.red_badge
    ),
)