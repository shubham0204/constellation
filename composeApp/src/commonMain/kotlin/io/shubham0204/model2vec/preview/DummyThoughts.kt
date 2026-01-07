import io.shubham0204.model2vec.data.Thought
import kotlin.time.Clock

val dummyThoughts =
    listOf<Thought>(
        Thought(
            id = 1,
            title = "First Thought",
            content = "This is the content of the first thought.",
            dateModifiedTimestamp = Clock.System.now().epochSeconds,
            embedding = floatArrayOf()
        ),
        Thought(
            id = 2,
            title = "Second Thought",
            content = "This is the content of the second thought.",
            dateModifiedTimestamp = Clock.System.now().epochSeconds,
            embedding = floatArrayOf()
        ),
        Thought(
            id = 3,
            title = "Third Thought",
            content = "This is the content of the third thought.",
            dateModifiedTimestamp = Clock.System.now().epochSeconds,
            embedding = floatArrayOf()
        ),
    )
