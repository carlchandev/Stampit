package carl.rs.logo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator


const val PICK_IMAGE = 1
const val FROM_ADD_LOGO = 2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadLaunchAnimation()
        Handler().postDelayed({ selectImage() }, 1500)
    }

    private fun loadLaunchAnimation() {
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = DecelerateInterpolator() //add this
        fadeOut.duration = 1500

        logo.startAnimation(fadeOut)
        logoName.startAnimation(fadeOut)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE) {
            println("user picked images")
            println("requestCode ${requestCode}")
            println("resultCode ${resultCode}")
            println("resultCode ${data?.data}")

            val addLogoActivity = Intent(this, AddLogoActivity::class.java)
            addLogoActivity.putExtra("imageUri", data!!.data.toString())
            startActivityForResult(addLogoActivity, FROM_ADD_LOGO)

//            val activity = Intent(this, ImageActivity::class.java)
//            activity.putExtra("imageUri", data!!.data.toString())
//            startActivityForResult(activity, FROM_ADD_LOGO)
        }

        if (requestCode == FROM_ADD_LOGO) {
            selectImage()
        }

    }

    fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

}
