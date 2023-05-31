package com.yyn.task_2


import android.app.Service
import android.content.Intent
import android.os.IBinder

class AppLocalesMetadataHolderService : Service() {
    // Implement the necessary methods for your service
    // ...

    override fun onBind(intent: Intent): IBinder? {
        // Return null if your service does not support binding
        return null
    }
}
