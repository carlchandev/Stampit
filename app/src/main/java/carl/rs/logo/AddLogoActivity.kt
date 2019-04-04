package carl.rs.logo

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import ja.burhanrashid52.photoeditor.PhotoEditor
import kotlinx.android.synthetic.main.activity_add_logo.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AddLogoActivity : AppCompatActivity() {

    var logoAlpha = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_logo)

        println("In AddLogoActivity")
        val bundle = intent.extras
        val imageUri = bundle.getString("imageUri")
        println("imageUri ${imageUri}")


        // selectedImage.setImageURI(Uri.parse(imageUri))
        photoEditorView.source.setImageURI(Uri.parse(imageUri))

        //Use custom font using latest support library
        val mTextRobotoTf = ResourcesCompat.getFont(this, R.font.open_sans)

        // loading font from assest
        val mEmojiTypeFace = Typeface.createFromAsset(assets, "emojione_android.ttf")

        val mPhotoEditor = PhotoEditor.Builder(this, photoEditorView)
            .setPinchTextScalable(true)
            .setDefaultTextTypeface(mTextRobotoTf)
            .setDefaultEmojiTypeface(mEmojiTypeFace)
            .build()

        opacitySlider.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    println("onProgressChanged: ${seekBar?.progress}")
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    println("onStartTrackingTouch: ${seekBar?.progress}")
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    println("onStopTrackingTouch: ${seekBar?.progress}")
                    val opacityValue = seekBar!!.progress
                    logoAlpha = opacityValue
                    opacity.text = "$opacityValue"
                }
            }
        )

        opacityBtn.setOnClickListener {
            toolPanel.visibility = View.GONE
            opacityPanel.visibility = View.VISIBLE
        }

        // TODO onblur, remove panel

        undoBtn.setOnClickListener {
            mPhotoEditor.undo()
        }
        redoBtn.setOnClickListener {
            mPhotoEditor.redo()
        }

        shareBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadDir.exists()) {
                    downloadDir.createNewFile()
                }
            }
            val jpgPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/rs" + DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + ".jpg"
            Log.e("PhotoEditor", "jpgPath: $jpgPath")
            mPhotoEditor.saveAsFile(jpgPath, object : PhotoEditor.OnSaveListener {
                override fun onSuccess(imagePath: String) {
                    Log.e("PhotoEditor", "Image Saved Successfully to $imagePath")
                    // share
                    val shareIntent = Intent().apply {
                        this.action = Intent.ACTION_SEND
                        this.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath))
                        this.type = "image/jpeg"
                    }
                    startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.shareMessage)))

                }

                override fun onFailure(exception: Exception) {
                    Log.e("PhotoEditor", "Sorry! Something goes wrong!")
                    Toast.makeText(this@AddLogoActivity, "Sorry! We need the permission to share!", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }


        // mPhotoEditor.addText("this is my testing", 123456);

        addLightLogoBtn.setOnClickListener {
            val rsLogo = this.getBitmapFromVectorDrawable(R.drawable.logo_rs_light)
            mPhotoEditor.addImage(rsLogo)
        }
        addDarkLogoBtn.setOnClickListener {
            val rsLogo = this.getBitmapFromVectorDrawable(R.drawable.logo_rs_dark)
            mPhotoEditor.addImage(rsLogo)
        }
    }

    fun Context.getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
        var drawable = ContextCompat.getDrawable(this, drawableId) ?: return null

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable).mutate()
        }

//        val bitmap = Bitmap.createBitmap(
//            drawable.intrinsicWidth,
//            drawable.intrinsicHeight,
//            Bitmap.Config.ARGB_8888) ?: return null
//        val canvas = Canvas(bitmap)
//        drawable.setBounds(0, 0, canvas.width, canvas.height)

        val bitmap = Bitmap.createBitmap(
            200, 200,
            Bitmap.Config.ARGB_8888
        ) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, 200, 200)
        // opacity 0-255
        drawable.alpha = logoAlpha * 255 / 100
        drawable.draw(canvas)

        return bitmap
    }
}