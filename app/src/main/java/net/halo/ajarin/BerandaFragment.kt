package net.halo.ajarin

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_beranda.*

class BerandaFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_beranda, null)

        view.findViewById<Button>(R.id.btnOnline).setOnClickListener {
            val pelajaran = ArrayList<String>()
            if (cbMtk.isChecked) {
                pelajaran.add("MTK")
            }
            if (cbIps.isChecked) {
                pelajaran.add("IPS")
            }
            if (cbIpa.isChecked) {
                pelajaran.add("IPA")
            }
            if (cbInggris.isChecked) {
                pelajaran.add("Inggris")
            }
            if (cbSejarah.isChecked) {
                pelajaran.add("Sejarah")
            }
            if (cbPkn.isChecked) {
                pelajaran.add("PKN")
            }

            val intent = Intent(activity, OnlineActivity::class.java)
            intent.putExtra("pelajaran", pelajaran)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.btnOffline).setOnClickListener {
            Toast.makeText(activity, "Offline", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}