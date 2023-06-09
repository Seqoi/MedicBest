package com.example.smartlab;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.smartlab.Activities.OnboardingActivity;
import com.example.smartlab.Activities.PasswordCreationActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class OnboardingActivityTest {

    App app;
    SharedPreferencesManager sharedPreferencesManager;

    @Rule
    public ActivityScenarioRule<OnboardingActivity> activityScenarioRule =
            new ActivityScenarioRule<>(OnboardingActivity.class);

    @Test
    public void skipButton_ChangesTextToContinue_WhenLastPageIsShown() throws InterruptedException {
        onView(withId(R.id.viewPager))
                .perform(ViewActions.swipeLeft());

        onView(withId(R.id.viewPager))
                .perform(ViewActions.swipeLeft());

        onView(withId(R.id.skip))
                .check(matches(ViewMatchers.withText("Продолжить")));
    }

    @Before
    public void setUpForPrefManagerTest() {
        sharedPreferencesManager = mock(SharedPreferencesManager.class);
        when(sharedPreferencesManager.isLoggedIn()).thenReturn(true);
        activityScenarioRule.getScenario().onActivity(activity -> {
            app = (App) activity.getApplication();
            app.setPreferencesManager(sharedPreferencesManager);
        });
        Intents.init();
    }

    @Test
    public void testButtonLaunchesSecondActivity() throws InterruptedException {
        activityScenarioRule.getScenario().onActivity(activity -> {
            Intent intent = new Intent(activity, PasswordCreationActivity.class);
            assertNotNull(activity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY));
        });
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}