package com.example.bloom.navigation

import android.app.Activity.RESULT_OK
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bloom.auth.presentation.GoogleAuthUiClient
import com.example.bloom.auth.presentation.SignInScreen
import com.example.bloom.auth.presentation.SignInViewModel
import com.example.bloom.chatbot.presentation.ChatBotScreen
import com.example.bloom.chatroom.presentation.ChatRoomListScreen
import com.example.bloom.chatroom.presentation.ChatRoomScreen
import com.example.bloom.journal.presentation.JournalScreen
import com.example.bloom.HomeScreen
import com.example.bloom.profile.presentation.ProfileScreen
import com.example.bloom.journal.data.models.Journal
import com.example.bloom.journal.presentation.JournalListScreen
import com.example.bloom.profile.presentation.AchievementScreen
import com.example.bloom.therapy.data.models.Doctors
import com.example.bloom.therapy.data.models.PrintDetails
import com.example.bloom.therapy.presentation.DoctorAppointmentScreen
import com.example.bloom.therapy.presentation.DoctorScreen
import com.example.bloom.therapy.presentation.PrintDetailsScreen
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(googleAuthUiClient: GoogleAuthUiClient,
               lifecycleScope: LifecycleCoroutineScope,
               applicationContext: Context,
               imagePicker: ActivityResultLauncher<PickVisualMediaRequest>,
               uriState: MutableStateFlow<String>
) {
    val navController = rememberNavController()

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Check if the user is already signed in
    val isUserSignedIn = googleAuthUiClient.getSignedInUser() != null

    // Initial destination based on sign-in status
    val startDestination = if (isUserSignedIn) Routes.HomeScreen.route else Routes.SignInScreen.route

    NavHost(navController = navController,
        startDestination = startDestination,
    ) {

        composable(Routes.SignInScreen.route) {
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = Unit) {
                if(googleAuthUiClient.getSignedInUser() != null) {
                    navController.navigate(Routes.HomeScreen.route)
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if(result.resultCode == RESULT_OK) {
                        lifecycleScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if(state.isSignInSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigate(Routes.HomeScreen.route)
                    viewModel.resetState()
                }
            }

            SignInScreen(
                state = state,
                onSignInClick = {
                    lifecycleScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }

        composable("${Routes.AppointmentScreen.route}/{doctorJson}") {

            val decodedJson = Uri.decode(it.arguments?.getString("doctorJson") ?: "")
            val doctor = decodedJson?.let { Doctors.fromJson(decodedJson) }

            if(doctor!=null){
                DoctorAppointmentScreen(
                    navController = navController,
                    doctor = doctor
                    ){printDetails->
                    val printJson = Gson().toJson(printDetails)
                    val encodedPrintJson = Uri.encode(printJson)
                    navController.navigate("${Routes.ConfirmationScreen.route}/$encodedPrintJson")
                }
            }
        }

        composable(route = Routes.HomeScreen.route){
            HomeScreen(navController, userData = googleAuthUiClient.getSignedInUser(), navigateToJournal = {journal->
                val journalJson = Gson().toJson(journal)
                val encodedJournalJson = Uri.encode(journalJson)

                val destination = "${Routes.JournalScreen.route}/$encodedJournalJson"
                navController.navigate((destination))

            }){ doctor->
                val doctorJson = Gson().toJson(doctor)
                val encodedDoctorJson = Uri.encode(doctorJson)

                val destination = "${Routes.AppointmentScreen.route}/$encodedDoctorJson"
                navController.navigate(destination)
            }
        }

        composable(route=Routes.JournalListScreen.route){
            JournalListScreen(navController){journal->
                val journalJson = Gson().toJson(journal)
                val encodedJournalJson = Uri.encode(journalJson)

                val destination = "${Routes.JournalScreen.route}/$encodedJournalJson"
                navController.navigate((destination))
            }
        }

        composable("${Routes.JournalScreen.route}/{journalJson}"){
            val decodedJson = Uri.decode(it.arguments?.getString("journalJson") ?: "")
            val journal = decodedJson?.let { Journal.fromJson(decodedJson) }

            if(journal!=null){
                JournalScreen(navController = navController, journal = journal)
            }
        }

        composable(Routes.ChatRoomsListScreen.route){
            ChatRoomListScreen(navController = navController){room, name->
                navController.navigate("${Routes.ChatRoomScreen.route}/${room.id}/${room.name}/${name}")
            }
        }

        composable("${Routes.ChatRoomScreen.route}/{roomId}/{roomName}/{name}"){ // Add roomName as a path parameter
            val roomId: String = it.arguments?.getString("roomId") ?: ""
            val roomName: String = it.arguments?.getString("roomName") ?: ""
            val anonymousName: String = it.arguments?.getString("name") ?: ""
            ChatRoomScreen(navController = navController, roomId = roomId, roomName = roomName, userData = googleAuthUiClient.getSignedInUser(), anonymousName = anonymousName)
        }

        composable(Routes.ChatBotScreen.route){
            ChatBotScreen(navController = navController, imagePicker = imagePicker, uriState = uriState)
        }

        composable(Routes.DoctorScreen.route) {
            DoctorScreen(navController = navController) { doctor ->

                val doctorJson = Gson().toJson(doctor)
                val encodedDoctorJson = Uri.encode(doctorJson)

                val destination = "${Routes.AppointmentScreen.route}/$encodedDoctorJson"
                navController.navigate(destination)
            }
        }

        composable("${Routes.ConfirmationScreen.route}/{printJson}"){
            val decodedJson = Uri.decode(it.arguments?.getString("printJson") ?: "")
            val printDetails = decodedJson?.let { PrintDetails.fromJson(decodedJson) }
            if (printDetails != null) {
                PrintDetailsScreen(navController = navController, printDetails = printDetails)
                }
        }

        composable(Routes.ProfileScreen.route) {
            ProfileScreen(
                navController = navController,
                userData = googleAuthUiClient.getSignedInUser(),
                onSignOut = {
                    lifecycleScope.launch {
                        googleAuthUiClient.signOut()
                        Toast.makeText(
                            applicationContext,
                            "Signed out",
                            Toast.LENGTH_LONG
                        ).show()
                        navController.navigate(Routes.SignInScreen.route)
                    }
                }
            ){journalsCount->
                navController.navigate("${Routes.AchievementScreen.route}/${journalsCount.toString()}")
            }
        }

        composable("${Routes.AchievementScreen.route}/{journalsCount}"){
            val journalsCount = it.arguments?.getString("journalsCount")?.toLong()?:0L
            AchievementScreen(navController = navController, journalsCount = journalsCount)
        }
    }
}