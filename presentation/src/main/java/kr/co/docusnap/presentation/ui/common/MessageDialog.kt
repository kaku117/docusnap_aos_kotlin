package kr.co.docusnap.presentation.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import kr.co.docusnap.presentation.R

@Composable
fun MessageDialog(
    isShowDialog: Boolean,
    title: String,
    message: String,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(isShowDialog) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = title
                )
            },
            text = {
                Text(
                    text = message
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmClick()
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Black
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.common_dialog_ok),
                        textDecoration = TextDecoration.Underline
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDismissClick()
                        showDialog = false
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.common_dialog_cancel)
                    )
                }
            }
        )
    }
}