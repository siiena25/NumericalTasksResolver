package com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentIntegralMethodsBinding


class IntegralMethodsFragment : Fragment() {
    private lateinit var binding: FragmentIntegralMethodsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntegralMethodsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    private fun init() {
        binding.rectangleMethodInfoButton.setOnClickListener {
            val action =
                IntegralMethodsFragmentDirections.actionIntegralMethodsFragmentToMethodInfoFragment(
                    "rectangleMethod"
                )
            findNavController().navigate(action)
        }
        binding.rectangleMethodButton.setOnClickListener {
            val action =
                IntegralMethodsFragmentDirections.actionIntegralMethodsFragmentToInputIntegralDataFragment(
                    "rectangleMethod"
                )
            findNavController().navigate(action)
        }

        binding.trapezoidMethodInfoButton.setOnClickListener {
            val action =
                IntegralMethodsFragmentDirections.actionIntegralMethodsFragmentToMethodInfoFragment(
                    "trapezoidMethod"
                )
            findNavController().navigate(action)
        }
        binding.trapezoidMethodButton.setOnClickListener {
            val action =
                IntegralMethodsFragmentDirections.actionIntegralMethodsFragmentToInputIntegralDataFragment(
                    "trapezoidMethod"
                )
            findNavController().navigate(action)
        }

        binding.simpsonMethodInfoButton.setOnClickListener {
            val action =
                IntegralMethodsFragmentDirections.actionIntegralMethodsFragmentToMethodInfoFragment(
                    "simpsonMethod"
                )
            findNavController().navigate(action)
        }
        binding.simpsonMethodButton.setOnClickListener {
            val action =
                IntegralMethodsFragmentDirections.actionIntegralMethodsFragmentToInputIntegralDataFragment(
                    "simpsonMethod"
                )
            findNavController().navigate(action)
        }
    }
}