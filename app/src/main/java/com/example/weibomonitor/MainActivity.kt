package com.example.weibomonitor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var posts: List<WeiboPost> = listOf()
    private lateinit var adapter: WeiboAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = WeiboAdapter(posts)
        recyclerView.adapter = adapter

        // 启动定时任务，每10分钟拉取一次微博
        val workRequest = PeriodicWorkRequestBuilder<WeiboMonitorWorker>(10, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(workRequest)

        // 首次启动时主动拉取一次
        WeiboMonitorWorker.fetchWeibo(this) { newPosts ->
            runOnUiThread {
                posts = newPosts
                adapter = WeiboAdapter(posts)
                recyclerView.adapter = adapter
            }
        }
    }
} 