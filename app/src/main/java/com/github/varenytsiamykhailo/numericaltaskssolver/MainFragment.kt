package com.github.varenytsiamykhailo.numericaltaskssolver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.FragmentMainBinding
import org.koin.android.ext.android.get
import org.koin.core.qualifier.named


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private var storage: SharedPreferencesStorage = get<SharedPreferencesStorage>(named("storage"))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    private fun init() {
        binding.mainActivityIntegralMethodsButton.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToIntegralMethodsFragment()
            findNavController().navigate(action)
        }

        binding.mainActivitySystemSolvingMethodsButton.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToSystemSolvingMethodsFragment()
            findNavController().navigate(action)
        }

        binding.mainActivityOptimizationMethodsButton.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToOptimizationMethodsFragment()
            findNavController().navigate(action)
        }

        binding.mainActivityMatrixCalcButton.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToMatrixCalculatorFragment()
            findNavController().navigate(action)
        }
    }
}