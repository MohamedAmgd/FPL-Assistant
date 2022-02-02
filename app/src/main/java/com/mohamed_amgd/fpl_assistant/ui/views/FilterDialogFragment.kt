package com.mohamed_amgd.fpl_assistant.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mohamed_amgd.fpl_assistant.data.models.FilterDialogValues
import com.mohamed_amgd.fpl_assistant.databinding.FilterDialogFragmentBinding
import com.mohamed_amgd.fpl_assistant.ui.intents.FilterDialogIntent
import com.mohamed_amgd.fpl_assistant.ui.viewModels.FilterDialogViewModel
import com.mohamed_amgd.fpl_assistant.ui.viewStates.FilterDialogViewState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FilterDialogFragment : DialogFragment() {
    private lateinit var binding: FilterDialogFragmentBinding
    private lateinit var playerValueAdapter: ArrayAdapter<String>
    private lateinit var sortDirectionAdapter: ArrayAdapter<String>
    private lateinit var playerValue: Spinner
    private lateinit var minValue: EditText
    private lateinit var maxValue: EditText
    private lateinit var sortDir: Spinner
    private lateinit var viewModel: FilterDialogViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FilterDialogFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: FilterDialogViewModel by viewModels()
        this.viewModel = viewModel
        playerValue = binding.playerValue
        minValue = binding.minValueInput
        maxValue = binding.maxValueInput
        sortDir = binding.sortDir
        playerValueAdapter =
            ArrayAdapter<String>(view.context, android.R.layout.simple_list_item_1)
        playerValue.adapter = playerValueAdapter

        sortDirectionAdapter =
            ArrayAdapter<String>(view.context, android.R.layout.simple_list_item_1)
        sortDir.adapter = sortDirectionAdapter


        binding.exitBtn.setOnClickListener {
            dismiss()
        }
        binding.confirmButton.setOnClickListener {
            confirmAction()
        }

        lifecycleScope.launch {
            viewModel.state.collect {
                render(it)
            }
        }
        lifecycleScope.launch { viewModel.intentChannel.send(FilterDialogIntent.PlayerValues) }
        lifecycleScope.launch { viewModel.intentChannel.send(FilterDialogIntent.SortDirections) }
        lifecycleScope.launch { viewModel.intentChannel.send(FilterDialogIntent.InitialValues) }
    }

    private fun confirmAction() {
        val playerValue = playerValue.selectedItemPosition
        val min = minValue.text.toString()
        val max = maxValue.text.toString()
        val dir = sortDir.selectedItemPosition
        val values = FilterDialogValues(playerValue, min, max, dir)
        lifecycleScope.launch {
            viewModel.intentChannel.send(FilterDialogIntent.Confirm(values))
        }
    }

    private fun render(state: FilterDialogViewState) {
        when (state) {
            is FilterDialogViewState.Idle -> {
            }
            is FilterDialogViewState.Loading -> {
            }
            is FilterDialogViewState.InitialValues -> {
                playerValue.setSelection(state.values.playerValueIndex)
                minValue.setText(state.values.minValue)
                maxValue.setText(state.values.maxValue)
                sortDir.setSelection(state.values.sortDirIndex)
            }
            is FilterDialogViewState.PlayerValues -> {
                playerValueAdapter.clear()
                playerValueAdapter.addAll(state.values)
                playerValueAdapter.notifyDataSetChanged()
            }
            is FilterDialogViewState.SortDirections -> {
                sortDirectionAdapter.clear()
                sortDirectionAdapter.addAll(state.values)
                sortDirectionAdapter.notifyDataSetChanged()
            }
            is FilterDialogViewState.Confirmed -> {
                dismiss()
            }
            is FilterDialogViewState.Error -> {
                Toast.makeText(this.context, state.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val TAG = "FilterDialogFragment"
    }
}