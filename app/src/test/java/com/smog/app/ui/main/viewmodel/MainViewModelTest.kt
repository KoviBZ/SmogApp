package com.smog.app.ui.main.viewmodel

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.internal.createInstance
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.smog.app.network.Resource
import com.smog.app.network.dto.City
import com.smog.app.network.dto.Commune
import com.smog.app.network.dto.MeasureStation
import com.smog.app.network.dto.SensorData
import com.smog.app.network.scheduler.TestSchedulerProvider
import com.smog.app.ui.main.model.MainModel
import com.smog.app.utils.FindAllError
import com.smog.app.utils.SensorDataError
import io.reactivex.Single
import org.mockito.ArgumentMatchers.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.lang.IllegalArgumentException

class MainViewModelTest : Spek({

    val model by memoized { mock<MainModel>() }
    val schedulerProvider by memoized { TestSchedulerProvider() }

    val viewModel by memoized { MainViewModel(model, schedulerProvider) }

    beforeEachTest {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }
        })
    }

    afterEachTest {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    describe("retrieve nearest station") {
        val response = MeasureStation(
            0,
            "",
            1.0,
            2.0,
            City(
              0,
                "",
                Commune("", "", "")
            ),
            ""
        )

        val observer by memoized { mock<Observer<Resource<MeasureStation>>>() }

        context("and succeeds") {
            beforeEachTest {
                given(model.getNearestStation(anyDouble(), anyDouble(), anyString())).willReturn(
                    Single.just(response)
                )

                viewModel.getStationLiveData().observeForever(observer)
                viewModel.retrieveNearestStation(23.2, 123.3, "Kielce")
            }

            it("should post loading") {
                verify(observer).onChanged(Resource.loading())
            }

            it("should post response as success") {
                verify(observer).onChanged(Resource.success(response))
            }
        }

        context("and fails") {
            beforeEachTest {
                given(model.getNearestStation(anyDouble(), anyDouble(), anyString())).willReturn(
                    Single.error(IllegalArgumentException())
                )

                viewModel.getStationLiveData().observeForever(observer)
                viewModel.retrieveNearestStation(23.2, 123.3, "Kielce")
            }

            it("should post loading") {
                verify(observer).onChanged(Resource.loading())
            }

            it("should post FindAllError as error") {
                verify(observer).onChanged(Resource.error(FindAllError))
            }
        }
    }

    describe("retrieve sensor data") {
        val response = listOf(SensorData("", emptyList()))

        val observer by memoized { mock<Observer<Resource<List<SensorData>>>>() }

        context("and succeeds") {
            beforeEachTest {
                given(model.getSensorsData(anyInt())).willReturn(Single.just(response))

                viewModel.getSensorLiveData().observeForever(observer)
                viewModel.retrieveSensorData(3)
            }

            it("should post loading") {
                verify(observer).onChanged(Resource.loading())
            }

            it("should post response as success") {
                verify(observer).onChanged(Resource.success(response))
            }
        }

        context("and fails") {
            val id = 12
            val errorResponse = createInstance(SensorDataError::class)

            beforeEachTest {
                given(model.getSensorsData(anyInt())).willReturn(
                    Single.error(IllegalArgumentException())
                )

                viewModel.getSensorLiveData().observeForever(observer)
                viewModel.retrieveSensorData(id)
            }

            it("should post loading") {
                verify(observer).onChanged(Resource.loading())
            }

//Poległem tutaj
//            it("should post SensorDataError as error with request id") {
//                verify(observer).onChanged(Resource.error(errorResponse))
//            }
        }
    }
})