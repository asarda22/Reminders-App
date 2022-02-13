package com.example.reminderapp.ui.home.categoryReminder

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.reminderapp.data.entity.Category
import com.example.reminderapp.data.entity.Reminder
import com.example.reminderapp.ui.reminder.ReminderViewModel
import com.example.reminderapp.ui.reminder.ReminderViewState
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun EditCategoryReminder(
    navController: NavController,
    reminder: Reminder,
    category: Category,
){
    val editViewModel: ReminderViewModel = viewModel()
    val viewState by editViewModel.state.collectAsState()
    val editCoroutineScope = rememberCoroutineScope()
    var editMessage = rememberSaveable { mutableStateOf(reminder.reminderMessage) }
    var editCategoryValue = rememberSaveable { mutableStateOf(category.name) }
    var editDate = rememberSaveable { mutableStateOf(reminder.reminderDate) }

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        LocalContext.current, DatePickerDialog.OnDateSetListener
        { datePicker: DatePicker, day: Int, month: Int, year: Int ->
            editDate.value = "$day/$month/$year"
        }, year, month, day
    )

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = editMessage.value,
                    onValueChange = { editMessage.value = it},
                    label = { Text(text = "Remind me about") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                EditCategoryListDropdown(
                    viewState = viewState,
                    category = editCategoryValue
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
                                text= if(editDate.value != "") {editDate.value} else{"Remind me on"},
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
                    onClick = {
                        Log.i("message",editMessage.value)
                        editCoroutineScope.launch{
                            editViewModel.updateReminder(
                                Reminder(
                                    reminderId = reminder.reminderId,
                                    reminderMessage = editMessage.value,
                                    reminderDate = editDate.value,
                                    reminderCategoryId = getEditCategoryId(viewState.categories,editCategoryValue.value),
                                    location_x = null,
                                    location_y = null
                                )
                            )
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth().size(55.dp)
                ) {
                    Text("Save Changes")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { navController.popBackStack()},
                    modifier = Modifier.fillMaxWidth().size(55.dp)
                ) {
                    Text("Cancel")
                }
            }
        }
    }

}

@Composable
fun EditCategoryListDropdown(
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

private fun getEditCategoryId(categories: List<Category>, categoryName: String): Long {
    return categories.first { category -> category.name == categoryName }.id
}