package com.oi.hata.task.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.oi.hata.R
import com.oi.hata.common.ui.HataTaskSheetIconButton
import com.oi.hata.common.ui.components.HataDatePicker
import com.oi.hata.common.util.ReminderUtil
import com.oi.hata.task.data.model.GroupTask
import com.oi.hata.ui.TaskViewModel


@ExperimentalAnimationApi
@Composable
private fun TaskSheet(reminderTxt: String,
                      onReminderTxtChange: (String) -> Unit,
                      customreminder: String,
                      reminder: String,
                      onClickReminder: () -> Unit,
                      onDueDateSelect: (year: Int,month: Int,day:Int) -> Unit,
                      onTimeSelect: (hour:Int,minute:Int,am:Boolean) -> Unit,
                      onSaveReminder: () -> Unit,
                      dueDateSelected: Boolean,
                      dueDate: String,
                      reminderSelected: Boolean,
                      onReminderSelected: (Boolean) -> Unit,
                      taskselected: Boolean,
                      diaryselected: Boolean,
                      onTaskSelected: (Boolean) -> Unit,
                      onReminderOptSelected: (String) -> Unit,
                      initReminderValues: () -> Unit,
                      onCloseTask: () -> Unit

){

   var (datePickerSelected,onDatePickerSelected) = remember { mutableStateOf(false) }
   val taskTransitionState = taskTransition(reminderSelected)

   Surface(modifier = Modifier
      .fillMaxWidth(),
      shape = RoundedCornerShape(topStart = 4.dp,topEnd = 4.dp),
      color = MaterialTheme.colors.surface.copy(alpha = 0.12f),
   ) {
      Column(modifier = Modifier.padding(start=8.dp)) {
         Row() {
            AnimatedVisibility(visible = datePickerSelected) {
               Dialog(onDismissRequest = {
                  onDatePickerSelected(false)
               }) {
                  HataDatePicker(onDatePickerSelected,onDueDateSelect)
               }
            }
         }
         Row(modifier = Modifier.align(Alignment.End).padding(top=12.dp)) {
            HataTaskSheetIconButton(
               onClick = {
                  onTaskSelected(false)
                  onCloseTask()
                  onReminderOptSelected(ReminderUtil.NONE)
               },
               painter = painterResource(id = R.drawable.ic_baseline_close_24),
               contentDescription = stringResource(id = R.string.close)
            )
         }


         Row(){
            OutlinedTextField(
               colors = TextFieldDefaults.outlinedTextFieldColors(
                  unfocusedBorderColor = Color.White.copy(alpha = 0.90f).compositeOver(Color.Black)
               ),
               value = reminderTxt,
               onValueChange = { onReminderTxtChange(it) },
               enabled =  when { (reminderSelected) -> false
                  else -> true
               }

            )
            IconButton(
               modifier = Modifier.align(Alignment.Bottom),
               enabled =  when { (reminderSelected) -> false
                  else -> true
               },
               onClick = {
                  onTaskSelected(false)
                  onSaveReminder()
                  onReminderOptSelected(ReminderUtil.NONE)
               }) {
               Icon(
                  painter = painterResource(R.drawable.ic_action_save),
                  tint = Color.White.copy(alpha = taskTransitionState.contentAlpha).compositeOver(
                     Color.Black),
                  contentDescription = null,
                  modifier = Modifier
                     .padding(start = 8.dp,top = 20.dp),

                  )
            }
         }


         Row(){
            Column() {
               IconButton( enabled =  when { (reminderSelected) -> false
                  else -> true
               },
                  onClick = {
                     if(datePickerSelected)
                        onDatePickerSelected(false)
                     else
                        onDatePickerSelected(true)

                  }) {
                  Icon(
                     painter = painterResource(R.drawable.ic_action_pick_date),
                     contentDescription = null,
                     tint = Color.White.copy(alpha = taskTransitionState.contentAlpha).compositeOver(
                        Color.Black),
                     modifier = Modifier
                        .padding(start = 8.dp)

                  )

               }
               AnimatedVisibility(visible = dueDateSelected) {
                  //DateChip(text = dueDate)
                  Text(
                     text = dueDate,
                     color = colorResource(id = R.color.pickdate),
                     fontSize = 10.sp,
                     style = MaterialTheme.typography.body2,
                     modifier = Modifier.padding(start = 8.dp,bottom = 8.dp)
                  )
               }
            }

            IconButton( enabled =  when { (reminderSelected) -> false
               else -> true
            },
               onClick = {  }) {
               Icon(
                  painter = painterResource(R.drawable.ic_action_tag),
                  contentDescription = null,
                  tint = Color.White.copy(alpha = taskTransitionState.contentAlpha).compositeOver(
                     Color.Black),
                  modifier = Modifier
                     .padding(start = 16.dp)
               )
            }

            /*IconButton(onClick = { onClickReminder() }) {
                Icon(imageVector = Icons.Filled.Notifications,
                    tint = colorResource(id = R.color.taskdivider),
                    contentDescription = "Reminder")
            }*/
            Column() {
               IconButton(onClick = {
                  if(reminderSelected) {
                     onReminderSelected(false)
                  }
                  else {
                     initReminderValues()
                     onReminderSelected(true)
                  }
               }) {
                  Icon(imageVector = Icons.Filled.Notifications,
                     tint = Color.White.copy(alpha = 0.90f).compositeOver(Color.Black),
                     modifier = Modifier
                        .padding(start = 16.dp),
                     contentDescription = "Reminder")
               }

               AnimatedVisibility(visible = reminder.isNotEmpty()) {
                  Text(
                     text = reminder,
                     color = colorResource(id = R.color.pickdate),
                     fontSize = 10.sp,
                     style = MaterialTheme.typography.body2,
                     modifier = Modifier.padding(start = 8.dp,bottom = 8.dp)
                  )
               }
            }

         }
      }

   }
}
