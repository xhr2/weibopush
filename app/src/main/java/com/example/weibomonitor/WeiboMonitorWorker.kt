package com.example.weibomonitor

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class WeiboMonitorWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        fetchWeibo(applicationContext) { posts ->
            if (posts.isNotEmpty()) {
                NotificationHelper.showNotification(applicationContext, posts[0])
            }
        }
        return Result.success()
    }

    companion object {
        private const val UID = "5997609176"
        private const val API_URL = "https://m.weibo.cn/api/container/getIndex?type=uid&value=$UID"
        private var lastWeiboId: String? = null

        fun fetchWeibo(context: Context, callback: (List<WeiboPost>) -> Unit) {
            Thread {
                val client = OkHttpClient()
                val request = Request.Builder().url(API_URL).build()
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: ""
                val posts = mutableListOf<WeiboPost>()
                try {
                    val json = JSONObject(body)
                    val cards = json.getJSONObject("data").getJSONArray("cards")
                    for (i in 0 until cards.length()) {
                        val card = cards.getJSONObject(i)
                        if (card.optInt("card_type") == 9) {
                            val mblog = card.getJSONObject("mblog")
                            val id = mblog.getString("id")
                            val content = mblog.getString("text").replace(Regex("<.*?>"), "")
                            val user = mblog.getJSONObject("user")
                            val userName = user.getString("screen_name")
                            val userAvatar = user.getString("profile_image_url")
                            val url = "https://weibo.com/${UID}/${id}"
                            posts.add(WeiboPost(id, content, userName, userAvatar, url))
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                // 通知只推送最新一条
                if (posts.isNotEmpty() && lastWeiboId != posts[0].id) {
                    lastWeiboId = posts[0].id
                    NotificationHelper.showNotification(context, posts[0])
                }
                callback(posts)
            }.start()
        }
    }
} 