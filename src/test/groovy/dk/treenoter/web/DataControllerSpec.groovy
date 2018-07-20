package dk.treenoter.web

import dk.treenoter.adapter.DataAdapter
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class DataControllerSpec extends Specification {
    @Shared @AutoCleanup EmbeddedServer server = ApplicationContext.run(EmbeddedServer)
    @Shared @AutoCleanup HttpClient client = HttpClient.create(server.URL)
    
    def "test json create, get, and update of data"() {
        when:
        DataAdapter createdData = client.toBlocking().retrieve(
                HttpRequest.POST('/data',
                [subject: 'a subject', text: 'some text']), DataAdapter)
        DataAdapter fetchedData = client.toBlocking().retrieve(
                HttpRequest.GET("/data/${createdData.id}"), DataAdapter)
        
        then:
        createdData.id
        createdData.subject == 'a subject'
        createdData.text == 'some text'
        fetchedData.id == createdData.id
        fetchedData.subject == createdData.subject
        fetchedData.text == createdData.text
        fetchedData == createdData
    }
}
