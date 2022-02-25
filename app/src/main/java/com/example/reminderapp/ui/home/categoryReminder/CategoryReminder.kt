package com.example.reminderapp.ui.home.categoryReminder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.reminderapp.R
import com.example.reminderapp.data.entity.Category
import com.example.reminderapp.data.entity.Reminder
import com.example.reminderapp.data.room.ReminderToCategory
import com.example.reminderapp.ui.reminder.ReminderViewModel
import com.example.reminderapp.util.viewModelProviderFactoryOf
import kotlinx.coroutines.launch

//These composables are used to show category wise reminders

@Composable
fun CategoryReminder(
    categoryId: Long,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel: CategoryReminderViewModel = viewModel(
        key = "category_list_$categoryId",
        factory = viewModelProviderFactoryOf { CategoryReminderViewModel(categoryId) }
    )

    val viewState by viewModel.state.collectAsState()

        Column(modifier = modifier) {
            ReminderList(
                list = viewState.reminders,
                navController = navController
            )
        }

}

    @Composable
    private fun ReminderList(
        list: List<ReminderToCategory>,
        navController : NavController
    ) {
            LazyColumn(
                contentPadding = PaddingValues(0.dp),
                verticalArrangement = Arrangement.Center
            ) {
                items(list) { item ->
                    if(item.reminder.reminderTime.toString().isEmpty()) {
                        ReminderListItem(
                            reminder = item.reminder,
                            category = item.category,
                            onClick = {},
                            modifier = Modifier.fillParentMaxWidth(),
                            navController = navController
                        )
                    }
            }
        }
    }


@Composable
private fun ReminderListItem(
    reminder: Reminder,
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val reminderViewModel : ReminderViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    ConstraintLayout(modifier = modifier.clickable { onClick() }) {
        val (divider, reminderMessage, icon, date,editIcon) = createRefs()
        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )

        // title
        Text(
            text = reminder.reminderMessage,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(reminderMessage) {
                linkTo(
                    start = parent.start,
                    end = icon.start,
                    startMargin = 24.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
                top.linkTo(parent.top, margin = 22.dp)
                width = Dimension.preferredWrapContent
            }
        )

        // date
        Text(
            text = "Due: " + reminder.reminderDate,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.constrainAs(date) {
                linkTo(
                    start = parent.start,
                    end = icon.start,
                    startMargin = 24.dp,
                    endMargin = 8.dp,
                    bias = 0f
                )

                top.linkTo(reminderMessage.bottom,margin = 6.dp)
                bottom.linkTo(parent.bottom,10.dp)
                width = Dimension.preferredWrapContent
            }
        )
        IconButton(
            onClick = {
                navController.currentBackStackEntry?.savedStateHandle?.set("reminder",reminder)
                navController.currentBackStackEntry?.savedStateHandle?.set("category",category)
              navController.navigate(route="edit")
            },
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(editIcon) {
                    top.linkTo(parent.top, 6.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(parent.end, margin = 42.dp)
                }
        ) {

                Icon(
                    painter = painterResource(id= R.drawable.ic_baseline_edit_24),
                    tint= Color.Black,
                    contentDescription = null,
                    //contentScale = ContentScale.Crop,

                )

        }

        //delete button
        IconButton(
            onClick = {
                coroutineScope.launch{
                    reminderViewModel.deleteReminder(reminder)
                }
            },
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(icon) {
                    start.linkTo(editIcon.end, 6.dp)
                    top.linkTo(parent.top, 6.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(parent.end, margin = 24.dp)
                }
        ) {

            Icon(
                painter = painterResource(id= R.drawable.ic_baseline_delete_24),
                tint= Color.Black,
                contentDescription = null,
                //contentScale = ContentScale.Crop,

            )

        }

    }

}