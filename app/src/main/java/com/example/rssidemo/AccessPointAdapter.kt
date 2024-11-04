package com.example.rssidemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class AccessPointInfo(
    val ssid: String,
    val bssid: String,
    val rssi: Int,
    val timestamp: Long = System.currentTimeMillis(),
)

class AccessPointAdapter : RecyclerView.Adapter<AccessPointAdapter.AccessPointViewHolder>() {
    private val accessPoints = mutableListOf<AccessPointInfo>()

    class AccessPointViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        val tvSsid: TextView = itemView.findViewById(R.id.tvSsid)
        val tvBssid: TextView = itemView.findViewById(R.id.tvBssid)
        val tvRssi: TextView = itemView.findViewById(R.id.tvRssi)
        val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AccessPointViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_access_point, parent, false)
        return AccessPointViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AccessPointViewHolder,
        position: Int,
    ) {
        val ap = accessPoints[position]
        holder.tvSsid.text = "SSID: ${ap.ssid}"
        holder.tvBssid.text = "BSSID: ${ap.bssid}"
        holder.tvRssi.text = "RSSI: ${ap.rssi} dBm"
        holder.tvTimestamp.text =
            java.text
                .SimpleDateFormat(
                    "HH:mm:ss",
                    java.util.Locale.getDefault(),
                ).format(ap.timestamp)
    }

    override fun getItemCount() = accessPoints.size

    fun updateAccessPoint(accessPointInfo: AccessPointInfo) {
        // Find existing entry or add new one
        val existingIndex = accessPoints.indexOfFirst { it.bssid == accessPointInfo.bssid }
        if (existingIndex != -1) {
            accessPoints[existingIndex] = accessPointInfo
            notifyItemChanged(existingIndex)
        } else {
            accessPoints.add(0, accessPointInfo)
            notifyItemInserted(0)
        }
    }
}
