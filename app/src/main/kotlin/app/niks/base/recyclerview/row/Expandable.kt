package app.niks.base.recyclerview.row

interface Expandable<ID> : RecyclerRowItem<ID> {
    var isExpanded: Boolean
    val items: List<RecyclerRowItem<ID>>
}
