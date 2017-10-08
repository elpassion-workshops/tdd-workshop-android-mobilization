package com.elpassion.mobilization.tddworkshop

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay

abstract class Model<Event, State>(initialState: State) {
    val events: PublishRelay<Event> = PublishRelay.create<Event>()
    val states: BehaviorRelay<State> = BehaviorRelay.createDefault(initialState)
    abstract fun onCleared()
}