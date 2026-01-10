package io.shubham0204.model2vec.di

import io.shubham0204.model2vec.FileUtils
import io.shubham0204.model2vec.Model2Vec
import io.shubham0204.model2vec.data.AppDatabase
import io.shubham0204.model2vec.data.DBProvider
import io.shubham0204.model2vec.screens.add_edit_thoughts.AddEditThoughtsScreenViewModel
import io.shubham0204.model2vec.screens.thoughts.ThoughtsScreenViewModel
import io.shubham0204.model2vec.services.SimilarThoughtsService
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

actual val targetModule: Module = module {
    single<FileUtils> { FileUtils() }
    single<AppDatabase> { DBProvider().getDatabase() }
    single<Model2Vec> { Model2Vec(fileUtils = get()) }
    single<SimilarThoughtsService> { SimilarThoughtsService(model2vec = get()) }
    viewModel<AddEditThoughtsScreenViewModel> {
        AddEditThoughtsScreenViewModel(
            db = get(),
            similarThoughtsService = get(),
            model2Vec = get()
        )
    }
    viewModel<ThoughtsScreenViewModel> { ThoughtsScreenViewModel(db = get()) }
}