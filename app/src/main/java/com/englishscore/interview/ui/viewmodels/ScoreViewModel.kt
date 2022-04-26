package com.englishscore.interview.ui.viewmodels

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.englishscore.interview.data.entities.Score
import com.englishscore.interview.data.repository.ScoreRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.ceil

class ScoreViewModel @ViewModelInject constructor(val scoreRepository: ScoreRepository) :
    ViewModel() {

    fun retrieveUpdatedScores() {
        viewModelScope.launch {
            scoreRepository.updateLatestScores()
        }
    }

    fun startTextAnimation() {
        viewModelScope.launch {
            currentScore.collect {
                if (it.isNotEmpty() && !isAnimationRunning) {
                    initiateTextAnimation(it.first().score.toInt())
                }
            }
        }

    }

    val currentScore: Flow<List<Score>> = scoreRepository.getScores()

    val scoreProgressValue: LiveData<String?>
        get() = _scoreProgressValue

    private val _scoreProgressValue = MutableLiveData<String>()


    val percentProgressValue: LiveData<Int>
        get() = _percentProgressValue.map { Math.round(it).toInt() }

    private val _percentProgressValue = MutableLiveData<Double>()

    val numberOfIntervals: LiveData<Int>
        get() = _numberOfIntervals

    private val _numberOfIntervals = MutableLiveData<Int>()

    val isButtonShowing: LiveData<Boolean>
        get() = _isButtonShowing

    private var _isButtonShowing = MutableLiveData(false)

    val areLevelAndAnimationShowing: LiveData<Boolean>
        get() = _areLevelAndAnimationShowing

    private var _areLevelAndAnimationShowing = MutableLiveData(false)

    val isProgressBarVisible: LiveData<Boolean>
        get() = _isProgressBarVisible

    private val _isProgressBarVisible = MutableLiveData<Boolean>()

    var isAnimationRunning = false

    /** Initiates the text animation */
    private fun initiateTextAnimation(scoreValue: Int) {
        isAnimationRunning = true


        val maxValue = if (scoreValue % 100 == 0) {
            scoreValue
        } else {
            ((ceil(scoreValue / 100.0)) * 100).toInt()
        }
        _numberOfIntervals.value = maxValue / 100
        _isProgressBarVisible.value = true

        val animator = ValueAnimator.ofInt(maxValue)
        animator.duration = maxValue * 25L
        animator.interpolator = LinearInterpolator()

        animator.addUpdateListener {
            val currentAnimatedValue = it.animatedValue as Int
            val currentProgress = ((currentAnimatedValue.toFloat() / maxValue) * 100)



            if (currentAnimatedValue <= scoreValue) {
                _scoreProgressValue.value = currentAnimatedValue.toString()
                _percentProgressValue.value = currentProgress.toDouble()
            }

            if (currentAnimatedValue % 100 == 0 && currentAnimatedValue > 0 && currentAnimatedValue < scoreValue) {
                animator.pause()
                _areLevelAndAnimationShowing.value = true
                viewModelScope.launch {
                    delay(1000L)
                    _areLevelAndAnimationShowing.value = false
                    animator.resume()
                }
            }

        }
        animator.doOnEnd {
            _isButtonShowing.value = true
            isAnimationRunning = false
        }
        animator.start()
    }
}