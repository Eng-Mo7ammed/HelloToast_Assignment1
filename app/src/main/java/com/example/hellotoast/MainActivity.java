
package com.example.hellotoast;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.hellotoast.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private int count = 0;
    private ActivityMainBinding mainBinding;

    private final int blue = Color.rgb(63, 81, 181);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(
                getWindow(), false
        );

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mainBinding = ActivityMainBinding.inflate(
                getLayoutInflater()
        );

        // Main screen container
        FrameLayout container = new FrameLayout(this);
        container.setBackgroundColor(blue);

        // Content below the status bar
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setBackgroundColor(Color.WHITE);

        FrameLayout.LayoutParams contentParams =
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );

        container.addView(content, contentParams);

        // Blue title bar
        TextView toolbar = new TextView(this);
        toolbar.setText("Hello Constraint");
        toolbar.setTextColor(Color.WHITE);
        toolbar.setTextSize(18);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);
        toolbar.setPadding(dp(16), 0, 0, 0);
        toolbar.setBackgroundColor(blue);

        content.addView(
                toolbar,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(56)
                )
        );

        // Original XML layout
        mainBinding.getRoot().setBackgroundColor(Color.WHITE);

        content.addView(
                mainBinding.getRoot(),
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        // Black area behind navigation buttons
        View navigationBackground = new View(this);
        navigationBackground.setBackgroundColor(Color.BLACK);

        FrameLayout.LayoutParams navParams =
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        Gravity.BOTTOM
                );

        container.addView(navigationBackground, navParams);

        setContentView(container);

        // Status bar and navigation bar icon colors
        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(
                        getWindow(),
                        getWindow().getDecorView()
                );

        controller.setAppearanceLightStatusBars(false);
        controller.setAppearanceLightNavigationBars(false);

        // Automatically reserve space for system bars
        ViewCompat.setOnApplyWindowInsetsListener(
                container,
                (view, insets) -> {

                    int statusHeight = insets.getInsets(
                            WindowInsetsCompat.Type.statusBars()
                    ).top;

                    int navigationHeight = insets.getInsets(
                            WindowInsetsCompat.Type.navigationBars()
                    ).bottom;

                    FrameLayout.LayoutParams params =
                            (FrameLayout.LayoutParams)
                                    content.getLayoutParams();

                    params.topMargin = statusHeight;
                    params.bottomMargin = navigationHeight;
                    content.setLayoutParams(params);

                    FrameLayout.LayoutParams bottomParams =
                            (FrameLayout.LayoutParams)
                                    navigationBackground.getLayoutParams();

                    bottomParams.height = navigationHeight;
                    navigationBackground.setLayoutParams(bottomParams);

                    return insets;
                }
        );

        // Buttons
        mainBinding.buttonToast.setOnClickListener(
                v -> show_toast(v)
        );

        mainBinding.buttonCount.setOnClickListener(
                v -> increase_count(v)
        );

        mainBinding.buttonZero.setOnClickListener(
                v -> show_zero(v)
        );

        // Restore count after rotation
        if (savedInstanceState != null) {
            count = savedInstanceState.getInt("count", 0);

            mainBinding.textDisplayCount.setText(
                    String.valueOf(count)
            );

            if (count > 0) {
                updateButtonColors();
            }
        }
    }

    private int dp(int value) {
        return (int) (value *
                getResources().getDisplayMetrics().density);
    }

    public void show_toast(View view) {
        Toast.makeText(
                this,
                "Hello Toast",
                Toast.LENGTH_SHORT
        ).show();
    }

    public void increase_count(View view) {
        count++;

        mainBinding.textDisplayCount.setText(
                String.valueOf(count)
        );

        updateButtonColors();
    }

    private void updateButtonColors() {

        int color = count % 2 == 1
                ? R.color.count_odd
                : R.color.count_even;

        mainBinding.buttonCount.setBackgroundTintList(
                ContextCompat.getColorStateList(this, color)
        );

        mainBinding.buttonZero.setBackgroundTintList(
                ContextCompat.getColorStateList(
                        this,
                        R.color.zero_active
                )
        );
    }

    public void show_zero(View view) {
        count = 0;

        mainBinding.textDisplayCount.setText("0");

        mainBinding.buttonZero.setBackgroundTintList(
                ContextCompat.getColorStateList(
                        this,
                        R.color.zero_inactive
                )
        );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("count", count);
        super.onSaveInstanceState(outState);
    }
}
