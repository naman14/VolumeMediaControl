package com.naman14.volumemedia

import android.os.AsyncTask

class doAsyncPostWithResult<T>(val handler: () -> T?, val postHandler: (bitmap: T?) -> Unit) : AsyncTask<Void, Void, T>() {
    override fun doInBackground(vararg params: Void?): T? {
        return handler()
    }

    override fun onPostExecute(result: T?) {
        postHandler(result)
    }
}