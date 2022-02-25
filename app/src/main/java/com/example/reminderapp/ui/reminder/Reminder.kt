package com.example.reminderapp.ui.reminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.insets.systemBarsPadding
import java.util.*
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reminderapp.data.entity.Category
import kotlinx.coroutines.launch

@Composable
fun Reminder(
    onBackPress: () -> Unit,
    viewModel: ReminderViewModel = viewModel()
) {
    val viewState by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val message = rememberSaveable { mutableStateOf("") }
    val category = rememberSaveable { mutableStateOf("") }
    val date = rememberSaveable { mutableStateOf("") }
    val time = rememberSaveable{mutableStateOf("")}

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH) + 1
    val day = c.get(Calendar.DAY_OF_MONTH)
    val hour = c[Calendar.HOUR_OF_DAY]
    val minute = c[Calendar.MINUTE]

    val datePickerDialog = DatePickerDialog(
        LocalContext.current, DatePickerDialog.OnDateSetListener
        { datePicker: DatePicker, day: Int, month: Int, year: Int ->
            date.value = "$day/$month/$year"
        }, year, month, day
    )

    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        {
            _, hour : Int, minute : Int ->
            time.value = "$hour:$minute"
        }, hour, minute,true
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
                    value = message.value,
                    onValueChange = { message.value = it},
                    label = { Text(text = "Remind me about") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                CategoryListDropdown(
                    viewState = viewState,
                    category = category
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
                Row {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
                            .clickable{
                                timePickerDialog.show()
                            }
                    ){
                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ){
                            val (tlable, ticonView) = createRefs()

                            Text(
                                text= if(time.value != "") {time.value} else{"Remind me at (time : Optional)"},
                                color = MaterialTheme.colors.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .constrainAs(tlable) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(ticonView.start)
                                        width = Dimension.fillToConstraints
                                    }
                            )

                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp, 20.dp)
                                    .constrainAs(ticonView) {
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
                    onClick = {
                              Log.i("datee",date.value)
                              coroutineScope.launch{
                                  viewModel.saveReminder(
                                        com.example.reminderapp.data.entity.Reminder(
                                          reminderMessage = message.value,
                                            reminderDate = date.value,
                                            reminderCategoryId = getCategoryId(viewState.categories,category.value),
                                            location_x = null,
                                            location_y = null,
                                            reminderTime = time.value
                                      )
                                  )
                              }
                        onBackPress()
                    },
                    modifier = Modifier.fillMaxWidth().size(55.dp)
                ) {
                    Text("Set Reminder")
                }
            }
        }
    }

}

private fun getCategoryId(categories: List<Category>, categoryName: String): Long {
    return categories.first { category -> category.name == categoryName }.id
}

@Composable
private fun CategoryListDropdown(
    viewState: ReminderViewState,
    category: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) {
        Icons.Filled.ArrowDropUp
    } else {
        Icons.Filled.ArrowDropDown
    }

    Column {
        OutlinedTextField(
            value = category.value,
            onValueChange = { category.value = it},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Category") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            viewState.categories.forEach { dropDownOption ->
                DropdownMenuItem(
                    onClick = {
                        category.value = dropDownOption.name
                        expanded = false
                    }
                ) {
                    Text(text = dropDownOption.name)
                }

            }
        }
    }
}
