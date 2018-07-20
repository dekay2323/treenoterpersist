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

    def "test json create"() {
        when:
        DataAdapter createdData = client.toBlocking().retrieve(
                HttpRequest.POST('/data',
                        [subject: 'a subject', text: 'some text']), DataAdapter)

        then:
        createdData.id
        createdData.subject == 'a subject'
        createdData.text == 'some text'
    }

    def "test json get"() {
        setup:
        DataAdapter createdData = client.toBlocking().retrieve(
                HttpRequest.POST('/data',
                        [subject: 'a subject', text: 'some text']), DataAdapter)

        when:
        DataAdapter fetchedData = client.toBlocking().retrieve(
                HttpRequest.GET("/data/${createdData.id}"), DataAdapter)

        then:
        fetchedData.id == createdData.id
        fetchedData.subject == createdData.subject
        fetchedData.text == createdData.text
        fetchedData == createdData
    }

    def "test json update"() {
        setup:
        DataAdapter createdData = client.toBlocking().retrieve(
                HttpRequest.POST('/data',
                        [subject: 'a subject', text: 'some text']), DataAdapter)
        DataAdapter fetchedData = client.toBlocking().retrieve(
                HttpRequest.GET("/data/${createdData.id}"), DataAdapter)

        when:
        DataAdapter changedData = client.toBlocking().retrieve(
                HttpRequest.PUT( "/data/${createdData.id}",
                        [subject: 'changed subject', text: 'changed text']), DataAdapter)

        then:
        changedData != fetchedData
        changedData != createdData
        changedData.id == createdData.id
        changedData.subject == 'changed subject'
        changedData.text == 'changed text'

    }

}
