package com.hfad.betterlift.ui.exercise

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hfad.betterlift.database.asDatabaseModel
import com.hfad.betterlift.database.asDomainModel
import kotlin.math.max

private const val STARTING_KEY : Long = 0
/*class ExercisePagingSource(val exerciseRepo: ExerciseRepo) : PagingSource<Long, Exercise>()  {

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Exercise> {
        val start = params.key ?: STARTING_KEY
        val range = start.until(start + params.loadSize)



        return LoadResult.Page(
            data = range.map { id -> exerciseRepo.retrieveExerciseItem(id).asDomainModel() },
            prevKey = when (start) {
                STARTING_KEY -> null
                else -> ensureValidKey(key = range.first - params.loadSize)
            },
            nextKey = range.last + 1
        )
    }
    override fun getRefreshKey(state: PagingState<Long, Exercise>): Long? {
        val anchorPosition = state.anchorPosition ?: return null
        val dbExercise = state.closestItemToPosition(anchorPosition)?.asDatabaseModel() ?: return null
        return ensureValidKey(key = dbExercise.exerciseId - (state.config.pageSize / 2))
    }

    private fun ensureValidKey(key: Long) = max(STARTING_KEY, key)
}*/

/*
The load() function will be called by the Paging library to asynchronously fetch more data to be
displayed as the user scrolls around. The LoadParams object keeps information related to the load
operation, including the following:

    Key of the page to be loaded - If this is the first time that load() is called, LoadParams.key will be null.
    In this case, you will have to define the initial page key. For our project, we use the article ID as the key.
    Let's also add a STARTING_KEY constant of 0 to the top of the ArticlePagingSource file for the initial page key.

    Load size - the requested number of items to load.
 */


/*
A LoadResult.Page has three required arguments:

data: A List of the items fetched.
prevKey: The key used by the load() method if it needs to fetch items behind the current page.
nextKey: The key used by the load() method if it needs to fetch items after the current page.

...and two optional ones:

itemsBefore: The number of placeholders to show before the loaded data.
itemsAfter: The number of placeholders to show after the loaded data.
 */

