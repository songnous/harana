package com.harana.designer.frontend.utils

import diode.{Circuit, Dispatcher, ModelR}
import slinky.core.facade.Hooks.{useContext, useEffect, useState}
import slinky.core.facade.ReactContext

object DiodeUtils {

  def use[M <: AnyRef, T, S <: Circuit[M]](context: ReactContext[S], selector: ModelR[M, T]): (T, Dispatcher) = {
    val circuit = useContext(context)
    val (state, setState) = useState[T](default = selector())

    useEffect(() => {
      val subscription = circuit.subscribe(selector)(state => setState(state.value))
      () => subscription()
    })

    (state, circuit)
  }
}