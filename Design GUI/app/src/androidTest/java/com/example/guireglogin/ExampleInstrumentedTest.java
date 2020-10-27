package com.example.guireglogin;

import android.content.pm.LauncherApps;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(loginactivity.class);
    @Test
    public void ClickAllTheButtons() throws Exception{
        Intents.init();
        ActivityScenario scenario = rule.getScenario();

        onView(withId(R.id.gotoreg))
                .perform(click());
        scenario.launch(regactivity.class);

        onView(withId(R.id.regemailusername))
                .perform(typeText("confirmedworksfromtestfile@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.regpassword))
                .perform(typeText("writtenfromTestfile"));
        closeSoftKeyboard();
        onView(withId(R.id.regpassword2))
                .perform(typeText("writtenfromTestfile"));
        closeSoftKeyboard();
        onView(withId(R.id.register)).perform(click());

        scenario.launch(loginactivity.class);
        Intents.release();
    }

}