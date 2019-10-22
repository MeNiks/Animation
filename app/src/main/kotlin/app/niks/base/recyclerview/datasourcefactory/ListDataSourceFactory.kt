package app.niks.base.recyclerview.datasourcefactory

import androidx.paging.DataSource

class ListDataSourceFactory<Value>
constructor(
    val list: List<Value>
) : DataSource.Factory<Int, Value>() {
    override fun create(): DataSource<Int, Value> {
        return ListPositionDataSource<Value>(list)
    }
}
