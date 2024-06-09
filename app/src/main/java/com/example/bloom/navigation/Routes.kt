package com.example.bloom.navigation

sealed class Routes(val route:String) {
    object HomeScreen:Routes("home_screen")
    object ProfileScreen:Routes("profile_screen")
    object JournalScreen:Routes("journal_screen")
    object JournalListScreen:Routes("journal_list_screen")
    object ChatRoomsListScreen:Routes("chatroomList_screen")
    object ChatRoomScreen:Routes("chatroom_screen")
    object DoctorScreen : Routes("doctor_screen")
    object ChatBotScreen : Routes("chatBot_screen")
    object SignInScreen:Routes("login_screen")
    object AppointmentScreen:Routes("appointment_screen")
    object ConfirmationScreen:Routes("confirmation_screen")
    object AchievementScreen:Routes("achievement_screen")
}