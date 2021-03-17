package ir.danialchoopan.danialgram.action

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class SingleTonRequestQueueVolley {
    companion object {
        var request_queue: RequestQueue? = null

        fun instance(context: Context): RequestQueue {
            if (request_queue == null)
                request_queue = Volley.newRequestQueue(context)
            return request_queue!!
        }
    }
}

