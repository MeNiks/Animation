package app.niks.base.recyclerview.datasourcefactory

class ListPositionDataSource<T>
constructor(
    private val itemList: List<T>
) : BaseDataSource<T>() {
    override fun countItems(): Int {
        return itemList.size
    }

    override fun loadRangeAtPosition(position: Int, size: Int): List<T>? {
        return getSubList(position, size)
    }

    private fun getSubList(from: Int, to: Int): List<T> {
        if (from > itemList.size) {
            return mutableListOf()
        }

        if (from + to > itemList.size) {
            return itemList.subList(from, itemList.size)
        }

        return itemList.subList(from, from + to)
    }
}
