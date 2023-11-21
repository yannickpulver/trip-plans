package com.yannickpulver.plans

import LocationsViewModel
import com.yannickpulver.plans.ui.feature.locations.LocationsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LocationsTest {

    private lateinit var dataRepository: InMemoryDataRepository
    private lateinit var mapsRepo: InMemoryMapsRepository
    private lateinit var viewModel: LocationsViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        dataRepository = InMemoryDataRepository()
        mapsRepo = InMemoryMapsRepository()
        viewModel = LocationsViewModel(
            dataRepository = dataRepository,
            mapsRepo = mapsRepo
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `empty predictions when empty query`() {
        viewModel.updateQuery("")
        assertEquals(LocationsState.Empty, viewModel.state.value)
    }

    @Test
    fun `has predictions from query`() = runTest {
        // State needs a collector, otherwise it wont combine state updates
        val collectJob = launch(Dispatchers.Unconfined) { viewModel.state.collect() }

        viewModel.updateQuery("test")

        assertEquals(
            LocationsState.Empty.copy(predictions = mapsRepo.placePredictions),
            viewModel.state.value
        )

        collectJob.cancel()
    }
}

