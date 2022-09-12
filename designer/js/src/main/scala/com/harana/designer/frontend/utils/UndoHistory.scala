package com.harana.designer.frontend.utils

import scala.collection.mutable.ArrayBuffer

class UndoHistory[C] {

    private val history = new ArrayBuffer[C]
    private var undoPointer = 0

    def init(change: C): Unit =
        history.append(change)

    def push(change: C): Unit = {
        history.append(change);
        undoPointer += 1
    }
    
    def currentHistory(): C =
        history(undoPointer)

    def currentPointer(): Int =
        undoPointer

    def reset(): Unit = {
        history.clear(); 
        undoPointer = 0
    }

    def undo(): C = {
        undoPointer -= 1
        history(undoPointer)
    }

    def redo(): C = {
        undoPointer += 1
        history(undoPointer)
    }

    def canUndo: Boolean =
        undoPointer > 0

    def canRedo: Boolean =
        undoPointer < (history.size - 1)
}