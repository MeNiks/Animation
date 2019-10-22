package app.niks.base.recyclerview.row

abstract class AdapterRowPresenterFactory<ID, T : RecyclerRowItem<ID>> {

    abstract fun getViewType(item: T): Int

    abstract fun getItemPresenter(viewType: Int): AdapterRowPresenter<ID, T>
}
