package dk.treenoter.db

import dk.treenoter.adapter.DataAdapter

interface Persist {
    DataAdapter create(DataAdapter data)
    DataAdapter show(String id)
    DataAdapter update(String id, DataAdapter data)
}