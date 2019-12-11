package endroad.photoquest.data

import android.content.Context
import endroad.photoquest.Quest.Quest

class QuestDataSource(context: Context) {

	companion object {
		const val QUEST_PATH = "quest/q1/"
	}

	private val hardcoreData = listOf(Quest(context, QUEST_PATH))

	fun getList() = hardcoreData
}