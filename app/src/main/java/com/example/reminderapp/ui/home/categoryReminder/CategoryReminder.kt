package com.example.reminderapp.ui.home.categoryReminder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reminderapp.data.entity.Reminder
import java.text.SimpleDateFormat
import java.util.*

//These composables are used to show category wise reminders

@Composable
fun CategoryReminder(
    modifier: Modifier = Modifier
) {
    val viewModel: CategoryReminderViewModel = viewModel()
    val viewState by viewModel.state.collectAsState()

    Column(modifier = modifier) {
        ReminderList(
            list = viewState.reminders
        )
    }
}

@Composable
private fun ReminderList(
    list: List<Reminder>
) {
    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items(list) { item ->
            ReminderListItem(
                reminder = item,
                onClick = {},
                modifier = Modifier.fillParentMaxWidth(),
            )
        }
    }
}

@Composable
private fun ReminderListItem(
    reminder: Reminder,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var color : Color
    if(reminder.reminderDate!= null
        && reminder.reminderDate.
        after(Date(122,Calendar.MONTH-1,Calendar.DAY_OF_MONTH+2))) {
        color = Color.Yellow
    }
    else if(reminder.reminderDate!= null && reminder.reminderDate.after(Date(122,
        Calendar.MONTH - 1 , Calendar.DAY_OF_MONTH + 3)))
    {
        color = Color.Green
    }
    else
    {
        color = Color.Red
    }
    ConstraintLayout(modifier = modifier.clickable { onClick() }) {
        val (divider, reminderTitle, reminderCategory, icon, date) = createRefs()
        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )

        // title
        Text(
            text = reminder.reminderTitle,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(reminderTitle) {
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
            text = "Due: " + when {
                reminder.reminderDate != null -> { reminder.reminderDate.formatToString() }
                else -> Date().formatToString()
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(date) {
                linkTo(
                    start = reminderTitle.end,
                    end = icon.start,
                    startMargin = 8.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )

                start.linkTo(reminderTitle.end, 6.dp)
                top.linkTo(parent.top, margin = 10.dp)
                bottom.linkTo(parent.bottom, 10.dp)
            }
        )

        // icon
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top, 6.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(parent.end)
                }
        ) {
            /*Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "check_mark"
            )*/
            Box(
                modifier = Modifier.size(10.dp)
                    .clip(shape = CircleShape)
                    .background(color)
            )
        }
    }
}

private fun Date.formatToString(): String {
    return SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(this)
}