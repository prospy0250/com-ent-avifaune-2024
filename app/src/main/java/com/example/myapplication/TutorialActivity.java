package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class TutorialActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        boolean fromPhotoPage = getIntent().getBooleanExtra("FROM_PHOTO_PAGE", false);

        ViewPager2 viewPager2=findViewById(R.id.tutorial_viewpager);

        List<Integer> layouts = Arrays.asList(
                R.layout.fragment_tutorial_page1,
                R.layout.fragment_tutorial_page2,
                R.layout.fragment_tutorial_page3
        );
        TutorialFragmentAdapter adapter = new TutorialFragmentAdapter(this, layouts);
        viewPager2.setAdapter(adapter);
        LinearLayout dotsLayout = findViewById(R.id.dots_layout);
        Button tutoEndButton = findViewById(R.id.tuto_end_button);

        View.OnClickListener lTutoEndButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromPhotoPage) {
                    Intent intent = new Intent(TutorialActivity.this, PublicActivity.class);
                    intent.putExtra("TARGET_FRAGMENT", "PhotoPage");
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(TutorialActivity.this, HomeActivity.class);
                    intent.putExtra("PREFERENCES", false);
                    startActivity(intent);
                    finish();
                }
            }
        };
        tutoEndButton.setOnClickListener(lTutoEndButton);

        // Add dots based on page count
        int pageCount = layouts.size();
        ImageView[] dots = new ImageView[pageCount];
        for (int i = 0; i < pageCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_inactive));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dots[i].setLayoutParams(params);
            dotsLayout.addView(dots[i]);
        }

        // Highlight the first dot
        dots[0].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_active));

        // Update dots on page change
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position==2) tutoEndButton.setVisibility(View.VISIBLE);
                else tutoEndButton.setVisibility(View.INVISIBLE);
                for (int i = 0; i < pageCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(
                            getApplicationContext(),
                            i == position ? R.drawable.dot_active : R.drawable.dot_inactive
                    ));
                }
            }
        });
    }
}
