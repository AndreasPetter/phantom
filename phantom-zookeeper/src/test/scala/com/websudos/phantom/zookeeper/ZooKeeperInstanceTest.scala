/*
 *
 *  * Copyright 2014 websudos ltd.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.websudos.phantom.zookeeper

import java.net.InetSocketAddress

import org.scalatest.{ BeforeAndAfterAll, Matchers, FlatSpec }
import com.newzly.util.testing.AsyncAssertionsHelper._

class ZooKeeperInstanceTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  it should "correctly set the status flag to true after starting the ZooKeeper Instance" in {
    val instance = new ZookeeperInstance()
    instance.start()
    instance.isStarted shouldEqual true
    instance.stop()
  }

  it should "correctly initialise a ZooKeeper ServerSet after starting a ZooKeeper instance" in {
    val instance = new ZookeeperInstance()
    instance.start()
    instance.zookeeperServer.isRunning shouldEqual true
    instance.stop()
  }

  it should "retrieve the correct data from the Cassandra path by default" in {
    val instance = new ZookeeperInstance()
    instance.start()
    instance.richClient.getData("/cassandra", watch = false).successful {
      res => {
        res shouldNot equal(null)
        res.data shouldNot equal(null)
        new String(res.data) shouldEqual "localhost:9142"
      }

    }
    instance.stop()
  }

  it should "correctly parse the retrieved data into a Sequence of InetSocketAddresses" in {
    val instance = new ZookeeperInstance()
    instance.start()
    instance.hostnamePortPairs.successful {
      res => {
        res shouldEqual Seq(new InetSocketAddress("localhost", 9142))
      }
    }
    instance.stop()
  }
}
