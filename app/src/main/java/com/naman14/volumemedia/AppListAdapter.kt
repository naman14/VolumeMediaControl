package com.naman14.volumemedia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppListAdapter(val context: Context): RecyclerView.Adapter<AppListAdapter.ViewHolder>() {

    private var appList: ArrayList<App> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_package, parent, false));
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = appList[position].name
        holder.checkbox.setOnCheckedChangeListener(null)
        holder.checkbox.isChecked = MediaVolumeService.enabledPackageList!!.contains(appList[position].`package`)
        holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            var set = MediaVolumeService.getEnabledPackageList(context).toMutableSet()
            if (isChecked) {
                set.add(appList[position].`package`)
            } else {
                if (set.contains(appList[position].`package`)) {
                    set.remove(appList[position].`package`)
                }
            }
            MediaVolumeService.setEnabledPackageList(context, set)
        }
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val checkbox: CheckBox = view.findViewById(R.id.cbEnabled)

    }

    fun setData(appList: ArrayList<App>) {
        this.appList = appList
        MediaVolumeService.enabledPackageList = MediaVolumeService.getEnabledPackageList(context)
        notifyDataSetChanged()
    }


}