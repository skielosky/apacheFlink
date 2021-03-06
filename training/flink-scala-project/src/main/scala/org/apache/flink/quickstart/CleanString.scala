package org.apache.flink.quickstart

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.flink.streaming.api.scala._
import com.dataartisans.flinktraining.exercises.datastream_java.datatypes.TaxiRide
import org.apache.flink.streaming.api.TimeCharacteristic
import com.dataartisans.flinktraining.exercises.datastream_java.sources.TaxiRideSource
import com.dataartisans.flinktraining.exercises.datastream_java.utils.GeoUtils
import org.apache.flink.api.java.utils.ParameterTool
/**
 * Skeleton for a Flink Streaming Job.
 *
 * For a full example of a Flink Streaming Job, see the SocketTextStreamWordCount.java
 * file in the same package/directory or have a look at the website.
 *
 * You can also generate a .jar file that you can submit on your Flink
 * cluster. Just type
 * {{{
 *   mvn clean package
 * }}}
 * in the projects root directory. You will find the jar in
 * target/flink-scala-project-0.1.jar
 * From the CLI you can then run
 * {{{
 *    ./bin/flink run -c org.apache.flink.quickstart.StreamingJob target/flink-scala-project-0.1.jar
 * }}}
 *
 * For more information on the CLI see:
 *
 * http://flink.apache.org/docs/latest/apis/cli.html
 */
object CleanString {
  def main(args: Array[String]) {

    // parse parameters
    val params = ParameterTool.fromArgs(args)

    val input = params.getRequired("input")
    val maxDelay = 60
    val speed = 600
    // set up the streaming execution environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    /**
     * Here, you can start creating your execution plan for Flink.
     *
     * Start with getting some data from the environment, like
     *  env.readTextFile(textPath);
     *
     * then, transform the resulting DataStream[String] using operations
     * like
     *   .filter()
     *   .flatMap()
     *   .join()
     *   .group()
     *
     * and many more.
     * Have a look at the programming guide:
     *
     * http://flink.apache.org/docs/latest/apis/streaming/index.html
     *
     */

    // execute program
    //
    //configure event-time processing
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val rides = env.addSource(new TaxiRideSource(input, maxDelay, speed))

    val filteredRides = rides
      .filter(r => GeoUtils.isInNYC(r.startLon, r.startLat) && GeoUtils.isInNYC(r.endLon, r.endLat))

    filteredRides.print()

    env.execute("Flink Streaming Scala API Skeleton")
  }
}
