package com.mohamed_amgd.fpl_assistant.ui.intents

import com.mohamed_amgd.fpl_assistant.data.models.FilterDialogValues

sealed class FilterDialogIntent {
    object PlayerValues : FilterDialogIntent()
    object SortDirections : FilterDialogIntent()
    object InitialValues : FilterDialogIntent()
    data class Confirm(val values: FilterDialogValues) : FilterDialogIntent()
}
