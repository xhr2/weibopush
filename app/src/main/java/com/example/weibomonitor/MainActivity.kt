package com.example.weibomonitor

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var posts: List<WeiboPost> = listOf()
    private lateinit var adapter: WeiboAdapter
    private lateinit var btnRefresh: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = WeiboAdapter(posts)
        recyclerView.adapter = adapter

        btnRefresh = findViewById(R.id.btnRefresh)
        btnRefresh.setOnClickListener {
            btnRefresh.isEnabled = false
            btnRefresh.text = "正在获取..."
            WeiboMonitorWorker.fetchWeibo(this) { newPosts ->
                runOnUiThread {
                    posts = newPosts
                    adapter = WeiboAdapter(posts)
                    recyclerView.adapter = adapter
                    btnRefresh.isEnabled = true
                    btnRefresh.text = "立即获取最新内容"
                }
            }
        }

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