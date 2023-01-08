package com.github.varenytsiamykhailo.numericaltaskssolver.methodstypesactivities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentSystemSolvingMethodsBinding


class SystemSolvingMethodsFragment : Fragment() {
    private lateinit var binding: FragmentSystemSolvingMethodsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSystemSolvingMethodsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    private fun init() {
        binding.gaussSimpleMethodInfoButton.setOnClickListener {
            val action =
                SystemSolvingMethodsFragmentDirections.actionSystemSolvingMethodsFragmentToMethodInfoFragment(
                    "gaussSimpleMethod"
                )
            findNavController().navigate(action)
        }
        binding.gaussSimpleMethodButton.setOnClickListener {
            val action =
                SystemSolvingMethodsFragmentDirections.actionSystemSolvingMethodsFragmentToInputSystemDataFragment(
                    "gaussSimpleMethod"
                )
            findNavController().navigate(action)
        }

        binding.thomasMethodInfoButton.setOnClickListener {
            val action =
                SystemSolvingMethodsFragmentDirections.actionSystemSolvingMethodsFragmentToMethodInfoFragment(
                    "thomasMethod"
                )
            findNavController().navigate(action)
        }
        binding.thomasMethodButton.setOnClickListener {
            val action =
                SystemSolvingMethodsFragmentDirections.actionSystemSolvingMethodsFragmentToInputSystemDataFragment(
                    "thomasMethod"
                )
            findNavController().navigate(action)
        }

        binding.jacobiMethodInfoButton.setOnClickListener {
            val action =
                SystemSolvingMethodsFragmentDirections.actionSystemSolvingMethodsFragmentToMethodInfoFragment(
                    "jacobiMethod"
                )
            findNavController().navigate(action)
        }
        binding.jacobiMethodButton.setOnClickListener {
            val action =
                SystemSolvingMethodsFragmentDirections.actionSystemSolvingMethodsFragmentToInputSystemDataFragment(
                    "jacobiMethod"
                )
            findNavController().navigate(action)
        }

        binding.seidelMethodInfoButton.setOnClickListener {
            val action =
                SystemSolvingMethodsFragmentDirections.actionSystemSolvingMethodsFragmentToMethodInfoFragment(
                    "seidelMethod"
                )
            findNavController().navigate(action)
        }
        binding.seidelMethodButton.setOnClickListener {
            val action =
                SystemSolvingMethodsFragmentDirections.actionSystemSolvingMethodsFragmentToInputSystemDataFragment(
                    "seidelMethod"
                )
            findNavController().navigate(action)
        }
    }
}