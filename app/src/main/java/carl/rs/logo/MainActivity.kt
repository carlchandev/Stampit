package carl.rs.logo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.os.HandlerCompat.postDelayed


const val PICK_IMAGE = 1
const val FROM_ADD_LOGO = 2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Make sure this is before calling super.onCreate
//        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // selectImageBtn.setOnClickListener {
        //    selectImage()
        // }

        Handler().postDelayed({ selectImage() }, 1000)


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
        }

        if (requestCode == FROM_ADD_LOGO) {
            selectImage()
        }

    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        selectImage()
//    }

//    override fun onResume() {
//        super.onResume()
//        selectImage()
//    }

    fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

}
