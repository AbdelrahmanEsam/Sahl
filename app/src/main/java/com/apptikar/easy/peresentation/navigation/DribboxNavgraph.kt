package com.apptikar.easy.peresentation.navigation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.apptikar.dribbox.utils.ScreenClassifier
import com.apptikar.dribbox.utils.sdp
import com.apptikar.easy.data.dto.DataX
import com.apptikar.easy.peresentation.ui.login.Login
import com.apptikar.easy.peresentation.ui.splash.Splash
import com.apptikar.easy.peresentation.ui.writeOnTag.InsertTheMount


@Composable
fun EasyNavGraph(
    screenClassifier: ScreenClassifier,
    navController: NavHostController,
    modifier: Modifier,
    scaffoldState: ScaffoldState,
    ) {



    NavHost(
        navController = navController,
        startDestination = Destinations.Splash,
        modifier = modifier
    ) {

        composable(Destinations.Splash){
            Splash(modifier = Modifier
                .fillMaxSize()
                .background(White)
                ,navController)
        }


        composable(Destinations.Login) {
            Login(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .verticalScroll(
                        rememberScrollState()
                    ),
               navController =  navController
            )

        }

        composable(Destinations.InsertTheMount,
        ){


            InsertTheMount(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(horizontal = 20.sdp), navController = navController
            )
        }




    }


}