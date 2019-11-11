package net.ebisoba.autoslideshowapp

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.net.Uri
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val PERMISSIONS_REQUEST_CODE = 100

    public var increment = 0
    public var max_increment = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo()
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }

    private fun getContentsInfo() {
        // 画像の情報を取得する
        val resolver = contentResolver
        val cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                null, // 項目(null = 全項目)
                null, // フィルタ条件(null = フィルタなし)
                null, // フィルタ用パラメータ
                null // ソート (null ソートなし)
        )

        // カーソルを最初に移動
        cursor.moveToFirst()

        // indexからIDを取得し、そのIDから画像のURIを取得する
        var fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        var id = cursor.getLong(fieldIndex)
        var imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)


        val list = ArrayList<Uri>()

            do {
//        if(cursor.moveToNext()){
//            // indexからIDを取得し、そのIDから画像のURIを取得する
            fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            id = cursor.getLong(fieldIndex)
            imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                list.add(imageUri)
//            Log.d("ANDROIDs", "URI : " + imageUri.toString())
//
//            // 番号の抽出
//            var regex = Regex(pattern = "1|2|3|4|5|6|7|8|9|0|")
//            var matched = regex.containsMatchIn(input = "imageUri.toString()")
//            var numbers = imageUri.toString()
//            numbers = numbers.replace("content://media/external/images/media/", "")
//            var numberInt = Integer.parseInt(numbers)
//            Log.d("ANDROIDs_matched", matched.toString())
//            Log.d("ANDROIDss_numberInt", numberInt.toString())
//
//
//            // 画像
            imageView.setImageURI(imageUri)
//            Log.d("ANDROIDss", cursor.moveToNext().toString())
//        }
            } while (cursor.moveToNext())

        // 画像表示
        imageView.setImageURI(list[increment])
        max_increment = list.size - 1
        Log.d("lists", max_increment.toString())

        for(i in list.indices) {
            Log.d("ANDROIDs",i.toString() + "," + list[i].toString())
        }
        Log.d("increments", increment.toString() + "/" + max_increment.toString())


//        if (cursor!!.moveToFirst()) {
//            do {
//        if(cursor.moveToNext()){
//            // indexからIDを取得し、そのIDから画像のURIを取得する
//            fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
//            id = cursor.getLong(fieldIndex)
//            imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//
//            Log.d("ANDROIDs", "URI : " + imageUri.toString())
//
//            // 番号の抽出
//            var regex = Regex(pattern = "1|2|3|4|5|6|7|8|9|0|")
//            var matched = regex.containsMatchIn(input = "imageUri.toString()")
//            var numbers = imageUri.toString()
//            numbers = numbers.replace("content://media/external/images/media/", "")
//            var numberInt = Integer.parseInt(numbers)
//            Log.d("ANDROIDs_matched", matched.toString())
//            Log.d("ANDROIDss_numberInt", numberInt.toString())
//
//
//            // 画像
//            imageView.setImageURI(imageUri)
//            Log.d("ANDROIDss", cursor.moveToNext().toString())
//        }
//            } while (cursor.moveToNext())

//        }
        cursor.close()
    }

    override fun onClick(v: View){
        if (v.id == R.id.button1) {
            getContentsInfo()
            increment += 1
            if(increment>max_increment){
                increment = 0
            }
            Log.d("button1", "進む ボタン1をタップした")
        }else if(v.id == R.id.button2){
            getContentsInfo()
            increment -= 1
            if(increment<0){
                increment = max_increment
            }
            Log.d("button2", "ボタン2をタップした")
        }else if(v.id == R.id.button3){
            Log.d("button3", "ボタン3をタップした")
        }
        Log.d("increments_onClick", increment.toString() + "/" + max_increment.toString())
    }
}
