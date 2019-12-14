package endroad.photoquest.application

import endroad.photoquest.data.PlaceDataSource
import endroad.photoquest.data.QuestDataSource
import org.koin.dsl.module
import org.koin.experimental.builder.single

val dataModule = module {
	single<PlaceDataSource>()
	single<QuestDataSource>()
}