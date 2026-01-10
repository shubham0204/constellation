package io.shubham0204.model2vec.preview

import io.shubham0204.model2vec.data.Thought

private fun daysAgoMillis(days: Int): Long =
    kotlin.time.Clock.System.now().toEpochMilliseconds() - (days * 24 * 60 * 60 * 1000L)

val dummyThoughts = listOf(
    // Gratitude cluster
    Thought(
        id = 1,
        title = "Feeling Grateful Today",
        content = "I'm so thankful for my family and friends. They've been incredibly supportive during difficult times. Gratitude really changes my perspective on life.",
        dateModifiedTimestamp = daysAgoMillis(2),
        embedding = floatArrayOf()
    ),
    Thought(
        id = 2,
        title = "Appreciating the Little Things",
        content = "Today I realized how grateful I am for simple pleasures - a warm cup of coffee, sunshine through the window, and a good book. Thankfulness brings joy.",
        dateModifiedTimestamp = daysAgoMillis(45),
        embedding = floatArrayOf()
    ),
    // Work stress cluster
    Thought(
        id = 3,
        title = "Overwhelmed at Work",
        content = "Work has been extremely stressful lately. Too many deadlines and not enough time. I need to find better ways to manage my workload and reduce anxiety.",
        dateModifiedTimestamp = daysAgoMillis(5),
        embedding = floatArrayOf()
    ),
    Thought(
        id = 4,
        title = "Job Pressure Getting to Me",
        content = "The pressure at the office is intense. I'm feeling burnt out from all the meetings and tasks piling up. Need to talk to my manager about work-life balance.",
        dateModifiedTimestamp = daysAgoMillis(30),
        embedding = floatArrayOf()
    ),
    Thought(
        id = 5,
        title = "Productivity Struggles",
        content = "Can't seem to focus at work today. The stress from pending projects is affecting my productivity. Maybe I should try the Pomodoro technique.",
        dateModifiedTimestamp = daysAgoMillis(60),
        embedding = floatArrayOf()
    ),
    // Exercise/Health cluster
    Thought(
        id = 6,
        title = "Morning Run Success",
        content = "Completed a 5K run this morning! Exercise really boosts my mood and energy levels. I should make running a regular habit for my physical health.",
        dateModifiedTimestamp = daysAgoMillis(3),
        embedding = floatArrayOf()
    ),
    Thought(
        id = 7,
        title = "Started Going to the Gym",
        content = "Finally joined a gym today. Working out and staying fit has been on my mind for months. Physical exercise is so important for mental health too.",
        dateModifiedTimestamp = daysAgoMillis(21),
        embedding = floatArrayOf()
    ),
    Thought(
        id = 8,
        title = "Health Goals for This Year",
        content = "I want to prioritize my fitness and well-being. Planning to exercise three times a week and eat healthier. A healthy body leads to a healthy mind.",
        dateModifiedTimestamp = daysAgoMillis(90),
        embedding = floatArrayOf()
    ),
    // Reading/Learning cluster
    Thought(
        id = 9,
        title = "Finished an Amazing Book",
        content = "Just completed reading 'Atomic Habits'. The insights about building good habits are life-changing. Learning through books is such a rewarding experience.",
        dateModifiedTimestamp = daysAgoMillis(7),
        embedding = floatArrayOf()
    ),
    Thought(
        id = 10,
        title = "New Book Recommendations",
        content = "A friend recommended some great books on personal development and learning. I'm excited to expand my reading list and gain new knowledge.",
        dateModifiedTimestamp = daysAgoMillis(35),
        embedding = floatArrayOf()
    ),
    // Unique thoughts
    Thought(
        id = 11,
        title = "Weekend Trip Planning",
        content = "Thinking about visiting the mountains next weekend. A short getaway would be perfect to recharge and enjoy nature. Need to book accommodation soon.",
        dateModifiedTimestamp = daysAgoMillis(10),
        embedding = floatArrayOf()
    ),
    Thought(
        id = 12,
        title = "Cooking Experiment",
        content = "Tried making homemade pasta for the first time today. It turned out surprisingly well! Cooking is becoming a new hobby I genuinely enjoy.",
        dateModifiedTimestamp = daysAgoMillis(14),
        embedding = floatArrayOf()
    ),
    Thought(
        id = 13,
        title = "Reflecting on Friendships",
        content = "Had a long phone call with an old friend today. It reminded me how important it is to maintain connections and nurture meaningful relationships.",
        dateModifiedTimestamp = daysAgoMillis(25),
        embedding = floatArrayOf()
    ),
    Thought(
        id = 14,
        title = "Learning to Play Guitar",
        content = "Started guitar lessons last week. My fingers hurt but I'm enjoying the process. Music has always been something I wanted to explore creatively.",
        dateModifiedTimestamp = daysAgoMillis(50),
        embedding = floatArrayOf()
    ),
    Thought(
        id = 15,
        title = "Mindfulness Practice",
        content = "Began meditating for 10 minutes every morning. It helps calm my mind and start the day with clarity. Mindfulness is a skill worth developing.",
        dateModifiedTimestamp = daysAgoMillis(40),
        embedding = floatArrayOf()
    )
)
