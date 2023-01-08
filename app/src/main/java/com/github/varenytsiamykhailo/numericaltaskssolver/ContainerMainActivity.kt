package com.github.varenytsiamykhailo.numericaltaskssolver

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.varenytsiamykhailo.numericaltaskssolver.databinding.ContainerMainBinding

class ContainerMainActivity : AppCompatActivity() {
    private var binding: ContainerMainBinding? = null

    private var navHostFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContainerMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.containerMain_root)
        init()
    }

    private fun init() {
        /*binding?.containerMainBottomNavigation?.apply {
            background = null
        }

        navHostFragment = supportFragmentManager.findFragmentById(R.id.containerMain_root)
        navHostFragment?.let { host ->
            binding?.containerMainBottomNavigation?.setupWithNavController(host.findNavController())
            host.findNavController().addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.authorizationFragment -> onHideNavigator()
                    else -> onShowNavigator()
                }
            }
        }*/
    }

    /*fun onHideNavigator() {
        binding?.containerMainBottomNavigation?.let { navigation ->
            navigation.hide()
            binding?.bottomAppBar?.hide()
        }
    }

    fun onShowNavigator() {
        binding?.containerMainBottomNavigation?.let { navigation ->
            navigation.show()
            binding?.bottomAppBar?.show()
        }
    }*/
}