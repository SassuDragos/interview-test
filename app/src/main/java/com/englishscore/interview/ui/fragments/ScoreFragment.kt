package com.englishscore.interview.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.englishscore.interview.R
import com.englishscore.interview.databinding.ScoreFragmentBinding
import com.englishscore.interview.ui.viewmodels.ScoreViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * Fragment enabling a user to view a score retrieved from a url
 */
@AndroidEntryPoint
class ScoreFragment : Fragment() {

    private val viewModel: ScoreViewModel by viewModels()

    private lateinit var binding: ScoreFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ScoreFragmentBinding.inflate(inflater, container, false)
        binding.scoreViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.retrieveUpdatedScores()
        initiateInitialAnimation()
        return binding.root
    }

    private fun initiateInitialAnimation() {
        binding.scoreTextAnimated.launchUpwardsAnimation()
    }

    private fun View.launchUpwardsAnimation() {
        val anim: Animation = AnimationUtils.loadAnimation(activity, R.anim.anim_up)
        anim.interpolator = AccelerateInterpolator()
        this.animation = anim

        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                this@launchUpwardsAnimation.launchDownwardsAnimation()

            }

            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })

    }

    private fun View.launchDownwardsAnimation() {
        val anim: Animation = AnimationUtils.loadAnimation(activity, R.anim.anim_down)
        anim.interpolator = DecelerateInterpolator()
        this.animation = anim
        this.animation.fillAfter = true
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                binding.scoreTextAnimated.visibility = View.INVISIBLE
                binding.scoreText.visibility = View.VISIBLE
                viewModel.startTextAnimation()
            }

            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
    }

}
