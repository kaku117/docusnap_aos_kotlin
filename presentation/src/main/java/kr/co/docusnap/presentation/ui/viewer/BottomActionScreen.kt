package kr.co.docusnap.presentation.ui.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kr.co.docusnap.domain.model.FileType
import kr.co.docusnap.domain.model.ScanFile
import kr.co.docusnap.presentation.R
import kr.co.docusnap.presentation.viewmodel.MainViewModel

@Composable
fun BoxScope.BottomActionScreen(
    fileId: Int,
    viewModel: MainViewModel,
    onFavoriteClick: (ScanFile) -> Unit,
    onShareClick: (FileType) -> Unit,
    onLockClick: (ScanFile) -> Unit
) {
    val scanFile by viewModel.getScanFile(fileId).collectAsState(null)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(
                color = Color.Transparent
                    .copy(alpha = 0.5f)
            ),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = {
                scanFile?.let {
                    onFavoriteClick(it)
                }
            }
        ) {
            Icon(
                imageVector = if (scanFile?.isFavorite == true) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Favorite Button",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 10.dp))
        IconButton(
            onClick = {
                scanFile?.let {
                    onShareClick(it.fileType)
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Share Button",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 10.dp))
        IconButton(
            onClick = {
                scanFile?.let {
                    onLockClick(it)
                }
            },
            modifier = Modifier.size(24.dp)
                .padding(start = 4.dp)
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                painter = painterResource(id = if (scanFile?.isLocked == true) R.drawable.ic_file_lock else R.drawable.ic_file_unlock),
                contentDescription = "Lock Button",
                tint = Color.White
            )
        }
    }
}