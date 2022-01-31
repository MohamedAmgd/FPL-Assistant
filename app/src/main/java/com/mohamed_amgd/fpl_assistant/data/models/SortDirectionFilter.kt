package com.mohamed_amgd.fpl_assistant.data.models

sealed class SortDirectionFilter(val key: String, val name: String) {
    object Ascending : SortDirectionFilter("asc", "Ascending")
    object Descending : SortDirectionFilter("dsc", "Descending")

    companion object {
        fun names(): List<String> {
            val result: ArrayList<String> = ArrayList()
            SortDirectionFilter::class.sealedSubclasses.forEach {
                it.objectInstance?.name?.let { name ->
                    result.add(name)
                }
            }
            return result
        }

        fun fromName(name: String): SortDirectionFilter {
            return SortDirectionFilter::class.sealedSubclasses.firstOrNull {
                it.objectInstance?.name == name
            }?.objectInstance ?: Descending
        }
    }
}
