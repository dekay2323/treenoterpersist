package dk.treenoter.web

import dk.treenoter.adapter.DataAdapter
import dk.treenoter.db.PersistReddis
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*

import java.util.concurrent.CompletableFuture

@Controller("/data")
class DataController {
    protected final PersistReddis persist

    DataController(PersistReddis persist) {
        this.persist = persist
    } 

    // curl -X POST localhost:8080/data --header "Content-Type: application/json" -d '{"subject":"Kurt Vonnegut","text":"Some text"}'
    @Post("/")
    CompletableFuture<HttpResponse<DataAdapter>> create(@Body CompletableFuture<DataAdapter> data) {
        return data.thenApply {
            assert it
          
            return HttpResponse.created(persist.create(it));
        }
    }

    // curl -X PUT localhost:8080/data/1def89ef-140e-4220-98ea-5d5e894646a5  --header "Content-Type: application/json" -d '{"id":"1def89ef-140e-4220-98ea-5d5e894646a5", "subject":"Kurt Vonnegut","text":"Some text"}'
    @Put("/{id}")
    CompletableFuture<HttpResponse<DataAdapter>> update(String id, @Body CompletableFuture<DataAdapter> data) {
        return data.thenApply {
            assert id
            assert it
           
            it.id = id;
            
            return HttpResponse.created(persist.update(id, it));
        }
    }

    // curl -X GET localhost:8080/data/01fb64db-51f8-4843-b870-55ca8e5f573e
    @Get("/{id}")
    CompletableFuture<HttpResponse<DataAdapter>> show(String id) {
        CompletableFuture.supplyAsync {
            assert id
            
            return HttpResponse.ok(persist.show(id));
        }
    }

}
