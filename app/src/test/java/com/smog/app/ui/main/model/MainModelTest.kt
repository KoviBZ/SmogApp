package com.smog.app.ui.main.model

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.smog.app.network.SmogApi
import com.smog.app.network.dto.*
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.mockito.ArgumentMatchers.anyInt
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class MainModelTest : Spek({

    val api: SmogApi by memoized { mock<SmogApi>() }

    val mainModel: MainModel by memoized { DefaultMainModel(api) }

    describe("get nearest station") {
        lateinit var observer: TestObserver<MeasureStation>
        val response: List<MeasureStation> = listOf(
            MeasureStation(
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
        )

        beforeEachTest {
            observer = TestObserver()

            given(api.findAllStations()).willReturn(Single.just(response))

            mainModel.getNearestStation(213.0, 22.0, "Busko-Zdrój").subscribe(observer)
        }

        it("should complete") {
            observer.assertComplete()
        }

        it("should return station") {
            observer.assertResult(response[0])
        }
    }

    describe("get nearest station") {
        lateinit var observer: TestObserver<List<SensorData>>
        val station = Station(23, 22, Param("", "", "", 2))

        context("and values are empty") {
            val sensor = listOf(SensorData("", emptyList()))

            beforeEachTest {
                observer = TestObserver()

                given(api.getStationSensors(anyInt())).willReturn(Single.just(listOf(station)))
                given(api.getSensorData(anyInt())).willReturn(Single.just(sensor[0]))

                mainModel.getSensorsData(23).subscribe(observer)
            }

            it("should complete") {
                observer.assertComplete()
            }

            it("should return empty list") {
                observer.assertResult(emptyList())
            }
        }

        context("and values are not empty") {
            val sensor = listOf(SensorData("", listOf(SensorValue("", 123.2))))

            beforeEachTest {
                observer = TestObserver()

                given(api.getStationSensors(anyInt())).willReturn(Single.just(listOf(station)))
                given(api.getSensorData(anyInt())).willReturn(Single.just(sensor[0]))

                mainModel.getSensorsData(23).subscribe(observer)
            }

            it("should complete") {
                observer.assertComplete()
            }

            it("should return list of sensors") {
                observer.assertResult(sensor)
            }
        }
    }
})