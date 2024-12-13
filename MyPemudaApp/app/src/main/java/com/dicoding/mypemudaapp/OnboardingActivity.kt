package com.dicoding.mypemudaapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val dotsIndicator = findViewById<WormDotsIndicator>(R.id.dotsIndicator)

        // Data for onboarding slides
        val onboardingItems = listOf(
            OnboardingItem(
                R.drawable.ic_slide1,
                "Kami adalah layanan penyedia lapangan pekerjaan terkini",
                "Untuk membantu generasi muda mendapatkan pengalaman baru!"
            ),
            OnboardingItem(
                R.drawable.ic_slide2,
                "Wadah dimana kamu bisa menemukan tempatmu",
                "Menampilkan berbagai macam jenis dan tempat pekerjaan terbaru"
            ),
            OnboardingItem(
                R.drawable.ic_slide3,
                "Mari bergabung dan mulai karirmu!",
                "Mulai dengan mudah berakhir dengan seru!"
            )
        )

        // Set up adapter and ViewPager
        val adapter = OnboardingAdapter(onboardingItems)
        viewPager.adapter = adapter

        // Connect DotsIndicator to ViewPager
        dotsIndicator.setViewPager2(viewPager)

        // Hide back button on first page
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                btnBack.visibility = if (position == 0) ImageView.INVISIBLE else ImageView.VISIBLE
            }
        })

        // Handle Next button click
        btnNext.setOnClickListener {
            if (viewPager.currentItem + 1 < onboardingItems.size) {
                viewPager.currentItem += 1
            } else {
                // Navigate to RegencyActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Handle Back button click
        btnBack.setOnClickListener {
            if (viewPager.currentItem > 0) {
                viewPager.currentItem -= 1
            }
        }
    }
}
