package com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentOptimizationMethodsBinding


class OptimizationMethodsFragment : Fragment() {
    private lateinit var binding: FragmentOptimizationMethodsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOptimizationMethodsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    private fun init() {
        binding.svennMethodInfoButton.setOnClickListener {
            val action =
                OptimizationMethodsFragmentDirections.actionOptimizationMethodsFragmentToMethodInfoFragment(
                    "svennMethod"
                )
            findNavController().navigate(action)
        }
        binding.svennMethodButton.setOnClickListener {
            val action =
                OptimizationMethodsFragmentDirections.actionOptimizationMethodsFragmentToInputSvennMethodDataFragment(
                    "svennMethod"
                )
            findNavController().navigate(action)
        }

        binding.goldenSectionMethodInfoButton.setOnClickListener {
            val action =
                OptimizationMethodsFragmentDirections.actionOptimizationMethodsFragmentToMethodInfoFragment(
                    "goldenSectionMethod"
                )
            findNavController().navigate(action)
        }
        binding.goldenSectionMethodButton.setOnClickListener {
            val action =
                OptimizationMethodsFragmentDirections.actionOptimizationMethodsFragmentToInputOptimizationDataFragment(
                    "goldenSectionMethod"
                )
            findNavController().navigate(action)
        }

        binding.fibonacciMethodInfoButton.setOnClickListener {
            val action =
                OptimizationMethodsFragmentDirections.actionOptimizationMethodsFragmentToMethodInfoFragment(
                    "fibonacciMethod"
                )
            findNavController().navigate(action)
        }
        binding.fibonacciMethodButton.setOnClickListener {
            val action =
                OptimizationMethodsFragmentDirections.actionOptimizationMethodsFragmentToInputOptimizationDataFragment(
                    "fibonacciMethod"
                )
            findNavController().navigate(action)
        }
    }
}