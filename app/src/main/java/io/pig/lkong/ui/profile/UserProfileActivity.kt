package io.pig.lkong.ui.profile

import androidx.fragment.app.Fragment
import io.pig.lkong.R
import io.pig.ui.common.SimpleContainerActivity

class UserProfileActivity : SimpleContainerActivity() {

    override fun newFragment(): Fragment {
        val intent = intent
        val extras = intent.extras
        return UserProfileFragment.newInstance(extras)
    }

    fun switchFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            android.R.anim.fade_in,
            android.R.anim.fade_out,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }
}