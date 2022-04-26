package com.englishscore.interview.ui.binding

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.englishscore.interview.R
import com.englishscore.interview.ui.view.ProgressDrawable


@BindingAdapter("animationView")
fun bindanimationView(animationView: LottieAnimationView, isAnimationPlaying: Boolean) {
    if (isAnimationPlaying) animationView.playAnimation() else animationView.cancelAnimation()
    animationView.visibility = if (isAnimationPlaying) View.VISIBLE else View.GONE
}

@BindingAdapter("progressBar")
fun bindProgressBar(progressBar: ProgressBar, numberOfIntervals: Int) {
    progressBar.progressDrawable = ProgressDrawable(
        numberOfIntervals,
        progressBar.context.getColorId(R.color.edu_blue),
        progressBar.context.getColorId(R.color.white)
    )

}


fun Context.getColorId(colorID: Int): Int {
    return resources.getColor(colorID, null)
}

