package dk.treenoter.db

import dk.treenoter.adapter.DataAdapter
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersistReddis implements Persist {
    @Inject
    StatefulRedisConnection<String, String> connection
    
    @Override
    DataAdapter create(DataAdapter data) {
        assert data
        
        RedisCommands<String, String> commands = connection.sync()
        commands.hmset(data.createNewId(), data.convertToHashmap())
        data;
    }

    @Override
    DataAdapter show(String id) {
        assert id
        
        RedisCommands<String, String> commands = connection.sync()
        final DataAdapter data = DataAdapter.buildFromHashmap(commands.hgetall(id))
        data
    }

    @Override
    DataAdapter update(String id, DataAdapter data) {
        assert id
        assert data
        assert id == data.id

        RedisCommands<String, String> commands = connection.sync()
        commands.hmset(data.id, data.convertToHashmap())
        data;
    }
}
