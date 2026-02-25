package com.example.expenses.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.expenses.data.AppDatabase
import com.example.expenses.databinding.FragmentSummaryBinding
import com.example.expenses.ui.ExpensesViewModel
import com.example.expenses.ui.ExpensesViewModelFactory
import kotlinx.coroutines.launch

class SummaryFragment : Fragment() {

    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpensesViewModel by activityViewModels {
        val database = AppDatabase.getDatabase(requireContext())
        ExpensesViewModelFactory(database.expenseDao(), database.categoryDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val expensesAdapter = SummaryAdapter()
        val incomesAdapter = SummaryAdapter()

        binding.expensesSummaryRecyclerView.adapter = expensesAdapter
        binding.incomesSummaryRecyclerView.adapter = incomesAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.expenseSummary.collect { summaryList ->
                expensesAdapter.submitList(summaryList)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.incomeSummary.collect { summaryList ->
                incomesAdapter.submitList(summaryList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}