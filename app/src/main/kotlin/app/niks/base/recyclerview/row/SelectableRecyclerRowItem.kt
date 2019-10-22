package app.niks.base.recyclerview.row

interface SelectableRecyclerRowItem<ID> : RecyclerRowItem<ID> {
    var isSelected: Boolean
    var inSelectionMode: Boolean
    fun canSelect(): Boolean
}
