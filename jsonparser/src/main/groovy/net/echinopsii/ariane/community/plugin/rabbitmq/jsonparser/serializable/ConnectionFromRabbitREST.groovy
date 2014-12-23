package net.echinopsii.ariane.community.plugin.rabbitmq.jsonparser.serializable

import net.echinopsii.ariane.community.plugin.rabbitmq.jsonparser.tools.RabbitClusterToConnect

import javax.persistence.Transient

class ConnectionFromRabbitREST implements Serializable {

    transient RabbitClusterToConnect cluster;

    String name
    Map<String, Object> properties

    ConnectionFromRabbitREST(String name, RabbitClusterToConnect cluster) {
        this.cluster = cluster
        this.name = name
    }

    ConnectionFromRabbitREST parse() {
        String connection_req_path =  '/api/connections/' + this.name;
        def connection_req = cluster.get(connection_req_path)
        if (connection_req.status == 200 && connection_req.data != null) {
            properties = connection_req.data
            properties.remove("name")
        }
        return this;
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        ConnectionFromRabbitREST that = (ConnectionFromRabbitREST) o

        if (name != that.name) return false

        return true
    }

    int hashCode() {
        return name.hashCode()
    }
}
