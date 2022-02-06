package com.example.reminderapp.ui.reminder

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.insets.systemBarsPadding
import java.util.*

@Composable
fun Reminder(
    onBackPress: () -> Unit
) {

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val date = remember { mutableStateOf("") }

    val datePickerDialog = DatePickerDialog(
        LocalContext.current, DatePickerDialog.OnDateSetListener
        { datePicker: DatePicker, day: Int, month: Int, year: Int ->
            date.value = "$day/$month/$year"
        }, year, month, day
    )

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar {
                IconButton(
                    onClick =  onBackPress
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(text = "Reminders")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(text = "Remind me about") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                Row {

                   Box(
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(top = 10.dp)
                           .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
                           .clickable{
                               datePickerDialog.show()
                           }
                   ){
                       ConstraintLayout(
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(16.dp)
                       ){
                           val (lable, iconView) = createRefs()

                           Text(
                               text= if(date.value != "") {date.value} else{"Remind me on"},
                               color = MaterialTheme.colors.onSurface,
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .constrainAs(lable) {
                                       top.linkTo(parent.top)
                                       bottom.linkTo(parent.bottom)
                                       start.linkTo(parent.start)
                                       end.linkTo(iconView.start)
                                       width = Dimension.fillToConstraints
                                   }
                           )

                           Icon(
                               imageVector = Icons.Default.DateRange,
                               contentDescription = null,
                               modifier = Modifier
                                   .size(20.dp, 20.dp)
                                   .constrainAs(iconView) {
                                       end.linkTo(parent.end)
                                       top.linkTo(parent.top)
                                       bottom.linkTo(parent.bottom)
                                   },
                               tint = MaterialTheme.colors.onSurface
                           )
                       }
                   }


                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth().size(55.dp)
                ) {
                    Text("Set Reminder")
                }
            }
        }
    }

}
