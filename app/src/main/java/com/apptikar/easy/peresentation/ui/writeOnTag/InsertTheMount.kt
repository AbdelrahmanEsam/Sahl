package com.apptikar.easy.peresentation.ui.writeOnTag

import android.nfc.Tag
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.apptikar.dribbox.utils.sdp
import com.apptikar.dribbox.utils.textSdp
import com.apptikar.easy.R
import com.apptikar.easy.common.theme.BoxBackGroundColor
import com.apptikar.easy.common.theme.Gray
import com.apptikar.easy.common.theme.LightBlue
import com.apptikar.easy.peresentation.navigation.Destinations
import com.apptikar.easy.peresentation.ui.doneDialog.DoneDialog


@Composable
fun InsertTheMount(
    modifier: Modifier,
    navController: NavHostController,
    writeOnTagViewModel: InsertTheMountViewModel = hiltViewModel(),
    tag: Tag?
) {
   val mount =  writeOnTagViewModel.mount.collectAsState()
    val showDialog = rememberSaveable { mutableStateOf(false) }
    val showToast = rememberSaveable { mutableStateOf(false) }
   val toastMessage =rememberSaveable { mutableStateOf(0) }
       Column(
           modifier = modifier,
           verticalArrangement = Arrangement.Top,
           Alignment.End
       ) {


           if (showDialog.value){

               DoneDialog(modifier = Modifier
                   .clip(RoundedCornerShape(10.sdp))
                   .background(Color.White)
                   , navController = navController, setShowDialog = {  show ->
                       showDialog.value = show
                   })
               writeOnTagViewModel.setMount("")
           }

           if (showToast.value){
               Toast.makeText(LocalContext.current, stringResource(id = toastMessage.value), Toast.LENGTH_LONG).show()
               showToast.value = false
           }

           Spacer(modifier = Modifier.size(30.sdp))
           Image(
               painter = painterResource(id = R.drawable.ic_back),
               contentDescription = stringResource(R.string.back),
               modifier = Modifier
                   .size(25.sdp)
                   .background(Color.White)
                   .clickable {
                       navController.popBackStack()
                   }

           )
           Spacer(modifier = Modifier.size(50.sdp))
           Text(text = stringResource(R.string.the_mount), style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold, fontSize = 20.textSdp)
           Spacer(modifier = Modifier.height(20.sdp))
           Text(text = stringResource(R.string.insert_mount), style = MaterialTheme.typography.body1, fontWeight = FontWeight.Normal, fontSize = 12.textSdp, color = Color.Black)
           Spacer(modifier = Modifier.height(20.sdp))
           Text(text = stringResource(R.string.mount), style = MaterialTheme.typography.body1, fontWeight = FontWeight.Normal, fontSize = 12.textSdp, color = Gray)
           Spacer(modifier = Modifier.height(5.sdp))
           TextField(
               value = mount.value ?: "" ,
               onValueChange = {  writeOnTagViewModel.setMount(it) },
               modifier = Modifier
                   .fillMaxWidth()
                   .height(45.sdp),
               textStyle = TextStyle(fontSize = 10.textSdp),
               shape = RoundedCornerShape(topStart = 8.sdp , topEnd = 8.sdp),
               maxLines = 1,
               singleLine = true,
               colors = TextFieldDefaults.textFieldColors(
                   backgroundColor = BoxBackGroundColor,
                   disabledIndicatorColor = Color.Black,
                   focusedIndicatorColor = Color.Black,
                   unfocusedIndicatorColor = Color.Black
               ),
               placeholder = {
                   Row(
                       modifier = Modifier.fillMaxSize(),
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.End
                   ){

                       Text(text = stringResource(R.string.price),
                           style = MaterialTheme.typography.body1,
                           color = Gray,
                           modifier = Modifier
                               .wrapContentSize()
                               .offset(x = (-2).dp),
                           fontSize = 10.textSdp)
                   }

               },
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

           )

           Spacer(modifier = Modifier.height(100.sdp))
           TextButton(
               onClick = {
                   Log.e("fromInsertMoney : " , tag.toString())
                   if (mount.value.isNullOrEmpty()){
                       Log.d("abdo","null or empty")
                       toastMessage.value =  R.string.please_insert_mount
                       showToast.value = true
                   }else if (tag == null){
                       toastMessage.value = R.string.this_device_isn_not_supported
                       showToast.value = true
                   }else{
                       showDialog.value = writeOnTagViewModel.writeToTagByNFC(tag)
                   }


               },
               modifier = Modifier
                   .background(
                       color = LightBlue,
                       shape = RoundedCornerShape(10.sdp)
                   )
                   .padding(horizontal = 10.dp)
                   .fillMaxWidth()
                   .height(45.sdp)

           ) {
               Text(text = stringResource(R.string.confirm), style =  MaterialTheme.typography.body1, fontSize = 12.textSdp, color = Color.White)
           }
           Spacer(modifier = Modifier.height(30.sdp))


       }

}




