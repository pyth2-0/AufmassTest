package com.aufmass.app

import android.app.Application

class AufmassApplication : Application() {
    val database: com.aufmass.app.data.local.AufmassDatabase by lazy {
        com.aufmass.app.data.local.AufmassDatabase.getDatabase(this)
    }
}
