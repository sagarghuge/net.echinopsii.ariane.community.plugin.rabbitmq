package net.echinopsii.ariane.community.plugin.rabbitmq.jsonparser

import net.echinopsii.ariane.community.plugin.rabbitmq.jsonparser.tools.RabbitRESTTools
import org.junit.Test

import static org.junit.Assert.assertTrue

class RabbitRESTToolsTest extends RabbitRESTTestSetup {

    @Test
    public void testConnectionsList() {
        if (rclient!=null) {
            List<String> connectionsList = RabbitRESTTools.getConnectionNames(validCluster)
            assertTrue(connectionsList.size() > 0)
        }
    }

    @Test
    public void testChannelsList() {
        if (rclient!=null) {
            List<String> list = RabbitRESTTools.getChannelNames(validCluster)
            assertTrue(list.size() > 0)
        }
    }

    @Test
    public void testExchangesList() {
        if (rclient!=null) {
            Map<String,List<String>> map = RabbitRESTTools.getExchangeNames(validCluster)
            assertTrue(map.size() > 0)
            for (List<String> listOnVhost: map.values())
                assertTrue(listOnVhost.size() > 0)
        }
    }

    @Test
    public void testQueueList() {
        if (rclient!=null) {
            Map<String,List<String>> map = RabbitRESTTools.getQueueNames(validCluster)
            assertTrue(map.size() > 0)
            for (List<String> listOnVhost : map.values())
                assertTrue(listOnVhost.size() > 0)
        }
    }

    @Test
    public void testBindingsList() {
        if (rclient!=null) {
            Map<String,List<String>> map = RabbitRESTTools.getBindingNames(validCluster)
            assertTrue(map.size() > 0)
            for (List<String> listOnVhost : map.values())
                assertTrue(listOnVhost.size() > 0)
        }
    }

    @Test
    public void testVhostsList() {
        if (rclient!=null) {
            List<String> list = RabbitRESTTools.getVhostNames(validCluster)
            assertTrue(list.size() > 0)
        }
    }

    @Test
    public void testOverview() {
        if (rclient!=null) {
            def test = rclient.get(path : '/api/overview')
            assertTrue(test.status == 200);
        }
    }
}
