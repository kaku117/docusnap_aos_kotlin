package kr.co.docusnap.presentation.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.co.docusnap.domain.model.FilterType
import kotlin.enums.EnumEntries

@Composable
fun SingleSelectionChips(
    options: EnumEntries<FilterType>,
    selectedOption: FilterType,
    onOptionSelected: (FilterType) -> Unit
) {
    Row(
        modifier = Modifier.padding(top = 10.dp, start = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            FilterChip(
                selected = option == selectedOption,
                onClick = { onOptionSelected(option) },
                label = { Text(text = option.toString()) }
            )
        }
    }
}