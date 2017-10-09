package com.elpassion.mobilization.tddworkshop

import com.jakewharton.rxrelay2.Relay
import com.jakewharton.rxrelay2.ReplayRelay
import io.reactivex.Observable
import io.reactivex.Observer

class ActionsRelay<Action> : Relay<Action>() {

    private var replayRelay = ReplayRelay.create<Action>()
    private var publishRelay = ReplayRelay.create<Action>()

    override fun accept(value: Action) {
        if (publishRelay.hasObservers()) {
            publishRelay.accept(value)
        } else {
            replayRelay.accept(value)
        }
    }

    override fun subscribeActual(observer: Observer<in Action>) {
        Observable.merge(replayRelay, publishRelay).subscribe(observer)
        replayRelay = ReplayRelay.create()
    }

    override fun hasObservers() = publishRelay.hasObservers()
}
