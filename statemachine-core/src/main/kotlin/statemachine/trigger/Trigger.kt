package statemachine.trigger

interface Trigger<T> {
    fun getTriggerId(): T
    companion object {
        fun <S, T> ofId(id: T): Trigger<T> {
            return object : Trigger<T> {
                override fun getTriggerId(): T {
                    return id
                }
            }
        }
    }
}
