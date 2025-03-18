package com.riguz.y.common

data class Range(val start: ULong, val end: ULong) {
    fun isEmpty(): Boolean {
        return start >= end
    }

    fun contains(item: ULong): Boolean {
        return item in start..end
    }

    companion object {
        fun isContinuousRange(l: Range, r: Range): Boolean {
            return l.end >= r.start && l.start <= r.end
        }
    }
}

sealed class OrderRange {
    abstract fun rangesLength(): Int
    abstract fun isEmpty(): Boolean
    abstract fun contains(clock: ULong): Boolean

    data class Single(val range: Range) : OrderRange() {
        override fun rangesLength(): Int {
            return 1
        }

        override fun isEmpty(): Boolean {
            return range.isEmpty()
        }

        override fun contains(clock: ULong): Boolean {
            return range.contains(clock)
        }
    }

    data class Fragment(val ranges: ArrayDeque<Range>) : OrderRange() {
        override fun rangesLength(): Int {
            return ranges.size
        }

        override fun isEmpty(): Boolean {
            return ranges.isEmpty()
        }

        override fun contains(clock: ULong): Boolean {
            return ranges.any { r -> r.contains(clock) }
        }
    }
}