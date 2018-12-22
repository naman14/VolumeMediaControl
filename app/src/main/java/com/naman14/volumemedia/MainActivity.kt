package com.naman14.volumemedia

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.Uri
import android.provider.Settings

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        doAsyncPostWithResult(handler = {
            val packages = packageManager.getInstalledPackages(0)

            val appList = arrayListOf<App>()

            for (packageInfo in packages) {
                appList.add(App(packageInfo.applicationInfo.loadLabel(packageManager).toString(), packageInfo.packageName))
            }
            appList.apply { sortBy { it.name.toLowerCase() } }
        }, postHandler = { appList ->
            progressBar.visibility = View.GONE
            appList?.let {
                val adapter = AppListAdapter(this)
                rvApps.layoutManager = LinearLayoutManager(this)
                rvApps.adapter = adapter
                adapter.setData(it)
            }

        }).execute()

        ivHelp.setOnClickListener {
            AlertDialog.Builder(this).setTitle("Volume Media Control")
                .setMessage(R.string.description)
                .setNegativeButton("Github", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse("https://github.com/naman14/VolumeMediaControl") })
                    }
                })
                .setPositiveButton("Close", null).create().show()
        }

        tvSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }
    }

    override fun onResume() {
        updateStatus()
        super.onResume()
    }

    private fun updateStatus() {
        if (Utils.checkAccessibilityPermission(this)) {
            tvStatus.text = "Enabled"
            tvStatusInfo.text = "Media volume will be adjusted by default for the apps that you have selected."
        } else {
            tvStatus.text = "Disabled"
            tvStatusInfo.text = "Please enable Volume Media Control accessibility service."
        }
    }
}
