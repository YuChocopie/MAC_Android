package mashup.mac.ui.mypage

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import mashup.mac.R
import mashup.mac.base.BaseActivity
import mashup.mac.base.BaseFragment
import mashup.mac.databinding.ActivityMyPageBinding

class MyPageActivity : BaseActivity<ActivityMyPageBinding>(R.layout.activity_my_page) {

    override var logTag = "MyPageActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initButton()
        showMyCounselingTab()
        replaceFragment(
            MyPageFragment.newInstanceCounseling()
        )
    }

    private var viewType = MyPageFragment.ViewType.MyCounseling

    private fun initButton() {
        with(binding) {
            btnMyCounseling.setOnClickListener {
                if (viewType == MyPageFragment.ViewType.MyCounseling) {
                    return@setOnClickListener
                }

                showMyCounselingTab()
                replaceFragment(
                    MyPageFragment.newInstanceCounseling()
                )
            }

            btnMyAnswer.setOnClickListener {
                if (viewType == MyPageFragment.ViewType.MyAnswer) {
                    return@setOnClickListener
                }

                showMyAnswerTab()
                replaceFragment(
                    MyPageFragment.newInstanceAnswer()
                )
            }
        }
    }

    private fun replaceFragment(fragment: BaseFragment<*>) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commitAllowingStateLoss()
    }

    private val focusedTabBackground by lazy {
        ResourcesCompat.getDrawable(resources, R.drawable.rect_white_rad_5, null)
    }

    private fun showMyCounselingTab() {
        hideAllTab()
        setFocusTabTitle(binding.tvMyCounseling)
    }

    private fun showMyAnswerTab() {
        hideAllTab()
        setFocusTabTitle(binding.tvMyAnswer)
    }

    private fun setFocusTabTitle(textView: TextView) {
        with(textView) {
            background = focusedTabBackground
            setTypeface(null, Typeface.BOLD)
            setTextColor(ContextCompat.getColor(context, R.color.gray11))
        }
    }

    private fun hideAllTab() {
        setUnFocusTabTitle(binding.tvMyCounseling)
        setUnFocusTabTitle(binding.tvMyAnswer)
    }

    private fun setUnFocusTabTitle(textView: TextView) {
        with(textView) {
            background = null
            setTypeface(null, Typeface.NORMAL)
            setTextColor(ContextCompat.getColor(context, R.color.gray6))
        }
    }
}